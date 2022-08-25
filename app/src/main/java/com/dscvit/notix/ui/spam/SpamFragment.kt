package com.dscvit.notix.ui.spam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.NotificationsAdapter
import com.dscvit.notix.adapters.UpDateNotificationInterface
import com.dscvit.notix.databinding.FragmentSpamBinding
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.ui.history.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpamFragment : Fragment(), UpDateNotificationInterface {

    private var _binding: FragmentSpamBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusBar = context?.let { ContextCompat.getColor(it, R.color.bg_color2) }
        if(statusBar!=null){
            activity?.window?.statusBarColor = statusBar
        }

        binding.spamSettings.setOnClickListener {
            view.findNavController().navigate(R.id.action_spamFragment_to_whitelistFragment)
        }

        binding.spamBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_spamFragment_to_homeFragment)
        }

        //Spam Recycler View
        val spamRV = binding.spamRV
        spamRV.layoutManager = LinearLayoutManager(activity)
        val spamRVAdapter = context?.let { NotificationsAdapter(it, this) }
        spamRV.adapter = spamRVAdapter

        viewModel.allSpam.observe(viewLifecycleOwner) { list ->
            list?.let {
                if(list.isEmpty()){
                    binding.defaultSpamText.visibility  = View.VISIBLE
                }else{
                    binding.defaultSpamText.visibility = View.GONE
                    spamRVAdapter?.updateList(list.reversed())
                }

            }
        }
    }

    override fun upDateNotification(notificationData: NotificationData) {
        viewModel.update(notificationData)
    }


}