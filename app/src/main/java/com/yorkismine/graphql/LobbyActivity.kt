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
        var token: String? = intent.getStringExtra("Token")

        info_btn.setOnClickListener{
            getApollo().mutate(RefreshTokenMutation.builder().refreshToken(token!!).build())
                .enqueue(object: ApolloCall.Callback<RefreshTokenMutation.Data>(){
                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(response: Response<RefreshTokenMutation.Data>) {
                        token = response.data()!!.refreshTokens.refreshToken
                        getApollo().query(GetInfoQuery.builder().build())
                            .requestHeaders(RequestHeaders.builder()
                                .addHeader("Authorization", "Bearer ${response.data()!!.refreshTokens.refreshToken}").build())
                            .enqueue(object: ApolloCall.Callback<GetInfoQuery.Data>(){
                                override fun onFailure(e: ApolloException) {
                                    e.printStackTrace()
                                }

                                override fun onResponse(response: Response<GetInfoQuery.Data>) {
                                    val infoText = "id: ${response.data()!!.me!!.id}\n"
                                    val text2 =
                                        "name: ${response.data()!!.me!!.name}\n"
                                    val text3 =
                                        "email: ${response.data()!!.me!!.email}"
                                    text.text = infoText
                                }

                            })
                    }

                })

        }
    }
}
