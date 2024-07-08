package com.example.findhomes.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.findhomes.R
import com.example.findhomes.databinding.FragmentHomeBinding
import com.example.findhomes.mypage.MyPageFragment
import com.example.findhomes.search.ContractSelectActivity
import com.example.findhomes.search.RegionSelectFragment
import com.example.findhomes.wish.WishFragment

class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val transaction = parentFragmentManager.beginTransaction()

        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireActivity(), ContractSelectActivity::class.java)
            startActivity(intent)
        }

        binding.btnWish.setOnClickListener {
            transaction.replace(R.id.main_frm, WishFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.btnMyPage.setOnClickListener {
            transaction.replace(R.id.main_frm, MyPageFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return binding.root
    }

}