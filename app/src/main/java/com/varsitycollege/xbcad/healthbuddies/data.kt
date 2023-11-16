package com.varsitycollege.xbcad.healthbuddies

import java.io.Serializable

class data {
    data class UserDetails(
        var id : String="",
        val username : String? ="",
        val email: String ="",
        val age: Int =0,
        val height: String ="",
        val weight: String ="",
        val metric: Boolean=  true,
        val imperial:Boolean= false,
        val profileImage: String="",
        val setDetails: Boolean =false,
        val gender: String ="",
        val aboutMe: String = ""
    )  : Serializable

    data class UserGoals(
        val dailySteps: Int=0,
        val goalWeight: String = "",
        val moveMinutes: Int=0,
        val sleep: Int =0,
        val dailyWaterAmount: Int =0,
        val dailyCalories: Int =0
    ): Serializable
}