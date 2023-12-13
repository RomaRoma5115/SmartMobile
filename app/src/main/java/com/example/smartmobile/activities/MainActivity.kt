package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.smartmobile.R
import com.example.smartmobile.adapters.TariffAdapter
import com.example.smartmobile.databinding.ActivityMainBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.ApiResponse
import com.example.smartmobile.model.Tariff
import com.example.smartmobile.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var tariffsList = ArrayList<Tariff>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllTariffs()
        getUserData()

        binding.accountSettingsButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@MainActivity, AccountActivity::class.java)
            intent.putExtra("USER",userId)
            startActivity(intent)
        }
    }

    private fun getUserData() {
        val userId = intent.getStringExtra("USER_ID")!!
        val api = ServiceBuilder.buildService(API::class.java)
        api.getUserById(userId).enqueue(object:Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    binding.userName.text = response.body()!!.login
                     api.getTariffById(response.body()!!.tariff!!).enqueue(object : Callback<Tariff>{
                         override fun onResponse(call: Call<Tariff>, response: Response<Tariff>) {
                             binding.userTariff.text = response.body()!!.name
                         }
                         override fun onFailure(call: Call<Tariff>, t: Throwable) {
                             Log.e("MainActivity", t.message.toString())
                         }

                     })
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("MainActivity", t.message.toString())
            }

        })
    }

    private fun getAllTariffs() {
        val api = ServiceBuilder.buildService(API::class.java)
        api.getAllTariffs().enqueue(object:Callback<ApiResponse<Tariff>>{
            override fun onResponse(
                call: Call<ApiResponse<Tariff>>,
                response: Response<ApiResponse<Tariff>>
            ) {

                tariffsList = response.body()!!.result as ArrayList<Tariff>
                val adapter = TariffAdapter(tariffsList){tariffId ->
                    val data = JsonObject()
                    val userId = intent.getStringExtra("USER_ID")!!
                    data.addProperty("tariff", tariffId)
                    api.updateUser(userId,data).enqueue(object : Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if(response.isSuccessful){
                                getUserData()
                            }
                        }
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.e("MainActivity", t.message.toString())
                        }
                    })
                }

                binding.recyclerView.adapter = adapter
            }
            override fun onFailure(call: Call<ApiResponse<Tariff>>, t: Throwable) {
                Log.e("MainActivity", t.message.toString())
            }

        })
    }
}