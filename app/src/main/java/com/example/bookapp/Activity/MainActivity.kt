package com.example.bookapp.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.Adapter.BannerAdapter
import com.example.bookapp.Adapter.ParentAdapter
import com.example.bookapp.Model.BannerModel
import com.example.bookapp.Model.ChildModel
import com.example.bookapp.Model.ParentModel
import com.example.bookapp.Model.User
import com.example.bookapp.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import de.hdodenhof.circleimageview.CircleImageView
import java.security.AccessController.getContext
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var bannerAdapter: BannerAdapter
    lateinit var parentAdapter: ParentAdapter
    lateinit var bannerRecyclerView: RecyclerView
    lateinit var bookRecyclerView: RecyclerView
    lateinit var bannerModels: ArrayList<BannerModel>
    lateinit var demoBackground: ConstraintLayout
    lateinit var loadingDialog: Dialog
    lateinit var profileDialog: Dialog
    lateinit var name: TextView
    lateinit var profileImage: CircleImageView
    lateinit var profileDialogImage: CircleImageView
    lateinit var profileDialogName: EditText
    lateinit var profileDialogEmail: TextView
    lateinit var profileDialogNumber: TextView
    lateinit var profileDialogSubmit: Button

    lateinit var motivationalList: ArrayList<ChildModel>
    lateinit var educationalList: ArrayList<ChildModel>
    lateinit var businessList: ArrayList<ChildModel>
    lateinit var storylList: ArrayList<ChildModel>
    lateinit var jokesList: ArrayList<ChildModel>
    lateinit var codingList: ArrayList<ChildModel>
    lateinit var parentModel: ArrayList<ParentModel>

    lateinit var filePath: Uri
    var updatePhoto = false

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bannerRecyclerView = findViewById(R.id.banner_recyclerView)
        bookRecyclerView = findViewById(R.id.rv_parent)
        demoBackground = findViewById(R.id.demo_background)
        profileImage = findViewById(R.id.circleImageView)
        name = findViewById(R.id.name)

        motivationalList = ArrayList();
        educationalList = ArrayList();
        businessList = ArrayList();
        storylList = ArrayList();
        jokesList = ArrayList();
        codingList = ArrayList();


        ////////////////loading dialog
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.loading_dialog)
        loadingDialog.setCancelable(false)
        loadingDialog.window!!.setBackgroundDrawable(getDrawable(R.drawable.banner_slider_baground))
        loadingDialog.window!!.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        loadingDialog.show()
        ////////////////loading dialog

        ////////////////profile dialog
        profileDialog = Dialog(this)
        profileDialog.setContentView(R.layout.profile_info_dialog)
        profileDialog.setCancelable(true)
        profileDialog.window!!.setBackgroundDrawable(getDrawable(R.drawable.banner_slider_baground))
        profileDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        profileDialogImage = profileDialog.findViewById(R.id.imageView)
        profileDialogName = profileDialog.findViewById(R.id.name)
        profileDialogEmail = profileDialog.findViewById(R.id.email)
        profileDialogSubmit = profileDialog.findViewById(R.id.submit)
        profileDialogNumber = profileDialog.findViewById(R.id.number)
        ////////////////profile dialog

        val bannerLayout = LinearLayoutManager(this)
        bannerLayout.orientation = RecyclerView.HORIZONTAL
        bannerRecyclerView.layoutManager = bannerLayout
        bannerModels = ArrayList<BannerModel>()

        bannerAdapter = BannerAdapter(this, bannerModels)
        bannerRecyclerView.adapter = bannerAdapter
        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(bannerRecyclerView)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (bannerLayout.findLastCompletelyVisibleItemPosition() < bannerAdapter.itemCount - 1) {
                    bannerLayout.smoothScrollToPosition(
                        bannerRecyclerView,
                        RecyclerView.State(),
                        bannerLayout.findLastCompletelyVisibleItemPosition() + 1
                    )
                } else if (bannerLayout.findLastCompletelyVisibleItemPosition() == bannerAdapter.itemCount - 1) {
                    bannerLayout.smoothScrollToPosition(bannerRecyclerView, RecyclerView.State(), 0)
                }
            }
        }, 0, 4000)

        FirebaseFirestore.getInstance().collection("Banners").get().addOnSuccessListener(
            OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                for (snapshot in queryDocumentSnapshots) {
                    val bannerModel: BannerModel = snapshot.toObject(BannerModel::class.java)
                    bannerModels.add(bannerModel)
                }
                bannerAdapter.notifyDataSetChanged()
            })

        FirebaseFirestore.getInstance().collection("USERS")
            .document(FirebaseAuth.getInstance().uid.toString())
            .get().addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                var user: User? = documentSnapshot.toObject(User::class.java)
                name.setText(user?.name)
                if (user?.profile.equals("")) {
                    Glide.with(this).load(R.drawable.avatarra).into(profileImage)
                } else {
                    try {
                        Glide.with(this).load(user?.profile)
                            .placeholder(R.drawable.avatarra).into(profileImage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                loadingDialog.dismiss()
            })

        FirebaseFirestore.getInstance().collection("Books")
            .get().addOnSuccessListener(
                OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
                    for (snapshot in queryDocumentSnapshots) {
                        val motivational: ChildModel = snapshot.toObject(ChildModel::class.java)
                        val educational: ChildModel = snapshot.toObject(ChildModel::class.java)
                        val business: ChildModel = snapshot.toObject(ChildModel::class.java)
                        val story: ChildModel = snapshot.toObject(ChildModel::class.java)
                        val jokes: ChildModel = snapshot.toObject(ChildModel::class.java)
                        val coding: ChildModel = snapshot.toObject(ChildModel::class.java)
                        if (motivational.title.equals("Motivational")) {
                            motivationalList.add(motivational)
                        }
                        if (educational.title.equals("Educational")) {
                            educationalList.add(educational)
                        }
                        if (coding.title.equals("Coding")) {
                            codingList.add(coding)
                        }
                        if (business.title.equals("Business")) {
                            businessList.add(business)
                        }
                        if (story.title.equals("Story")) {
                            storylList.add(story)
                        }
                        if (jokes.title.equals("Jokes")) {
                            jokesList.add(jokes)
                        }
                        parentModel = ArrayList<ParentModel>()
                        val bookLayout = LinearLayoutManager(this@MainActivity)
                        bookLayout.orientation = RecyclerView.VERTICAL
                        bookRecyclerView.layoutManager = bookLayout

                        parentAdapter = ParentAdapter(this@MainActivity, parentModel)
                        bookRecyclerView.adapter = parentAdapter
                        demoBackground.visibility = View.GONE
                        bannerRecyclerView.visibility = View.VISIBLE
                        loadingDialog.dismiss()

                        parentModel.add(ParentModel("Motivational", motivationalList))
                        parentModel.add(ParentModel("Educational", educationalList))
                        parentModel.add(ParentModel("Business", businessList))
                        parentModel.add(ParentModel("Story", storylList))
                        parentModel.add(ParentModel("Jokes", jokesList))
                        parentModel.add(ParentModel("Coding", codingList))


                        profileImage.setOnClickListener(View.OnClickListener {
                            profileDialog.show()

                            FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().uid.toString())
                                .get()
                                .addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                                    var user: User? = documentSnapshot.toObject(User::class.java)
                                    profileDialogName.setText(user?.name)
                                    profileDialogEmail.setText(user?.email)
                                    profileDialogNumber.setText("+91 "+user?.number)

                                    profileDialogImage.setOnClickListener(View.OnClickListener {
                                        Dexter.withActivity(this)
                                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                            .withListener(object : PermissionListener {
                                                override fun onPermissionGranted(
                                                    permissionGrantedResponse: PermissionGrantedResponse?
                                                ) {
                                                    val intent = Intent(Intent.ACTION_PICK)
                                                    intent.type = "image/*"
                                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
                                                }

                                                override fun onPermissionDenied(
                                                    permissionDeniedResponse: PermissionDeniedResponse?
                                                ) {
                                                }

                                                override fun onPermissionRationaleShouldBeShown(
                                                    permissionRequest: PermissionRequest?,
                                                    permissionToken: PermissionToken
                                                ) {
                                                    permissionToken.continuePermissionRequest()
                                                }
                                            })
                                            .check()
                                    })

                                    if (user?.profile.equals("")) {
                                        Glide.with(this).load(R.drawable.avatarra)
                                            .into(profileDialogImage)
                                    } else {
                                        try {
                                            Glide.with(this).load(user?.profile)
                                                .placeholder(R.drawable.avatarra)
                                                .into(profileDialogImage)


                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                    loadingDialog.dismiss()
                                })
                        })
                    }
                })

        profileDialogSubmit.setOnClickListener {
            loadingDialog.show()
            updatePhoto()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filePath = data!!.data!!
            updatePhoto = true
            Glide.with(this).load(filePath).into(profileDialogImage)
        }
    }

    private fun updatePhoto() {
        if (updatePhoto) {
            if (filePath != null) {
                val reference: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("profiles")
                        .child(FirebaseAuth.getInstance().uid.toString())
                reference.putFile(filePath).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            if (task.isSuccessful) {
                                filePath = uri

                                val updateProfile: MutableMap<String, Any> =
                                    HashMap()
                                updateProfile["email"] = profileDialogEmail.getText().toString()
                                updateProfile["name"] = profileDialogName.getText().toString()
                                updateProfile["profile"] = filePath
                                FirebaseFirestore.getInstance().collection("USERS")
                                    .document(FirebaseAuth.getInstance().uid.toString())
                                    .update(updateProfile).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this,MainActivity::class.java))
                                        } else {
                                            val error = task.exception?.message
                                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                                        }
                                        loadingDialog.dismiss()
                                        profileDialog.dismiss()
                                    }
                            } else {
                                loadingDialog.dismiss()
                                profileDialog.dismiss()
                                val error = task.exception!!.message
                                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else { ///remove photo
                val updateProfile: MutableMap<String, Any> = HashMap()
                updateProfile["email"] = profileDialogEmail.getText().toString()
                updateProfile["name"] = profileDialogName.getText().toString()
                updateProfile["profile"] = ""
                FirebaseFirestore.getInstance().collection("USERS")
                    .document(FirebaseAuth.getInstance().uid!!)
                    .update(updateProfile).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        } else {
                            val error = task.exception!!.message
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        loadingDialog.dismiss()
                        profileDialog.dismiss()
                    }
            }
        } else {
            val updateProfile: MutableMap<String, Any> = HashMap()
            updateProfile["name"] = profileDialogName.getText().toString()
            updateProfile["email"] = profileDialogEmail.getText().toString()
            FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().uid!!)
                .update(updateProfile).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (updateProfile.size > 1) {

                            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        } else {
                            val error = task.exception!!.message
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        loadingDialog.dismiss()
                        profileDialog.dismiss()
                    }
                }
        }
    }
}