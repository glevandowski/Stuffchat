package com.levandowski.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.levandowski.model.Message
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.levandowski.data.FirebaseManager
import com.levandowski.model.User
import com.levandowski.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_bottom_component.*
import kotlinx.android.synthetic.main.chat_toolbar.*
import kotlinx.android.synthetic.main.chat_toolbar.view.*
import java.util.Date
import com.levandowski.R
import com.levandowski.adapter.chat.ChatAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val manager: FirebaseManager = FirebaseManager()
    private var adapter: ChatAdapter? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(chat_toolbar)
        onClickActions()
        route()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_REGISTER) {
            when (resultCode) {
                Activity.RESULT_OK -> registerUser()
                else -> {
                    "We couldn't sign you in. Please try again later.".showMessage()
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> removeUser()
        }
        return true
    }

    private fun registerUser() {
        manager.run {
            add(COLLECTION_USER, getCurrentUser()?.email.toString(), buildUser()).addOnSuccessListener {
                "Successfully signed in. Welcome!".showMessage()
                loadData()
            }
        }
    }

    private fun route() {
        when (manager.getCurrentUser()) {
            null -> startActivityForResult(manager.getIntentNewCredentials(), RESULT_REGISTER)
            else -> {
                "Welcome ${manager.getCurrentUser()?.email}".showMessage()
                loadData()
            }
        }
    }

    private fun loadData() {
        loadMessages()
        loadToolbar()
    }

    private fun loadMessages() {
        val query =
            manager.querySubCollection(COLLECTION_USER, manager.getCurrentUser()?.email.toString(), COLLECTION_CHAT )

        adapter = ChatAdapter(
            FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message::class.java).build()
        )

        adapter?.let {
            setupRecyclerView(it)
            it.startListening()
        }
    }

    private fun loadToolbar() {
        setUserToolbar()
        setUserActive()
    }

    private fun setUserToolbar() {
        Picasso.get().load(manager.getCurrentUser()?.photoUrl).transform(CircleTransform()).into(chat_toolbar.image_user)
        chat_toolbar.user_name.text = manager.getCurrentUser()?.displayName
    }

    private fun setUserActive() {
        var users = ""
        manager.query(COLLECTION_USER).addOnSuccessListener {
            it.documents.forEach { document ->
                    users = when (users.isEmpty()) {
                        true ->  document.toObject(User::class.java)?.name.toString()
                        else -> "${document.toObject(User::class.java)?.name}},$users"
                    }
            }.run {
                chat_toolbar.user_active.text = users
            }
        }
    }

    private fun setupRecyclerView(chatAdapter: ChatAdapter?) = list_of_messages.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
        }

    private fun removeUser() {
        manager.run {
            deleteAllDocumentSubCollection(
                COLLECTION_USER,
                manager.getCurrentUser()?.email.toString(),
                COLLECTION_CHAT
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    "You have been signed out.".showMessage()
                    signOut(this@MainActivity).addOnSuccessListener { finish() }
                }
            }
        }
    }

    private fun onClickActions() {
        fab.setOnClickListener {
            manager.run {
                    addSubCollection(COLLECTION_USER, getCurrentUser()?.email.toString(), COLLECTION_CHAT, buildMessage())
                        .addOnCompleteListener { adapter?.let { chatAdapter ->
                            list_of_messages.scrollToPosition(chatAdapter.snapshots.lastIndex)
                        }
                    }
            }
            input.setText("")
        }
    }

    private fun buildMessage() = Message().apply {
        messageText = input.text.toString()
        messageTime = Date().time
    }

    private fun buildUser() = User().apply {
        manager.getCurrentUser()?.run {
            this@apply.email = email.toString()
            this@apply.name = displayName.toString()
            this@apply.phone = phoneNumber.toString()
            this@apply.photoURL = photoUrl.toString()
        }
    }

    private fun String.showMessage() = Toast.makeText(
        this@MainActivity,
        this,
        Toast.LENGTH_LONG
    ).show()

    companion object {
        private const val RESULT_REGISTER = 39
        private const val COLLECTION_CHAT = "chat"
        private const val COLLECTION_USER = "users"
    }
}
