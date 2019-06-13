package u.magomed.gamzatov.money.transfer.application.service

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import ru.magomed.gamzatov.money.transfer.application.exception.AccountNotFoundException
import ru.magomed.gamzatov.money.transfer.application.exception.BadRequestException
import ru.magomed.gamzatov.money.transfer.application.model.Account
import ru.magomed.gamzatov.money.transfer.application.model.Transfer
import ru.magomed.gamzatov.money.transfer.application.service.AccountService
import ru.magomed.gamzatov.money.transfer.application.service.TransferService
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier

class TransferServiceTest {

    @Test
    fun `transfer money between different accounts`() {
        val acc1 = Account(1L, "test account 1", BigDecimal.valueOf(100))
        val acc2 = Account(2L, "test account 2", BigDecimal.valueOf(40))
        mockkObject(AccountService)
        every { AccountService.getById(acc1.id) } returns acc1
        every { AccountService.getById(acc2.id) } returns acc2

        TransferService.transfer(Transfer(acc1.id, acc2.id, BigDecimal.valueOf(50)))

        assertEquals(BigDecimal.valueOf(50), acc1.money)
        assertEquals(BigDecimal.valueOf(90), acc2.money)
    }

    @Test
    fun `throws exception for transferring money to the same account`() {
        val acc1 = Account(1L, "test account 1", BigDecimal.valueOf(100))
        mockkObject(AccountService)
        every { AccountService.getById(acc1.id) } returns acc1

        assertThrows(BadRequestException::class.java) {
            TransferService.transfer(Transfer(acc1.id, acc1.id, BigDecimal.valueOf(50)))
        }
    }

    @Test
    fun `throws exception for transferring zero or negative amount`() {
        val acc1 = Account(1L, "test account 1", BigDecimal.valueOf(100))
        val acc2 = Account(2L, "test account 2", BigDecimal.valueOf(40))
        mockkObject(AccountService)
        every { AccountService.getById(acc1.id) } returns acc1
        every { AccountService.getById(acc2.id) } returns acc2

        assertThrows(BadRequestException::class.java) {
            TransferService.transfer(Transfer(acc1.id, acc2.id, BigDecimal.valueOf(0)))
        }
        assertThrows(BadRequestException::class.java) {
            TransferService.transfer(Transfer(acc1.id, acc2.id, BigDecimal.valueOf(-10)))
        }
    }

    @Test
    fun `throws exception for transferring from or to nonexistent account`() {
        val acc1 = Account(1L, "test account 1", BigDecimal.valueOf(100))
        val nonexistentAccountId = 123L
        val nonexistentAccountId2 = 124L
        mockkObject(AccountService)
        every { AccountService.getById(acc1.id) } returns acc1

        assertThrows(AccountNotFoundException::class.java) {
            TransferService.transfer(Transfer(acc1.id, nonexistentAccountId, BigDecimal.valueOf(50)))
        }
        assertThrows(AccountNotFoundException::class.java) {
            TransferService.transfer(Transfer(nonexistentAccountId, acc1.id, BigDecimal.valueOf(50)))
        }
        assertThrows(AccountNotFoundException::class.java) {
            TransferService.transfer(Transfer(nonexistentAccountId, nonexistentAccountId2, BigDecimal.valueOf(50)))
        }
    }

    @Test
    fun `throws exception for transferring too match amount`() {
        val acc1 = Account(1L, "test account 1", BigDecimal.valueOf(100))
        val acc2 = Account(2L, "test account 2", BigDecimal.valueOf(40))
        mockkObject(AccountService)
        every { AccountService.getById(acc1.id) } returns acc1
        every { AccountService.getById(acc2.id) } returns acc2

        assertThrows(BadRequestException::class.java) {
            TransferService.transfer(Transfer(acc1.id, acc2.id, BigDecimal.valueOf(1000)))
        }
    }

    @Test
    fun `transfer money between different accounts in 10 threads`() {
        val acc1 = Account(1L, "test account 1", BigDecimal.valueOf(100))
        val acc2 = Account(2L, "test account 2", BigDecimal.valueOf(40))
        mockkObject(AccountService)
        every { AccountService.getById(acc1.id) } returns acc1
        every { AccountService.getById(acc2.id) } returns acc2

        val latch = CountDownLatch(10)
        val barrier = CyclicBarrier(10)

        repeat(5) {
            Thread {
                barrier.await()
                TransferService.transfer(Transfer(acc1.id, acc2.id, BigDecimal.valueOf(2)))
                latch.countDown()
            }.start()
            Thread {
                barrier.await()
                TransferService.transfer(Transfer(acc2.id, acc1.id, BigDecimal.valueOf(3)))
                latch.countDown()
            }.start()
        }

        latch.await()

        assertEquals(BigDecimal.valueOf(105), acc1.money)
        assertEquals(BigDecimal.valueOf(35), acc2.money)
    }

    @AfterEach
    fun afterTests() {
        unmockkAll()
    }
}