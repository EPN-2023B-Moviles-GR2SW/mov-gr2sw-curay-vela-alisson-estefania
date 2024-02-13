package com.example.deber03recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber03recyclerview.adapter.DiscordMessageAdapter
import com.example.deber03recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {

        binding.recyclerDiscordMessages.layoutManager = LinearLayoutManager(this)
        binding.recyclerDiscordMessages.adapter = DiscordMessageAdapter(DiscordMessageProvider.discordMessageList)
    }
}