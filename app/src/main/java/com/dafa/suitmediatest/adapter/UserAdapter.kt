package com.dafa.suitmediatest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dafa.suitmediatest.databinding.ItemUserBinding
import com.dafa.suitmediatest.response.DataItem
import com.dafa.suitmediatest.screen.SecondScreenActivity

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val data: MutableList<DataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = data[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    fun addData(newData: List<DataItem>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItem)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }
        }

        fun bind(user: DataItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar)
                    .circleCrop()
                    .into(ivUser)
            }
            binding.tvUserName.text = user.firstName + " " + user.lastName
            binding.tvUserEmail.text = user.email
        }

    }

}