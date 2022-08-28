package com.dscvit.notix.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.NotificationsAdapter
import com.dscvit.notix.adapters.OpenHistoryPage
import com.dscvit.notix.adapters.UpDateNotificationInterface
import com.dscvit.notix.databinding.FragmentHistoryBinding
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.ui.home.HomeFragmentDirections
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

        val statusBar = context?.let { ContextCompat.getColor(it, R.color.bg_color2) }
        if(statusBar!=null){
            activity?.window?.statusBarColor = statusBar
        }


        binding.historyBackButton.setOnClickListener {
            view.findNavController().navigate(HistoryFragmentDirections.actionHistoryFragmentToHomeFragment())
        }



        //History Recycler View
        val historyRV = binding.historyRV
        historyRV.layoutManager = LinearLayoutManager(activity)
        val historyRVAdapter = context?.let { NotificationsAdapter(it, this) }
        historyRV.adapter = historyRVAdapter

        viewModel.allNotifications.observe(viewLifecycleOwner) { list ->
            list?.let {
                if(list.isEmpty()){
                    binding.defaultHistoryText.visibility  = View.VISIBLE
                }else{
                    binding.defaultHistoryText.visibility = View.GONE
                    historyRVAdapter?.updateList(list.reversed())
                }

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