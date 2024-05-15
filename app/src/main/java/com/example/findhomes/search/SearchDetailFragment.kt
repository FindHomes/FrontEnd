package com.example.findhomes.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.findhomes.R
import com.example.findhomes.databinding.FragmentSearchDetailBinding

class SearchDetailFragment : Fragment() {
    private lateinit var binding : FragmentSearchDetailBinding
    private lateinit var selectedItems: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDetailBinding.inflate(inflater, container, false)

        val bundle = arguments
        if (bundle != null) {
            val value = bundle.getStringArrayList("category")
            selectedItems = value!!
            Log.d("category 출력", selectedItems.toString())
        }

        binding.ivConditionConfirm.setOnClickListener {
            val resultFragment = SearchResultFragment()
//        detailFragment.arguments = bundleOf("data" to postData)

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, resultFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}