package de.mabe

import de.mabe.util.log
import spark.kotlin.ignite

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)


object NWinsServer {
  fun start() {
    log("starting server")

    val http = ignite()
    http.port(8080)

    http.get("/**") { 
      log("FOOO")
    }
    http.get("/hello") {
      "Hello Spark Kotlin!"
    }
  }


  fun onRequest(req: Request, res: Response) {
    try {
      onRequestInternal(req)
    } catch (serverException: ServerException) {
      // TODO
    } catch (exception: Exception) {
      // TODO
    }
  }

  private fun onRequestInternal(req: Request) {
    RequestCounter.registerRequestFromIp(req.ip)
    val user = UserService.getUserFromHeader(req.headers["Authentication"])
    if (user != null) RequestCounter.registerRequestFromUser(user)
    val url = req.path
    val payload = when {
      url.startsWith("GET") -> getPayload(req)
      else -> null
    }

    val responsePayload = when {
      url == "GET /" -> AdminController.getIndexHtml()
      url == "GET /ux-data" -> AdminController.getUxData()
      url.contains("/api") && user == null -> throw ServerException(400, "Pass Authentication Header when using /api interface.")
      url == "GET /api" -> GameController.getDataForUser(user!!)
      url == "POST /api" && payload == null -> throw ServerException(400, "Pass Payload for POST /api.")
      url == "POST /api " -> GameController.handleDataForUser(user!!, payload!!)
      else -> throw ServerException(404, "Unknown url '$url'")
    }
  }


}

private fun getPayload(req: Request): Payload {
  TODO()
}


class Request {
  val ip: String = ""
  /** "GET /api" */
  val path: String = ""
  val headers: Map<String, String> = emptyMap()
}

class Response {
  fun tooManyRequestsFromIp(): Response {
    return this
  }
}