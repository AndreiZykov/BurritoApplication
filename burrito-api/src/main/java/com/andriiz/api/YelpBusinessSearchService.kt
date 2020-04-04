package com.andriiz.api

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.andriiz.domain.data.Business
import com.andriiz.domain.data.Error
import com.andriiz.domain.data.Error.*
import com.andriiz.domain.data.PhoneNumber
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.rx2.rxQuery
import com.yelp.SearchQuery
import io.reactivex.Single

class YelpBusinessSearchService(apolloClientProvider: ApolloClientProvider) {

    private val apolloClient: ApolloClient = apolloClientProvider.get()

    fun search(term: String, lat: Double, lng: Double, radius: Double): Single<Either<Error, List<Business>>> =
        apolloClient.rxQuery(SearchQuery(term = term, radius = radius, latitude = lat, longitude = lng))
            .singleOrError()
            .map { response -> response.data()?.search?.business.orEmpty() }
            .map { business ->
                business
                    .filterNotNull()
                    .filter(filterBusinessObj)
                    .map(toBusinessObj)
            }.map<Either<Error, List<Business>>> { it.right() }
            .onErrorReturn {
                when(it){
                    is ApolloException -> SERVER_ERROR
                    else               -> UNKNOWN_ERROR
                }.left()
            }

    private val filterBusinessObj: (SearchQuery.Business) -> Boolean = { item ->
        when {
            item.name.isNullOrBlank() -> false
            item.location == null -> false
            item.location.formatted_address.isNullOrBlank() -> false
            item.price.isNullOrBlank() -> false
            item.phone.isNullOrBlank() -> false
            item.coordinates == null -> false
            item.coordinates.latitude == null -> false
            item.coordinates.longitude == null -> false
            else -> true
        }
    }

    private val toBusinessObj: (SearchQuery.Business) -> Business = { item ->
        Business(
            id = item.id.orEmpty(),
            name = item.name.orEmpty(),
            address = item.location?.formatted_address.orEmpty(),
            price = item.price.orEmpty(),
            phoneNumber = PhoneNumber(item.phone.orEmpty()),
            lat = item.coordinates?.latitude ?: DEFAULT_COORDINATE,
            lng = item.coordinates?.longitude ?: DEFAULT_COORDINATE
        )
    }

    companion object {
        private const val DEFAULT_COORDINATE = 0.0
    }



}