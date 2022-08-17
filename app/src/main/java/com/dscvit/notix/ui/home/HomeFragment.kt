package com.dscvit.notix.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.NotificationsAdapter
import com.dscvit.notix.adapters.UpDateNotificationInterface
import com.dscvit.notix.databinding.FragmentHomeBinding
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.ui.conversations.ConversationsViewModel
import com.dscvit.notix.ui.history.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), UpDateNotificationInterface {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date()).toString()


        viewModel.getTotalTodayNotifs(currentDate).observe(viewLifecycleOwner) { list ->
            list?.let {
                binding.totalNotificationsNumber.text = list.size.toString()
            }
        }
        viewModel.getTotalTodaySpamNotifs(currentDate).observe(viewLifecycleOwner) { list ->
            list?.let {

                binding.spamNotificationsNumber.text = list.size.toString()
            }
        }

        //Recents Recycler View
        val recentRV = binding.recentsRecyclerView
        recentRV.layoutManager = LinearLayoutManager(activity)
        val recentRVAdapter = context?.let { NotificationsAdapter(it, this) }
        recentRV.adapter = recentRVAdapter

        historyViewModel.allNotifications.observe(viewLifecycleOwner) { list ->
            list?.let {
                if(list.isNotEmpty()){
                    binding.recentsRecyclerView.visibility = View.VISIBLE
                    var endIndex = 0
                    if(list.size>4){
                        endIndex = 5
                    }else{
                        endIndex = list.size
                    }
                    val recentList = list.reversed().subList(0,endIndex)
                    recentRVAdapter?.updateList(recentList)
                }
            }
        }


        binding.analyticsCardView.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_analyticsFragment)
        }

        binding.conversationsCard.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_conversationsFragment)
        }

        binding.savedCard.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_savedNotifications)
        }

        binding.spamCardView.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_spamFragment)
        }

        binding.recentNotificationsCard.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun upDateNotification(notificationData: NotificationData) {
        historyViewModel.update(notificationData)
    }

}