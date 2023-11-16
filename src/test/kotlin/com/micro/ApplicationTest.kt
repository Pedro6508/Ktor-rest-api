package com.micro

import com.micro.models.User
import com.micro.models.usersFakeDB
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.*

class UserRouteTest {
    @Test
    fun getById() = testApplication {
        val randomUser = usersFakeDB.random()
        val expectedBody = Json.encodeToJsonElement(
            User.serializer(), randomUser
        ).toString()
        val response = client.get("/user/${randomUser.id}")

        assertEquals(
            HttpStatusCode.OK, response.status
        )

        assertEquals(
            expectedBody, response.bodyAsText()
        )
    }

    @Test
    fun getAll() = testApplication {
        val expected = usersFakeDB.associateBy { user -> user.id }
        val response = client.get("/user")
        val userMap = Json.decodeFromString(
            ListSerializer(User.serializer()), response.bodyAsText()
        ).associateBy { user -> user.id }

        assertEquals(
            HttpStatusCode.OK, response.status
        )
        
        expected.forEach {
            (id, user) -> assertEquals(userMap[id], user)
        }
    }

    @Test
    fun post() = testApplication {
        val user = User(
            "Invalid Id",
            "Paulo",
            "paulo@gmail.com"
        )
        val response = client.post("/user") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToJsonElement(User.serializer(), user).toString())
        }
        val expected = usersFakeDB.last()

        assertEquals(
            HttpStatusCode.Created, response.status
        )

        assertNotEquals(
            user.id, expected.id
        )

        assertEquals(
            user.copy(id = response.bodyAsText()), expected
        )
    }

    @Test
    fun delete() = testApplication {
        val randomUser = usersFakeDB.random()
        val response = client.delete("/user/${randomUser.id}")

        assertEquals(
            "User removed correctly", response.bodyAsText()
        )

        assertEquals(
            HttpStatusCode.Accepted, response.status
        )

        assertFalse{
            usersFakeDB.contains(randomUser)
        }
    }
}
