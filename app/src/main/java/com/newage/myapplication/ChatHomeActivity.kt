package com.newage.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_home.*


class ChatHomeActivity : AppCompatActivity() {

    //var myDialog: Dialog? = null

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
        var total_new_message: Int? = 0
    }


    val latestMessagesMap = HashMap<String, ChatMessage>()

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_home)

        recyclerView_chat_home.adapter = adapter
        recyclerView_chat_home.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(this, ChatLogActivity::class.java)

            val row = item as LatestMessageRow
            intent.putExtra(PeopleActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }


        listenForLatestMessages()
        fetchCurrentUser()
        isLogInSuccessful()
        allClickingEvents()
    }

    private fun allClickingEvents(){
        chatHomeProfile.setOnClickListener(){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        chatHomePeople.setOnClickListener(){
            val intent = Intent(this,PeopleActivity::class.java)
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)

                Picasso.get().load(currentUser?.userImage).into(chatHomeProfile)

                chatHomeTitle.text = currentUser?.username

                Log.d("LatestMessages", "Current user ${currentUser?.userImage}")
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }


    private fun isLogInSuccessful(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid==null){
            val intent = Intent(this,SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
            total_new_message = adapter.itemCount
        }
    }


    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(i: DataSnapshot, p1: String?) {
                val chatMessage = i.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[i.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }


}
