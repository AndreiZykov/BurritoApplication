package com.andriiz.myfancyburrito.utils

import android.Manifest.permission.*
import android.app.Activity
import android.location.Location
import arrow.core.*
import com.andriiz.domain.contract.LocationProvider
import com.andriiz.domain.contract.Provider
import com.andriiz.domain.data.Error
import com.andriiz.domain.data.Error.LOCATION_PERMISSION_NOT_GRANTED
import com.andriiz.domain.data.Error.LOCATION_REQUEST_FAILED
import com.andriiz.myfancyburrito.ui.MainActivity
import com.google.android.gms.location.LocationServices
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import io.reactivex.Single

class LocationProviderImpl(private val activityProvider: Provider<MainActivity>) :
    LocationProvider {

    private val activity: Activity
        get() = activityProvider.get()

    private val rxPermission: RxPermission = RealRxPermission.getInstance(activity)

    private val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(activity)

    private val locationRequest: Single<Either<Error, Location>> = Single.create { emitter ->
        fusedLocationProvider.lastLocation
            .addOnSuccessListener { emitter.onSuccess(it.right()) }
            .addOnFailureListener { emitter.onSuccess(LOCATION_REQUEST_FAILED.left())}
    }

    override fun get(): Single<Either<Error, Location>> =
        rxPermission.request(ACCESS_COARSE_LOCATION)
            .map { permission -> if (permission.isGranted()) (None).right() else (LOCATION_PERMISSION_NOT_GRANTED).left() }
            .map { grantedOrError -> grantedOrError.map { locationRequest } }
            .flatMap { it.getOrHandle { error -> Single.just(error.left()) } }
}

fun Permission.isGranted(): Boolean = this.state() == Permission.State.GRANTED