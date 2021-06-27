package com.newage.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_log_in.*

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        logInButton.setOnClickListener(){
            //startActivity(Intent(this,PeopleActivity::class.java))
            logInAction()
        }

          doNotHaveAnAccount.setOnClickListener(){
            startActivity(Intent(this@LogInActivity,SignUpActivity::class.java))
            overridePendingTransition(17432578,17432579)
        }

    }

    private fun logInAction(){
        val email = logInEmail.text.toString()
        val password = logInPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

                val intent = Intent(this, ChatHomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

}
