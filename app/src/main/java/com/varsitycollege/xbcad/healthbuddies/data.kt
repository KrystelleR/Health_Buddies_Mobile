package com.varsitycollege.xbcad.healthbuddies

import java.io.Serializable
import java.util.Currency

class data {
    data class UserDetails(
        var uid : String="",
        val username : String? ="",
        val email: String ="",
        val age: Int =0,
        val height: String ="",
        val weight: String ="",
        val metric: Boolean=  true,
        val imperial:Boolean= false,
        val profileImage: Int=0,
        val setDetails: Boolean =false,
        val gender: String ="",
        val aboutMe: String = "",
        val userCurrency: Int = 0,
        val userCurrentCalories: Int =0,
        val dailySteps: Int=0,
        val goalWeight: String = "",
        val moveMinutes: Int=0,
        val sleep: Int =0,
        val dailyWaterAmount: Int =0,
        val dailyCalories: Int =0
    )  : Serializable


    data class Quote(
        val _id: String,
        val tags: List<String>,
        val content: String,
        val author: String,
        val authorSlug: String,
        val length: Int,
        val dateAdded: String,
        val dateModified: String
    )


}