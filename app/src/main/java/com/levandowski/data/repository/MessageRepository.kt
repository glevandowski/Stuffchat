package com.levandowski.data.repository

import androidx.lifecycle.LiveData
import com.levandowski.StuffChatApplication
import com.levandowski.data.dao.MessageDaoAccess
import com.levandowski.data.database.StuffDataBase
import com.levandowski.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessageRepository @Inject constructor() {

    @Inject
    lateinit var dataBase: StuffDataBase

    private var daoAccess: MessageDaoAccess

    init {
        StuffChatApplication.applicationComponent.inject(this)
        daoAccess = dataBase.messageDaoAccess()
    }

    suspend fun insertTask(message: Message, encrypt: Boolean, password: String?) {
        insertTask(message)
    }

    suspend fun insertTask(message: Message) = withContext(Dispatchers.Default) {
        daoAccess.insertTask(message)
    }

    suspend fun updateTask(message: Message) = withContext(Dispatchers.Default) {
        daoAccess.updateTask(message)
    }

    suspend fun deleteTask(id: Int) = withContext(Dispatchers.Default) {
        getTask(id)?.value?.let {
            daoAccess.deleteTask(it)
        }
    }

    suspend fun deleteTask(message: Message) = withContext(Dispatchers.Default) {
        daoAccess.deleteTask(message)
    }

    fun getTask(id: Int): LiveData<Message>? = daoAccess.getTask(id)

    fun getTasks() = daoAccess.fetchAllTasks()

}