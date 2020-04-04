package com.andriiz.myfancyburrito.ui.core.listfragment

import arrow.core.Option
import arrow.core.getOrElse
import com.airbnb.mvrx.MvRxState
import com.andriiz.domain.data.Error
import com.andriiz.myfancyburrito.R

data class GenericListViewState<Entity>(
    val list: List<Entity> = emptyList(),
    val error: Option<Error> = Option.empty(),
    val isLoading: Boolean = false
) : MvRxState

fun <Entity> GenericListViewState<Entity>.errorMessage(): Int {
    return error.map { it.message }
        .getOrElse { R.string.unknown_error }
}