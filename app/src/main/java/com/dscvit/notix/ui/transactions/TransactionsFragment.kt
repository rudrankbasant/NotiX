package com.dscvit.notix.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.TransactionsAdapter
import com.dscvit.notix.databinding.FragmentTransactionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.transactionsBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_transactionsFragment_to_homeFragment)
        }

        //Transactions Recycler View
        val transactionsRV = binding.transactionsRV
        transactionsRV.layoutManager = LinearLayoutManager(activity)
        val transactionsRVAdapter = context?.let { TransactionsAdapter(it) }
        transactionsRV.adapter = transactionsRVAdapter

        viewModel.allTransactions.observe(viewLifecycleOwner) { list ->
            list?.let {
                transactionsRVAdapter?.updateList(list)
            }
        }
    }


}