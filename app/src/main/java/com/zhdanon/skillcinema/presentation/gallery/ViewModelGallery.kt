package com.zhdanon.skillcinema.presentation.gallery

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.zhdanon.skillcinema.core.AppResources
import com.zhdanon.skillcinema.core.BaseViewModel
import com.zhdanon.skillcinema.core.CategoriesImages
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.domain.models.Image
import com.zhdanon.skillcinema.domain.models.MovieGallery
import com.zhdanon.skillcinema.domain.usecasesAPI.GetImagesByMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelGallery @Inject constructor(
    private val getImages: GetImagesByMovieUseCase,
    private val appResources: AppResources
) : BaseViewModel() {
    lateinit var imagesPaging: Flow<PagingData<Image>>

    private val _galleryChipList = MutableStateFlow<Map<String, Int>>(emptyMap())

    val galleryChipList = _galleryChipList.asStateFlow()

    private var _category = MutableStateFlow("")
    val category = _category.asStateFlow()

    fun setChipGroup(movieId: Int) {
        _galleryChipList.value = emptyMap()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadState.value = StateLoading.Loading
                val tempChipsMap = mutableMapOf<String, Int>()
                CategoriesImages.entries.forEach { category ->
                    val response: MovieGallery = getImages.execute(movieId, category.name)
                    if (response.images.isNotEmpty()) {
                        tempChipsMap[category.name] = response.totalImages
                        if (_category.value == "") _category.value = category.name
                    }
                }
                _galleryChipList.value = tempChipsMap
                _loadState.value = StateLoading.Success
            } catch (e: Exception) {
                _loadState.value = StateLoading.Error(e.message.toString())
            }
        }
    }

    fun setGallery(movieId: Int) {
        imagesPaging = getImages.executePaging(movieId, _category)
    }

    fun updateGallery(category: String) {
        _category.value = category
    }

    fun getAppResources() = appResources
}