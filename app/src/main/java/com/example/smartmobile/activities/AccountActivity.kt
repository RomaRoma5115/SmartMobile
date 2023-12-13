package com.example.smartmobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.smartmobile.R
import com.example.smartmobile.databinding.ActivityAccountBinding
import com.example.smartmobile.datasource.ServiceBuilder
import com.example.smartmobile.interfaces.API
import com.example.smartmobile.model.Tariff
import com.example.smartmobile.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserData()
        binding.editButton.setOnClickListener {

            val userId = intent.getStringExtra("USER")!!
            val data = JsonObject()

            val login = binding.loginEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if(login.isNotEmpty()){
                data.addProperty("login", login)
                editUserData(userId,data)
                binding.loginEditText.text!!.clear()
            }
            if(password.isNotEmpty()){
                data.addProperty("password",password)
                editUserData(userId,data)
                binding.passwordEditText.text!!.clear()
            }

        }

        binding.deleteButton.setOnClickListener {
            val userId = intent.getStringExtra("USER")!!
            deleteAccount(userId)
        }

        binding.backButton.setOnClickListener {
            val userId = intent.getStringExtra("USER")!!
            val intent = Intent(this@AccountActivity, MainActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

        binding.adminButton.setOnClickListener {
            val userId = intent.getStringExtra("USER")!!
            val intent = Intent(this@AccountActivity, AdminActivity::class.java)
            intent.putExtra("USER_ID",userId)
            startActivity(intent)
        }

    }

    private fun deleteAccount(userId: String) {
        val api = ServiceBuilder.buildService(API::class.java)
        api.deleteUser(userId).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
               if(response.isSuccessful){
                   val intent = Intent(this@AccountActivity, LoginActivity::class.java)
                   startActivity(intent)
                   Toast.makeText(applicationContext, "Аккаунт удалён", Toast.LENGTH_SHORT).show()
               }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountActivity", t.message.toString())
            }

        })
    }

    private fun editUserData(userId:String, data:JsonObject) {
        val api = ServiceBuilder.buildService(API::class.java)
        api.updateUser(userId,data).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Toast.makeText(applicationContext, "Данные обновлены", Toast.LENGTH_SHORT).show()
                    getUserData()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountActivity", t.message.toString())
            }

        })
    }

    private fun getUserData() {
        val userId = intent.getStringExtra("USER")!!
        val api = ServiceBuilder.buildService(API::class.java)
        api.getUserById(userId).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                      if(response.body()!!.role == "2"){
                          binding.adminButton.visibility = View.VISIBLE
                      } else{
                          binding.adminButton.visibility = View.GONE
                      }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountActivity", t.message.toString())
            }

        })
    }
}