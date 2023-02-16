package com.alfiansyah.movieinfodb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfiansyah.movieinfodb.data.entity.Genre
import com.alfiansyah.movieinfodb.databinding.ItemGenreBinding
import java.util.ArrayList
import javax.inject.Inject


class GenreListAdapter @Inject constructor() :
    RecyclerView.Adapter<GenreListAdapter.GenreViewHolder>() {
    //    private var oldListMovie = emptyList<Genre>()
    private var listMovie = ArrayList<Genre>()

    class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre?) {
            with(binding) {
                genreTitle.text = genre?.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val itemGenreBinding =
            ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(itemGenreBinding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = listMovie[position]
        holder.bind(genre)
    }

    override fun getItemCount(): Int = listMovie.size

    fun setGenre(movies: List<Genre>) {
        if (movies.isEmpty()) return
        this.listMovie.clear()
        this.listMovie.addAll(movies)
    }
}