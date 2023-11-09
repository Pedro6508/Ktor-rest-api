package com.micro.routes

import com.micro.models.User
import com.micro.models.fakeDB
import com.micro.models.makeId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    route("/user") {
        get {
            if (fakeDB.isEmpty()) {
                call.respondText("No users found", status = HttpStatusCode.OK)
            } else {
                call.respond(fakeDB)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing userId",
                status = HttpStatusCode.BadRequest
            )
            val user =
                fakeDB.find { u -> u.id == id } ?: return@get call.respondText(
                    "No user with $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(user)
        }
        post {
            val userId = makeId()
            val user = call.receive<User>().copy(id = userId)
            fakeDB.add(user)
            call.respondText(
                "User stored correctly, id = $userId",
                status = HttpStatusCode.Created
            )
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (fakeDB.removeIf { u -> u.id == id }) {
                call.respondText(
                    "User removed correctly",
                    status = HttpStatusCode.Accepted
                )
            } else {
                call.respondText(
                    "Not Found",
                    status = HttpStatusCode.NotFound
                )
            }
        }
    }
}