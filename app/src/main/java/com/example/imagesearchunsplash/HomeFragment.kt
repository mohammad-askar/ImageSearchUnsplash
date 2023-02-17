package com.example.imagesearchunsplash

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.imagesearchunsplash.data.model.UnsplashPhoto
import com.example.imagesearchunsplash.databinding.FragmentHomeBinding
import com.example.imagesearchunsplash.presentation.adapter.UnsplashPhotoAdapter
import com.example.imagesearchunsplash.presentation.adapter.UnsplashPhotoStateAdapter
import com.example.imagesearchunsplash.presentation.viewModel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), MenuProvider,
    UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: UnsplashPhotoAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UnsplashPhotoAdapter(this)
        _binding = FragmentHomeBinding.bind(view)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter!!.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoStateAdapter { adapter!!.retry() },
                footer = UnsplashPhotoStateAdapter { adapter!!.retry() }
            )
            buttonRetry.setOnClickListener {
                adapter!!.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter!!.submitData(viewLifecycleOwner.lifecycle, it)
        }
        activity?.addMenuProvider(this, viewLifecycleOwner)
        adapter!!.addLoadStateListener { loadState ->
            getBinding(loadState)
        }
    }

    private fun getBinding(loadState: CombinedLoadStates) = binding.apply {

        progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
        buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
        textViewError.isVisible = loadState.source.refresh is LoadState.Error

        if (loadState.source.refresh is LoadState.NotLoading &&
            loadState.append.endOfPaginationReached &&
            adapter!!.itemCount < 1
        ) {
            recyclerView.isVisible = false
            textViewEmpty.isVisible = true
        } else {
            textViewEmpty.isVisible = false
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.menu_gallery, menu)
        val searchItem = menu.findItem(R.id.searchBar)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

    override fun onItemClick(photo: UnsplashPhoto) {

        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(photo)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapter = null
    }
}