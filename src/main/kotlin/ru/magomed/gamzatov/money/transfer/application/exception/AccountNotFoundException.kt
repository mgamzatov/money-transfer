package ru.magomed.gamzatov.money.transfer.application.exception

class AccountNotFoundException(accountId: Long): RuntimeException("Not found Account with id: $accountId")