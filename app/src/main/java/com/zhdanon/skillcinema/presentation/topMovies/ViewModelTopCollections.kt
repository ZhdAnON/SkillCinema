package com.zhdanon.skillcinema.presentation.topMovies

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.zhdanon.skillcinema.core.AppResources
import com.zhdanon.skillcinema.core.BaseViewModel
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.core.CategoriesMovies
import com.zhdanon.skillcinema.domain.usecasesAPI.GetPremierCollectionsUseCase
import com.zhdanon.skillcinema.domain.usecasesAPI.GetTopCollectionsUseCase
import com.zhdanon.skillcinema.domain.models.Movie
import com.zhdanon.skillcinema.domain.usecasesAPI.GetMoviesByFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Month
import java.util.Calendar
import javax.inject.Inject
import kotlin.enums.EnumEntries

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ViewModelTopCollections @Inject constructor(
    private val topUseCase: GetTopCollectionsUseCase,
    private val premierUseCase: GetPremierCollectionsUseCase,
    private val moviesByFilterUseCase: GetMoviesByFilterUseCase,
    private val appResources: AppResources
) : BaseViewModel() {

    private var _topMovies = MutableStateFlow<List<HomeList>>(emptyList())
    val topMovies = _topMovies.asStateFlow()

    init {
        getTopCollection()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTopCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loadState.value = StateLoading.Loading
                val topCollection: EnumEntries<CategoriesMovies> = CategoriesMovies.entries
                val tempList: MutableList<HomeList> = mutableListOf()
                topCollection.forEach { category ->
                    when (category.name) {
                        CategoriesMovies.TV_SERIES.name -> {
                            tempList.add(
                                HomeList(
                                    categoryType = category.name,
                                    categoryLabel = appResources.getCategoryTitle(category.name),
                                    filmList = moviesByFilterUseCase.execute(
                                        type = CategoriesMovies.TV_SERIES.name
                                    )
                                )
                            )
                        }
                        CategoriesMovies.PREMIERES.name -> {
                            val calendar = Calendar.getInstance()
                            tempList.add(
                                HomeList(
                                    categoryType = category.name,
                                    categoryLabel = appResources.getCategoryTitle(category.name),
                                    filmList = premierUseCase.execute(
                                        year = calendar.get(Calendar.YEAR),
                                        month = (calendar.get(Calendar.MONTH) + 1).converterInMonth()
                                    )
                                )
                            )
                        }
                        else -> {
                            tempList.add(
                                HomeList(
                                    categoryType = category.name,
                                    categoryLabel = appResources.getCategoryTitle(category.name),
                                    filmList = topUseCase.execute(
                                        topType = category.name
                                    )
                                )
                            )
                        }
                    }
                }
                _topMovies.value = tempList
                _loadState.value = StateLoading.Success
            } catch (e: Exception) {
                _loadState.value = StateLoading.Error(e.message.toString())
            }
        }
    }

    companion object {
        data class HomeList(
            val categoryType: String,
            val categoryLabel: String,
            val filmList: List<Movie>
        )

        @OptIn(ExperimentalStdlibApi::class)
        @RequiresApi(Build.VERSION_CODES.O)
        fun Int.converterInMonth(): String {
            var textMonth = ""
            if (this <= 0 || this > 12)
                textMonth = Month.AUGUST.name
            else
                Month.entries.toTypedArray().forEach { month ->
                    if (this == month.value) textMonth = month.name
                }
            return textMonth
        }
    }
}