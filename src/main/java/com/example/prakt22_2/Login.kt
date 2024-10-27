package com.example.prakt22_2

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.prakt22_2.Data.AppDatabase
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    private lateinit var pref: SharedPreferences
    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var signin: Button
    private lateinit var regist: TextView
    private lateinit var userDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        login = findViewById(R.id.login)
        password = findViewById(R.id.password)
        signin = findViewById(R.id.registration)
        regist = findViewById(R.id.register_click)

        userDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "user_database"
        ).build()


        regist.setOnClickListener {
            var intent = Intent(this, Registratio::class.java)
            startActivity(intent)
        }

        signin.setOnClickListener {
            if (login.text.isNotEmpty() && password.text.isNotEmpty()) {
                val userLogin = login.text.toString()
                val userPassword = password.text.toString()

                if (userPassword.length <= 8) {

                    CoroutineScope(Dispatchers.IO).launch {
                        val userDao = userDatabase.userDao()
                        val user = userDao.getUserByUsername(userLogin)

                        withContext(Dispatchers.Main) {
                            if (user == null || user.password != userPassword) {
                                showSnackbar("Неверный логин или пароль")
                            } else {
                                showSnackbar("Вход выполнен")
                                val dbName = "${userLogin}_database"
                                val intent = Intent(this@Login, MainActivity::class.java)
                                intent.putExtra("DB_NAME", dbName)
                                startActivity(intent)

                            }
                        }
                    }
                }
                else {
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
