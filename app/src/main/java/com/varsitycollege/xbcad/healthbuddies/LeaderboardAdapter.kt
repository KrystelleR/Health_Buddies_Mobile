package com.varsitycollege.xbcad.healthbuddies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class LeaderboardAdapter(private val context: Context, private val entries: List<LeaderboardEntry>) : BaseAdapter() {

    override fun getCount(): Int = entries.size

    override fun getItem(position: Int): Any = entries[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false)

        val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
        val pointsTextView = view.findViewById<TextView>(R.id.pointsTextView)

        val entry = getItem(position) as LeaderboardEntry

        userNameTextView.text = entry.userName
        pointsTextView.text = entry.points.toString()

        return view
    }
}

data class LeaderboardEntry(val userName: String, val points: Int)


class StepsLeaderboardAdapter(private val context: Context, private val entries: List<StepsLeaderboardEntry>) : BaseAdapter() {

    override fun getCount(): Int = entries.size

    override fun getItem(position: Int): Any = entries[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.leaderboard_item, parent, false)

        val userNameTextView = view.findViewById<TextView>(R.id.userNameTextView)
        val pointsTextView = view.findViewById<TextView>(R.id.pointsTextView)

        val entry = getItem(position) as StepsLeaderboardEntry

        userNameTextView.text = entry.username
        pointsTextView.text = entry.highestSteps.toString()

        return view
    }
}


