package com.dscvit.notix.ui.spam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.pm.PackageManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.notix.R
import com.dscvit.notix.adapters.OnWhitelistClickInterface
import com.dscvit.notix.adapters.WhitelistAdapter
import com.dscvit.notix.databinding.FragmentWhitelistBinding
import com.dscvit.notix.model.NotificationData

import com.dscvit.notix.model.WhiteListData
import com.dscvit.notix.ui.history.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class WhitelistFragment : Fragment(), OnWhitelistClickInterface {

    private var _binding: FragmentWhitelistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWhitelistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.whitelistBackButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_whitelistFragment_to_spamFragment)
        }



        val applicationInfoList = activity?.packageManager?.getInstalledApplications(PackageManager.GET_META_DATA);
        if (applicationInfoList != null) {

            viewModel.allWhitelistedApps.observe(viewLifecycleOwner) { list ->
                list?.let {
                    for(i in applicationInfoList){
                        var alreadyInTheList = false
                        for(j in list){
                            if(i.packageName == j.pkgName){
                                alreadyInTheList = true
                                break
                            }
                        }
                        if(!alreadyInTheList){
                            val data = WhiteListData(i.packageName, false)
                            runBlocking {
                                viewModel.insertWhitelisted(data)
                            }
                        }
                    }


                }
            }
        }

        val whitelistRV = binding.whitelistRV
        whitelistRV.layoutManager = LinearLayoutManager(activity)
        val whitelistRVAdapter = context?.let { WhitelistAdapter(it, this) }
        whitelistRV.adapter = whitelistRVAdapter


        viewModel.allWhitelistedApps.observe(viewLifecycleOwner) { list ->
            list?.let {
                    whitelistRVAdapter?.updateList(list)
            }
        }


    }

    override fun onWhitelistUpdate(whiteListData: WhiteListData) {
        viewModel.updateWhitelist(whiteListData)
    }

}


