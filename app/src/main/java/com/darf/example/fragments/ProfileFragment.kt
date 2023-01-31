package com.darf.example.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.darf.example.Database
import com.darf.example.IBottomNavViewCallback
import com.darf.example.R
import com.darf.example.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentProfileBinding
    lateinit var curActivity: AppCompatActivity

    private val db = Database.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        curActivity = activity as AppCompatActivity
        curActivity.title = db.user.displayName
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()

        val bottomNavigationView = binding.include.bottomNavigation
        bottomNavigationView.selectedItemId = R.id.nav_profile
        (activity as IBottomNavViewCallback).runBottomNavigationView(bottomNavigationView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profile_sign_out) {
            db.auth.signOut()
//            navController.backStack.clear()
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }

        return super.onOptionsItemSelected(item)
    }
}