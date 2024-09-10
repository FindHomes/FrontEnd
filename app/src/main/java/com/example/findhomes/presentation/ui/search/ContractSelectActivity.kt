package com.example.findhomes.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.findhomes.R
import com.example.findhomes.databinding.ActivityContractSelectBinding
import com.example.findhomes.data.dataprovider.DataProvider
import com.example.findhomes.data.model.ManConRequest

class ContractSelectActivity : AppCompatActivity() {
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnOffice: Button
    private lateinit var btnApart: Button
    private lateinit var btnMonthly: Button
    private lateinit var btnCharter: Button
    private lateinit var btnTrading: Button
    private val manConRequest = ManConRequest()

    lateinit var binding: ActivityContractSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContractSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBefore()
        initHousingTypes()
        initContractTypes()
        initSeekBar()

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, RegionSelectActivity::class.java)
            intent.putExtra("manConRequest", manConRequest)
            startActivity(intent)
        }
    }

    private fun initBefore() {
        binding.btnBefore.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initHousingTypes() {
        btnOne = binding.btnEssentialCategoryOne
        btnTwo = binding.btnEssentialCategoryTwo
        btnThree = binding.btnEssentialCategoryThree
        btnOffice = binding.btnEssentialCategoryOffice
        btnApart = binding.btnEssentialCategoryApart

        // 초기 월세 버튼 선택된 상태, manConRequest 에도 추가
        btnOne.isSelected = true
        updateHousingTypes(btnOne, manConRequest)
        btnOne.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))

        listOf(btnOne, btnTwo, btnThree, btnOffice, btnApart).forEach { button ->
            button.setOnClickListener {
                val previous = button.isSelected
                toggleButton(button)
                if(!warningSelected(listOf(btnOne, btnTwo, btnThree, btnOffice, btnApart))){
                    button.isSelected = previous
                } else {
                    updateHousingTypes(button, manConRequest)
                }
            }
        }
    }

    // manCon에 추가될 housingTypes
    private fun updateHousingTypes(button: Button, manConRequest : ManConRequest) {
        val type = button.text.toString()
        if (button.isSelected) {
            if (!manConRequest.housingTypes.contains(type)) {
                manConRequest.housingTypes.add(type)
            }
        } else {
            manConRequest.housingTypes.remove(type)
        }
    }

    // 계약 형태 선택
    private fun initContractTypes() {
        btnMonthly = binding.btnContractMonthly
        btnCharter = binding.btnContractCharter
        btnTrading = binding.btnContractTrading

        btnMonthly.isSelected = true
        btnMonthly.setTextColor(resources.getColorStateList(R.color.button_selector_text_color,null))

        listOf(btnMonthly, btnCharter, btnTrading).forEach { button ->
            button.setOnClickListener {
                val previous = button.isSelected
                toggleButton(button)
                if (!warningSelected(listOf(btnMonthly, btnCharter, btnTrading))) {
                    button.isSelected = previous
                }
                initPriceTypes(button)
            }
        }
    }

    private fun toggleButton(button: Button) {
        button.isSelected = !button.isSelected
        button.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))
    }


    // 계약 형태 선택에 따른 가격 UI 변경
    private fun initPriceTypes(button: Button) {
        when (button) {
            btnMonthly-> {
                val isVisible = btnMonthly.isSelected
                binding.clContractMonthly.visibility = if (isVisible) View.VISIBLE else View.GONE
                binding.clContractDeposit.visibility = if (btnMonthly.isSelected || btnCharter.isSelected) View.VISIBLE else View.GONE
                updatePriceTypes("월세", binding.sbMonthly.progress)
                updatePriceTypes("보증금", binding.sbDeposit.progress)
            }
            btnCharter ->{
                binding.clContractDeposit.visibility = if (btnMonthly.isSelected || btnCharter.isSelected) View.VISIBLE else View.GONE
                updatePriceTypes("월세", binding.sbMonthly.progress)
                updatePriceTypes("보증금", binding.sbDeposit.progress)
            }
            btnTrading -> {
                binding.clContractTrading.visibility = if (btnTrading.isSelected) View.VISIBLE else View.GONE
                updatePriceTypes("매매", binding.sbTrading.progress)
            }
        }
    }

    // ManCon에 추가될 pricesTypes
    private fun updatePriceTypes(type: String, progress: Int) {
        when (type) {
            "월세" -> {
                if (btnMonthly.isSelected) {
                    manConRequest.prices.ws.rent = progress
                    manConRequest.prices.ws.deposit = binding.sbDeposit.progress
                } else {
                    manConRequest.prices.ws.rent = 0
                    manConRequest.prices.ws.deposit = 0
                }
            }

            "보증금" -> {
                if (btnMonthly.isSelected) {
                    manConRequest.prices.ws.deposit = progress
                } else {
                    manConRequest.prices.ws.deposit = 0
                }

                if (btnCharter.isSelected) {
                    manConRequest.prices.js = progress
                } else {
                    manConRequest.prices.js = 0
                }
            }

            "매매" -> {
                if (btnTrading.isSelected) {
                    manConRequest.prices.mm = progress
                } else {
                    manConRequest.prices.mm = 0
                }
            }
        }
    }

    private fun warningSelected(buttons: List<Button>): Boolean {
        if (buttons.none { it.isSelected }) {
            Toast.makeText(this, "하나 이상의 옵션을 선택해주세요!", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun initSeekBar() {
        val config: List<Triple<SeekBar, String, TextView>> = listOf(
            Triple(binding.sbMonthly, "월세", binding.tvMaxMonthly),
            Triple(binding.sbDeposit, "보증금", binding.tvMaxDeposit),
            Triple(binding.sbTrading, "매매", binding.tvMaxTrading)
        )

        config.forEach { (seekBar, type, price) ->
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    price.text = DataProvider.getSbData(type, progress)
                    updatePriceTypes(type, progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

    }
}