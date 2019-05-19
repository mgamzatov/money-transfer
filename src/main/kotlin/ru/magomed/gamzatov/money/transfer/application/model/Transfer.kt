package ru.magomed.gamzatov.money.transfer.application.model

import java.math.BigDecimal

data class Transfer(val fromAccountId: Long, val toAccountId: Long, val amount: BigDecimal)
