package com.newage.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_prople_profile.*

class PropleProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prople_profile)

        Picasso.get().load(ChatLogActivity.toUser?.userImage).into(people_profile_Image)
        people_profile_Username.text = ChatLogActivity.toUser?.username
        people_profile_Email.text = ChatLogActivity.toUser?.email

        people_new_age_bd.setOnClickListener{
            Toast.makeText(this,"Team: Green University\n" +
                    "Programmer: Nazmus Shakib\n" +
                    "Backend dev: Shakil Ahsan\n" +
                    "Designer: Md. Sumon Fakir\n" +
                    "Institution: GUB",Toast.LENGTH_LONG).show()
        }

        People_profile_Close.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0,0)
    }
}

