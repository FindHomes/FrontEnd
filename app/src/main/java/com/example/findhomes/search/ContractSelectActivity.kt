package com.example.findhomes.search

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.findhomes.R
import com.example.findhomes.databinding.ActivityContractSelectBinding
import com.example.findhomes.databinding.FragmentSearchBinding

class ContractSelectActivity : AppCompatActivity() {
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnOffice: Button
    private lateinit var btnApart: Button
    private lateinit var btnMonthly : Button
    private lateinit var btnCharter : Button
    private lateinit var btnTrading : Button
    private var priceAdapter : ContractPriceAdapter ?= null
    lateinit var binding : ActivityContractSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContractSelectBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        initRoomType()
        initContractType()

        setContentView(binding.root)
    }

    private fun initRoomType() {
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
    }

    private fun initContractType() {
        btnMonthly = binding.btnContractMonthly
        btnCharter = binding.btnContractCharter
        btnTrading = binding.btnContractTrading

        //초기 진입 화면은 월세와 전세가 선택된 상황
        btnMonthly.isSelected
        btnCharter.isSelected
        btnMonthly.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))
        btnCharter.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))


        listOf(btnMonthly, btnCharter, btnTrading).forEach{ button ->
            button.setOnClickListener {
                button.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))
                it.isSelected = !it.isSelected
                if(it == btnMonthly && it.isSelected){
                    binding.clContractMonthly.visibility = View.VISIBLE
                } else if (it == btnMonthly && !it.isSelected) {
                    binding.clContractMonthly.visibility = View.GONE
                }
                if(it == btnCharter && it.isSelected){
                    binding.clContractCharter.visibility = View.VISIBLE
                }else if (it == btnCharter && !it.isSelected) {
                    binding.clContractCharter.visibility = View.GONE
                }
            }
        }
    }
}