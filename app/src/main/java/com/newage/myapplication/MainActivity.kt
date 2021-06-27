package com.newage.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isLogInSuccessful()
    }

    private fun isLogInSuccessful(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid==null){
            Handler().postDelayed(Runnable {
                val intent = Intent(this@MainActivity,LogInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            },SPLASH_TIME.toLong())

        }
        else{
            Handler().postDelayed(Runnable {
                startActivity(Intent(this@MainActivity, ChatHomeActivity::class.java))
                finish()
            }, SPLASH_TIME.toLong())
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
