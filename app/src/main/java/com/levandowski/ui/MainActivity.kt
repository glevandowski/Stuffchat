package com.levandowski.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.database.FirebaseListAdapter
import com.levandowski.R
import com.levandowski.model.ChatMessage
import com.google.firebase.database.FirebaseDatabase
import android.text.format.DateFormat
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClickActions()
        route()
    }

    private fun route() {
        when (FirebaseAuth.getInstance().currentUser) {
            null -> startActivityForResult(
                authUI.createSignInIntentBuilder().build(), RESULT_REGISTER)
            else -> {
                "Welcome ${FirebaseAuth.getInstance().currentUser?.email}".showMessage()
                displayChatMessages()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_REGISTER) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    "Successfully signed in. Welcome!".showMessage()
                    displayChatMessages()
                }
                else -> {
                    "We couldn't sign you in. Please try again later.".showMessage()
                    finish()
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sign_out -> {
                authUI.signOut(this)
                    .addOnCompleteListener {
                        "You have been signed out.".showMessage()
                        finish()
                    }
            }
        }
        return true
    }

    private fun displayChatMessages() {
        list_of_messages.adapter =
            object : FirebaseListAdapter<ChatMessage>(
                this,
                ChatMessage::class.java,
                R.layout.message,
                dataBase.reference
            ) {

                override fun populateView(view: View, model: ChatMessage, position: Int) {
                    val id = intArrayOf(R.id.message_text, R.id.message_user, R.id.message_time)

                    id.forEach {
                        view.findViewById<TextView>(it).run {
                            when (it) {
                                id[0] -> text = model.messageText
                                id[1] -> text = model.messageUser
                                id[2] -> text = DateFormat.format("HH:mm", model.messageTime)
                            }
                        }
                    }
                }
            }
    }

    private fun onClickActions() {
        fab.setOnClickListener {
            dataBase.reference.push().setValue(
                firebaseAuth.currentUser?.email?.let {
                    ChatMessage(input.text.toString(), it)
                }
            )
            input.setText("")
        }
    }

    private fun String.showMessage() = Toast.makeText(
        this@MainActivity,
        this,
        Toast.LENGTH_LONG
    ).show()

    companion object {
        private val dataBase = FirebaseDatabase.getInstance()
        private val authUI = AuthUI.getInstance()
        private val firebaseAuth = FirebaseAuth.getInstance()
        private const val RESULT_REGISTER = 39
    }
}
