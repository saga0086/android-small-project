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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acronymlookup.databinding.FragmentFullformBinding
import com.example.acronymlookup.datamodel.Fullform

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
        this.viewModel = ViewModelProvider(this, LookupViewModelFactory()).get(LookupViewModel::class.java)
        this.binding = FragmentFullformBinding.inflate(inflater, container, false)
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

        setupRecyclerView()
        (activity as AppCompatActivity).supportActionBar?.hide()

        this.binding.buttonGoBack.setOnClickListener {
            //findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_content, UserInputFragment())
            transaction.commit()
        }

        this.binding.spinner.visibility = View.VISIBLE
        this.viewModel.fetchData(this.userInput)
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this.context)
        this.binding.recyclerview.layoutManager = layoutManager
        this.adapter = FullformAdapter(mutableListOf<Fullform>())
        this.binding.recyclerview.adapter = this.adapter

        viewModel.errorMessage.observe(viewLifecycleOwner, object : Observer<String> {
            override fun onChanged(msg: String?) {
                if (!msg.isNullOrEmpty()) {
                    binding.spinner.visibility = View.GONE
                    binding.errorMessage.setText(msg)
                }
            }
        })
        viewModel.fullFormsData.observe(viewLifecycleOwner, object : Observer<List<Fullform>> {
            override fun onChanged(data: List<Fullform>) {
                binding.spinner.visibility = View.GONE
                adapter.getDataList().clear()
                adapter.getDataList().addAll(data)
                adapter.notifyDataSetChanged()
            }
        })
    }
}