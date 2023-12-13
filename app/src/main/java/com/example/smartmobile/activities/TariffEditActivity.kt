package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.smartmobile.R
import com.example.smartmobile.databinding.ActivityTariffEditBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.Tariff
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TariffEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTariffEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTariffEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getTariffById()

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER")!!
            val intent = Intent(this@TariffEditActivity, AdminActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        binding.editButton.setOnClickListener {

            val name = binding.name.text.toString().trim()
            val desc = binding.description.text.toString().trim()
            val sms = binding.countSms.text.toString().trim()
            val internet = binding.packageInternet.text.toString().trim()
            val minutes = binding.countMinutes.text.toString().trim()

            if(name.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("name",name)
                editTariff(data)
                binding.name.text.clear()
            }
            if(desc.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("description",desc)
                editTariff(data)
                binding.description.text.clear()
            }
            if(sms.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("countSms",sms)
                editTariff(data)
                binding.countSms.text.clear()
            }
            if(internet.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("packageInternet",internet)
                editTariff(data)
                binding.packageInternet.text.clear()
            }
            if(minutes.isNotEmpty()){
                val data = JsonObject()
                data.addProperty("countMinutes",minutes)
                editTariff(data)
                binding.countMinutes.text.clear()
            }
            getTariffById()
        }

    }

    private fun getTariffById() {
        val api = ServiceBuilder.buildService(API::class.java)
      val tariffId = intent.getStringExtra("TARIFF_ID")!!
        api.getTariffById(tariffId).enqueue(object :Callback<Tariff>{
            override fun onResponse(call: Call<Tariff>, response: Response<Tariff>) {
                if(response.isSuccessful){
                    binding.name.hint = response.body()!!.name
                    binding.description.hint = response.body()!!.description
                    binding.countMinutes.hint = response.body()!!.countMinutes
                    binding.countSms.hint = response.body()!!.countSms
                    binding.packageInternet.hint = response.body()!!.packageInternet
                }
            }
            override fun onFailure(call: Call<Tariff>, t: Throwable) {
                Log.e("TariffEditActivity", t.message.toString())
            }

        })
    }

    private fun editTariff(data: JsonObject) {
        val api = ServiceBuilder.buildService(API::class.java)
        val tariffId = intent.getStringExtra("TARIFF_ID")!!
        api.updateTariffById(tariffId,data).enqueue(object: Callback<Tariff>{
            override fun onResponse(call: Call<Tariff>, response: Response<Tariff>) {

            }

            override fun onFailure(call: Call<Tariff>, t: Throwable) {
                Log.e("TariffEditActivity", t.message.toString())
            }

        })
    }
}