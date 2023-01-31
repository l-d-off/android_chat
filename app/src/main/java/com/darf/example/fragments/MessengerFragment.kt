package com.darf.example.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.darf.example.Database
import com.darf.example.IBottomNavViewCallback
import com.darf.example.R
import com.darf.example.adapters.MessengerAdapter
import com.darf.example.databinding.FragmentMessengerBinding
import com.darf.example.models.ChatInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Thread.sleep

class MessengerFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var binding: FragmentMessengerBinding
    private lateinit var curActivity: AppCompatActivity

    private val db = Database.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        navController = findNavController()
        db.adapterMessenger = MessengerAdapter(db.listChatInUser, navController)

        onDataChange()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        curActivity = activity as AppCompatActivity
        curActivity.title = "Messenger"
        return inflater.inflate(R.layout.fragment_messenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMessengerBinding.bind(view)

        binding.apply {
            messengerRecyclerView.layoutManager = LinearLayoutManager(activity)
            messengerRecyclerView.adapter = db.adapterMessenger
        }
    }

    override fun onStart() {
        super.onStart()

        val bottomNavigationView = binding.include.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.nav_messenger
        (activity as IBottomNavViewCallback).runBottomNavigationView(bottomNavigationView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_messenger_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.messenger_add) {
//            db.setValueChat("Chat name #${(1..100).shuffled().last()}")
//            db.setValueChatInUser()
//            db.setValueUserInChat()
            navController.navigate(R.id.createChatFragment)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun onDataChange() {
        db.listenerChat()
        db.listenerUser()
//        db.listenerUserInChat()
        db.listenerChatInUserChild()
    }
}