package com.levandowski.model

import java.util.*

class ChatMessage() {

    var messageText: String = ""
    var messageUser: String = ""
    var messageTime: Long = 0

    constructor(messageText: String, messageUser: String) : this() {
        this.messageText = messageText
        this.messageUser = messageUser
        this.messageTime = Date().time
    }
}