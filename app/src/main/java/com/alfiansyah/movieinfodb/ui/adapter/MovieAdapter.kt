package com.alfiansyah.movieinfodb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alfiansyah.movieinfodb.R
import com.alfiansyah.movieinfodb.data.model.Movie
import com.alfiansyah.movieinfodb.databinding.ItemMovieBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MovieAdapter @Inject constructor(): PagingAdapter<Movie>(){
    private lateinit var onItemClickCallback: OnItemMovieClickCallback
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Movie) {
        if (holder is MovieViewHolder){
            holder.bind(item)
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(item) }
        }
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemMovieClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolderData(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val itemMovieBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemMovieBinding)
    }
    class MovieViewHolder (private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.run {
                itemTitle.text = movie.title
                itemRating.text = (movie.vote.average).toFloat().toString()
                Picasso.get().load(movie.movieImage?.posterPath)
                    .into(itemPoster, object : Callback {
                        override fun onSuccess() {
                            itemPoster.scaleType = ImageView.ScaleType.FIT_CENTER
                        }

                        override fun onError(e: Exception?) {
                            itemPoster.run {
                                scaleType = ImageView.ScaleType.CENTER_INSIDE
                                setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_broken_image_black))
                            }
                        }
                    })
            }
        }
    }
}