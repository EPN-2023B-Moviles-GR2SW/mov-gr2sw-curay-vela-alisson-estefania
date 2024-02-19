package com.example.deber03recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03recyclerview.adapter.DiscordFriendAdapter
import com.example.deber03recyclerview.adapter.DiscordMessageAdapter
import com.example.deber03recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerViewDiscordFriend(binding.recyclerDiscordFriend, DiscordFriendProvider.discordFriendList)
        initRecyclerViewDiscordMessage(binding.recyclerDiscordMessages, DiscordMessageProvider.discordMessageList)

    }
    private fun initRecyclerViewDiscordFriend(recyclerView: RecyclerView, dataList: List<DiscordFriend>) {
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = DiscordFriendAdapter(dataList)
    }

    private fun initRecyclerViewDiscordMessage(recyclerView: RecyclerView, dataList: List<DiscordMessage>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DiscordMessageAdapter(dataList)
    }

}