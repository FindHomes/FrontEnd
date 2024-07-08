package com.example.findhomes.search

import android.os.Bundle
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
    lateinit var binding : ActivityContractSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContractSelectBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        initHousingType()

        setContentView(binding.root)
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
    }

    private fun initContractForm() {

    }
}