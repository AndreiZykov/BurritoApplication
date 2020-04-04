package com.andriiz.domain.data

import java.lang.StringBuilder

data class Business(
    val id: String,
    val name: String,
    val address: String,
    val price: String,
    val phoneNumber: PhoneNumber,
    val lat: Double,
    val lng: Double
)

val Business.info: String
    get() {
        return StringBuilder().apply {
            append(price)
            if (price.isNotEmpty() && phoneNumber.value.isNotEmpty()) append(" • ")
            append(phoneNumber.value)
        }.toString()
    }