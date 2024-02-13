package com.zhdanon.skillcinema.presentation.detailMovie

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.core.BaseFragment
import com.zhdanon.skillcinema.core.StateLoading
import com.zhdanon.skillcinema.core.extensions.loadImage
import com.zhdanon.skillcinema.databinding.FragmentDetailMovieBinding
import com.zhdanon.skillcinema.domain.CategoriesMovies
import com.zhdanon.skillcinema.domain.models.MovieDetail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDetailMovie : BaseFragment<FragmentDetailMovieBinding>() {

    private val viewModel: ViewModelDetailMovie by viewModels()
    override fun initBinding(inflater: LayoutInflater) =
        FragmentDetailMovieBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FragmentDetailMovieArgs by navArgs()
        viewModel.getFilmById(args.movieId)

        stateLoadingListener()                                  // Setup load listener

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filmDetailInfo.collect { movie ->
                    if (movie != null) {
                        setFilmDetails(movie)                   // Setup poster and movie describe
                        setButtonsOnPoster(movie)               // Setup buttons for DB-collections
                        setMoviePersons(movie)                  // Setup movie actors/makers list
                        setMovieGallery(movie)                  // Setup movie gallery
                        setSimilar(movie)                       // Setup similar movie list
                    }
                }
            }
        }


        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun stateLoadingListener() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadState.collect { state ->
                    when (state) {
                        is StateLoading.Loading -> {
                            binding.apply {
                                movieDetail.progress = 1f

                                incProgress.loadingProgress.isVisible = true
                                incProgress.loadingBanner.isVisible = true
                                incProgress.loadingRefreshBtn.isVisible = false

                                movieName.isVisible = false
                                movieMainGroup.isVisible = false
                                movieDescriptionGroup.isVisible = false
                                myScroll.isVisible = false
                            }
                        }

                        is StateLoading.Success -> {
                            binding.apply {
                                movieDetail.progress = 0f

                                incProgress.loadingBanner.isVisible = false
                                incProgress.loadingRefreshBtn.isVisible = false
                                incProgress.loadingProgress.isVisible = false

                                movieName.isVisible = true
                                movieMainGroup.isVisible = true
                                movieDescriptionGroup.isVisible = true
                                myScroll.isVisible = true
                            }
                        }

                        else -> {
                            binding.apply {
                                movieDetail.progress = 1f

                                incProgress.loadingBanner.isVisible = true
                                incProgress.loadingRefreshBtn.isVisible = true
                                incProgress.loadingRefreshBtn.setOnClickListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Повторим загрузку",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                incProgress.loadingProgress.isVisible = false

                                movieName.isVisible = false
                                movieMainGroup.isVisible = false
                                movieDescriptionGroup.isVisible = false
                                myScroll.isVisible = false
                            }
                        }
                    }
                }
            }
        }
    }

    // Film details
    private fun setFilmDetails(movie: MovieDetail) {
        binding.apply {
            movieName.text = movie.name
            moviePoster.loadImage(movie.poster)
            if (movie.shortDescription != null) {
                movieDescriptionShort.text = movie.shortDescription
            } else {
                movieDescriptionShort.isVisible = false
            }
            movieDescriptionFull.text = movie.description
            movieRatingNameTv.text = getRatingName(movie)
            movieYearGenresTv.text = getYearGenres(movie, requireContext())
            movieCountryLengthAgeLimitTv.text = getStrCountriesLengthAge(movie)

        }
        if (movie.type == CategoriesMovies.TV_SERIES.name) {
            binding.seasonsGroup.isVisible = true
            getSeriesSeasons(movie)
        } else {
            binding.seasonsGroup.isVisible = false
        }
    }

    // Seasons details
    private fun getSeriesSeasons(movie: MovieDetail) {}

    // Persons list
    private fun setMoviePersons(movie: MovieDetail) {}

    // Film gallery
    private fun setMovieGallery(movie: MovieDetail) {}

    // Similar films
    private fun setSimilar(movie: MovieDetail) {}

    // ClickListeners
    private fun onClickShowAllSeasons(movieId: Int, movieName: String) {}

    private fun onClickShowAllPersons(movieId: Int, professionKey: String) {}

    private fun onClickItemPerson(personId: Int) {}

    private fun onClickShowAllGallery(movieId: Int) {}

    private fun onClickShowAllSimilar(movieId: Int) {}

    private fun onClickSimilarItem(movieId: Int) = viewModel.getFilmById(movieId)

    private fun setButtonsOnPoster(movie: MovieDetail) {
        val context = requireContext()
        binding.apply {
            btnToFavorite.setOnClickListener {
                showMyToast("Добавлено в избранное", context)
            }
            btnToBookmark.setOnClickListener {
                showMyToast("Добавлено в закладки", context)
            }
            btnIsViewed.setOnClickListener {
                showMyToast("Фильм просмотрен", context)
            }
            btnShare.setOnClickListener {
                showMyToast("Поделиться фильмом", context)
            }
            btnShowMore.setOnClickListener {
                showMyToast("Другие варианты действия", context)
            }
        }

    }

    companion object {
        private fun getRatingName(movie: MovieDetail): String {
            val result = mutableListOf<String>()

            val tempRating = movie.rating
            val rating = if (tempRating != null) {
                if (tempRating.contains("%")) {
                    (tempRating.substringBefore(".").removeSuffix(".").toInt() / 10).toString()
                } else tempRating
            } else null

            if (rating != null) result.add(rating)
            val name = movie.name
            if (name.isNotEmpty()) result.add(name)
            return result.joinToString(", ")
        }

        private fun getYearGenres(movie: MovieDetail, context: Context): String {
            val result = mutableListOf<String>()

            if (movie.type == CategoriesMovies.TV_SERIES.name) {
                val tempStr = mutableListOf<String>()
                if (movie.startYear != null && movie.endYear != null) {
                    tempStr.add(movie.startYear.toString())
                    tempStr.add(movie.endYear.toString())
                } else {
                    if (movie.startYear != null) tempStr.add(movie.startYear.toString())
                    else context.getString(R.string.placeholder_series_start_year)
                    if (movie.endYear != null) tempStr.add(movie.endYear.toString())
                    else context.getString(R.string.placeholder_series_end_year)
                }
                result.add(tempStr.joinToString("-"))
            } else {
                if (movie.year != null) result.add(movie.year.toString())
            }

            if (movie.genres.size > 1) {
                movie.genres.take(2).map { result.add(it.genre) }
            } else if (movie.genres.size == 1) {
                result.add(movie.genres.first().genre)
            } else result.add("")

            return result.joinToString(", ")
        }

        private fun getStrCountriesLengthAge(movie: MovieDetail): String {
            val result = mutableListOf<String?>()

            val countries = movie.countries
            val resultCountries = if (countries.isNotEmpty()) {
                if (countries.size == 1) {
                    countries.first().country
                } else {
                    countries.joinToString(", ") { it.country }
                }
            } else {
                null
            }
            result.add(resultCountries)

            val length = movie.length
            val resultLength = if (length != null) {
                val hours = length.div(60)
                val minutes = length.rem(60)
                "$hours ч $minutes мин"
            } else null
            if (resultLength != null) {
                result.add(resultLength)
            }

            val resultAgeLimit = movie.ageLimit?.removePrefix("age")
            result.add("$resultAgeLimit+")

            return result.joinToString(", ")
        }

        private fun showMyToast(message: String, context: Context) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}