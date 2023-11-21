package com.varsitycollege.xbcad.healthbuddies

import java.io.Serializable

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


    data class UserSteps(
        val uid: String,
        val _00h00 : Int,
        val _01h00 : Int,
        val _02h00 : Int,
        val _03h00 : Int,
        val _04h00 : Int,
        val _05h00 : Int,
        val _06h00 : Int,
        val _07h00 : Int,
        val _08h00 : Int,
        val _09h00 : Int,
        val _10h00 : Int,
        val _11h00 : Int,
        val _12h00 : Int,
        val _13h00 : Int,
        val _14h00 : Int,
        val _15h00 : Int,
        val _16h00 : Int,
        val _17h00 : Int,
        val _18h00 : Int,
        val _19h00 : Int,
        val _20h00 : Int,
        val _21h00 : Int,
        val _22h00 : Int,
        val _23h00 : Int
    )

    data class UserMoveMinutes(
        val uid: String,
        val monday: Int,
        val tuesday: Int,
        val wednesday: Int,
        val thursday: Int,
        val friday: Int,
        val saturday: Int,
        val sunday: Int,
    )

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
    //for UserCalories

}

data class UserCaloriesItem(
    val mealType: String,
    val mealName: String,
    val calories: Long
)


