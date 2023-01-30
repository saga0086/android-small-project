/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acronymlookup.databinding.FragmentFullformBinding
import com.example.acronymlookup.datamodel.Fullform
import com.example.acronymlookup.datamodel.LookupData
import com.example.acronymlookup.datasource.AcronymLookupService
import com.example.acronymlookup.datasource.LookupListener
import com.example.acronymlookup.datasource.remote.RemoteAcronymLookupService

/**
 * The fragment to display full form of user input
 */
const val KEY_USER_INPUT = "userInput"
class FullformFragment : Fragment() {
    private lateinit var binding: FragmentFullformBinding
    private lateinit var viewModel: LookupViewModel
    private lateinit var adapter: FullformAdapter
    private lateinit var userInput: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        this.viewModel = LookupViewModel(RemoteAcronymLookupService())
        this.binding = FragmentFullformBinding.inflate(inflater, container, false)
        this.binding.viewModel = this.viewModel
        this.binding.lifecycleOwner = this
        if (this.arguments != null){
            this.userInput = this.requireArguments().getString(KEY_USER_INPUT, "")
        }else{
            this.userInput = ""
        }

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(this.context)
        this.binding.recyclerview.layoutManager = layoutManager
        this.adapter = FullformAdapter(mutableListOf<Fullform>())
        this.binding.recyclerview.adapter = this.adapter
        this.binding.buttonGoBack.setOnClickListener {
            //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, UserInputFragment())
            transaction.commit()
        }

        this.viewModel.fetchData(this.userInput)
    }

    inner class LookupViewModel(service: AcronymLookupService) {
        val lookupService = service
        val isLoading: MutableLiveData<Boolean> = MutableLiveData()
        val errorMessage: MutableLiveData<String> = MutableLiveData()
        val lookupListener = object: LookupListener<List<LookupData>> {
            override fun onSuccess(data: List<LookupData>) {
                isLoading.value = false
                if (data.isEmpty() || data[0].lfs.isEmpty()) {
                    errorMessage.value = "Full form is not found..."
                } else{
                    adapter.getDataList().addAll(data[0].lfs)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(error: String) {
                isLoading.value = false
                errorMessage.value = error
            }
        }

        fun fetchData(name: String) {
            isLoading.value = true
            errorMessage.value = ""

            lookupService.lookup(name, lookupListener)
        }
    }
}