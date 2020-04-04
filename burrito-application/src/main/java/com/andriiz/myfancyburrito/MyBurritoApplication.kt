package com.andriiz.myfancyburrito

import android.app.Application
import com.andriiz.api.ApolloClientProvider
import com.andriiz.api.OkHTTPClientProvider
import com.andriiz.api.YelpBusinessSearchService
import com.andriiz.domain.contract.GenericDataSource
import com.andriiz.domain.data.Business
import com.andriiz.model.BusinessRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MyBurritoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@MyBurritoApplication)
            modules(applicationScopeModule)
        }

    }

    private val applicationScopeModule = module {
        single { OkHTTPClientProvider(getString(R.string.yelp_api_key)) }
        single { ApolloClientProvider(getString(R.string.yelp_graphql_url), get()) }
        single { YelpBusinessSearchService(get()) }
        single <GenericDataSource<Business>> { BusinessRepository(get(), get()) }
    }

}