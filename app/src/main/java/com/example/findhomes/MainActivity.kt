package com.example.findhomes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.findhomes.databinding.ActivityMainBinding
import com.example.findhomes.presentation.ui.home.HomeFragment
import com.example.findhomes.presentation.ui.mypage.MyPageFragment
import com.example.findhomes.presentation.ui.search.SearchFragment
import com.example.findhomes.presentation.ui.wish.WishFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentToOpen = getFragmentFromIntent(intent)
        initBottomNavigation(fragmentToOpen)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)  // 새 인텐트로 현재 인텐트 업데이트
        val fragmentToOpen = getFragmentFromIntent(intent)
        initBottomNavigation(fragmentToOpen)
    }

    private fun getFragmentFromIntent(intent: Intent): Fragment {
        return if (intent.hasExtra("openFragment")) {
            when (intent.getStringExtra("openFragment")) {
                "searchFragment" -> SearchFragment()
                "interestFragment" -> WishFragment()
                "myPageFragment" -> MyPageFragment()
                else -> HomeFragment()
            }
        } else {
            HomeFragment()
        }
    }

    private fun initBottomNavigation(defaultFragment: Fragment) {
        openFragment(defaultFragment)
        binding.mainBnv.selectedItemId = when (defaultFragment) {
            is SearchFragment -> R.id.searchFragment
            is WishFragment -> R.id.interestFragment
            is MyPageFragment -> R.id.myPageFragment
            else -> R.id.homeFragment
        }

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.searchFragment -> {
                    openFragment(SearchFragment())
                    true
                }
                R.id.interestFragment -> {
                    openFragment(WishFragment())
                    true
                }
                R.id.myPageFragment -> {
                    openFragment(MyPageFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment)
            .commitAllowingStateLoss()
    }
}
