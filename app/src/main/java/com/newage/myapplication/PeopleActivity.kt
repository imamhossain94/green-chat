package com.newage.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_home.*
import kotlinx.android.synthetic.main.activity_people.*
import kotlinx.android.synthetic.main.member_people_row.view.*

class PeopleActivity : AppCompatActivity() {

    companion object {
        val USER_KEY = "USER_KEY"
        var total_people: Int? = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        recyclerView_member.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        fetchUsers()

        Picasso.get().load(ChatHomeActivity.currentUser?.userImage).into(chatMemberProfile)

        chatMemberProfile.setOnClickListener(){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }

                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }
                total_people = adapter.itemCount
                recyclerView_member.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}

class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.member_people_row_text.text = user.username
        Picasso.get().load(user.userImage).into(viewHolder.itemView.member_people_row_Profile)
    }

    override fun getLayout(): Int {
        return R.layout.member_people_row
    }
}
