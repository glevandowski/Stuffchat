package com.levandowski.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.levandowski.R
import com.levandowski.ui.chat.ChatFragment

class MainActivity : AppCompatActivity() {

    private var viewModel: MainActivityViewModel? = null

    private val authUI = AuthUI.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        route()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_REGISTER) {
            when (resultCode) {
                Activity.RESULT_OK -> registerUser()
                else -> finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.menu_sign_out -> GlobalScope.launch {
//                removeUser()
//            }
        }
        return true
    }

    private fun registerUser() {
        auth.currentUser?.let {
            viewModel?.addUserFirebase(it)
        }
        openChat()
    }

    private fun route() {
            when (viewModel?.getToken()) {
                null -> {
                    val intent = authUI.createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false).setLogo(R.mipmap.ic_launcher).build()
                    startActivityForResult(intent, RESULT_REGISTER)
                }
                else -> {
                    openChat()
                }
            }
    }

    private fun openChat() {
        supportFragmentManager.beginTransaction().add(ChatFragment(),"chat").commit()
    }

//    private fun removeUser() {
//        viewModel?.run {
//            deleteUserFirebase().addOnSuccessListener {
//                signOut().addOnSuccessListener {
//                    removeUserRepository()
//                }
//            }
//        }?.addOnCompleteListener {
//            finish()
//        }
//    }

    companion object {
        private const val RESULT_REGISTER = 39
    }
}
