package com.zhdanon.skillcinema.app.presentation.detailMovie

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhdanon.skillcinema.R
import com.zhdanon.skillcinema.app.core.BaseFragment
import com.zhdanon.skillcinema.app.core.StateLoading
import com.zhdanon.skillcinema.app.core.loadImage
import com.zhdanon.skillcinema.app.core.CategoriesMovies
import com.zhdanon.skillcinema.app.presentation.adapters.MyAdapterTypes
import com.zhdanon.skillcinema.app.presentation.adapters.MyListAdapter
import com.zhdanon.skillcinema.databinding.FragmentDetailMovieBinding
import com.zhdanon.skillcinema.domain.models.MovieDetail
import com.zhdanon.skillcinema.domain.models.Staff
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDetailMovie : BaseFragment<FragmentDetailMovieBinding>() {

    override fun initBinding(inflater: LayoutInflater) =
        FragmentDetailMovieBinding.inflate(inflater)

    private val viewModel: ViewModelDetailMovie by viewModels()

    private lateinit var staffMakersAdapter: MyListAdapter
    private lateinit var staffActorsAdapter: MyListAdapter
    private lateinit var galleryAdapter: MyListAdapter
    private lateinit var similarAdapter: MyListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: FragmentDetailMovieArgs by navArgs()
        viewModel.getFilmById(args.movieId)

        binding.incProgress.loadingRefreshBtn.setOnClickListener { viewModel.getFilmById(args.movieId) }
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        stateLoadingListener()                                  // Setup load listener

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieDetailInfo.collect { movie ->
                    if (movie != null) {
                        setFilmDetails(movie)                   // Setup poster and movie describe
                        setButtonsOnPoster(movie)               // Setup buttons for DB-collections
                        setMovieStaffs(movie.movieId)           // Setup movie actors/makers list
                        setMovieGallery(movie.movieId)          // Setup movie gallery
                        setSimilar(movie.movieId)               // Setup similar movie list
                    }
                }
            }
        }
    }

    private fun stateLoadingListener() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadState.collect { state ->
                    when (state) {
                        is StateLoading.Loading -> {
                            setVisibilityViews(
                                motionProgress = 1f,
                                progressBar = true,
                                progressBanner = true,
                                progressBtn = false,
                                data = false
                            )
                        }

                        is StateLoading.Success -> {
                            setVisibilityViews(
                                motionProgress = 0f,
                                progressBar = false,
                                progressBanner = false,
                                progressBtn = false,
                                data = true
                            )
                        }

                        else -> {
                            setVisibilityViews(
                                motionProgress = 1f,
                                progressBar = false,
                                progressBanner = true,
                                progressBtn = true,
                                data = false
                            )
                        }
                    }
                }
            }
        }
    }

    // Movie details
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

    private fun onClickShowAllSeasons(movieId: Int, movieName: String) {}

    // Staffs list
    private fun setMovieStaffs(movieId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.staffs.collect {
                    val actors = mutableListOf<Staff>()
                    val makers = mutableListOf<Staff>()

                    it.forEach { staff ->
                        if (staff.professionKey == "ACTOR") {
                            actors.add(staff)
                        } else makers.add(staff)
                    }

                    setMovieMakers(makers, movieId)
                    setMovieActors(actors, movieId)
                }
            }
        }
    }

    private fun setMovieMakers(makers: List<Staff>, movieId: Int) {
        staffMakersAdapter = MyListAdapter(
            maxListSize = 20,
            clickEndButton = { },
            clickItem = ::onClickItemStaff
        )
        binding.apply {
            movieMakersList.layoutManager = GridLayoutManager(
                requireContext(),
                MAX_STAFFS_MAKERS_ROWS,
                GridLayoutManager.HORIZONTAL,
                false
            )
            movieMakersList.adapter = staffMakersAdapter
            movieMakersBtn.setOnClickListener {
                onClickShowAllStaffs(movieId = movieId, professionKey = "")
            }
            movieMakersCount.setOnClickListener {
                onClickShowAllStaffs(movieId = movieId, professionKey = "")
            }
        }
        staffMakersAdapter.submitList(
            if (makers.size < MAX_STAFFS_MAKERS_COLUMN * MAX_STAFFS_MAKERS_ROWS) {
                binding.movieMakersBtn.isVisible = false
                binding.movieMakersCount.isVisible = false
                makers.map { maker -> MyAdapterTypes.ItemMovieStaff(maker) }
            } else {
                binding.movieMakersBtn.isVisible = true
                binding.movieMakersCount.isVisible = true
                binding.movieMakersCount.text = makers.size.toString()
                val tempMakersList =
                    makers.take(MAX_STAFFS_MAKERS_COLUMN * MAX_STAFFS_MAKERS_ROWS)
                tempMakersList.map { maker -> MyAdapterTypes.ItemMovieStaff(maker) }
            }
        )
    }

    private fun setMovieActors(actors: List<Staff>, movieId: Int) {
        staffActorsAdapter = MyListAdapter(
            maxListSize = 20,
            clickEndButton = {},
            clickItem = ::onClickItemStaff
        )
        binding.apply {
            movieActorsList.layoutManager = GridLayoutManager(
                requireContext(),
                MAX_STAFFS_ACTORS_ROWS,
                GridLayoutManager.HORIZONTAL,
                false
            )
            movieActorsList.adapter = staffActorsAdapter
            movieActorsBtn.setOnClickListener {
                onClickShowAllStaffs(movieId = movieId, professionKey = "ACTOR")
            }
            movieActorsCount.setOnClickListener {
                onClickShowAllStaffs(movieId = movieId, professionKey = "ACTOR")
            }
        }
        staffActorsAdapter.submitList(
            if (actors.size < MAX_STAFFS_ACTORS_COLUMN * MAX_STAFFS_ACTORS_ROWS) {
                binding.movieActorsBtn.isVisible = false
                binding.movieActorsCount.isVisible = false
                actors.map { actor -> MyAdapterTypes.ItemMovieStaff(actor) }
            } else {
                binding.movieActorsBtn.isVisible = true
                binding.movieActorsCount.isVisible = true
                binding.movieActorsCount.text = actors.size.toString()
                val tempActorsList =
                    actors.take(MAX_STAFFS_ACTORS_COLUMN * MAX_STAFFS_ACTORS_ROWS)
                tempActorsList.map { actor -> MyAdapterTypes.ItemMovieStaff(actor) }
            }
        )
    }

    private fun onClickShowAllStaffs(movieId: Int, professionKey: String) {
        if (professionKey == "ACTOR") {
            showMyToast("Все, кто снимался в фильме $movieId", requireContext())
        }
        else showMyToast("Все, кто работал над фильмом $movieId", requireContext())

    }

    private fun onClickItemStaff(staffId: Int) {
        showMyToast("StaffId = $staffId", requireContext())
    }

    // Gallery
    private fun setMovieGallery(movieId: Int) {
        galleryAdapter = MyListAdapter(10, {}) { onClickShowAllGallery(movieId) }
        binding.movieGalleryList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.movieGalleryList.adapter = galleryAdapter

        binding.movieGalleryBtn.setOnClickListener { onClickShowAllGallery(movieId) }
        binding.movieGalleryCount.setOnClickListener { onClickShowAllGallery(movieId) }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.images.collect { gallery ->
                    if (gallery != null) {
                        when (gallery.totalImages) {
                            0 -> {
                                binding.movieGalleryGroup.isVisible = false
                            }

                            in 1..10 -> {
                                binding.movieGalleryCount.isVisible = false
                                binding.movieGalleryBtn.isVisible = false
                                galleryAdapter.submitList(gallery.images.map {
                                    MyAdapterTypes.ItemMovieDetailImage(
                                        it
                                    )
                                })
                            }

                            else -> {
                                binding.movieGalleryCount.text = gallery.totalImages.toString()
                                galleryAdapter.submitList(
                                    gallery.images.take(10)
                                        .map { MyAdapterTypes.ItemMovieDetailImage(it) })
                            }
                        }

                    }
                }
            }
        }
    }

    private fun onClickShowAllGallery(movieId: Int) {
        val action = FragmentDetailMovieDirections
            .actionFragmentDetailMovieToFragmentGallery(movieId)
        findNavController().navigate(action)
    }

    // Similar movies
    private fun setSimilar(movieId: Int) {
        similarAdapter =
            MyListAdapter(20, { onClickShowAllSimilar(movieId) }, ::onClickSimilarItem)
        binding.movieSimilarList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.movieSimilarList.adapter = similarAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.similar.collect {
                    if (it != null) {
                        binding.movieSimilarCount.text = it.total.toString()
                        similarAdapter.submitList(it.movies.map { movie ->
                            MyAdapterTypes.ItemMovie(movie)
                        })
                    }
                }
            }
        }

        binding.movieSimilarCount.setOnClickListener { onClickShowAllSimilar(movieId) }
        binding.movieSimilarBtn.setOnClickListener { onClickShowAllSimilar(movieId) }
    }

    private fun onClickShowAllSimilar(movieId: Int) {
        showMyToast("Все фильмы, похожие на $movieId", requireContext())
    }

    private fun onClickSimilarItem(movieId: Int) {
        viewModel.getFilmById(movieId)
    }

    // ClickListeners

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

    private fun setVisibilityViews(
        motionProgress: Float,
        progressBar: Boolean,
        progressBanner: Boolean,
        progressBtn: Boolean,
        data: Boolean
    ) {
        binding.apply {
            movieDetail.progress = motionProgress

            incProgress.loadingProgress.isVisible = progressBar
            incProgress.loadingBanner.isVisible = progressBanner
            incProgress.loadingRefreshBtn.isVisible = progressBtn

            movieName.isVisible = data
            movieMainGroup.isVisible = data
            movieDescriptionGroup.isVisible = data
            myScroll.isVisible = data
        }
    }

    companion object {
        private const val MAX_STAFFS_ACTORS_COLUMN = 5
        private const val MAX_STAFFS_ACTORS_ROWS = 4
        private const val MAX_STAFFS_MAKERS_COLUMN = 3
        private const val MAX_STAFFS_MAKERS_ROWS = 2

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