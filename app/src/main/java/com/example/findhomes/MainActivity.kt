package com.example.findhomes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.findhomes.data.model.ManConRequest
import com.example.findhomes.databinding.ActivityMainBinding
import com.example.findhomes.presentation.ui.home.HomeFragment
import com.example.findhomes.presentation.ui.mypage.MyPageFragment
import com.example.findhomes.presentation.ui.search.SearchFragment
import com.example.findhomes.presentation.ui.search.SearchViewModel
import com.example.findhomes.presentation.ui.search.TestFragment
import com.example.findhomes.presentation.ui.wish.WishFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initBottomNavigation(getFragmentFromIntent(intent))
        setContentView(binding.root)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // 새 인텐트 설정

        val manConRequest = intent.getSerializableExtra("manConRequest") as? ManConRequest
        Log.d("manConRequest", manConRequest.toString())
        val fragmentToOpen = getFragmentFromIntent(intent)

//        if (manConRequest != null && fragmentToOpen is SearchFragment) {
//            viewModel.loadSearchData(manConRequest)
//        }
        if (manConRequest != null && fragmentToOpen is TestFragment) {
            viewModel.loadSearchData(manConRequest)
        }

        openFragment(fragmentToOpen)
        updateBottomNavigationSelection(fragmentToOpen)
    }

    private fun updateBottomNavigationSelection(fragment: Fragment) {
        binding.mainBnv.selectedItemId = when (fragment) {
            is HomeFragment -> R.id.homeFragment
//            is SearchFragment -> R.id.searchFragment
            is TestFragment -> R.id.searchFragment
            is WishFragment -> R.id.interestFragment
            is MyPageFragment -> R.id.myPageFragment
            else -> throw IllegalStateException("Unknown fragment type")
        }
    }

    private fun getFragmentFromIntent(intent: Intent): Fragment {
        return if (intent.hasExtra("openFragment")) {
            when (intent.getStringExtra("openFragment")) {
//                "searchFragment" -> SearchFragment()
                "searchFragment" -> TestFragment()
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
//            is SearchFragment -> R.id.searchFragment
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
//                    openFragment(SearchFragment())
                    openFragment(TestFragment())
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
            .commit()
    }
}
