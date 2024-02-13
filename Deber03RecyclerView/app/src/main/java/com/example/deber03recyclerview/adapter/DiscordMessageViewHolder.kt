package com.example.deber03recyclerview.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deber03recyclerview.DiscordMessage
import com.example.deber03recyclerview.R
import com.example.deber03recyclerview.databinding.ItemDiscordmessageBinding
import de.hdodenhof.circleimageview.CircleImageView

class DiscordMessageViewHolder(view: View):RecyclerView.ViewHolder(view) {

val binding = ItemDiscordmessageBinding.bind(view)

    fun render (discordMessagemodel: DiscordMessage){
        binding.tvDiscordMessageSender.text = discordMessagemodel.senderName
        binding.tvDiscordMessagemessages.text = discordMessagemodel.message
        binding.tvDiscordMessageDays.text = discordMessagemodel.days
        Glide.with(binding.ivDiscordMessage.context).load(discordMessagemodel.profilePhoto).into(binding.ivDiscordMessage)




    }
}