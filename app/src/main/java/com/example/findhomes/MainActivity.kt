package com.example.findhomes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.findhomes.databinding.ActivityMainBinding
import com.example.findhomes.home.HomeFragment
import com.example.findhomes.mypage.MyPageFragment
import com.example.findhomes.search.SearchFragment
import com.example.findhomes.wish.WishFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인텐트로부터 프래그먼트 정보를 받고, 조건에 따라 초기 프래그먼트 설정
        val fragmentToOpen = if (intent != null && intent.hasExtra("openFragment")) {
            when (intent.getStringExtra("openFragment")) {
                "searchFragment" -> SearchFragment()
                "interestFragment" -> WishFragment()
                "myPageFragment" -> MyPageFragment()
                else -> HomeFragment()  // 기본 값
            }
        } else {
            HomeFragment()  // 인텐트가 없는 경우 기본 프래그먼트로 HomeFragment 설정
        }

        initBottomNavigation(fragmentToOpen)
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
