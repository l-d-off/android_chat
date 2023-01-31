package com.darf.example.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darf.example.R
import com.darf.example.databinding.AddUserRecyclerviewItemBinding
import com.darf.example.models.User

class AddUserAdapter(
    private var userList: List<User>
) :
    RecyclerView.Adapter<AddUserAdapter.ViewHolder>() {

    private val checkedUserList: MutableList<User> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User, addUserToList: (Boolean) -> Unit) {
            val binding = AddUserRecyclerviewItemBinding.bind(itemView)
            binding.addUserCheckBox.text = user.userName

            binding.addUserCheckBox.also {
                it.setOnCheckedChangeListener { _, isChecked ->
                    addUserToList(isChecked)
                }
            }
        }
    }

    fun submitList(list: List<User>) {
        userList = list.toList()
        notifyDataSetChanged()
    }

    fun getCheckedUserList() = checkedUserList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_user_recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user) { isChecked ->
            if (isChecked && user !in checkedUserList)
                checkedUserList.add(user)
            else if (!isChecked && user in checkedUserList)
                checkedUserList.remove(user)
        }
    }

    override fun getItemCount() = userList.size
}