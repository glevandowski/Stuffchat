package com.levandowski.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levandowski.model.User

@Dao
interface UserDaoAccess {

    @Insert
    fun insertTask(user: User): Long?

    @Query("SELECT * FROM User ORDER BY name desc")
    fun fetchAllTasks(): LiveData<List<User>>

    @Query("SELECT * FROM User WHERE token =:token")
    fun getTask(token: String): LiveData<User>

    @Update
    fun updateTask(user: User)

    @Delete
    fun deleteTask(user: User)
}