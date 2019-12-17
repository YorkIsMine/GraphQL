package com.yorkismine.graphql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.request.RequestHeaders
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        var tokenA: String = intent.getStringExtra("TokenA")
        var tokenR: String = intent.getStringExtra("TokenR")

        info_btn.setOnClickListener{
            val apollo = getApollo()
            apollo.mutate(RefreshTokenMutation.builder().refreshToken(tokenR).build())
                .enqueue(object: ApolloCall.Callback<RefreshTokenMutation.Data>(){
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(response: Response<RefreshTokenMutation.Data>) {
                        tokenA = response.data()!!.refreshTokens.accessToken
                        tokenR = response.data()!!.refreshTokens.refreshToken
                    }

                })

            apollo.query(GetInfoQuery.builder().build()).requestHeaders(RequestHeaders.builder()
                .addHeader("Authorization", "Bearer $tokenA").build())
                .enqueue(object: ApolloCall.Callback<GetInfoQuery.Data>(){
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(response: Response<GetInfoQuery.Data>) {
                        text.text = response.data()!!.me!!.name
                    }

                })

        }
    }
}
