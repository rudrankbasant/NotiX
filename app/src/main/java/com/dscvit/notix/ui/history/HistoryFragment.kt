package com.dscvit.notix.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.HistoryAdapter
import com.dscvit.notix.adapters.UpDateNotificationInterface
import com.dscvit.notix.databinding.FragmentHistoryBinding
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(), UpDateNotificationInterface {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_historyFragment_to_homeFragment)
        }

        //History Recycler View
        val historyRV = binding.historyRV
        historyRV.layoutManager = LinearLayoutManager(activity)
        val historyRVAdapter = context?.let { HistoryAdapter(it, this) }
        historyRV.adapter = historyRVAdapter

        viewModel.allNotifications.observe(viewLifecycleOwner) { list ->
            list?.let {
                historyRVAdapter?.updateList(list)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun upDateNotification(notificationData: NotificationData) {
        viewModel.update(notificationData)
    }


}