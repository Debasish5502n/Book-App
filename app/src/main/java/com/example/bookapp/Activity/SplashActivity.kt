package com.example.bookapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()

        val time: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val currentUser: FirebaseUser? = firebaseAuth.getCurrentUser()
                    if (currentUser == null) {
                        startActivity(Intent(this@SplashActivity, RegistrationActivity::class.java))
                    } else {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
        time.start()
    }
}