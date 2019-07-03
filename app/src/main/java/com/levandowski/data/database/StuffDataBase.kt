package com.levandowski.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.levandowski.data.dao.MessageDaoAccess
import com.levandowski.model.User
import com.levandowski.data.dao.UserDaoAccess
import com.levandowski.model.Message

@Database(entities = [User::class, Message::class], version = 1, exportSchema = false)
abstract class StuffDataBase: RoomDatabase() {

    abstract fun userDaoAccess(): UserDaoAccess

    abstract fun messageDaoAccess(): MessageDaoAccess
}