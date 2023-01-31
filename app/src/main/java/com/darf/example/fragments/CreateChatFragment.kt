package com.darf.example.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.darf.example.Database
import com.darf.example.R
import com.darf.example.databinding.FragmentCreateChatBinding

class CreateChatFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentCreateChatBinding
    private lateinit var curActivity: AppCompatActivity

    private val db = Database.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        curActivity = activity as AppCompatActivity
        curActivity.title = "Create Chat"
        return inflater.inflate(R.layout.fragment_create_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateChatBinding.bind(view)

        binding.apply {
            createChatNoUsersAvailable.visibility =
                if (db.adapterCreateChat.itemCount != 0) View.GONE else View.VISIBLE

            createChatRecyclerView.layoutManager = LinearLayoutManager(activity)
            createChatRecyclerView.adapter = db.adapterCreateChat

            createChatButtonCreate.setOnClickListener {
                val chatName = createChatChatName.text.toString()
                db.setValueChat(chatName = chatName)
                // для текущего user
                db.setValueUserInChat()
                db.setValueChatInUser()
                // для отмеченных user
                db.adapterCreateChat.getCheckedUserList().forEach { user ->
                    db.setValueUserInChat(userId = user.userId!!)
                    db.setValueChatInUser(userId = user.userId)
                }
                navController.popBackStack()
                navController.navigate(R.id.chatFragment)
            }

            createChatButtonCancel.setOnClickListener {
                navController.popBackStack()
            }
        }
    }
}