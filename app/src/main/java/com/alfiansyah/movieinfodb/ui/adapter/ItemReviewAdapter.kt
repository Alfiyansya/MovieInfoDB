package com.alfiansyah.movieinfodb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfiansyah.movieinfodb.data.model.Review
import com.alfiansyah.movieinfodb.databinding.ItemReviewBinding
import javax.inject.Inject

class ItemReviewAdapter @Inject constructor(): PagingAdapter<Review>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Review) {
        if (holder is ReviewViewHolder){
            holder.bind(item)
        }
    }

    override fun onCreateViewHolderData(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val itemReviewBinding =
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(itemReviewBinding)
    }
    class ReviewViewHolder (private val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.run {
                reviewerTv.text = review.author
                contentTv.text = review.content
            }
        }
    }
}