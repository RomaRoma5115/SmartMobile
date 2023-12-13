package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.smartmobile.adapters.TariffAdminAdapter
import com.example.smartmobile.databinding.ActivityAdminBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.ApiResponse
import com.example.smartmobile.model.Tariff
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    var tariffsList = ArrayList<Tariff>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@AdminActivity, AccountActivity::class.java)
            intent.putExtra("USER",userId)
            startActivity(intent)
        }

        binding.addButton.setOnClickListener {
            val userId = intent.getStringExtra("USER_ID")!!
            val intent = Intent(this@AdminActivity, TariffActivity::class.java)
            intent.putExtra("USER",userId)
            startActivity(intent)
        }
        getAllTariffs()

    }

    private fun getAllTariffs() {
        val api = ServiceBuilder.buildService(API::class.java)
        api.getAllTariffs().enqueue(object: Callback<ApiResponse<Tariff>> {
            override fun onResponse(
                call: Call<ApiResponse<Tariff>>,
                response: Response<ApiResponse<Tariff>>
            ) {
                tariffsList = response.body()!!.result as ArrayList<Tariff>
                val adapter = TariffAdminAdapter(tariffsList)
                binding.recyclerView.adapter = adapter
                adapter.onUpdateClick = {tariff ->
                    val userId = intent.getStringExtra("USER_ID")!!
                    val intent = Intent(this@AdminActivity, TariffEditActivity::class.java)
                    intent.putExtra("USER",userId)
                    intent.putExtra("TARIFF_ID",tariff.tariffId)
                    startActivity(intent)
                }
                adapter.onDeleteClick = {tariff ->
                     api.deleteTariffById(tariff.tariffId.toString()).enqueue(object:Callback<Tariff>{
                         override fun onResponse(call: Call<Tariff>, response: Response<Tariff>) {
                             getAllTariffs()
                             Toast.makeText(applicationContext, "Тариф удален", Toast.LENGTH_SHORT).show()
                         }

                         override fun onFailure(call: Call<Tariff>, t: Throwable) {
                             Log.e("AdminActivity", t.message.toString())
                         }

                     })
                }
            }



            override fun onFailure(call: Call<ApiResponse<Tariff>>, t: Throwable) {
                Log.e("AdminActivity", t.message.toString())
            }

        })
    }
}