package com.example.bookapp.Fregment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.bookapp.Activity.MainActivity
import com.example.bookapp.R
import com.google.firebase.auth.FirebaseAuth

class SigniinFregment : Fragment() {

    lateinit var dont_have_an_account: TextView
    lateinit var password_reset: TextView
    lateinit var frameLayout: FrameLayout

    lateinit var password: EditText
    lateinit var email: EditText
    lateinit var close: ImageView
    lateinit var signin: Button
    lateinit var progressBar: ProgressBar

    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_signiin_fregment, container, false)

        dont_have_an_account = view.findViewById(R.id.sign_up)
        frameLayout = requireActivity().findViewById(R.id.frameLayout)

        email = view.findViewById(R.id.sign_in_email)
        password = view.findViewById(R.id.sign_in_password)
        close = view.findViewById(R.id.sign_in_skip)
        signin = view.findViewById(R.id.sign_in_btn)
        progressBar = view.findViewById(R.id.sign_in_progressbar)
        password_reset = view.findViewById(R.id.sign_in_reset_password)

        auth = FirebaseAuth.getInstance()

        signin.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            if (!email.text.isEmpty()) {
                    if (!password.text.toString().isEmpty() && password.length() >= 8) {

                        auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    progressBar.visibility = View.GONE
                                    startActivity(Intent(context, MainActivity::class.java))
                                    requireActivity().finish()
                                    Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    var error : String? = task.exception?.message
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            password.error = "Wrong Password!"
                        }
                    }else{
                        email.error = "Enter Email"
                    }
        })

        dont_have_an_account.setOnClickListener { setFragment(SignupFragment()) }
        password_reset.setOnClickListener { setFragment(password_reset_Fregment()) }
        return view
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right)
        fragmentTransaction.replace(frameLayout.getId(), fragment)
        fragmentTransaction.commit()
    }
}