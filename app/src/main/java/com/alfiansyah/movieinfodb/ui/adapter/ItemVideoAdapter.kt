package com.alfiansyah.movieinfodb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfiansyah.movieinfodb.data.model.Video
import com.alfiansyah.movieinfodb.databinding.ItemVideoBinding
import javax.inject.Inject

class ItemVideoAdapter @Inject constructor():
    RecyclerView.Adapter<ItemVideoAdapter.VideoViewHolder>()  {
    private var listVideo = ArrayList<Video>()

    class VideoViewHolder (private val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(video: Video){
            binding.run {
                titleTv.text = video.name
                videoTypeTv.text = video.type.value
                sourceTv.text = video.site
                sizeTv.text = "${video.size.value} p"
            }

        }
    }

    private lateinit var onItemClickCallback: OnItemVideoClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemVideoClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemVideoBinding =
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(itemVideoBinding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = listVideo[position]
        holder.bind(video)
        holder.bind(listVideo[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(video) }

    }

    override fun getItemCount(): Int = listVideo.size
    fun setVideo(listVideo: List<Video>) {
        if (listVideo.isEmpty()) return
        this.listVideo.clear()
        this.listVideo.addAll(listVideo)
        notifyDataSetChanged()
    }

}