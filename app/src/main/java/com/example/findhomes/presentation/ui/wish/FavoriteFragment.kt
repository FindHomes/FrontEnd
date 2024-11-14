package com.example.findhomes.presentation.ui.wish

import android.content.Intent
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
import com.example.findhomes.presentation.ui.search.SearchDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    lateinit var binding : FragmentFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel: WishViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.loadFavoriteData()

        initRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun initRecyclerView() {
        favoriteAdapter = FavoriteAdapter(requireContext())
        binding.rvWishFavorite.adapter = favoriteAdapter
        binding.rvWishFavorite.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        favoriteAdapter.setOnItemClickListener(object : FavoriteAdapter.OnItemClickListener{
            override fun onItemClicked(data: SearchCompleteResponse) {
                val intent = Intent(requireContext(), SearchDetailActivity::class.java)
                intent.putExtra("houseId", data.houseId)
                intent.putExtra("source", "FavoriteFragment")
                startActivity(intent)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.favoriteData.observe(viewLifecycleOwner){ favoriteData ->
            favoriteAdapter.submitList(favoriteData)
        }
    }
}