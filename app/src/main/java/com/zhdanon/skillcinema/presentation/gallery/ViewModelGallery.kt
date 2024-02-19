package com.zhdanon.skillcinema.presentation.gallery

import androidx.lifecycle.viewModelScope
import com.zhdanon.skillcinema.core.AppResources
import com.zhdanon.skillcinema.core.BaseViewModel
import com.zhdanon.skillcinema.core.CategoriesImages
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.domain.models.Image
import com.zhdanon.skillcinema.domain.usecasesAPI.GetImagesByMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGallery @Inject constructor(
    private val getImages: GetImagesByMovieUseCase,
    private val appResources: AppResources
) : BaseViewModel() {

    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val images = _images.asStateFlow()

    private lateinit var allImagesByFilm: Map<String, List<Image>>
    private lateinit var currentCategory: String

    private val _galleryChipList = MutableStateFlow<Map<String, Int>>(emptyMap())
    val galleryChipList = _galleryChipList.asStateFlow()

    fun setChipGroup(movieId: Int) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                _loadState.value = StateLoading.Loading
                val tempChipsMap = mutableMapOf<String, Int>()
                val tempImagesMap = mutableMapOf<String, List<Image>>()
                CategoriesImages.entries.forEach { category ->
                    val response = getImages.execute(movieId, category.name)
                    if (response.images.isNotEmpty()) {
                        tempChipsMap[category.name] = response.totalImages
                        tempImagesMap[category.name] = response.images
                    }
                }
                allImagesByFilm = tempImagesMap
                _galleryChipList.value = tempChipsMap
                _loadState.value = StateLoading.Success
            }
        } catch (e: Exception) {
            _loadState.value = StateLoading.Error(e.message.toString())
        }
    }

    fun updateGalleryType(category: String) {
        currentCategory = category
        viewModelScope.launch(Dispatchers.IO) {
            if (allImagesByFilm.keys.contains(category)) _images.value = allImagesByFilm[category]!!
        }
    }

    fun getAppResources() = appResources
}