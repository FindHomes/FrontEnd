package com.example.findhomes.presentation.ui.essential

import android.app.ActivityOptions
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

        initBack()
        initHousingTypes()
        initContractTypes()
        initSeekBar()

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, RegionSelectActivity::class.java)
            val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.stay_in_place)
            intent.putExtra("manConRequest", manConRequest)
            startActivity(intent, options.toBundle())
        }
    }

    private fun initBack() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.stay_in_place, R.anim.slide_out_right)
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
        initPriceTypes(btnMonthly)

        listOf(btnMonthly, btnCharter, btnTrading).forEach { button ->
            button.setOnClickListener {
                val previous = button.isSelected
                toggleButton(button)
                if (!warningSelected(listOf(btnMonthly, btnCharter, btnTrading))) {
                    button.isSelected = previous
                } else {
                    initPriceTypes(button)

                }
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

    // Data Provider의 금액 text와 mapping
    fun parsePrice(text: String): Int {
        return when {
            text.contains("무제한") -> Int.MAX_VALUE
            text.contains("억") -> {
                val parts = text.split("억")
                var sum = 0
                if (parts[0].isNotEmpty()) {
                    // "억" 앞의 수치 계산 (억은 10,000으로 계산)
                    sum += (parts[0].filter { it.isDigit() }.toIntOrNull() ?: 0) * 10000
                }
                if (parts.size > 1 && parts[1].isNotEmpty()) {
                    // "억" 뒤의 "만" 단위 수치 계산
                    val millionPart = parts[1].filter { it.isDigit() }
                    if (millionPart.isNotEmpty()) {
                        sum += millionPart.toInt() // 억 + 만
                    }
                }
                sum
            }
            else -> {
                // "만" 단위만 있는 경우 수치 계산
                val millionPart = text.filter { it.isDigit() }
                if (millionPart.isNotEmpty()) {
                    millionPart.toInt() // 만
                } else {
                    0
                }
            }
        }
    }


    // ManCon에 추가될 pricesTypes
    private fun updatePriceTypes(type: String, progress: Int) {
        val priceText = DataProvider.getSbData(type, progress)
        val priceValue = parsePrice(priceText)

        when (type) {
            "월세" -> {
                if (btnMonthly.isSelected) {
                    manConRequest.prices.ws.rent = priceValue
                    manConRequest.prices.ws.deposit = parsePrice(DataProvider.getSbData("보증금", binding.sbDeposit.progress))
                } else {
                    manConRequest.prices.ws.rent = 0
                    manConRequest.prices.ws.deposit = 0
                }
            }

            "보증금" -> {
                if (btnMonthly.isSelected) {
                    manConRequest.prices.ws.deposit = priceValue
                } else {
                    manConRequest.prices.ws.deposit = 0
                }

                if (btnCharter.isSelected) {
                    manConRequest.prices.js = priceValue
                } else {
                    manConRequest.prices.js = 0
                }
            }

            "매매" -> {
                if (btnTrading.isSelected) {
                    manConRequest.prices.mm = priceValue
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

        config.forEach { (seekBar, type, textView) ->
            val maxProgress = seekBar.max
            seekBar.progress = maxProgress  // 초기 진행 상태를 최대로 설정
            textView.text = DataProvider.getSbData(type, maxProgress)  // 최대값 표시
            updatePriceTypes(type, maxProgress)  // 초기 가격 유형을 최대값으로 업데이트

            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    textView.text = DataProvider.getSbData(type, progress)
                    updatePriceTypes(type, progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

    }
}