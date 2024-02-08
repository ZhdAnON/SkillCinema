package com.zhdanon.skillcinema.presentation.adapters.viewHolders

import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.zhdanon.skillcinema.databinding.ItemMovieBinding
import com.zhdanon.skillcinema.domain.extensions.loadImage
import com.zhdanon.skillcinema.domain.models.Genre
import com.zhdanon.skillcinema.presentation.adapters.MyAdapterTypes

class ItemMovieViewHolder(private val binding: ItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindNextShow(clickNextButton: () -> Unit) {
        binding.apply {
            showAll.isInvisible = false
            itemFilm.isInvisible = true
        }

        binding.btnArrowShowAll.setOnClickListener { clickNextButton() }
    }

    fun bindItem(item: MyAdapterTypes.ItemMovie, clickFilms: (filmId: Int) -> Unit) {
        binding.apply {
            showAll.isInvisible = true
            itemFilm.isInvisible = false
            itemFilmName.text = item.movie.name
            itemFilmGenre.text = createGenreName(item.movie.genres.take(2))
            itemFilmPoster.loadImage(item.movie.poster)
            itemFilmRating.text = item.movie.rating
            itemFilmRating.isInvisible = item.movie.rating == null
        }
        binding.itemFilm.setOnClickListener {
            clickFilms(item.movie.movieId)
        }
    }

    private fun createGenreName(genres: List<Genre>): String {
        var genreName = ""
        genres.forEachIndexed { index, genre ->
            genreName += if (index == genres.lastIndex) genre.genre
            else "${genre.genre}, "
        }
        return genreName
    }
}