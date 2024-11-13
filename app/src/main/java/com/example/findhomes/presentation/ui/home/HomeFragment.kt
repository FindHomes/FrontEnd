package com.example.findhomes.presentation.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.R
import com.example.findhomes.databinding.FragmentHomeBinding
import com.example.findhomes.presentation.ui.mypage.MyPageFragment
import com.example.findhomes.presentation.ui.essential.ContractSelectActivity
import com.example.findhomes.presentation.ui.wish.WishFragment

class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val transaction = parentFragmentManager.beginTransaction()

        val imgList = listOf(R.drawable.ic_house_example, R.drawable.ic_house_example_2, R.drawable.ic_house_example_3)
        binding.homeIvTitle.setImageResource(imgList.random())

        binding.btnSearch.setOnClickListener {
            val intent = Intent(requireActivity(), ContractSelectActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(requireContext(), R.anim.slide_in_right, R.anim.stay_in_place)
            startActivity(intent, options.toBundle())
        }

        binding.btnWish.setOnClickListener {
            transaction.replace(R.id.main_frm, WishFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.btnMy.setOnClickListener {
            transaction.replace(R.id.main_frm, MyPageFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return binding.root
    }

}