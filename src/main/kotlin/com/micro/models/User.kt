package com.micro.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String
)

val usersFakeDB = mutableListOf<User>(
    User(
        id = makeId(),
        name = "Pedro",
        email = "pedro@gmail.com"
    ),
    User(
        id = makeId(),
        name = "Levi",
        email = "levi@gmail.com"
    )
)
private var idSeed = 0

fun makeId(): String {
    idSeed += 1
    return idSeed.toString()
}