package com.levandowski.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Message: Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "name")
    var name:String = ""

    @ColumnInfo(name = "message")
    var messageText: String = ""

    @ColumnInfo(name = "date")
    var messageTime: Long = 0
}