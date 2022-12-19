package com.example.bookapp.Fregment

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.bookapp.Activity.MainActivity
import com.example.bookapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignupFragment : Fragment() {
    lateinit var already_have_an_account: TextView
    lateinit var email: EditText
    lateinit var name: EditText
    lateinit var password: EditText
    lateinit var number: EditText
    lateinit var conform_password: EditText
    lateinit var signup: Button
    lateinit var progressBar: ProgressBar
    lateinit var auth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var frameLayout : FrameLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_signup, container, false)
        already_have_an_account = view.findViewById(R.id.already_have_an_account)
        frameLayout = requireActivity().findViewById<FrameLayout>(R.id.frameLayout)

        email = view.findViewById(R.id.sign_up_email)
        name = view.findViewById(R.id.sign_up_name)
        password = view.findViewById(R.id.sign_up_password)
        number = view.findViewById(R.id.sign_up_number)
        conform_password = view.findViewById(R.id.sign_up_conform_password)
        signup = view.findViewById(R.id.sign_up_ptn)
        progressBar = view.findViewById(R.id.sign_up_progressbar)
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        signup.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            if (!email.text.isEmpty()) {
                if (!name.text.toString().isEmpty()) {
                    if (!number.text.toString().isEmpty() && number.length() == 10) {
                        if (!password.text.toString().isEmpty() && password.length() >= 8) {
                            if (!conform_password.text.toString()
                                    .isEmpty() && password.text.toString()
                                    .equals(conform_password.text.toString())
                            ) {

                                auth.createUserWithEmailAndPassword(
                                    email.text.toString(),
                                    password.text.toString()
                                )
                                    .addOnCompleteListener() { task ->
                                        if (task.isSuccessful) {
                                            val userData: MutableMap<Any, String?> = HashMap()
                                            userData["name"] = name.text.toString()
                                            userData["email"] = email.text.toString()
                                            userData["number"] = number.text.toString()
                                            userData["uid"] = auth.uid
                                            userData["profile"] = ""

                                            firebaseFirestore.collection("USERS")
                                                .document(auth.uid.toString())
                                                .set(userData)
                                                .addOnCompleteListener() { task ->
                                                    if (task.isSuccessful) {
                                                        progressBar.visibility = View.GONE
                                                        startActivity(
                                                            Intent(
                                                                context,
                                                                MainActivity::class.java
                                                            )
                                                        )
                                                        requireActivity().finish()
                                                        Toast.makeText(
                                                            context,
                                                            "Successfully your profile completed",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        var error: String? = task.exception?.message
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                        } else {
                                            var error: String? = task.exception?.message
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                        }

                                    }

                            } else {
                                conform_password.error = "Password does not match"
                                progressBar.visibility = View.GONE
                                conform_password.setText("")
                            }
                        } else {
                            password.error = "Enter Password"
                            progressBar.visibility = View.GONE
                            password.setText("")
                        }
                    } else {
                        number.error = "Enter Phone Number"
                        progressBar.visibility = View.GONE
                        number.setText("")
                    }
                }else{
                    name.error = "Enter Name"
                    progressBar.visibility = View.GONE
                    name.setText("")
                }
            }else{
                email.error = "Enter Email"
                progressBar.visibility = View.GONE
                email.setText("")
            }
        })

        already_have_an_account.setOnClickListener { setFragment(SigniinFregment()) }
        return view
    }

    private fun setFragment(fragment: SigniinFregment) {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right)
        fragmentTransaction.replace(frameLayout.getId(), fragment)
        fragmentTransaction.commit()
    }
}