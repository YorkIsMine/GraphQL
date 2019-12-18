package com.yorkismine.graphql

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.request.RequestHeaders
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import okhttp3.logging.HttpLoggingInterceptor

class MainActivity : AppCompatActivity() {
    private val base_url = "https://api.stage.hawk.so/graphql"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etLogin = et_login as EditText
        val etPassword = et_password as EditText

        val intent = Intent(this, LobbyActivity::class.java)

        sign_in_btn.setOnClickListener{
            if (etLogin.text.isEmpty() || etPassword.text.isEmpty()) {
                Toast.makeText(this, "Enter all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                getToken(etLogin.text.toString(), etPassword.text.toString(), object: FinishCallBack{
                    override fun finish() {
                        intent.putExtra("TokenA", returnAccess())
                        intent.putExtra("TokenR", returnRefresh())
                        startActivity(intent)
                    }

                })

            }
        }

//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

//        sign_in_btn.setOnClickListener{
//            if (etLogin.text.isEmpty() || etPassword.text.isEmpty()) {
//                Toast.makeText(this, "Enter all fields!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            else{
//                val apolloClient = ApolloClient.builder()
//                    .okHttpClient(OkHttpClient.Builder()
//                        .addInterceptor(interceptor)
//                        .build())
//                    .serverUrl(base_url)
//                    .build()
//                apolloClient.mutate(GetTokenMutation.builder()
//                    .email(etLogin.text.toString())
//                    .password(etPassword.text.toString())
//                    .build()).enqueue(object: ApolloCall.Callback<GetTokenMutation.Data>(){
//                    override fun onFailure(e: ApolloException) {
//                        Toast.makeText(this@MainActivity, "Wrong email or password", Toast.LENGTH_SHORT)
//                            .show()
//                        return
//                    }
//
//                    override fun onResponse(response: Response<GetTokenMutation.Data>) {
//                        var token = response.data()!!.login.accessToken
//                        apolloClient.query(GetNameQuery.builder().build())
//                            .requestHeaders(RequestHeaders.builder()
//                                .addHeader("Authorization", "Bearer $token")
//                                .build())
//                            .enqueue(object: ApolloCall.Callback<GetNameQuery.Data>(){
//                                override fun onResponse(response: Response<GetNameQuery.Data>) {
//                                    intent.putExtra("Name", response.data()!!.me!!.name)
//                                    startActivity(intent)
//                                }
//
//                                override fun onFailure(e: ApolloException) {
//                                    Toast.makeText(this@MainActivity, "Oops!", Toast.LENGTH_SHORT)
//                                        .show()
//                                    return
//                                }
//
//                            })
//                    }
//
//                })
//
//            }
//        }

    }
}
