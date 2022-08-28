package com.dscvit.notix.ui.spam

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
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

        val sharedPref = context?.getSharedPreferences("spam_switch", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        //getting values
        val spamToggle = sharedPref?.getString("switch_toggle","true")
        binding.spamSwitch.isChecked = spamToggle=="true"

        //setting values
        binding.spamSwitch.setOnClickListener{view ->

            val switchView = view as SwitchCompat
            when (switchView.isChecked) {
                true -> {
                    Toast.makeText(activity, "Spam Notifications will be removed from status bar.", Toast.LENGTH_LONG).show()
                    editor?.apply {
                        putString("switch_toggle", "true")
                        apply()
                    }
                }
                false -> {
                    Toast.makeText(activity, "No Spam Notifications will be removed from status bar.", Toast.LENGTH_LONG).show()
                    editor?.apply {
                        putString("switch_toggle", "false")
                        apply()
                    }
                }
            }
        }
    }

    override fun upDateNotification(notificationData: NotificationData) {
        viewModel.update(notificationData)
    }


}