package com.example.findhomes.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.findhomes.R
import com.example.findhomes.databinding.FragmentSearchDetailBinding

class SearchDetailFragment : Fragment() {
    private lateinit var binding : FragmentSearchDetailBinding
    private lateinit var categories: ArrayList<String>
    private lateinit var contracts: ArrayList<String>
    private lateinit var regions: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDetailBinding.inflate(inflater, container, false)

        val bundle = arguments
        if (bundle != null) {
            val category = bundle.getStringArrayList("category")
            val contract = bundle.getStringArrayList("contract")
            val region = bundle.getStringArrayList("region")

            categories = category!!
            contracts = contract!!
            regions = region!!

            Log.d("category 출력", categories.toString())
            Log.d("contract 출력", contracts.toString())
            Log.d("region 출력", regions.toString())

        }

        binding.ivConditionConfirm.setOnClickListener {
            initInput(categories)
        }

        initBack()

        return binding.root
    }

    private fun initInput(categories: java.util.ArrayList<String>) {

        val detailData = binding.etConditionInput.text.toString()
        val resultFragment = SearchResultFragment()
        resultFragment.arguments = bundleOf(
            "detail" to detailData,
            "category" to categories,
            "region" to regions,
            "contract" to contracts)

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, resultFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            
        }
    }

}