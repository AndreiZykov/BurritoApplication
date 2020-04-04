package com.andriiz.domain.contract

import android.location.Location
import arrow.core.Either
import com.andriiz.domain.data.Error
import io.reactivex.Single

interface LocationProvider: Provider<Single<Either<Error, Location>>>