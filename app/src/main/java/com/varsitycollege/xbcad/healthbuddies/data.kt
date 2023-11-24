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
        var userCurrency: Int = 0,
        val userCurrentCalories: Int =0,
        val dailySteps: Int=0,
        val goalWeight: String = "",
        val moveMinutes: Int=0,
        val sleep: Int =0,
        val dailyWaterAmount: Int =0,
        val dailyCalories: Int =0,
        val backgroundImageUrl: String = "",
        val profilePicURL: String = ""
    )  : Serializable


    data class UserSteps(
        val _00h00 : Int =0,
        val _01h00 : Int=0,
        val _02h00 : Int=0,
        val _03h00 : Int=0,
        val _04h00 : Int=0,
        val _05h00 : Int=0,
        val _06h00 : Int=0,
        val _07h00 : Int=0,
        val _08h00 : Int=0,
        val _09h00 : Int=0,
        val _10h00 : Int=0,
        val _11h00 : Int=0,
        val _12h00 : Int=0,
        val _13h00 : Int=0,
        val _14h00 : Int=0,
        val _15h00 : Int=0,
        val _16h00 : Int=0,
        val _17h00 : Int=0,
        val _18h00 : Int=0,
        val _19h00 : Int=0,
        val _20h00 : Int=0,
        val _21h00 : Int=0,
        val _22h00 : Int=0,
        val _23h00 : Int=0
    ): Serializable

    data class UserMinutes(
        var minutes: Int =0
    ): Serializable

    data class Quote(
        val _id: String,
        val tags: List<String>,
        val content: String,
        val author: String,
        val authorSlug: String,
        val length: Int,
        val dateAdded: String,
        val dateModified: String
    ): Serializable
    //for UserCalories

}

data class UserCaloriesItem(
    val mealType: String,
    val mealName: String,
    val calories: Long
): Serializable


