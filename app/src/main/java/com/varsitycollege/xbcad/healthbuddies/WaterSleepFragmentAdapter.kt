package com.varsitycollege.xbcad.healthbuddies
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WaterSleepTracker()
            1 -> WaterSleep_Graphs()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 2 // Number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Tracker" // Set the title for the first tab
            1 -> "My Progress" // Set the title for the second tab
            else -> null
        }
    }
}