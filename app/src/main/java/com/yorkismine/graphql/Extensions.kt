package com.yorkismine.graphql

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import okhttp3.logging.HttpLoggingInterceptor
import kotlin.concurrent.thread


object Extensions {
    private const val base_url = "https://api.stage.hawk.so/graphql"
    private var accessToken = ""
    private var refreshToken = ""

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val apolloClient: ApolloClient = ApolloClient.builder()
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        )
        .serverUrl(base_url)
        .build()

    @Synchronized
    fun getToken(email: String, password: String, finishCall: FinishCallBack) {
        apolloClient.mutate(
            GetTokenMutation.builder()
                .email(email)
                .password(password)
                .build()
        ).enqueue(object : ApolloCall.Callback<GetTokenMutation.Data>() {
            override fun onFailure(e: ApolloException) {
                e.printStackTrace()
            }

            override fun onResponse(response: Response<GetTokenMutation.Data>) {
                accessToken = response.data()!!.login.accessToken
                refreshToken = response.data()!!.login.refreshToken
                finishCall.finish()
            }
        })


    }

    fun refreshToken() {
        getApollo().mutate(
            RefreshTokenMutation.builder()
                .refreshToken(refreshToken).build()
        )
            .enqueue(object : ApolloCall.Callback<RefreshTokenMutation.Data>() {
                override fun onFailure(e: ApolloException) {
                    e.printStackTrace()
                }

                override fun onResponse(response: Response<RefreshTokenMutation.Data>) {
                    accessToken = response.data()!!.refreshTokens.accessToken
                    refreshToken = response.data()!!.refreshTokens.refreshToken
                }

            })
    }

    @Synchronized
    fun getApollo(): ApolloClient {
        return apolloClient
    }

    @Synchronized
    fun returnAccess(): String {
        return accessToken
    }

    interface FinishCallBack {
        fun finish()
    }

}