package com.homes.findhomes.data.model

import java.io.Serializable

data class ManConRequest (
    val housingTypes: MutableList<String> = mutableListOf(),
    val prices: Prices = Prices(),
    val region: Region = Region()
) : Serializable

data class Prices(
    var mm: Int = 0,
    var js: Int = 0,
    var ws: WSInfo = WSInfo()
) : Serializable

data class WSInfo(
    var deposit: Int = 0,
    var rent: Int = 0
) : Serializable

data class Region(
    var district: String = "",
    var city: String = ""
) : Serializable