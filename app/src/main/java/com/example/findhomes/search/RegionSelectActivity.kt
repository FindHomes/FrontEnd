package com.example.findhomes.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.findhomes.databinding.ActivityContractRegionBinding

class RegionSelectActivity : AppCompatActivity() {
    lateinit var binding : ActivityContractRegionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContractRegionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)


        setContentView(binding.root)

    }
}