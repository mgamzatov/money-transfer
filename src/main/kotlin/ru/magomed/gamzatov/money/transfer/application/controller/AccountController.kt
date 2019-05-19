package ru.magomed.gamzatov.money.transfer.application.controller

import com.fasterxml.jackson.module.kotlin.readValue
import ru.magomed.gamzatov.money.transfer.application.model.Transfer
import ru.magomed.gamzatov.money.transfer.helper.SparkController
import ru.magomed.gamzatov.money.transfer.helper.jsonTransformer
import ru.magomed.gamzatov.money.transfer.application.service.AccountService
import ru.magomed.gamzatov.money.transfer.application.service.TransferService
import ru.magomed.gamzatov.money.transfer.helper.mapper
import spark.Route
import spark.Spark.*

@SparkController
class AccountController {

    init {
        path("/accounts") {
            get("", getAll(), jsonTransformer)
            get("/:id", getById(), jsonTransformer)
            post("/transfer", transfer())
        }
        afterAfter { _, res -> res.type("application/json") }
    }

    private fun getAll() = Route { _, _ ->
        AccountService.getAll()
    }

    private fun getById() = Route { req, _ ->
        AccountService.getById(id = req.params("id").toLong())
    }

    private fun transfer() = Route { req, _ ->
        val transfer = mapper.readValue<Transfer>(req.body())
        TransferService.transfer(transfer)
        """{"message": "Success"}"""
    }
}
