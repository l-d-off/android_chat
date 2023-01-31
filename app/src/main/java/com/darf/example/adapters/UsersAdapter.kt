package com.darf.example.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darf.example.R
import com.darf.example.databinding.UsersRecyclerviewItemBinding
import com.darf.example.models.User

class UsersAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder(private val binding: UsersRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(user: User) = with(binding) {
            usersUserName.text = "[USER] ${user.userName}"
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val bindingTo = UsersRecyclerviewItemBinding.inflate(inflater, parent, false)
                return ViewHolder(bindingTo)
            }
        }
    }

    fun submitList(list: ArrayList<User>) {
        userList = list.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.users_recyclerview_item, parent, false)
        return ViewHolder(UsersRecyclerviewItemBinding.bind(itemView))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size
}