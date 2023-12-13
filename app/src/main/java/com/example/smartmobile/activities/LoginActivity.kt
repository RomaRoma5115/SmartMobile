package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.smartmobile.databinding.ActivityLoginBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.regButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

         val login = binding.loginEditText.text.toString().trim()
         val password = binding.passwordEditText.text.toString().trim()

            if(login.isNotEmpty() && password.isNotEmpty()){
                signIn(login,password)
            }

        }
    }

    private fun signIn(login: String, password: String) {
        val data = JsonObject()
        data.addProperty("login",login)
        data.addProperty("password",password)
        val api = ServiceBuilder.buildService(API::class.java)
        api.login(data).enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("USER_ID",response.body()!!.userId)
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
               Log.e("LoginActivity", t.message.toString())
            }

        })
    }
}