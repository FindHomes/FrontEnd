package com.example.findhomes.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.findhomes.MainActivity
import com.example.findhomes.databinding.ActivitySearchDetailBinding


class SearchDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBefore()
        initNext()
    }

    private fun initBefore() {
        binding.ivBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initNext() {
        binding.ivConditionConfirm.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("openFragment", "searchFragment")
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}