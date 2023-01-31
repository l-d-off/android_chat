package com.darf.example

import androidx.collection.ArrayMap
import androidx.navigation.NavController
import com.darf.example.adapters.*
import com.darf.example.models.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database private constructor() {
    private val database = Firebase.database
    val auth = Firebase.auth
    val user = auth.currentUser!!

    var chatId = ""

    val listChat = ArrayMap<String, Chat>()
    val listUser = ArrayList<User>()
    val mapUser = ArrayMap<String, User>()
    val listMessage = ArrayMap<String, MutableList<Message>>()
    val listChatInUser = ArrayList<ChatInUser>()
    val listUserInChat = ArrayMap<String, MutableList<UserInChat>>()

    var adapterMessenger: MessengerAdapter? = null
    val adapterAddUser = AddUserAdapter(listUser)
    val adapterCreateChat = CreateChatAdapter(listUser)
    val adapterUsers = UsersAdapter(listUser)
    val adaptersChat = ArrayMap<String, ChatAdapter>()

    init {
        adaptersChat[chatId]
    }

    private val dbRefChat = database.getReference("node_chat")
    private val dbRefUser = database.getReference("node_user")
    private val dbRefMessage = database.getReference("node_message")
    private val dbRefChatInUser = database.getReference("node_chat_in_user")
    private val dbRefUserInChat = database.getReference("node_user_in_chat")

    private fun keyChat() = dbRefChat.push().key!!
    private fun keyMessage(chatId: String) = dbRefMessage.child(chatId).push().key!!
    private fun keyChatInUser(userId: String) = dbRefChatInUser.child(userId).push().key!!
    private fun keyUserInChat(chatId: String) = dbRefUserInChat.child(chatId).push().key!!

    // LISTENERS
    fun listenerChat() {
//        val listChatId = listChatInUser.map { it.chatId!! }.listIterator()
        dbRefChat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()
                snapshot.children.forEach { snap ->
//                    if (snap.key == listChatId.next()) {
                    val chat = snap.getValue(Chat::class.java)!!
                    listChat[snap.key] = chat
//                    }
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun listenerUser() {
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUser.clear()
                mapUser.clear()
                snapshot.children.forEach { snap ->
                    val user = snap.getValue(User::class.java)!!
                    listUser += user
                    mapUser[user.userId] = user
                }
                adapterUsers.submitList(listUser)
                adapterCreateChat.submitList(listUser - mapUser[user.uid]!!)
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun listenerMessage() {
//        val listChatId = listChatInUser.map { it.chatId!! }.listIterator()
        dbRefMessage.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listMessage.clear()
                snapshot.children.forEach { snapChatId ->
//                    var id: String
//                    if (snapChatId.key == (listChatId.next().also { id = it })) {
                    val listMsg = ArrayList<Message>()
                    snapChatId.children.forEach { snap ->
                        val message = snap.getValue(Message::class.java)!!
                        listMsg += message
                    }
                    val id = snapChatId.key //?
                    listMessage[id] = listMsg
                    if (adaptersChat[id] == null) adaptersChat[id] = ChatAdapter(listMsg)
                    else adaptersChat[id]!!.submitList(listMsg)
//                    }
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun listenerMessageChild() {
        dbRefMessage.child(chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listMessage[chatId]?.clear()
                val listMsg = ArrayList<Message>()
                snapshot.children.forEach { snap ->
                    val message = snap.getValue(Message::class.java)!!
                    listMsg += message
                }
                listMessage[chatId] = listMsg
                if (adaptersChat[chatId] == null) adaptersChat[chatId] = ChatAdapter(listMsg)
                else adaptersChat[chatId]!!.submitList(listMsg)
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun listenerChatInUserChild() {
        dbRefChatInUser.child(user.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listChatInUser.clear()
                snapshot.children.forEach { snap ->
                    val chatInUser = snap.getValue(ChatInUser::class.java)!!
                    listChatInUser += chatInUser
                }
                adapterMessenger!!.submitList(listChatInUser)
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun listenerUserInChat() {
//        val listChatId = listChatInUser.map { it.chatId!! }.listIterator()
        dbRefUserInChat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUserInChat.clear()
                snapshot.children.forEach { snapChatId -> // не хватает условия сравнения snapChatId == listChatId
                    val listUIC = ArrayList<UserInChat>()
                    snapChatId.children.forEach { snap ->
                        val userInChat = snap.getValue(UserInChat::class.java)!!
                        listUIC += userInChat
                    }
//                    listUserInChat[listChatId.next()] = listUIC
                    listUserInChat[snapChatId.key] = listUIC //?
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    fun listenerUserInChatChild() {
        dbRefUserInChat.child(chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUserInChat[chatId]?.clear()
                val listUIC = ArrayList<UserInChat>()
                snapshot.children.forEach { snap ->
                    val userInChat = snap.getValue(UserInChat::class.java)!!
                    listUIC += userInChat
                }
                listUserInChat[chatId] = listUIC
                val listU = listUIC.map { mapUser[it.userId] }
                adapterAddUser.submitList(listUser.filterNot { listU.contains(it) })
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    // SET VALUES
    fun setValueChat(chatName: String = "Chat name #${(1..100).shuffled().last()}") {
        val key = keyChat()
        chatId = key
        dbRefChat.child(key).setValue(
            Chat(
                chatId = key,
                chatName = chatName
            )
        )
    }

    fun setValueUser(aboutUser: String = "About user #${(1..100).shuffled().last()}") {
        dbRefUser.child(user.uid).setValue(
            User(
                userId = user.uid,
                userName = user.displayName,
                aboutUser = aboutUser
            )
        )
    }

    fun setValueMessage(text: String = "Message text #${(1..100).shuffled().last()}") {
        val key = keyMessage(chatId)
        dbRefMessage.child(chatId).child(key).setValue(
            Message(
                messageId = key,
                chatId = chatId,
                userId = user.uid,
                text = text
            )
        )
    }

    fun setValueChatInUser(userId: String = this.user.uid, chatId: String = this.chatId) {
        val key = keyChatInUser(userId)
        dbRefChatInUser.child(userId).child(key).setValue(
            ChatInUser(
                chatInUserId = key,
                userId = userId,
                chatId = chatId
            )
        )
    }

    fun setValueUserInChat(userId: String = this.user.uid, chatId: String = this.chatId) {
        val key = keyUserInChat(chatId)
        dbRefUserInChat.child(chatId).child(key).setValue(
            UserInChat(
                userInChatId = key,
                userId = userId,
                chatId = chatId
            )
        )
    }

    companion object {
        private var obj: Database? = null

        fun instance(): Database {
            if (obj == null)
                obj = Database()
            return obj!!
        }

        fun get(): Database = obj!!
    }
}