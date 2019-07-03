package com.levandowski.ui.chat

import androidx.lifecycle.ViewModel
import com.levandowski.StuffChatApplication
import com.levandowski.data.FirebaseManager
import com.levandowski.data.FirebaseManager.Companion.COLLECTION_CHAT
import com.levandowski.data.FirebaseManager.Companion.COLLECTION_USER
import com.levandowski.data.repository.UserRepository
import com.levandowski.model.Message
import com.levandowski.model.User
import kotlinx.coroutines.SupervisorJob
import java.util.*
import javax.inject.Inject

class ChatViewModel : ViewModel() {

    @Inject
    lateinit var manager: FirebaseManager

//    @Inject
//    lateinit var userRepository: UserRepository
//
    private var user: User? = null

    private val viewModelJob = SupervisorJob()

    init {
        StuffChatApplication.applicationComponent.inject(this)
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    private fun queryUsers()= manager.query(COLLECTION_USER)

    fun getUserActive(): String {
        queryUsers().addOnSuccessListener { query ->
            var users = ""
            query.documents.forEach { document ->
                users = when (users.isEmpty()) {
                    true -> document.toObject(User::class.java)?.name.toString()
                    else -> "${document.toObject(User::class.java)?.name}},$users"
                }
            }.run {
                return@run users
            }
        }
        return ""
    }

    fun messagesQuery() =
        manager.querySubCollection(
            COLLECTION_USER, user?.email.toString(), COLLECTION_CHAT
        )

    fun sendMessage(string: String) =
        manager.addSubCollection(
            COLLECTION_USER, user?.email.toString(),
            COLLECTION_CHAT, buildMessage(string)
        )

    private fun buildMessage(string: String) = Message().apply {
        name = user?.name.toString()
        messageText = string
        messageTime = Date().time
    }

    fun getUser() = user
}
