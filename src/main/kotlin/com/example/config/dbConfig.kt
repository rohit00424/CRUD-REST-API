package com.example.config
import java.sql.Connection
import java.sql.DriverManager

class dbConfig {
    fun conn() : Connection {
          val url = "jdbc:postgresql://localhost:5432/sampledb"
        //val url = "jdbc:postgresql://localhost:5432/                                                 "
        //username
        val username = "postgres"
        //password
        val  password = "root"

        return DriverManager.getConnection(url, username, password)
    }
}
