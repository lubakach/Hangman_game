package com.example.hangman

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser


class JoinActivity : Activity(), GamesAdapter.OnGameClickListener{

    private lateinit var databaseReference: DatabaseReference
    private lateinit var games: ArrayList<Game>
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var currentUser: FirebaseUser
    private lateinit var recyclerViewGames: RecyclerView
    private lateinit var btnCreate: Button

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_layout)
        currentUser = FirebaseAuth.getInstance().currentUser!!
        recyclerViewGames = findViewById(R.id.roomsList)
        recyclerViewGames.layoutManager = LinearLayoutManager(this)
        games = ArrayList<Game>()
        gamesAdapter = GamesAdapter(games, this, this)
        recyclerViewGames.adapter = gamesAdapter
        readGames()
    }

    fun readGames(){
        databaseReference = Firebase.database.reference.child("games")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                games.clear()
                for (dataSnapshot in snapshot.children) {
                    val game= dataSnapshot.getValue<Game>()!!
                    //if (game.creator != currentUser.uid){
                        games.add(game)
                    //}
                }
                gamesAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onGameClick(gameId: String, word : String) {
        val intent = Intent(this@JoinActivity, GameActivity::class.java)
        intent.putExtra("gameId", gameId)
        intent.putExtra("word", word )
        startActivity(intent)
    }

    fun createNewRoom(view : View){
        val text = findViewById<EditText>(R.id.roomNameInput).text
        if (text.length != 0){
            val game = Game()
            game.creatorName = currentUser.displayName!!
            game.creator = currentUser.uid!!
            game.word = text.toString()
            var key = databaseReference.push().key
            game.gameId = key!!
            databaseReference.child(key).setValue(game)
            findViewById<EditText>(R.id.roomNameInput).text = null
        }
    }

}
