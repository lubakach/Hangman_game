package com.example.hangman

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class GamesAdapter(games: ArrayList<Game>, context: Context, listener: OnGameClickListener) : RecyclerView.Adapter<GamesAdapter.GameHolder>(), Adapter {
    val inflater = LayoutInflater.from(context)
    val gamesArray = games
    val OnGameClickL = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
        val view = inflater.inflate(R.layout.game_item, parent, false)
        return GameHolder(view, OnGameClickL)
    }

    override fun getItemCount() = gamesArray.size

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val game = gamesArray.get(position)
        holder.userName?.text = game.creatorName
        holder.joinButton?.tag = game.gameId + "\t" + game.word
        holder.losed?.text = '☠' + game.loosers.size.toString()
        holder.woned?.text = '⚔' + game.winners.size.toString()
        if (FirebaseAuth.getInstance().currentUser?.uid == game.creator)
            holder.joinButton?.visibility = View.INVISIBLE
    }

    class GameHolder(itemView: View, l: OnGameClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var userName: TextView? = null
        var joinButton: Button? = null
        var losed: TextView? = null
        var woned: TextView? = null
        var OnGameClickListener = l

        init {
            userName = itemView?.findViewById(R.id.userName)
            joinButton = itemView?.findViewById((R.id.button2))
            losed = itemView?.findViewById(R.id.losed)
            woned = itemView?.findViewById(R.id.wins)
            itemView.findViewById<Button>(R.id.button2).setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val strs = v?.tag.toString().split("\t").toTypedArray()
            OnGameClickListener.onGameClick(strs[0], strs[1])
        }
    }

    public interface OnGameClickListener{
        fun onGameClick(gameId : String, word : String)
    }


    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

}