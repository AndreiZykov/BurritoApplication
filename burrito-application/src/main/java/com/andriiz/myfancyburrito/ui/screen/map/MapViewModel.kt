package com.andriiz.myfancyburrito.ui.screen.map

import arrow.core.extensions.list.foldable.find
import arrow.core.k
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.andriiz.domain.contract.GenericDataSource
import com.andriiz.domain.data.Business
import com.andriiz.myfancyburrito.ui.core.MvRxViewModel
import org.koin.android.ext.android.inject

class MapViewModel(initialState: MapViewState) : MvRxViewModel<MapViewState>(initialState) {

    companion object : MvRxViewModelFactory<MapViewModel, MapViewState> {

        override fun create(viewModelContext: ViewModelContext, state: MapViewState): MapViewModel {
            return MapViewModel(state)
        }

        override fun initialState(viewModelContext: ViewModelContext) : MapViewState {
            val dataSource: GenericDataSource<Business> by viewModelContext.activity.inject()
            val args = viewModelContext.args<MapFragmentArgs>()
            val optionalBusiness = dataSource.value.k().find { it.id == args.id }
            return MapViewState(optionalBusiness.orNull())
        }

    }

}