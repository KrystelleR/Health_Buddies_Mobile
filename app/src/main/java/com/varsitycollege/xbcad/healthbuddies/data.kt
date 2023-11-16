package com.varsitycollege.xbcad.healthbuddies

import java.io.Serializable

class data {
    data class UserDetails(
        var id : String="",
        val Username : String? ="",
        val Email: String ="",
        val Age: Int =0,
        val Height: String ="",
        val Weight: String ="",
        val Metric: Boolean=  true,
        val Imperial:Boolean= false,
        val ProfileImage: String="",
        val setDetails: Boolean =false,
        val Gender: String ="",
        val AboutMe: String = ""
    )  : Serializable

    data class UserGoals(
        val DailySteps: Int=0,
        val GoalWeight: String = "",
        val MoveMinutes: Int=0,
        val Sleep: Int =0,
        val DailyWaterAmount: Int =0,
        val DailyCalories: Int =0
    ): Serializable
}