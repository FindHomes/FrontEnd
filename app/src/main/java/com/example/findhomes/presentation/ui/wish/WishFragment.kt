package com.example.findhomes.presentation.ui.wish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.databinding.FragmentWishBinding

class WishFragment : Fragment() {
    private lateinit var binding : FragmentWishBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishBinding.inflate(inflater, container, false)


        return binding.root
    }
}