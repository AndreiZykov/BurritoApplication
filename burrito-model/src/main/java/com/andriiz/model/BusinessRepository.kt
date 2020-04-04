package com.andriiz.model

import android.location.Location
import arrow.core.Either
import arrow.core.getOrHandle
import arrow.core.left
import arrow.core.right
import com.andriiz.api.YelpBusinessSearchService
import com.andriiz.domain.contract.GenericDataSource
import com.andriiz.domain.contract.LocationProvider
import com.andriiz.domain.data.Business
import com.andriiz.domain.data.Error
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class BusinessRepository(
    private val yelpBusinessSearchService: YelpBusinessSearchService,
    private val locationProvider: LocationProvider
) : GenericDataSource<Business> {

    private val _onValueChanged: BehaviorSubject<Either<Error, List<Business>>> =
        BehaviorSubject.createDefault(emptyList<Business>().right())

    override val onValueChanged: Observable<Either<Error, List<Business>>>
        get() = _onValueChanged

    override val value: List<Business>
        get() = _onValueChanged.value
            ?.fold( ifLeft = { emptyList<Business>() }, ifRight = { it })
            ?: emptyList()

    override fun fetch(page: Int): Completable = locationProvider.get()
        .map { locationOrError -> locationOrError.map(::searchBurrito) }
        .flatMap { it.getOrHandle { error -> Single.just(error.left()) } }
        .doOnSuccess { _onValueChanged.onNext(it) }
        .ignoreElement()


    private fun searchBurrito(location: Location): Single<Either<Error, List<Business>>> =
        yelpBusinessSearchService.search(term = HARDCODED_BURRITO_STRING, lat = location.latitude, lng =  location.longitude, radius = TWELVE_MILES_IN_METERS)

    companion object {
        private const val HARDCODED_BURRITO_STRING = "burrito"
        private const val TWELVE_MILES_IN_METERS = 19312.1
    }

}