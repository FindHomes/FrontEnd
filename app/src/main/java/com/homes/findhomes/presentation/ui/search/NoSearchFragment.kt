package com.homes.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.homes.findhomes.R
import com.homes.findhomes.databinding.FragmentNoSearchBinding

class NoSearchFragment : Fragment() {
    lateinit var binding : FragmentNoSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoSearchBinding.inflate(layoutInflater)
        return binding.root
    }
}