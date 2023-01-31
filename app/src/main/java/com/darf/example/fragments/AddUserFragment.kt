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
import com.darf.example.R
import com.darf.example.adapters.AddUserAdapter
import com.darf.example.adapters.ChatAdapter
import com.darf.example.databinding.FragmentAddUserBinding
import com.darf.example.databinding.FragmentChatBinding
import com.darf.example.models.User
import com.darf.example.models.UserInChat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddUserFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentAddUserBinding
    lateinit var curActivity: AppCompatActivity

    private val db = Database.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        onDataChange()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        curActivity = activity as AppCompatActivity
        curActivity.title = "Add User"
        return inflater.inflate(R.layout.fragment_add_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUserBinding.bind(view)

        binding.apply {
            addUserNoUsersAvailable.visibility =
                if (db.adapterAddUser.itemCount != 0) View.GONE else View.VISIBLE

            addUserRecyclerView.layoutManager = LinearLayoutManager(activity)
            addUserRecyclerView.adapter = db.adapterAddUser

            addUserButtonAdd.setOnClickListener {
                db.adapterAddUser.getCheckedUserList().forEach { user ->
                    db.setValueUserInChat(user.userId!!)
                    db.setValueChatInUser(user.userId)
                }
                navController.popBackStack()
            }

            addUserButtonCancel.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    private fun onDataChange() {
//        db.listenerUser()
//        db.listenerUserInChatChild()
    }
}