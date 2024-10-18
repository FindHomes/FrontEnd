package com.example.findhomes.presentation.ui.wish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.databinding.FragmentWishBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishFragment : Fragment() {
    private lateinit var binding : FragmentWishBinding
    val tabList = arrayListOf("최근 본 방","찜한 방", "검색 기록")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        binding.vpMain.adapter = TabLayoutVPAdapter(requireActivity())
        TabLayoutMediator(binding.tbWish, binding.vpMain){ tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}