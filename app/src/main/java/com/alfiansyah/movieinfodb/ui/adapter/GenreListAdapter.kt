package com.alfiansyah.movieinfodb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfiansyah.movieinfodb.data.model.Genre
import com.alfiansyah.movieinfodb.databinding.ItemGenreBinding
import java.util.ArrayList
import javax.inject.Inject


class GenreListAdapter @Inject constructor():
    RecyclerView.Adapter<GenreListAdapter.GenreViewHolder>() {
    //    private var oldListMovie = emptyList<Genre>()
    private lateinit var onItemClickCallback: OnItemGenreClickCallback
    private var listGenre = ArrayList<Genre>()

    class GenreViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre?) {
            binding.genreTitle.text = genre?.name
        }
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemGenreClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val itemGenreBinding =
            ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(itemGenreBinding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = listGenre[position]
        holder.bind(genre)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(genre) }
    }

    override fun getItemCount(): Int = listGenre.size

    fun setGenre(movies: List<Genre>) {
        if (movies.isEmpty()) return
        this.listGenre.clear()
        this.listGenre.addAll(movies)
    }
}