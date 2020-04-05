package com.andriiz.api

import arrow.core.Either
import arrow.core.right
import com.andriiz.domain.data.Business
import com.andriiz.domain.data.Error
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.rxQuery
import com.yelp.SearchQuery
import io.reactivex.Single

class YelpBusinessSearchService(apolloClientProvider: ApolloClientProvider) {

    private val apolloClient: ApolloClient = apolloClientProvider.get()

    fun search(term: String, lat: Double, lng: Double, radius: Double): Single<Either<Error, List<Business>>> =
        apolloClient.rxQuery(SearchQuery(term = term, radius = radius, latitude = lat, longitude = lng))
            .singleOrError()
            .map { response -> response.data()?.search?.business.orEmpty() }
            .map { it.filterNotNull() }
            .map { it.filter(SearchQuery.Business::isValid) }
            .map { it.map(SearchQuery.Business::toLocal) }
            .map<Either<Error, List<Business>>> { it.right() }
            .onErrorReturn { it.left() }

}