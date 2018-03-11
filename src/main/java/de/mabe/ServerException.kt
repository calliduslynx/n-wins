package de.mabe

class ServerException(
    val returnCode: Int,
    message: String
) : RuntimeException(message)