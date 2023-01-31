package com.darf.example.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.darf.example.Database
import com.darf.example.R
import com.darf.example.databinding.ChatRecyclerviewItemLeftBinding
import com.darf.example.databinding.ChatRecyclerviewItemRightBinding
import com.darf.example.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatAdapter(
    private var messageList: List<Message>,
    private val auth: FirebaseAuth = Firebase.auth
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View,
        private val db: Database = Database.get()
    ) :
        RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(message: Message) {
            val bindingMy =
                if (itemViewType == USER.MY.ordinal) ChatRecyclerviewItemRightBinding.bind(itemView) else null
            val bindingAny =
                if (itemViewType == USER.ANY.ordinal) ChatRecyclerviewItemLeftBinding.bind(itemView) else null

            val user = db.mapUser[message.userId]!!
            if (itemViewType == USER.MY.ordinal)
                bindingMy?.apply {
                    chatMessageUserName.text = "[YOU] ${user.userName}"
                    chatMessageText.text = message.text
                }
            else if (itemViewType == USER.ANY.ordinal)
                bindingAny?.apply {
                    chatMessageUserName.text = "[USER] ${user.userName}"
                    chatMessageText.text = message.text
                }
        }
/*
        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val leftBinding = ChatRecyclerviewItemLeftBinding.inflate(inflater, parent, false)
                val rightBinding = ChatRecyclerviewItemRightBinding.inflate(inflater, parent, false)
                return ViewHolder(leftBinding, rightBinding)
            }
        }
 */
    }

    fun submitList(list: List<Message>) {
        messageList = list.toList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLeftView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_recyclerview_item_left, parent, false)
        val itemRightView = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_recyclerview_item_right, parent, false)

        val itemView = when (viewType) {
            USER.ANY.ordinal -> itemLeftView
            USER.MY.ordinal -> itemRightView
            else -> null
        }!!

        // вернём itemViewRight, если отрисовываем "моё" сообщение
        // вернём itemViewLeft, если отрисовываем "чужое" сообщение
        return ViewHolder(
//            ChatRecyclerviewItemLeftBinding.bind(itemLeftView),
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int = when (messageList[position].userId) {
        auth.currentUser!!.uid -> USER.MY.ordinal
        else -> USER.ANY.ordinal
    }

    companion object {
        private enum class USER {
            MY,
            ANY
        }
    }
}