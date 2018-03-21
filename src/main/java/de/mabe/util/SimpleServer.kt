package de.mabe.util

import de.mabe.ServerException
import spark.kotlin.ignite

abstract class SimpleServer {
  fun start() {
    log("starting server")

    val http = ignite()
    http.port(8080)

    http.get("/*") {
      val internalResponse = onRequestSafe(Request(request))
      response.status(internalResponse.code)
      response.type(internalResponse.contentType)
      internalResponse.body ?: ""
    }
  }

  private fun onRequestSafe(req: Request): Response {
    return try {
      onRequest(req)
    } catch (serverException: ServerException) {
      Response(serverException.returnCode, "message" to serverException.message) // TODO ObjectMapper <- Json
    } catch (exception: Exception) {
      log("interner Fehler")
      exception.printStackTrace()
      Response(500, "Interner Fehler") // TODO json
    }
  }

  abstract fun onRequest(req: Request): Response

  protected fun readJson(jsonAsString: String): JsonNode {
    TODO()
  }
}

class Request(request: spark.Request) {
  val clientIp = request.ip()
  /** "GET /api" */
  val path = request.requestMethod() + " " + request.uri()
  val body: String = request.body()
  val headers = request.headers().map { it to request.headers(it) }.toMap()
}

class Response(
    val code: Int,
    val body: Any?,
    val contentType: String = "application/json"
) 