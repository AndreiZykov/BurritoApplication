package com.andriiz.myfancyburrito.ui.core.listfragment

import arrow.core.Option
import arrow.core.toOption
import com.andriiz.domain.contract.GenericDataSource
import com.andriiz.myfancyburrito.ui.core.MvRxViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class GenericListViewModel<Entity>(
    initialState: GenericListViewState<Entity>,
    private val genericDataSource: GenericDataSource<Entity>
) : MvRxViewModel<GenericListViewState<Entity>>(initialState) {

    init {
        genericDataSource.onValueChanged
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ businessesOrError ->
                businessesOrError.fold({ setState { copy(list = emptyList(), error = it.toOption()) } })
                { setState { copy(list = it, error = Option.empty()) } }
            }, Timber::e)
            .disposeOnClear()
    }

    fun refresh() {
        genericDataSource.fetch()
            .doOnSubscribe { setState { copy(isLoading = list.isEmpty()) } }
            .doOnComplete { setState { copy(isLoading = false) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .disposeOnClear()
    }

}