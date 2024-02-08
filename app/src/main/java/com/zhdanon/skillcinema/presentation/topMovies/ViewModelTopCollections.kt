package com.zhdanon.skillcinema.presentation.topMovies

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhdanon.skillcinema.domain.CategoriesMovies
import com.zhdanon.skillcinema.domain.GetPremierCollectionsUseCase
import com.zhdanon.skillcinema.domain.GetTopCollectionsUseCase
import com.zhdanon.skillcinema.domain.models.Movie
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
    private val premierUseCase: GetPremierCollectionsUseCase
) : ViewModel() {

    private var _topMovies = MutableStateFlow<List<HomeList>>(emptyList())
    val topMovies = _topMovies.asStateFlow()

    init {
        getTopCollection()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTopCollection() {
        viewModelScope.launch(Dispatchers.IO) {

            val topCollection: EnumEntries<CategoriesMovies> = CategoriesMovies.entries
            val tempList: MutableList<HomeList> = mutableListOf()
            topCollection.forEach { category ->
                if (category.name == CategoriesMovies.PREMIERES.name) {
                    val calendar = Calendar.getInstance()
                    tempList.add(
                        HomeList(
                            category = category,
                            filmList = premierUseCase.executePremierCollections(
                                year = calendar.get(Calendar.YEAR),
                                month = (calendar.get(Calendar.MONTH) + 1).converterInMonth()
                            )
                        )
                    )
                } else {
                    tempList.add(
                        HomeList(
                            category = category,
                            filmList = topUseCase.executeTopCollections(
                                topType = category.name
                            )
                        )
                    )
                }
            }
            _topMovies.value = tempList
        }
    }

    companion object {
        data class HomeList(
            val category: CategoriesMovies,
            val filmList: List<Movie>
        )

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