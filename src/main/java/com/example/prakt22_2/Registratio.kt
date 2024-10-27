package com.example.prakt22_2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.example.prakt22_2.Data.AppDatabase
import com.example.prakt22_2.Data.UserEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Registratio : AppCompatActivity() {
    private lateinit var pref: SharedPreferences
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var signin: Button
    private lateinit var userDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registratio)
        login = findViewById(R.id.login)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.registration)

        userDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()

        signin.setOnClickListener {
            if (login.text.isNotEmpty() && password.text.isNotEmpty()) {
                val userLogin = login.text.toString()
                val userPassword = password.text.toString()

                if (userPassword.length <= 8) {

                    CoroutineScope(Dispatchers.IO).launch {
                        val userDao = userDatabase.userDao()
                        val existingUser = userDao.getUserByUsername(userLogin)

                        if (existingUser == null) {
                            val newUser = UserEntity(username = userLogin, password = userPassword)
                            userDao.insertUser(newUser)

                            withContext(Dispatchers.Main) {
                                showSnackbar("Регистрация успешна")
                                val dbName = "${userLogin}_database"
                                val intent = Intent(this@Registratio, MainActivity::class.java)
                                intent.putExtra("DB_NAME", dbName)
                                startActivity(intent)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                showSnackbar("Пользователь уже существует")
                            }
                        }
                    }
                }else {
                    showSnackbar("Пароль не должен превышать 8 символов")
                }
            } else {
                showSnackbar("Обнаружены пустые поля")
            }
        }


    }

    private fun showSnackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}