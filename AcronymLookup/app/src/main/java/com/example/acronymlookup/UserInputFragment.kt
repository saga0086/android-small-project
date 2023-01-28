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
import com.example.acronymlookup.databinding.FragmentUserInputBinding

/**
 * A simple [Fragment] to take user input.
 */
class UserInputFragment : Fragment() {

    private lateinit var binding: FragmentUserInputBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentUserInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.binding.buttonLookup.setOnClickListener {
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            val transaction = parentFragmentManager.beginTransaction()
            val fragment = FullformFragment()
            val bundle = Bundle()
            bundle.putString(KEY_USER_INPUT, this.binding.textInput.text.toString())
            fragment.arguments = bundle
            transaction.replace(R.id.nav_host_fragment_content_main, fragment)
            transaction.commit()
        }
    }
}