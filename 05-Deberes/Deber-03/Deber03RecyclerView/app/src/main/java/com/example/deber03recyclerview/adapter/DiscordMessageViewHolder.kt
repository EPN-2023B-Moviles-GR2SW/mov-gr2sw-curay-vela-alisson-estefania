package com.example.deber03recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deber03recyclerview.DiscordMessage
import com.example.deber03recyclerview.databinding.ItemDiscordmessageBinding

class DiscordMessageViewHolder(view: View):RecyclerView.ViewHolder(view) {

val bindingDiscordMessage = ItemDiscordmessageBinding.bind(view)

    fun render (discordMessagemodel: DiscordMessage){
        bindingDiscordMessage.tvDiscordMessageSender.text = discordMessagemodel.senderName
        bindingDiscordMessage.tvDiscordMessagemessages.text = discordMessagemodel.message
        bindingDiscordMessage.tvDiscordMessageDays.text = discordMessagemodel.days
        Glide.with(bindingDiscordMessage.ivDiscordMessage.context).load(discordMessagemodel.profilePhoto).into(bindingDiscordMessage.ivDiscordMessage)
    }
}