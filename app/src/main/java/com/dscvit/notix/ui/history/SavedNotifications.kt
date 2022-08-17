package com.dscvit.notix.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.NotificationsAdapter
import com.dscvit.notix.adapters.UpDateNotificationInterface
import com.dscvit.notix.databinding.FragmentSavedNotificationsBinding
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNotifications : Fragment(), UpDateNotificationInterface {
    private var _binding: FragmentSavedNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.savedBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_savedNotifications_to_homeFragment)
        }

        //Saved Recycler View
        val savedRV = binding.savedRV
        savedRV.layoutManager = LinearLayoutManager(activity)
        val savedRVAdapter = context?.let { NotificationsAdapter(it, this) }
        savedRV.adapter = savedRVAdapter
        viewModel.allSavedNotifications .observe(viewLifecycleOwner) { list ->
            list?.let {
                savedRVAdapter?.updateList(list.reversed())
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