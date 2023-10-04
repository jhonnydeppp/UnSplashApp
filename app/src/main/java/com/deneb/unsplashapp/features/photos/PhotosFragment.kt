package com.deneb.unsplashapp.features.photos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.deneb.unsplashapp.core.exception.Failure
import com.deneb.unsplashapp.core.extensions.failure
import com.deneb.unsplashapp.core.extensions.observe
import com.deneb.unsplashapp.core.platform.BaseFragment
import com.deneb.unsplashapp.databinding.FragmentPhotosBinding
import com.deneb.unsplashapp.features.photos.model.UnsplashItemView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhotosFragment : BaseFragment<FragmentPhotosBinding>(FragmentPhotosBinding::inflate) {

    @Inject
    lateinit var photosAdapter: PhotosAdapter

    private val photosViewModel: PhotosViewModel by viewModels()
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(photosViewModel) {
            observe(photos, ::renderPhotoList)
            failure(failure, ::renderFailure)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
        loadPhotoList()
    }

    private fun initializeView() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.photoList.layoutManager = layoutManager
        binding.photoList.adapter = photosAdapter
        photosAdapter.clickListener = { photo ->
            val action = PhotosFragmentDirections.actionPhotosFragmentToDetailPhotoFragment(photo.id)
            findNavController().navigate(action)
        }

        binding.photoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPositions =
                    layoutManager.findLastCompletelyVisibleItemPositions(null)
                val lastVisibleItemPosition = lastVisibleItemPositions.max()

                if (totalItemCount-1 == lastVisibleItemPosition) {
                    page++
                    loadPhotoList()
                }
            }
        })
    }

    private fun loadPhotoList() {
        showProgress()
        photosViewModel.loadPhotos(page)
    }

    private fun renderPhotoList(photos: List<UnsplashItemView>?) {
        photosAdapter.collection = photos.orEmpty()
        hideProgress()
    }

    private fun renderFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> Log.e(DetailPhotoFragment::class.java.canonicalName, "Network Connection Error")
            is Failure.ServerError -> Log.e(DetailPhotoFragment::class.java.canonicalName, "Server Error")
            else -> Log.e(DetailPhotoFragment::class.java.canonicalName, "Error")
        }
    }

}