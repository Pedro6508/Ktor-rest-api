package com.micro

import com.micro.models.User
import com.micro.models.fakeDB
import com.micro.plugins.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.math.exp
import kotlin.test.*

class UserRouteTest {
    @Test
    fun getById() = testApplication {
        val randomUser = fakeDB.random()
        val expected = Json.encodeToJsonElement(
            User.serializer(), randomUser
        ).toString()
        val response = client.get("/user/${randomUser.id}")

        assertEquals(
            expected, response.bodyAsText()
        )
    }

    @Test
    fun getAll() = testApplication {
        val expected = fakeDB.associateBy { user -> user.id }
        val response = client.get("/user")
        val userMap = Json.decodeFromString<List<User>>(
            ListSerializer(User.serializer()), response.bodyAsText()
        ).associateBy { user -> user.id }

        expected.forEach {
            (id, user) -> assertEquals(userMap[id], user)
        }
    }
}
