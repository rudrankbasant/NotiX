package com.dscvit.notix.ui.conversations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.ConversationsAdapter
import com.dscvit.notix.adapters.ConversationsClickInterface
import com.dscvit.notix.databinding.FragmentConversationsBinding
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConversationsFragment : Fragment(), ConversationsClickInterface {

    private var _binding: FragmentConversationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConversationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConversationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusBar = context?.let { ContextCompat.getColor(it, R.color.bg_color2) }
        if(statusBar!=null){
            activity?.window?.statusBarColor = statusBar
        }


        binding.conversationsBackButton.setOnClickListener {
            view.findNavController()
                .navigate(com.dscvit.notix.R.id.action_conversationsFragment_to_homeFragment)
        }

        Log.d("Filter: ", "Here started fragment")

        //Conversations Recycler View
        val conversationsRV = binding.conversationsRV
        conversationsRV.layoutManager = LinearLayoutManager(activity)
        val conversationsRVAdapter = context?.let { ConversationsAdapter(it, this) }
        conversationsRV.adapter = conversationsRVAdapter

        val patternNewMessages = Regex("[0-9]+ new messages")
        //var conversationsList = emptyList<NotificationData>()
        viewModel.allUniqueConversation.observe(viewLifecycleOwner) { list ->
            list?.let {
                Log.d("Filter: ", "Here list received started")
                if(list.isEmpty()){
                    binding.defaultConversationsText.visibility = View.VISIBLE
                }else{
                    binding.defaultConversationsText.visibility = View.GONE
                    conversationsRVAdapter?.updateList(list.reversed())

                    /*if(list.size>2){
                        for(i in 1..list.size){
                            if(list[i-1].title != list[i].title && list[i-1].desc != list[i].desc){
                                listWithNoDuplicates.add(list[i])
                            }
                        }
                        conversationsRVAdapter?.updateList(listWithNoDuplicates.reversed())
                    }else{
                        conversationsRVAdapter?.updateList(list.reversed())
                    }*/


                }

            }
        }

        /*val filteredList = removeRedundantConversations(conversationsList)
        conversationsRVAdapter?.updateList(filteredList)*/
    }


    private fun removeRedundantConversations(list: List<NotificationData>): List<NotificationData> {

        Log.d("Filter: ", "Here started")
        var newList = mutableListOf<NotificationData>()
        val patternNewMessages = Regex("[0-9]+ new messages")
        for (i in list) {
            newList.add(i)
            Log.d("Filter: ", i.desc.toString())
            /*if(i.desc?.contains(patternNewMessages) == true || i.desc?.trim { it <= ' ' }  == "Checking for new messages"){
                Log.d("Filter: ", i.desc.toString())
                newList.toMutableList().add(i)
            }else{
            }*/
        }
        return newList
    }

    override fun onConversationsClick(selectedConversation: NotificationData) {

        val bundle = Bundle()
        bundle.putString("title", selectedConversation.title)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_conversationsFragment_to_chatMessages, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}