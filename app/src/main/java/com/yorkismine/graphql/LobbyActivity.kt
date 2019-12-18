package com.yorkismine.graphql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.request.RequestHeaders
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlin.concurrent.thread

class LobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val apollo = Extensions.getApollo()
        info_btn.setOnClickListener{
            apollo.query(GetInfoQuery.builder().build()).requestHeaders(RequestHeaders.builder()
                .addHeader("Authorization", "Bearer ${Extensions.returnAccess()}").build())
                .enqueue(object: ApolloCall.Callback<GetInfoQuery.Data>(){
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(response: Response<GetInfoQuery.Data>) {
                        text.text = response.data()!!.me!!.name
                        Extensions.refreshToken()
                    }

                })

        }

        get_id_btn.setOnClickListener{
            apollo.query(GetInfoQuery.builder().build())
                .requestHeaders(RequestHeaders.builder()
                    .addHeader("Authorization", "Bearer ${Extensions.returnAccess()}").build())
                .enqueue(object: ApolloCall.Callback<GetInfoQuery.Data>(){
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(response: Response<GetInfoQuery.Data>) {
                        text.text = response.data()!!.me!!.id
                        Extensions.refreshToken()
                    }

                })
        }
    }
}
