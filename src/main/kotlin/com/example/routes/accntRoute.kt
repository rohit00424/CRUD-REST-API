package com.example.routes

import AccountController
import com.example.model.GlobalResponse
import com.example.model.UserDataType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.lang.Exception

fun Route.userRouting() {
    val userDetails = AccountController().accDetails()
    route("/employee/") {
        get {
            if (userDetails.isEmpty()) {
                return@get call.respond(HttpStatusCode.OK, GlobalResponse(400, "No data to show.")) // return agad value/guard clause
            }
            call.respond(HttpStatusCode.OK, userDetails)
        }
        get ("{userID}") {
            val getUserByID = call.parameters["userID"]?: return@get call.respond(GlobalResponse(300,"Id not found."))
            val userData = userDetails.find { it.id == getUserByID.toInt() }?: return@get call.respond(GlobalResponse(300,"Missing ID"))
            call.respond(userData)
        }
        post {
            val accDetail = call.receive<UserDataType>()
            val (id, username, password, fname, lname, email) = accDetail
            val getID = userDetails.find { it.id == id }

            if (getID != null)
                return@post call.respond("Username already taken.") //guard clause

            val emailValidator = AccountController().isValidEmail(email)
            if(!emailValidator) return@post call.respondText("Invalid email format. Please try again", status = HttpStatusCode.BadRequest)

            val getAccountControllerReturnValue = AccountController().addUser(id, username, password, fname, lname, email)
            println(getAccountControllerReturnValue.toString())

            if (getAccountControllerReturnValue == 0) return@post call.respondText("Error adding account, please try again", status = HttpStatusCode.BadRequest)

            call.respondText("User has been inserted.", status = HttpStatusCode.Created)
        }
        put ("{userID}"){
            val accDetails = call.receive<UserDataType>()
            val(id,username,password,fname,lname,email) = accDetails
            val userID = call.parameters["userID"] ?: return@put call.respond(GlobalResponse(300,"Id not found."))
            val getID = userDetails.find { it.id == userID.toInt() }?: return@put call.respond(GlobalResponse(300,"Missing ID."))
            println(getID)

            val getAccountControllerReturnValue = AccountController().updateUserDetails(userID.toInt(), username, password, fname, lname, email)
            println(getAccountControllerReturnValue)
            if (getAccountControllerReturnValue == 0) return@put call.respondText("Error updating user details. Please try again", status = HttpStatusCode.BadRequest)

            return@put call.respondText("User's details has been updated.", status = HttpStatusCode.Created)
        }
        delete ("{userID}") {
            val userID = call.parameters["userID"] ?: return@delete call.respond(GlobalResponse(300,"Id not found."))
            val getID = userDetails.find { it.id == userID.toInt() }?: return@delete call.respond(GlobalResponse(300,"Missing ID."))
            println(getID)
            val getAccountControllerReturnValue = AccountController().deleteUser(userID.toInt())
            if (getAccountControllerReturnValue == 0) return@delete call.respondText("Error removing user. Please try again", status = HttpStatusCode.BadRequest)

            return@delete call.respondText("User has been removed.", status = HttpStatusCode.Created)
        }
    }
}


/*      val checkCharacters = AccountController().passwordCharacterCheck(password)
      val checkLength = AccountController().passwordLengthCheck(password)
       if(!checkLength) return@post call.respondText("Password must be at least 8 characters long. Please try again.", status = HttpStatusCode.BadRequest)
      if(!checkCharacters ) return@post call.respondText("Invalid Password. \n" +
              "Password should not contain any space. \n" +
              "Password should contain at least one lowercase letter. \n" +
              "Password should contain at least one uppercase letter. \n" +
              "Password should contain at least one special character.", status = HttpStatusCode.BadRequest)
*/
