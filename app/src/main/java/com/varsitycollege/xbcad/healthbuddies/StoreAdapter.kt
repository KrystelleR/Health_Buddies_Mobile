package com.varsitycollege.xbcad.healthbuddies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StoreAdapter(private val items: List<StoreItem>, private val onItemClick: (StoreItem) -> Unit) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)

        init {
            itemView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        // Use Glide to load the image from the URL
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.itemImage)
        holder.itemPrice.text = currentItem.price
    }

    override fun getItemCount() = items.size
}
