package com.darf.example.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.darf.example.Database
import com.darf.example.R
import com.darf.example.databinding.MessengerRecyclerviewItemBinding
import com.darf.example.fragments.KEY_GET_CHAT_ID
import com.darf.example.fragments.KEY_GET_CHAT_NAME
import com.darf.example.models.Chat
import com.darf.example.models.ChatInUser
import com.darf.example.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessengerAdapter(
    private var chatList: List<ChatInUser>,
    private val navController: NavController,
) :
    RecyclerView.Adapter<MessengerAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: MessengerRecyclerviewItemBinding,
        private val db: Database = Database.get(),
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(chatInUser: ChatInUser, nav: (String?, String) -> Unit) = with(binding) {
            messengerChatName.text = "[CHAT] ${db.listChat[chatInUser.chatId]?.chatName}"

            messengerCard.setOnClickListener {
                Log.d("Adapter", "Clicked")
                db.chatId = chatInUser.chatId!!
                val chatId = chatInUser.chatId
                val chatName = messengerChatName.text.toString()
                nav(chatId, chatName)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bindingTo = MessengerRecyclerviewItemBinding.inflate(inflater, parent, false)
                return ViewHolder(bindingTo)
            }
        }
    }

    fun submitList(list: List<ChatInUser>) {
        chatList = list.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.messenger_recyclerview_item, parent, false)
        return ViewHolder(MessengerRecyclerviewItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position]) { chatId, chatName ->
            val bundle = Bundle()
            bundle.putString(KEY_GET_CHAT_ID, chatId)
            bundle.putString(KEY_GET_CHAT_NAME, chatName)
            navController.navigate(R.id.chatFragment, bundle)
        }
    }

    override fun getItemCount() = chatList.size
}