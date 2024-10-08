package com.example.findhomes.presentation.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.R
import com.example.findhomes.databinding.FragmentShowStatisticBinding

class ShowStatisticFragment : Fragment() {
    lateinit var binding : FragmentShowStatisticBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowStatisticBinding.inflate(layoutInflater)

        return binding.root
    }
}