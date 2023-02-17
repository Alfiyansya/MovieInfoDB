package com.alfiansyah.movieinfodb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfiansyah.movieinfodb.databinding.ItemLoadMoreBinding
import com.alfiansyah.movieinfodb.databinding.ItemLoadMoreErrorBinding
import com.alfiansyah.movieinfodb.databinding.ItemNoMoreDataBinding
import com.alfiansyah.movieinfodb.utils.MapperList
import com.alfiansyah.movieinfodb.utils.PageViewType
import com.alfiansyah.movieinfodb.utils.TypeWrapper

abstract class PagingAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var typeWrappers: ArrayList<TypeWrapper<T>> = arrayListOf()
    var onRetryLoadMoreListener: OnRetryLoadMoreListener? = null

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(PageViewType.toPageViewType(viewType)) {
            PageViewType.DATA -> {
                onCreateViewHolderData(inflater, parent)
            }
            PageViewType.LOADING -> {
                val itemLoadMoreBinding =
                    ItemLoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(itemLoadMoreBinding)
            }
            PageViewType.NO_MORE_DATA -> {
                val itemNoMoreDataBinding =
                    ItemNoMoreDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NoMoreDataViewHolder(itemNoMoreDataBinding)
            }
            PageViewType.ERROR_OCCURED -> {
                val itemLoadMoreErrorBinding =
                    ItemLoadMoreErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadMoreErrorViewHolder(itemLoadMoreErrorBinding)
            }
        }
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val typeWrapper = typeWrappers[position]
        if (typeWrapper is TypeWrapper.DataWrapper<T>) {
            onBindViewHolder(holder, typeWrapper.item)
        } else if (typeWrapper is TypeWrapper.ErrorWrapper<T> && holder is LoadMoreErrorViewHolder) {
            val onRetryLoadMoreListener= onRetryLoadMoreListener
            if (onRetryLoadMoreListener != null) {
                holder.bind(onRetryLoadMoreListener)
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        val pageViewType = when (typeWrappers[position]) {
            is TypeWrapper.DataWrapper -> PageViewType.DATA
            is TypeWrapper.LoadingWrapper -> PageViewType.LOADING
            is TypeWrapper.NoMoreDataWrapper -> PageViewType.NO_MORE_DATA
            is TypeWrapper.ErrorWrapper -> PageViewType.ERROR_OCCURED
        }

        return pageViewType.value
    }

    final override fun getItemCount(): Int {
        return typeWrappers.size
    }

    fun showLoading() {
        typeWrappers.add(TypeWrapper.LoadingWrapper())
        notifyItemInserted(typeWrappers.size -1)
    }

    fun closeLoading() {
        var positionToRemove: Int = -1

        for(i in 0 until typeWrappers.size) {
            if (typeWrappers[i] is TypeWrapper.LoadingWrapper) {
                positionToRemove = i
                break
            }
        }

        typeWrappers.remove(typeWrappers[positionToRemove])
        notifyItemRemoved(positionToRemove)
    }

    fun showNoMoreData() {
        typeWrappers.add(TypeWrapper.NoMoreDataWrapper())
        notifyItemInserted(typeWrappers.size -1)
    }

    fun closeNoMoreData() {
        val lastPosition = typeWrappers.size -1
        val typeWrapper = typeWrappers[lastPosition]

        if (typeWrapper is TypeWrapper.NoMoreDataWrapper) {
            typeWrappers.remove(typeWrapper)
            notifyItemRemoved(typeWrappers.size)
        }
    }

    fun showError() {
        typeWrappers.add(TypeWrapper.ErrorWrapper())
        notifyItemInserted(typeWrappers.size -1)
    }

    fun closeError() {
        var positionToRemove: Int = -1

        for(i in 0 until typeWrappers.size) {
            if (typeWrappers[i] is TypeWrapper.ErrorWrapper) {
                positionToRemove = i
                break
            }
        }

        typeWrappers.remove(typeWrappers[positionToRemove])
        notifyItemRemoved(positionToRemove)
    }

    fun clear() {
        typeWrappers.clear()
        notifyDataSetChanged()
    }

    fun changeData(newListData: List<T>) {
        notifyDataSetChanged()
        val newItem = MapperList.map(newListData) {
            TypeWrapper.DataWrapper(it)
        }

        typeWrappers = arrayListOf<TypeWrapper<T>>().apply {
            addAll(newItem)
        }
    }


    interface OnRetryLoadMoreListener {
        fun onRetryLoadMore()
    }

    protected abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T)

    protected abstract fun onCreateViewHolderData(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder
    class LoadingViewHolder(binding: ItemLoadMoreBinding) : RecyclerView.ViewHolder(binding.root)
    class LoadMoreErrorViewHolder(private val binding: ItemLoadMoreErrorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(onRetryLoadMoreListener: OnRetryLoadMoreListener) {
            binding.retryBtn.setOnClickListener {
                onRetryLoadMoreListener.onRetryLoadMore()
            }
        }
    }
    class NoMoreDataViewHolder(binding: ItemNoMoreDataBinding): RecyclerView.ViewHolder(binding.root)
}