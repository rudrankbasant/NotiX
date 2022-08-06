package com.dscvit.notix.ui.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.ConversationsAdapter
import com.dscvit.notix.databinding.FragmentConversationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversationsFragment : Fragment() {

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

        binding.conversationsBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_conversationsFragment_to_homeFragment)
        }

        //Conversations Recycler View
        val conversationsRV = binding.conversationsRV
        conversationsRV.layoutManager = LinearLayoutManager(activity)
        val conversationsRVAdapter = context?.let { ConversationsAdapter(it) }
        conversationsRV.adapter = conversationsRVAdapter

        viewModel.allUniqueConversation.observe(viewLifecycleOwner) { list ->
            list?.let {
                conversationsRVAdapter?.updateList(list)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}