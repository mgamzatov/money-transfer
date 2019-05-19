package ru.magomed.gamzatov.money.transfer.helper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper = jacksonObjectMapper()

val jsonTransformer = JsonTransformer(mapper)