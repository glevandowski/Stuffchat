package com.levandowski.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.levandowski.R
import com.levandowski.adapter.chat.ChatAdapter
import com.levandowski.model.Message
import kotlinx.android.synthetic.main.chat_bottom_component.*
import kotlinx.android.synthetic.main.fragment_chat.list_of_messages

class ChatFragment : Fragment() {

    private var viewModel: ChatViewModel? = null
    private var adapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setSupportActionBar(chat_toolbar)
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onResume() {
        super.onResume()
        loadData()
        onClickActions()
    }

    private fun loadData() {
        loadMessages()
//        loadToolbar()
    }

    private fun loadMessages() {
        viewModel?.messagesQuery()?.let {

            adapter = ChatAdapter(
                FirestoreRecyclerOptions.Builder<Message>()
                    .setQuery(it, Message::class.java).build()
            )

            adapter?.let { chatAdapter ->
                setupRecyclerView(chatAdapter)
                chatAdapter.startListening()
            }
        }
    }

//    private fun loadToolbar() {
//        setUserToolbar()
//        setUserActive()
//    }

//    private fun setUserToolbar() {
//        viewModel?.getToken()?.let {
//            Picasso.get().load(it.photoURL).transform(CircleTransform()).into(chat_toolbar.image_user)
//            chat_toolbar.user_name.text = it.name
//        }
//    }

//    private fun setUserActive() {
//        chat_toolbar.user_active.text = viewModel?.getUserActive()
//    }

    private fun setupRecyclerView(chatAdapter: ChatAdapter?) = list_of_messages.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this@ChatFragment.context)
        adapter = chatAdapter
    }

    private fun onClickActions() {
        fab.setOnClickListener {
            viewModel?.sendMessage(input.text.toString())?.addOnSuccessListener {
                adapter?.snapshots?.lastIndex?.let {
                    list_of_messages.scrollToPosition(it)
                }
            }
            input.setText("")
        }
    }
}
