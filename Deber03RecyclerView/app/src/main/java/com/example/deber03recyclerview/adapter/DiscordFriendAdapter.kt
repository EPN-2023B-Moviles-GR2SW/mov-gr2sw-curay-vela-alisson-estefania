package com.example.deber03recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03recyclerview.DiscordFriend
import com.example.deber03recyclerview.DiscordMessage
import com.example.deber03recyclerview.R

class DiscordFriendAdapter(private val discordfriendlist:List<DiscordFriend>) : RecyclerView.Adapter<DiscordFriendViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscordFriendViewHolder {

        //se pasa el layout que se va a poder modificar
        val layoutInflaterDiscordFriend = LayoutInflater.from(parent.context)
        return DiscordFriendViewHolder(layoutInflaterDiscordFriend.inflate(R.layout.item_discordfriends,parent,false))
    }

    override fun getItemCount(): Int {
        return discordfriendlist.size

    }
    override fun onBindViewHolder(holder: DiscordFriendViewHolder, position: Int) {
        //pasar por cada item para llamar al render
        val itemDiscordFriend = discordfriendlist[position]
        holder.render(itemDiscordFriend)

    }
}