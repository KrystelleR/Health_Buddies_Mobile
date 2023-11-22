package com.varsitycollege.xbcad.healthbuddies

import java.io.Serializable

data class StoreItem(
    val imageUrl: String? = null,
    val name: String? = null,
    val points: Long = 0,
    val storeId: String
) : Serializable
