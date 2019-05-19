package ru.magomed.gamzatov.money.transfer.application.model

import java.math.BigDecimal

data class Account(val id: Long, val name: String, var money: BigDecimal)
