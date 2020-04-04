package com.andriiz.domain.contract

import arrow.core.Either
import com.andriiz.domain.data.Error
import io.reactivex.Completable
import io.reactivex.Observable

interface GenericDataSource<Entity> {
    val onValueChanged: Observable<Either<Error, List<Entity>>>
    val value: List<Entity>
    fun fetch(page: Int = 0): Completable
}