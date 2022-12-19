package com.example.bookapp.Fregment

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookapp.R
import com.google.firebase.auth.FirebaseAuth

class password_reset_Fregment : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var resetEmail: EditText
    lateinit var resetButton: Button
    lateinit var goBack: TextView

    lateinit var linearLayout: LinearLayout
    lateinit var emailIcon: ImageView
    lateinit var emailText: TextView
    lateinit var progressBar: ProgressBar

    lateinit var frameLayout: FrameLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_password_reset__fregment, container, false)

        auth = FirebaseAuth.getInstance()
        resetButton = view.findViewById(R.id.reset_btn)
        resetEmail = view.findViewById(R.id.reset_email)
        goBack = view.findViewById(R.id.reset_go_back)
        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        linearLayout = view.findViewById(R.id.linearLayout)
        emailIcon = view.findViewById(R.id.reset_email_icon)
        emailText = view.findViewById(R.id.reset_email_text)
        progressBar = view.findViewById(R.id.progressBar)

        goBack.setOnClickListener { setFragment(SigniinFregment()) }

        resetButton.setOnClickListener {
            emailText.text = "successfully reset link sent to your registered email"
            TransitionManager.beginDelayedTransition(linearLayout)
            emailText.visibility = View.GONE
            TransitionManager.beginDelayedTransition(linearLayout)
            emailIcon.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            resetButton.isEnabled = false
            resetButton.setTextColor(Color.argb(50, 255, 255, 255))
            if (!resetEmail.text.isEmpty()){
                auth.sendPasswordResetEmail(resetEmail.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Glide.with(emailIcon).load(R.drawable.greem_email).into(emailIcon)
                            emailText.setTextColor(resources.getColor(R.color.successGreen))
                            TransitionManager.beginDelayedTransition(linearLayout)
                            emailText.visibility = View.VISIBLE
                            emailIcon.visibility = View.VISIBLE
                        } else {
                            val error = task.exception!!.message
                            Glide.with(emailIcon).load(R.drawable.red_email).into(emailIcon)
                            emailText.text = error
                            emailText.setTextColor(resources.getColor(R.color.red))
                            TransitionManager.beginDelayedTransition(linearLayout)
                            emailText.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.INVISIBLE
                        resetButton.isEnabled = true
                        resetButton.setTextColor(Color.rgb(255, 255, 255))
                    }
            }else{
                resetEmail.error = "Enter Email"
            }
        }

        return view
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right)
        fragmentTransaction.replace(frameLayout.id, fragment)
        fragmentTransaction.commit()
    }
}