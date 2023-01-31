package com.darf.example.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.darf.example.Database
import com.darf.example.IBottomNavViewCallback
import com.darf.example.R
import com.darf.example.adapters.UsersAdapter
import com.darf.example.databinding.FragmentUsersBinding
import com.darf.example.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsersFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentUsersBinding
    lateinit var curActivity: AppCompatActivity

    private val db = Database.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        onDataChange()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        curActivity = activity as AppCompatActivity
        curActivity.title = "Users"
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersBinding.bind(view)

        binding.apply {
            usersRecyclerView.layoutManager = LinearLayoutManager(activity)
            usersRecyclerView.adapter = db.adapterUsers
        }
    }

    override fun onStart() {
        super.onStart()

        val bottomNavigationView = binding.include.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.nav_users
        (activity as IBottomNavViewCallback).runBottomNavigationView(bottomNavigationView)
    }

    private fun onDataChange() {
        db.listenerUser()
    }
}