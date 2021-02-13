package com.example.hangman

import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class Game() {
    var gameId: String
    var creator: String
    var creatorName: String
    var word: String
    var startPlaying : String
    var winners: HashMap<String, String>
    var loosers: HashMap<String, String>

    init{
        //gameId = Integer(Integer.parseInt(SimpleDateFormat("yyyyMMdd").format(Date())) * 100 + Random.nextInt(0, 100))
        gameId = String()
        creator = String()
        creatorName = String()
        word = String()
        startPlaying = String()
        winners = HashMap<String, String>()
        loosers = HashMap<String, String>()

    }
}