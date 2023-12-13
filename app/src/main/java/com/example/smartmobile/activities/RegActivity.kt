package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.smartmobile.databinding.ActivityRegBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent = Intent(this@RegActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.regButton.setOnClickListener {
            val login = binding.loginEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if(login.isNotEmpty() && password.isNotEmpty()){
                registration(login, password)
            }

        }

    }

    private fun registration(login: String, password: String) {
        val data = JsonObject()
        data.addProperty("login",login)
        data.addProperty("password",password)
        val api = ServiceBuilder.buildService(API::class.java)
        api.registration(data).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
               if(response.isSuccessful){
                   Toast.makeText(applicationContext, "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show()
                   val intent = Intent(this@RegActivity,LoginActivity::class.java)
                   startActivity(intent)
               }
                if(response.code() == 409){
                    Toast.makeText(applicationContext, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
               Log.e("RegActivity", t.message.toString())
            }

        })
    }
}