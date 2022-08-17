package com.dscvit.notix.ui.conversations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.ChatAdapter
import com.dscvit.notix.databinding.FragmentChatMessagesBinding
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class ChatMessages : Fragment() {

    private var _binding: FragmentChatMessagesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConversationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChatMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_chatMessages_to_conversationsFragment)
        }

        //Chats Recycler View
        val chatsRV = binding.chatMessagesRV
        chatsRV.layoutManager = LinearLayoutManager(activity)
        val chatsRVAdapter = context?.let { ChatAdapter(it) }
        chatsRV.adapter = chatsRVAdapter

        val chatName = arguments!!.getString("title")
        binding.chatName.text =  chatName

        val patternNewMessages = Regex("[0-9]+ new messages")
        if(chatName!=null){
            viewModel.getAllChatsFromTitle(chatName).observe(viewLifecycleOwner) { list ->
                var finalList = list?.filter {
                    //Log.d("Rudrank","${it.desc} : ${it.desc?.contains(patternNewMessages)}")
                    it.desc?.contains(patternNewMessages) != true


                }
                    if(finalList!=null){
                        chatsRVAdapter?.updateList(finalList)
                    }


            }
        }


    }




}
