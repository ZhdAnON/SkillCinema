package com.zhdanon.skillcinema.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhdanon.skillcinema.data.api.CinemaApi
import com.zhdanon.skillcinema.data.api.model.mapToDomainMovie
import com.zhdanon.skillcinema.core.CategoriesMovies
import com.zhdanon.skillcinema.domain.models.Movie
import java.time.Month
import java.util.Calendar

class TopCollectionPagingSource(
    private val api: CinemaApi,
    private val category: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int = FIRST_PAGE

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        val pageSize = params.loadSize.coerceAtMost(LOAD_SIZE)
        return kotlin.runCatching {
            when (category) {
                CategoriesMovies.PREMIERES.name -> {
                    val calendar = Calendar.getInstance()
                    api.getPremier(
                        year = calendar.get(Calendar.YEAR),
                        month = (calendar.get(Calendar.MONTH) + 1).converterInMonth()
                    ).items.map { it.mapToDomainMovie() }
                }
                CategoriesMovies.TV_SERIES.name -> {
                    api.getFilmsByFilter(category).items.map { it.mapToDomainMovie() }
                }
                else -> {
                    api.getFilmsTop(
                        type = category,
                        page = page
                    ).items.map { it.mapToDomainMovie() }
                }
            }
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = if (it.size < pageSize) null else page - 1,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 1
        private const val LOAD_SIZE = 20

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