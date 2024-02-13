package com.example.deber03recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03recyclerview.DiscordMessage
import com.example.deber03recyclerview.R

class DiscordMessageAdapter(private val discordmessagelist:List<DiscordMessage>) : RecyclerView.Adapter<DiscordMessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscordMessageViewHolder {

        //se pasa el layout que se va a poder modificar
        val layoutInflater = LayoutInflater.from(parent.context)
        return DiscordMessageViewHolder(layoutInflater.inflate(R.layout.item_discordmessage,parent,false))
    }
    override fun onBindViewHolder(holder: DiscordMessageViewHolder, position: Int) {

        //pasar por cada item para llamar al render
        val item = discordmessagelist[position]
        holder.render(item)

    }
    override fun getItemCount(): Int {
        return discordmessagelist.size
    }

}