package com.varsitycollege.xbcad.healthbuddies

// PurchasedItemsAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PurchasedItemsAdapter(
    private val items: List<String>, // Replace String with your actual data type
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<PurchasedItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_purchased_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: String) {
            // Load image into the ImageView using Glide
            Glide.with(itemView.context)
                .load(item)
                .into(imageView)

            // Set click listener
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
