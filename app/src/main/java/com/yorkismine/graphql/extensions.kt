package com.yorkismine.graphql

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private val base_url = "https://api.stage.hawk.so/graphql"

val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val apolloClient: ApolloClient = ApolloClient.builder()
    .okHttpClient(
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build())
    .serverUrl(base_url)
    .build()

fun getToken(email: String, password: String, whatType: String): String{
    var accessToken = ""
    var refreshToken = ""
    apolloClient.mutate(GetTokenMutation.builder()
        .email(email)
        .password(password)
        .build()).enqueue(object: ApolloCall.Callback<GetTokenMutation.Data>(){
        override fun onFailure(e: ApolloException) {
            e.printStackTrace()
        }

        override fun onResponse(response: Response<GetTokenMutation.Data>) {
            accessToken = response.data()!!.login.accessToken
            refreshToken = response.data()!!.login.refreshToken
        }
    })
    return if (whatType == "A") accessToken
    else refreshToken
}

fun getApollo(): ApolloClient{
    return apolloClient
}

