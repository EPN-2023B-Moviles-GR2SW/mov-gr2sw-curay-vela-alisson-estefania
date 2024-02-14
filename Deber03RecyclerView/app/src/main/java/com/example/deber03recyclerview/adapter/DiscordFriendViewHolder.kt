package com.example.deber03recyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deber03recyclerview.DiscordFriend
import com.example.deber03recyclerview.databinding.ItemDiscordfriendsBinding
import com.example.deber03recyclerview.databinding.ItemDiscordmessageBinding

class DiscordFriendViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val bindingDiscordFriend = ItemDiscordfriendsBinding.bind(view)
    fun render (discordFriendModel: DiscordFriend){
        Glide.with(bindingDiscordFriend.ivDiscordFriend).load(discordFriendModel.profilePhoto).into(bindingDiscordFriend.ivDiscordFriend)
    }
}