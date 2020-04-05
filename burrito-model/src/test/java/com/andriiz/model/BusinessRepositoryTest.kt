package com.andriiz.model

import android.location.Location
import arrow.core.None
import arrow.core.extensions.either.foldable.isEmpty
import arrow.core.left
import arrow.core.right
import com.andriiz.api.YelpBusinessSearchService
import com.andriiz.domain.contract.LocationProvider
import com.andriiz.domain.data.Business
import com.andriiz.domain.data.Error
import com.andriiz.domain.data.Error.LOCATION_PERMISSION_NOT_GRANTED
import com.andriiz.domain.data.PhoneNumber
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test

class BusinessRepositoryTest {

    @MockK
    lateinit var businessSearchService: YelpBusinessSearchService

    @MockK
    lateinit var locationProvider: LocationProvider

    @InjectMockKs
    lateinit var repository: BusinessRepository

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun testInitialState_value_shouldReturnEmptyList(){
        Assert.assertTrue(repository.value.isEmpty())
    }

    @Test
    fun testInitialState_onValueChanged_shouldReturnEmptyList(){
        repository.onValueChanged
            .doOnNext { Assert.assertTrue(it.isEmpty()) }
            .ignoreElements()
            .onErrorComplete()
    }

    @Test
    fun testInitialState_fetch_shouldRequestLocation(){
        // given
        every { locationProvider.get() } returns Single.just(LOCATION_PERMISSION_NOT_GRANTED.left())
        // when
        repository.fetch()
            .doOnComplete {
                // then
                verify { locationProvider.get() }
            }
            .onErrorComplete()
            .subscribe()
    }

    @Test
    fun testInitialState_fetch_locationPermissionNotGranted_valueShouldBeEmpty() {
        // given
        every { locationProvider.get() } returns Single.just(LOCATION_PERMISSION_NOT_GRANTED.left())

        // when
        repository.fetch()
            .onErrorComplete()
            .subscribe()
        // then
        Assert.assertTrue(repository.value.isEmpty())
    }

    @Test
    fun testInitialState_fetch_locationPermissionNotGranted_onValueChangedShouldReturnProperError() {
        // given
        every { locationProvider.get() } returns Single.just(LOCATION_PERMISSION_NOT_GRANTED.left())

        // when
        repository.fetch()
            .onErrorComplete()
            .subscribe()

        // then
        repository.onValueChanged
            .doOnNext { errorOrBusinesses ->
                errorOrBusinesses.fold(ifLeft = { Assert.assertEquals(LOCATION_PERMISSION_NOT_GRANTED, it) },
                    ifRight = { Assert.assertTrue(it.isEmpty()) })
            }
            .ignoreElements()
            .onErrorComplete()
    }

    @Test
    fun testInitialState_fetch_shouldCallSearchOnBusinessSearchService(){
        // given
        every { locationProvider.get() } returns Single.just(LOCATION_PERMISSION_NOT_GRANTED.left())
        every { businessSearchService.search(term = DEFAULT_NON_EMPTY_STRING, lng = DEFAULT_DOUBLE_VALUE,
            lat = DEFAULT_DOUBLE_VALUE, radius = DEFAULT_DOUBLE_VALUE) } returns Single.just(Error.SERVER_ERROR.left())

        // when
        repository.fetch()
            .doOnComplete {
                // then
                verify { businessSearchService.search(term = DEFAULT_NON_EMPTY_STRING, lng = DEFAULT_DOUBLE_VALUE,
                    lat = DEFAULT_DOUBLE_VALUE, radius = DEFAULT_DOUBLE_VALUE) } }
            .onErrorComplete()
            .subscribe()
    }

    @Test
    fun testInitialState_fetch_businessSearchServiceReturnsBusinessList_valueSizeShouldBeEqualToReturnedBusinessListSize(){
        // given
        every { locationProvider.get() } returns Single.just(LOCATION_PERMISSION_NOT_GRANTED.left())
        every { businessSearchService.search(term = DEFAULT_NON_EMPTY_STRING, lng = DEFAULT_DOUBLE_VALUE,
            lat = DEFAULT_DOUBLE_VALUE, radius = DEFAULT_DOUBLE_VALUE) } returns Single.just(listOfBusinesses.right())

        // when
        repository.fetch()
            .doOnComplete {
                // then
                Assert.assertEquals(listOfBusinesses.size, repository.value.size)
            }
            .onErrorComplete()
            .subscribe()
    }

    @Test
    fun testInitialState_fetch_businessSearchServiceReturnsBusinessList_onValueChangedListSizeShouldBeEqualToReturnedBusinessListSize(){
        // given
        every { locationProvider.get() } returns Single.just(Location(DEFAULT_NON_EMPTY_STRING).right())
        every { businessSearchService.search(term = DEFAULT_NON_EMPTY_STRING, lng = DEFAULT_DOUBLE_VALUE,
            lat = DEFAULT_DOUBLE_VALUE, radius = DEFAULT_DOUBLE_VALUE) } returns Single.just(listOfBusinesses.right())

        // when
        repository.fetch()
            .onErrorComplete()
            .subscribe()

        repository.onValueChanged
            .doOnNext { errorOrBusinesses ->
                // then
                errorOrBusinesses.fold(ifLeft = { Assert.assertEquals(None, it) },
                    ifRight = { Assert.assertEquals(listOfBusinesses.size, repository.value.size) })
            }
            .ignoreElements()
            .onErrorComplete()
            .subscribe()
    }

    companion object {
        @ClassRule
        @JvmField
        internal var schedulers = RxImmediateSchedulerRule()

        private const val DEFAULT_NON_EMPTY_STRING = "DEFAULT_NON_EMPTY_STRING"
        private const val DEFAULT_DOUBLE_VALUE = 0.0
        private val listOfBusinesses = listOf<Business>(
            Business(
                id = DEFAULT_NON_EMPTY_STRING,
                name = DEFAULT_NON_EMPTY_STRING,
                address = DEFAULT_NON_EMPTY_STRING,
                phoneNumber = PhoneNumber(DEFAULT_NON_EMPTY_STRING),
                price = DEFAULT_NON_EMPTY_STRING,
                lat = DEFAULT_DOUBLE_VALUE,
                lng = DEFAULT_DOUBLE_VALUE
            )
        )

    }

}