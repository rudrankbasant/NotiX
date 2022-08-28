package com.dscvit.notix.ui.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.OpenHistoryPage
import com.dscvit.notix.adapters.RecentsAdapter
import com.dscvit.notix.adapters.UpDateNotificationInterface
import com.dscvit.notix.databinding.FragmentHomeBinding
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.ui.history.HistoryViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment(), UpDateNotificationInterface, OpenHistoryPage {

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

        val statusBar = context?.let { ContextCompat.getColor(it, R.color.bg_color) }
        if(statusBar!=null){
            activity?.window?.statusBarColor = statusBar
        }




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


        val bsb: BottomSheetBehavior<*> = BottomSheetBehavior.from(binding.bottomSheet)
        binding.bottomSheet.minimumHeight = 64;
        bsb.setBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                var strNewState = ""
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> strNewState = "STATE_COLLAPSED"
                    BottomSheetBehavior.STATE_EXPANDED -> strNewState = "STATE_EXPANDED"
                    BottomSheetBehavior.STATE_HIDDEN -> strNewState = "STATE_HIDDEN"
                    BottomSheetBehavior.STATE_DRAGGING -> strNewState = "STATE_DRAGGING"
                    BottomSheetBehavior.STATE_SETTLING -> strNewState = "STATE_SETTLING"
                }
                Log.i("BottomSheets", "New state: $strNewState")
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                Log.i("BottomSheets", "Offset: $slideOffset")
            }
        })

        //Recents Recycler View
        val recentRV = binding.recentsRecyclerView
        recentRV.layoutManager = LinearLayoutManager(activity)
        val recentRVAdapter = context?.let { RecentsAdapter(it, this, this) }
        recentRV.adapter = recentRVAdapter

        historyViewModel.allNotifications.observe(viewLifecycleOwner) { list ->
            list?.let {
                if (list.isNotEmpty()) {
                    binding.recentsRecyclerView.visibility = View.VISIBLE
                    var endIndex = 0
                    if (list.size > 14) {
                        endIndex = 15
                    } else {
                        endIndex = list.size
                    }
                    val recentList = list.reversed().subList(0, endIndex)
                    recentRVAdapter?.updateList(recentList)
                }
            }
        }

        binding.viewFullHistoryTV.setOnClickListener {
            view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment())
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
            view.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment())
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun upDateNotification(notificationData: NotificationData) {
        historyViewModel.update(notificationData)
    }

    override fun openHistoryPage() {
        findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
    }

}