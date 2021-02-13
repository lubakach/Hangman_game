package com.example.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            findViewById<TextView>(R.id.userNameMain).setText(FirebaseAuth.getInstance().currentUser?.displayName)
            findViewById<Button>(R.id.btnLogin).setText("start")
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    val intent = Intent(this@MainActivity, JoinActivity::class.java)
                    startActivity(intent)
                }
                else
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), 1)

            }})
    }

}

