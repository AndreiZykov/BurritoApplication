package com.andriiz.api

import arrow.core.Either
import arrow.core.left
import com.andriiz.domain.data.Error
import com.apollographql.apollo.exception.ApolloException

fun <Entity> Throwable.left(): Either<Error, Entity> = when (this) {
    is ApolloException -> Error.SERVER_ERROR
    else -> Error.UNKNOWN_ERROR
}.left()