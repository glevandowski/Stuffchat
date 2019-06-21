package com.levandowski.adapter.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.levandowski.R

class ChatHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    inner class Received {
        val messageText: TextView = itemView.findViewById(R.id.message_text)
        val messageUser: TextView = itemView.findViewById(R.id.message_user)
        val messageTime: TextView = itemView.findViewById(R.id.message_time)
    }

    inner class Send {
        val messageText: TextView = itemView.findViewById(R.id.message_text)
        val messageUser: TextView = itemView.findViewById(R.id.message_user)
        val messageTime: TextView = itemView.findViewById(R.id.message_time)
    }
}