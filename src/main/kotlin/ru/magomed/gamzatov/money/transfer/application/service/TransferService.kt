package ru.magomed.gamzatov.money.transfer.application.service

import ru.magomed.gamzatov.money.transfer.application.exception.BadRequestException
import ru.magomed.gamzatov.money.transfer.application.model.Transfer
import java.math.BigDecimal

object TransferService {

    fun transfer(transfer: Transfer) {
        validate(transfer)
        val from = AccountService.getById(transfer.fromAccountId)
        val to = AccountService.getById(transfer.toAccountId)
        val amount = transfer.amount

        val (lock1, lock2) = if (from.id < to.id) {
            from to to
        } else {
            to to from
        }

        synchronized(lock1) {
            synchronized(lock2) {
                if (amount > from.money) {
                    throw BadRequestException("Account ${from.name} does not have enough money")
                }
                from.money -= amount
                to.money += amount
            }
        }
    }

    private fun validate(transfer: Transfer) {
        with(transfer) {
            if (fromAccountId == toAccountId) {
                throw BadRequestException("fromAccountId and toAccountId must be different")
            }
            if (amount <= BigDecimal.ZERO) {
                throw BadRequestException("Amount must be bigger than zero")
            }
        }
    }
}
