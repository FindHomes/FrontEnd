package com.example.findhomes.data.model

data class City(
    val name : String,
    val counties : List<County>
)

data class County(
    val name : String
)
