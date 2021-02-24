package com.example.miooapp.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.miooapp.R
import com.example.miooapp.fragment.MessageFragment
import com.example.miooapp.model.ChatMessage
import com.example.miooapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

import kotlinx.android.synthetic.main.chat_from_row.view.textView
import java.io.IOException


class ChatActivity : AppCompatActivity() {
    val PICK_CONTACT_REQUEST =  20
    private val TAG = "ChatLogActivity"
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    var uri: Uri?=null
    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        recyclerview_chat_log.adapter = adapter
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        supportActionBar?.title = toUser?.username


        listenForMessages()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Send button clicked!")

            performSendMessage()

        }
//        floatingChatBotom.setOnClickListener {
//            val intent = Intent().apply {
//                type = "image/*"
//                action = Intent.ACTION_GET_CONTENT
//            }
//            startActivityForResult(Intent.createChooser(intent, "Select Image"), 2)
//        }
//
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.data != null) {
//            uri = data.data
//            try {
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//              //  imageview_chat_to_row.setImageBitmap(bitmap)
//                //   ImageChatToRow.setImageBitmap(bitmap)
//                floatingChatBotom.alpha = 0f
//
//
//            } catch (e: IOException) {
//                // Log the exception
//                e.printStackTrace()
//            }
//        }
    }


    private fun performSendMessage() {
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val text = edittext_chat_log.text.toString()



        val fromId = auth.uid
        val toId = user!!.uid

        if (fromId === null)return

//        val ref = database.getReference("/messages").push()
        val ref = database.getReference("/user-messages/$fromId/$toId").push()
        val toRef = database.getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(
            ref.key!!,
            text,
            fromId,
            toId
        )
        ref.setValue(chatMessage).addOnSuccessListener {
            edittext_chat_log.text.clear()
            recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            Log.d(TAG, "Saved out chat message: ${ref.key}")
        }
        toRef.setValue(chatMessage)

        val latestMessageRef = database.getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = database.getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }





    private fun listenForMessages() {
        val fromId = auth.uid
        val toId = toUser?.uid
        val ref = database.getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    if (chatMessage.toId == auth.uid) {
                        adapter.add(ChatFromItem(chatMessage.text, toUser!!))
                    } else {
                        val currentUser = MessageFragment.currentUser
                        adapter.add(ChatToItem(chatMessage.text, currentUser!!))
                    }
                    Log.d(TAG, chatMessage.text)
                }
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}

class ChatFromItem(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView.text = text


    }
}

class ChatToItem(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView.text = text


    }
}