package com.example.miooapp


import com.example.miooapp.model.ChatMessage
import com.example.miooapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.lates_mesage_row.view.*


class LatestMessageRow(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    var chatPartnerUser: User?= null

    override fun getLayout(): Int {
        return R.layout.lates_mesage_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_message_row.text = chatMessage.text

        val chatPartnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = chatMessage.toId
        } else {
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java)
                chatPartnerUser?.username
                viewHolder.itemView.username_textview_latest_message_row.text = chatPartnerUser?.username


        viewHolder.itemView.username_textview_latest_message_row.text = "Some time"
    }
            }) }}
