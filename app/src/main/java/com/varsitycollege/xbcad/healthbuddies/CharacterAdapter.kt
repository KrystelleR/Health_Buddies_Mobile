package com.varsitycollege.xbcad.healthbuddies

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class CharacterAdapter(
    private val items: ArrayList<StoreItem>,
    private val onItemClick: (StoreItem) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    private var selectedItem: StoreItem? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)

        init {
            itemView.setOnClickListener {
                selectedItem = items[adapterPosition]
                onItemClick(selectedItem!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        // Use Glide to load the image from the URL with placeholder and error images
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .placeholder(R.drawable.bannerbg)
            .error(R.drawable.bg)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide", "Error loading image", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // This method needs to be implemented even if it's empty
                    return false
                }
            })
            .into(holder.itemImage)

        holder.itemPrice.text = currentItem.points.toString()
    }

    fun getSelectedItem(): StoreItem? {
        return selectedItem
    }

    fun setItems(newItems: List<StoreItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size
}

