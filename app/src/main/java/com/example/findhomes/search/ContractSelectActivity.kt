package com.example.findhomes.search

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
import com.example.findhomes.dataprovider.DataProvider

class ContractSelectActivity : AppCompatActivity() {
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button
    private lateinit var btnOffice: Button
    private lateinit var btnApart: Button
    private lateinit var btnMonthly: Button
    private lateinit var btnCharter: Button
    private lateinit var btnTrading: Button
    lateinit var binding: ActivityContractSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContractSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBefore()
        initRoomType()
        initContractType()
        initSeekBar()

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, RegionSelectActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBefore() {
        binding.btnBefore.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRoomType() {
        btnOne = binding.btnEssentialCategoryOne
        btnTwo = binding.btnEssentialCategoryTwo
        btnThree = binding.btnEssentialCategoryThree
        btnOffice = binding.btnEssentialCategoryOffice
        btnApart = binding.btnEssentialCategoryApart

        btnOne.isSelected = true
        btnOne.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))

        listOf(btnOne, btnTwo, btnThree, btnOffice, btnApart).forEach { button ->
            button.setOnClickListener {
                val previous = button.isSelected
                toggleButton(button)
                if(!warningSelected(listOf(btnOne, btnTwo, btnThree, btnOffice, btnApart))){
                    button.isSelected = previous
                }
                updatePrice(button)
            }
        }
    }

    private fun initContractType() {
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
                updatePrice(button)
            }
        }
    }

    private fun toggleButton(button: Button) {
        button.isSelected = !button.isSelected
        button.setTextColor(resources.getColorStateList(R.color.button_selector_text_color, null))
    }

    private fun updatePrice(button: Button) {
        when (button) {
            btnMonthly -> {
                binding.clContractMonthly.visibility =
                    if (btnMonthly.isSelected) View.VISIBLE else View.GONE
                binding.clContractDeposit.visibility =
                    if (btnMonthly.isSelected || btnCharter.isSelected) View.VISIBLE else View.GONE
            }

            btnCharter -> {
                binding.clContractDeposit.visibility =
                    if (btnCharter.isSelected || btnMonthly.isSelected) View.VISIBLE else View.GONE
            }

            btnTrading -> {
                binding.clContractTrading.visibility =
                    if (btnTrading.isSelected) View.VISIBLE else View.GONE
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
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

    }
}