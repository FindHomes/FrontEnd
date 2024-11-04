package com.example.findhomes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.findhomes.databinding.ActivityMainBinding
import com.example.findhomes.presentation.ui.home.HomeFragment
import com.example.findhomes.presentation.ui.mypage.MyPageFragment
import com.example.findhomes.presentation.ui.search.SearchFragment
import com.example.findhomes.presentation.ui.search.SearchViewModel
import com.example.findhomes.presentation.ui.wish.WishFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

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

        val fragmentToOpen = getFragmentFromIntent(intent) // 프래그먼트 및 데이터 설정

        openFragment(fragmentToOpen)
    }

    private fun getFragmentFromIntent(intent: Intent): Fragment {
        val fragment = when (intent.getStringExtra("openFragment")) {
            "searchFragment" -> SearchFragment()
            "interestFragment" -> WishFragment()
            "myPageFragment" -> MyPageFragment()
            else -> HomeFragment()
        }

        intent.getSerializableExtra("manConRequest")?.let {
            val bundle = Bundle()
            bundle.putSerializable("manConRequest", it)
            Log.d("manConRequestMain", "getFragmentFromIntent: $it")
            fragment.arguments = bundle
            Log.d("manConBundle", "getFragmentFromIntent: $bundle")
        }

        return fragment
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
            .commit()
    }
}
