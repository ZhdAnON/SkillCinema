package com.zhdanon.skillcinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zhdanon.skillcinema.data.api.CinemaApi
import com.zhdanon.skillcinema.data.api.model.mapToDomain
import com.zhdanon.skillcinema.domain.models.Image
import kotlinx.coroutines.flow.StateFlow

class GalleryPagingSource(
    private val api: CinemaApi,
    private val movieId: Int,
    private val category: StateFlow<String>
) : PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: FIRST_PAGE
        val pageSize = params.loadSize.coerceAtMost(LOAD_SIZE)
        return kotlin.runCatching {
            val response = api.getFilmImages(
                id = movieId,
                type = category.value,
                page = page
            ).mapToDomain()
            response
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.images,
                    prevKey = if (it.images.size < pageSize) null else page - 1,
                    nextKey = if (it.images.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val FIRST_PAGE = 1
        private const val LOAD_SIZE = 20
    }
}