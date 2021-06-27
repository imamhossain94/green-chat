package com.newage.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_profile)


        Picasso.get().load(ChatHomeActivity.currentUser?.userImage).into(profile_Image)
        profile_Username.text = ChatHomeActivity.currentUser?.username
        profileEmail.text = ChatHomeActivity.currentUser?.email
        profile_TotalMessage.text = ChatHomeActivity.total_new_message.toString()
        profile_TotalPeople.text = PeopleActivity.total_people.toString()


        profileClose.setOnClickListener{
            onBackPressed()
        }

        profile_Log_Out_Button.setOnClickListener {
            LogOutThisSeation()
        }

    }

    private fun LogOutThisSeation(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    override fun onBackPressed() {
        finish()
        overridePendingTransition(0,0)
    }

}