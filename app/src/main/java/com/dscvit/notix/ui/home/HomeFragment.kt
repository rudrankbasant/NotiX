package com.dscvit.notix.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dscvit.notix.R
import com.dscvit.notix.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.recentsForwardButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }

        binding.conversationsForwardButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_conversationsFragment)
        }

        binding.transactionsForwardButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_transactionsFragment)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}