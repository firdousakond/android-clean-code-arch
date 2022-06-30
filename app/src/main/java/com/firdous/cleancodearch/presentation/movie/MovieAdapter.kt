package com.firdous.cleancodearch.presentation.movie

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firdous.cleancodearch.R
import com.firdous.cleancodearch.databinding.ItemMovieBinding
import com.firdous.cleancodearch.domain.model.Movie

class MovieAdapter(private val movieClick: (Movie) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var movieList: MutableList<Movie?> = ArrayList()

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie?) {
            binding.movie = movie
            with(binding.root) {
                setOnClickListener { movieClick.invoke(movie!!) }
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }

    }
    fun addData(data: List<Movie>) {
        data.forEach {
            if(!movieList.contains(it)){
                movieList.add(it)
                notifyItemInserted(movieList.size - 1)
            }
        }
    }

    fun addLoadingView() {
        Handler(Looper.getMainLooper()).post {
            movieList.add(null)
            notifyItemInserted(movieList.size - 1)
        }
    }

    fun removeLoadingView() {
        if (movieList.size != 0) {
            movieList.removeAt(movieList.size - 1)
            notifyItemRemoved(movieList.size)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            holder.bind(movieList[position])
        }
    }

    override fun getItemCount(): Int = movieList.size

    override fun getItemViewType(position: Int): Int {
        return if (movieList[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}