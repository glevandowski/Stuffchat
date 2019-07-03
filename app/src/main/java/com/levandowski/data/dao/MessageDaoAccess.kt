package com.levandowski.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levandowski.model.Message

@Dao
interface MessageDaoAccess {

    @Insert
    fun insertTask(message: Message): Long?

    @Query("SELECT * FROM Message ORDER BY date desc")
    fun fetchAllTasks(): LiveData<List<Message>>

    @Query("SELECT * FROM Message WHERE id =:taskId")
    fun getTask(taskId: Int): LiveData<Message>

    @Update
    fun updateTask(message: Message)

    @Delete
    fun deleteTask(message: Message)
}