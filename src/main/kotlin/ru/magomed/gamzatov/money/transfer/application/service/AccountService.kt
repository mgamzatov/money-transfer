package ru.magomed.gamzatov.money.transfer.application.service

import ru.magomed.gamzatov.money.transfer.application.exception.AccountNotFoundException
import ru.magomed.gamzatov.money.transfer.application.model.Account
import java.math.BigDecimal

object AccountService {

    private val accounts = mapOf(
            0L to Account(0L, "Account 0", BigDecimal.valueOf(100500)),
            1L to Account(1L, "Account 1", BigDecimal.valueOf(500100)),
            2L to Account(2L, "Account 2", BigDecimal.valueOf(100)),
            3L to Account(3L, "Account 3", BigDecimal.valueOf(-100))
    )

    fun getById(id: Long) = accounts[id] ?: throw AccountNotFoundException(id)

    fun getAll() = accounts.values
}
