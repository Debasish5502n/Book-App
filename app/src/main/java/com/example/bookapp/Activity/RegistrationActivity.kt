package com.example.bookapp.Activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bookapp.Fregment.SigniinFregment
import com.example.bookapp.Fregment.SignupFragment
import com.example.bookapp.R

class RegistrationActivity : AppCompatActivity() {
    var frameLayout: FrameLayout? = null
    companion object{
        var signUpFragment = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        frameLayout = findViewById(R.id.frameLayout)
        if (signUpFragment) {
            signUpFragment = false
            setFragment(SignupFragment())
        } else {
            setDefaultFregment(SigniinFregment())
        }
    }

    private fun setDefaultFregment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(frameLayout!!.id, fragment)
        fragmentTransaction.commit()
    }

    fun setFragment(fragment: Fragment?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left)
        fragmentTransaction.replace(frameLayout!!.id, fragment!!)
        fragmentTransaction.commit()
    }
}