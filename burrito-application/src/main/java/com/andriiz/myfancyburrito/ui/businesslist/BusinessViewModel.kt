package com.andriiz.myfancyburrito.ui.businesslist

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.andriiz.domain.contract.GenericDataSource
import com.andriiz.domain.data.Business
import com.andriiz.myfancyburrito.ui.core.listfragment.GenericListViewModel
import com.andriiz.myfancyburrito.ui.core.listfragment.GenericListViewState
import org.koin.android.ext.android.inject

class BusinessViewModel(initialState: GenericListViewState<Business>, dataSource: GenericDataSource<Business>)
    : GenericListViewModel<Business>(initialState, dataSource) {

    companion object : MvRxViewModelFactory<BusinessViewModel, GenericListViewState<Business>> {

        override fun create(viewModelContext: ViewModelContext, state: GenericListViewState<Business>): BusinessViewModel {
            val dataSource: GenericDataSource<Business> by viewModelContext.activity.inject()
            return BusinessViewModel(state, dataSource)
        }

        override fun initialState(viewModelContext: ViewModelContext) = GenericListViewState<Business>()

    }

}