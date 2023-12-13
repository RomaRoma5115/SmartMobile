package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.smartmobile.R
import com.example.smartmobile.databinding.ActivityTariffBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.Tariff
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TariffActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTariffBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTariffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER")!!
            val intent = Intent(this@TariffActivity, AdminActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        binding.addButton.setOnClickListener {
            val tariffName = binding.name.text.toString().trim()
            val tariffDesc = binding.description.text.toString().trim()
            val countSms = binding.countSms.text.toString().trim()
            val countMinutes = binding.countMinutes.text.toString().trim()
            val packageInternet = binding.packageInternet.text.toString().trim()

            if(tariffName.isNotEmpty() && tariffDesc.isNotEmpty()
                && countSms.isNotEmpty() && countMinutes.isNotEmpty()
                && packageInternet.isNotEmpty()) {
                addNewTariff(tariffName,tariffDesc,countSms,countMinutes,packageInternet)
            }

        }
    }

    private fun addNewTariff(name: String, desc: String, sms: String, minutes: String, internet: String) {
        val data = JsonObject()
        data.addProperty("name",name)
        data.addProperty("description",desc)
        data.addProperty("countSms",sms)
        data.addProperty("packageInternet",internet)
        data.addProperty("countMinutes",minutes)
        val api = ServiceBuilder.buildService(API::class.java)
        api.createTariff(data).enqueue(object: Callback<Tariff>{
            override fun onResponse(call: Call<Tariff>, response: Response<Tariff>) {
                if(response.isSuccessful){
                    val userId = intent.getStringExtra("USER")!!
                    val intent = Intent(this@TariffActivity, AdminActivity::class.java)
                    intent.putExtra("USER_ID",userId)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Тариф успешно добавлен", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tariff>, t: Throwable) {
                Log.e("TariffActivity", t.message.toString())
            }

        })
    }
}