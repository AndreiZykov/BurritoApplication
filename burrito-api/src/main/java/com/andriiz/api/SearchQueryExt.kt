package com.andriiz.api

import com.andriiz.domain.data.Business
import com.andriiz.domain.data.PhoneNumber
import com.yelp.SearchQuery

private const val DEFAULT_COORDINATE = 0.0

fun SearchQuery.Business.isValid(): Boolean = when {
    name.isNullOrBlank() -> false
    location == null -> false
    location.formatted_address.isNullOrBlank() -> false
    coordinates == null -> false
    coordinates.latitude == null -> false
    coordinates.longitude == null -> false
    else -> true
}

fun SearchQuery.Business.toLocal() = Business(
    id = id.orEmpty(),
    name = name.orEmpty(),
    address = location?.formatted_address.orEmpty(),
    price = price.orEmpty(),
    phoneNumber = PhoneNumber(phone.orEmpty()),
    lat = coordinates?.latitude ?: DEFAULT_COORDINATE,
    lng = coordinates?.longitude ?: DEFAULT_COORDINATE
)