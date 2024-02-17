package com.zhdanon.skillcinema.core

import android.content.Context
import com.zhdanon.skillcinema.R
import javax.inject.Inject

class AppResources @Inject constructor(private val context: Context) {
    fun getCategoryTitle(type: String): String {
        return context.getString(
            when(type) {
                CategoriesMovies.TOP_250_MOVIES.name -> R.string.TOP_250_MOVIES
                CategoriesMovies.TOP_POPULAR_MOVIES.name -> R.string.TOP_POPULAR_MOVIES
                CategoriesMovies.PREMIERES.name -> R.string.PREMIERES
                CategoriesMovies.TOP_POPULAR_ALL.name -> R.string.TOP_POPULAR_ALL
                CategoriesMovies.TOP_250_TV_SHOWS.name -> R.string.TOP_250_TV_SHOWS
                CategoriesMovies.VAMPIRE_THEME.name -> R.string.VAMPIRE_THEME
                CategoriesMovies.COMICS_THEME.name -> R.string.COMICS_THEME
                CategoriesMovies.CLOSES_RELEASES.name -> R.string.CLOSES_RELEASES
                CategoriesMovies.FAMILY.name -> R.string.FAMILY
                CategoriesMovies.OSKAR_WINNERS_2021.name -> R.string.OSKAR_WINNERS_2021
                CategoriesMovies.LOVE_THEME.name -> R.string.LOVE_THEME
                CategoriesMovies.ZOMBIE_THEME.name -> R.string.ZOMBIE_THEME
                CategoriesMovies.CATASTROPHE_THEME.name -> R.string.CATASTROPHE_THEME
                CategoriesMovies.KIDS_ANIMATION_THEME.name -> R.string.KIDS_ANIMATION_THEME
                CategoriesMovies.TV_SERIES.name -> R.string.TV_SERIES
                else -> -1
            }
        )
    }
}