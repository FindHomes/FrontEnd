package com.homes.findhomes.presentation.ui.wish

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.homes.findhomes.R
import com.homes.findhomes.data.model.SearchCompleteResponse
import com.homes.findhomes.databinding.FragmentRecentViewBinding
import com.homes.findhomes.presentation.ui.search.SearchDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class RecentViewFragment : Fragment() {
    lateinit var binding : FragmentRecentViewBinding
    private lateinit var recentAdapter: FavoriteAdapter
    private val viewModel: WishViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentViewBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadRecentData()

        initRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclerView() {
        recentAdapter = FavoriteAdapter(requireContext())
        binding.rvWishRecent.adapter = recentAdapter
        binding.rvWishRecent.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        recentAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener{
            override fun onItemClicked(data: SearchCompleteResponse) {
                val intent = Intent(requireContext(), SearchDetailActivity::class.java)
                intent.putExtra("houseId", data.houseId)
                intent.putExtra("source", "FavoriteFragment")
                startActivity(intent)
            }

        })
    }

    private fun observeViewModel() {
        viewModel.recentData.observe(viewLifecycleOwner){ recentData ->
            recentAdapter.submitList(recentData)
        }
    }
}