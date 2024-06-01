package com.example.findhomes.search

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.ContractFormData
import com.example.findhomes.data.RankingInfo
import com.example.findhomes.databinding.FragmentSearchBinding
import org.jetbrains.annotations.Contract

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnOffice: Button
    private lateinit var btnApart: Button
    var essentialContractFormAdapter : EssentialContractFormAdapter ?= null
    var essentialPreferredRegionAdapter : EssentialPreferredRegionAdapter ?= null
    var contractFormList: ArrayList<ContractFormData> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        initHousingType()
        initContractForm()
        initPreferredRegion()

        return binding.root
    }
    private fun initHousingType() {
        btnOne = binding.btnEssentialCategoryOne
        btnTwo = binding.btnEssentialCategoryTwo
        btnThree = binding.btnEssentialCategoryThree
        btnOffice = binding.btnEssentialCategoryOffice
        btnApart = binding.btnEssentialCategoryApart

        listOf(btnOne, btnTwo, btnThree, btnOffice, btnApart).forEach { button ->
            button.setOnClickListener {
                button.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))
                it.isSelected = !it.isSelected
            }
        }

        binding.btnEssentialComplete.setOnClickListener {
            val detailFragment = SearchDetailFragment()

            val selectedItems = arrayListOf<String>()
            if (btnOne.isSelected) selectedItems.add("one")
            if (btnTwo.isSelected) selectedItems.add("two")
            if (btnThree.isSelected) selectedItems.add("three")
            if (btnOffice.isSelected) selectedItems.add("office")
            if (btnApart.isSelected) selectedItems.add("apart")

            detailFragment.arguments = bundleOf("category" to selectedItems)
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initPreferredRegion() {
        essentialContractFormAdapter = EssentialContractFormAdapter(contractFormList)
        binding.rvEssentialConditionContractForm.adapter = essentialContractFormAdapter
        binding.rvEssentialConditionContractForm.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initContractForm() {
        TODO("Not yet implemented")
    }

}