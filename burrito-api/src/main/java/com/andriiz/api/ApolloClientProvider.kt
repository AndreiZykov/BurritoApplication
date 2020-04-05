package com.andriiz.api

import com.andriiz.domain.contract.Provider
import com.apollographql.apollo.ApolloClient

class ApolloClientProvider(private val url: String,
                           private val okHTTPClientProvider: OkHTTPClientProvider) : Provider<ApolloClient> {

    override fun get(): ApolloClient = ApolloClient.builder()
        .serverUrl(url)
        .okHttpClient(okHTTPClientProvider.get())
        .build()

}