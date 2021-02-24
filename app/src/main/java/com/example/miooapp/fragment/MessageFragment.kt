package com.example.miooapp.fragment



import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.miooapp.LatestMessageRow

import com.example.miooapp.R
import com.example.miooapp.model.ChatMessage
import com.example.miooapp.activity.NewMessageActivity
import com.example.miooapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_message.view.*


class MessageFragment: Fragment() {
    private val TAG = "LatestMessagesActivity"



    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    val adapter = GroupAdapter<GroupieViewHolder>()

    companion object {
        var currentUser: User? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_message, container, false)
               val toolbar=view.findViewById<Toolbar>(R.id.toolbar)


    setHasOptionsMenu(true)
        adapter.setOnItemClickListener { item, view ->
             val intent = Intent(context, NewMessageActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        view.recyclerview_latest_messages.adapter = adapter
        view.recyclerview_latest_messages.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        )

        listenForLatestMessages()
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        fetchCurrentUser()
        verifyUserIsLoggedIn()

        return view
    }




    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = auth.uid
        val ref = database.getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }



    private fun fetchCurrentUser() {
        val uid = auth.uid
        val ref = database.getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)

            }
        })
    }

    private fun verifyUserIsLoggedIn() {
        auth = FirebaseAuth.getInstance()
        val uid = auth.uid

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meni,menu)
        return super.onCreateOptionsMenu(menu,inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(requireContext(), NewMessageActivity::class.java)
                startActivity(intent)
              //  Navigation.findNavController(requireView()).navigate(com.example.miooapp.R.id.action_messageFragment_to_newMessageFragment)
            }
            R.id.menu_sign_out -> {
                val intent = Intent(requireContext(), FiristActivity::class.java)
                startActivity(intent)
                auth.signOut()

            }

            else -> {

            }
        }


        return super.onOptionsItemSelected(item)
    }


}