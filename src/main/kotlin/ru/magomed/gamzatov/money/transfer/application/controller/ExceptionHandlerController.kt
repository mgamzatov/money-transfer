package ru.magomed.gamzatov.money.transfer.application.controller

import com.fasterxml.jackson.core.JsonProcessingException
import org.slf4j.LoggerFactory
import ru.magomed.gamzatov.money.transfer.application.exception.AccountNotFoundException
import ru.magomed.gamzatov.money.transfer.application.exception.BadRequestException
import ru.magomed.gamzatov.money.transfer.helper.SparkController
import ru.magomed.gamzatov.money.transfer.helper.mapper
import spark.ExceptionHandler
import spark.Spark.exception

@SparkController
class ExceptionHandlerController {

    private val logger = LoggerFactory.getLogger(ExceptionHandlerController::class.java)

    init {
        exception(AccountNotFoundException::class.java, exceptionHandler(404))
        exception(BadRequestException::class.java, exceptionHandler(400))
        exception(JsonProcessingException::class.java, exceptionHandler(400))
        exception(NumberFormatException::class.java, exceptionHandler(400))
    }

    private fun <T: Exception> exceptionHandler(code: Int) = ExceptionHandler<T> { ex, _, res ->
        logger.error(ex.message, ex)
        res.status(code)
        res.body(mapper.writeValueAsString(mapOf("message" to ex.message)))
    }
}