package com.example.findhomes.presentation.ui.wish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.findhomes.R
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.databinding.FragmentFavoriteBinding
import com.example.findhomes.databinding.FragmentRecentViewBinding
import com.example.findhomes.presentation.ui.search.ResultRankingAdapter
import com.example.findhomes.presentation.ui.search.SearchDetailFragment
import com.example.findhomes.presentation.ui.search.SearchViewModel
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
                val bundle = Bundle()
                bundle.putInt("houseId", data.houseId)

                val nextFragment = SearchDetailFragment()
                nextFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, nextFragment)
                    .addToBackStack(null)
                    .commit()
            }

        })
    }

    private fun observeViewModel() {
        viewModel.recentData.observe(viewLifecycleOwner){ recentData ->
            recentAdapter.submitList(recentData)
        }
    }
}