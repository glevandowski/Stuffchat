package com.levandowski.data.repository

import androidx.lifecycle.LiveData
import com.levandowski.StuffChatApplication
import com.levandowski.data.dao.UserDaoAccess
import com.levandowski.data.database.StuffDataBase
import com.levandowski.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor() {

    @Inject
    lateinit var dataBase: StuffDataBase

    private var daoAccess: UserDaoAccess

    init {
        StuffChatApplication.applicationComponent.inject(this)
        daoAccess = dataBase.userDaoAccess()
    }

    suspend fun insertTask(user: User, encrypt: Boolean, password: String?) {
        insertTask(user)
    }

    suspend fun insertTask(user: User)= coroutineScope {
        daoAccess.insertTask(user)
    }

    suspend fun updateTask(user: User) {
        daoAccess.updateTask(user)
    }

    suspend fun deleteTask(token: String) = coroutineScope {
        getTask(token)?.value?.let {
           daoAccess.deleteTask(it)
        }
    }

    fun getTask(token: String): LiveData<User>? = daoAccess.getTask(token)

    fun getTasks() = daoAccess.fetchAllTasks()
}
