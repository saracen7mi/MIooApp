package com.example.miooapp.model

import java.util.*

class ChatMessage(
    val id: String,
    val text: String,
    val fromId: String,
    val toId: String,

) {
    constructor() : this("", "", "", "")
}