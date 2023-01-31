package com.darf.example.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.darf.example.Database
import com.darf.example.R
import com.darf.example.adapters.ChatAdapter
import com.darf.example.databinding.FragmentChatBinding
import com.darf.example.models.Chat
import com.darf.example.models.ChatInUser
import com.darf.example.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Thread.sleep

const val KEY_GET_CHAT_ID = "get_chat_id"
const val KEY_GET_CHAT_NAME = "get_chat_name"

class ChatFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentChatBinding
    private lateinit var curActivity: AppCompatActivity

    private val db = Database.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        navController = findNavController()

        onDataChange()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        curActivity = activity as AppCompatActivity
        curActivity.title = db.listChat[db.chatId]?.chatName
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)
        Log.d("ChatFragment", "Запускается еще раз")

        binding.apply {
            db.adaptersChat[db.chatId] =
                db.adaptersChat[db.chatId] ?: ChatAdapter(listOf()) // костыль
            chatRecyclerView.layoutManager = LinearLayoutManager(activity)
            chatRecyclerView.adapter = db.adaptersChat[db.chatId]

            chatButtonSend.setOnClickListener {
                val message = chatMessage.text.toString()
                chatMessage.text.clear()
                db.setValueMessage(message)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_chat_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.chat_add)
            navController.navigate(R.id.addUserFragment)

        return super.onOptionsItemSelected(item)
    }

    private fun onDataChange() {
        db.listenerMessageChild()
        db.listenerUserInChatChild()
    }
}