package com.newage.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SignUpProfile.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }

        signUpButton.setOnClickListener(){
            createNewUser()
        }

        alreadyHaveAnAccount.setOnClickListener(){
            startActivity(Intent(this@SignUpActivity,LogInActivity::class.java))
        }
    }

    private fun createNewUser(){
        val username = signUpUsername.text.toString()
        if(username.isEmpty()){
            signUpUsername.error = "Please enter username"
        }
        val email = SignUpEmail.text.toString()
        if(email.isEmpty()){
            SignUpEmail.error ="Please enter an valid email"
        }
        val password = signUpPassword.text.toString()
        val passwordCheck = signUpReTypePassword.text.toString()
        if(password != passwordCheck){
            signUpPassword.error = "Password not matched"
            signUpReTypePassword.error = "Password not matched"
        }else if(password.isEmpty() || passwordCheck.isEmpty()){
            signUpPassword.error = ""
            signUpReTypePassword.error = ""
        }

        //FireBase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                uploadImageToDatabase()
                // else if successful
                // Log.d("Main", "Successfully created user with uid: ${it.result?.user?.uid}")
                Toast.makeText(this, "Successfully created user with uid: ${it.result?.user?.uid}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data!=null){
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            //val bitmapDrawable = BitmapDrawable(bitmap)
            //SignUpProfile.setBackgroundDrawable(bitmapDrawable)
            SignUpProfile.setImageBitmap(bitmap)
        }

    }

    private fun uploadImageToDatabase(){
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    uploadUserToDatabase(it.toString())
                }
            }
            .addOnFailureListener {

            }
    }

    private fun uploadUserToDatabase(userImage: String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,signUpUsername.text.toString(),SignUpEmail.text.toString(),signUpPassword.text.toString(),userImage)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Main", "done:")
                val intent = Intent(this@SignUpActivity,ChatHomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(17432578,17432579)
    }

}

