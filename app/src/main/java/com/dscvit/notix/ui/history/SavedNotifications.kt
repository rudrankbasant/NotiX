package com.dscvit.notix.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

        val statusBar = context?.let { ContextCompat.getColor(it, R.color.bg_color2) }
        if(statusBar!=null){
            activity?.window?.statusBarColor = statusBar
        }


        binding.savedBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_savedNotifications_to_homeFragment)
        }

        //Saved Recycler View
        val savedRV = binding.savedRV
        savedRV.layoutManager = LinearLayoutManager(activity)
        val savedRVAdapter = context?.let { NotificationsAdapter(it, this) }
        savedRV.adapter = savedRVAdapter
        viewModel.allSavedNotifications.observe(viewLifecycleOwner) { list ->
            list?.let {
                if(list.isEmpty()){
                    binding.defaultSavedText.visibility  = View.VISIBLE
                    savedRVAdapter?.updateList(list.reversed())
                }else{
                    binding.defaultSavedText.visibility = View.GONE
                    savedRVAdapter?.updateList(list.reversed())
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