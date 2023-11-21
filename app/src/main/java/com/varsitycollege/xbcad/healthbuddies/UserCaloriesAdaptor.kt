// UserCaloriesAdapter.kt
package com.varsitycollege.xbcad.healthbuddies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView


class UserCaloriesAdapter(
    private val context: Context,
    private var itemList: List<UserCaloriesItem>,
    private val resetButtonClickListener: ResetButtonClickListener
) : RecyclerView.Adapter<UserCaloriesAdapter.ViewHolder>() {

    // Interface for handling reset button clicks
    interface ResetButtonClickListener {
        fun onResetButtonClick(mealType: String)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMealType: TextView = itemView.findViewById(R.id.textMealType)
        val textMealName: TextView = itemView.findViewById(R.id.textMealName)
        val textCalories: TextView = itemView.findViewById(R.id.textCalories)
        val resetButton: AppCompatButton = itemView.findViewById(R.id.btn_reset_meal)
    }
    fun updateData(newList: List<UserCaloriesItem>) {
        itemList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_calories, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to views
        holder.textMealType.text = item.mealType
        holder.textMealName.text = item.mealName
        holder.textCalories.text = "${item.calories} kcal"

        // Set onClickListener for the reset button
        holder.resetButton.setOnClickListener {
            resetButtonClickListener.onResetButtonClick(item.mealType)
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }
}
