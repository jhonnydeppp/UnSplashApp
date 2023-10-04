package com.deneb.unsplashapp.features.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deneb.unsplashapp.core.interactor.UseCase
import com.deneb.unsplashapp.core.platform.BaseViewModel
import com.deneb.unsplashapp.features.photos.model.UnsplashItemView
import com.deneb.unsplashapp.features.photos.model.UnsplashResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel
@Inject constructor(private val getPhotos: GetPhotos): BaseViewModel() {

    private val _photos: MutableLiveData<List<UnsplashItemView>> = MutableLiveData()
    val photos: LiveData<List<UnsplashItemView>> = _photos

    fun loadPhotos(page: Int = 1) =
        getPhotos(page, viewModelScope) {it.fold(::handleFailure, ::handlePhotoList)}

    private fun handlePhotoList(photos: List<UnsplashItemView>) {
        if(_photos.value == null)
            _photos.value = emptyList()
        _photos.value = _photos.value?.plus(photos)
    }
}