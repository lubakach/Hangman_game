package com.example.hangman

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class GameActivity : Activity() {
    private lateinit var game : Game
    private lateinit var gameId : String
    private val ERRORS : Int = 6
    private var stage : Int = 1
    private var word : String = ""
    private lateinit var displayText : String
    private lateinit var wordView : TextView
    private lateinit var stageImage : ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var currentUser : FirebaseUser

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_layout)
        wordView = findViewById<TextView>(R.id.word)
        stageImage = findViewById<ImageView>(R.id.stageImage)
        currentUser = FirebaseAuth.getInstance().currentUser!!

        val arguments = intent.extras
        gameId = arguments!!["gameId"] as String
        word = arguments!!["word"] as String
        databaseReference = Firebase.database.reference.child("games").child(gameId)
        beginGame()
    }

    fun beginGame(){
        displayText = ""
        for (i in 1..word.length)
            displayText += "_"
        wordView.text = displayText

        word = word.toLowerCase()
    }

    fun onLetterTouched(view : View){
        val letterButton = view as Button
        val letter = letterButton.text.toString().toLowerCase()

        if (word.contains(letter)){

            var newDisplayText = ""
            for (i in word.indices)
                if (word[i] == letter[0])
                    newDisplayText += letter
                else
                    newDisplayText += displayText[i]
            displayText = newDisplayText
            wordView.setText(displayText)

            view.isEnabled = false
            view.background = getApplicationContext().getResources().getDrawable(R.drawable.not_clickable)

            if(!displayText.contains("_")){
                Toast.makeText(this,"You won!", Toast.LENGTH_LONG).show()
                addWin()
                this.finish()
            }
        }
        else{
            view.isEnabled = false
            view.background = getApplicationContext().getResources().getDrawable(R.drawable.not_clickable)

            stage += 1
            stageImage.setImageURI(Uri.parse("android.resource://com.example.hangman/drawable/_$stage"))

            if (stage == ERRORS){
                Toast.makeText(this,"You lose!", Toast.LENGTH_LONG).show()
                addLoss()
                this.finish()
            }

        }
    }

    fun addWin(){
        databaseReference = Firebase.database.reference.child("games").child(gameId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                game = snapshot.getValue<Game>()!!
                game.winners[currentUser.uid] = currentUser.displayName!!
                databaseReference.setValue(game)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addLoss(){
        databaseReference = Firebase.database.reference.child("games").child(gameId)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                game = snapshot.getValue<Game>()!!
                game.loosers[currentUser.uid] = currentUser.displayName!!
                databaseReference.setValue(game)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }




}