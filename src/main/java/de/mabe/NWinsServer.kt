package de.mabe

import de.mabe.util.Request
import de.mabe.util.Response
import de.mabe.util.SimpleServer


fun main(args: Array<String>) {
  NWinsServer.start()
}

object NWinsServer : SimpleServer() {
  init{
    HeartBeat().start()  
  }
  
  override fun onRequest(req: Request): Response {
    RequestCounter.registerRequestFromIp(req.clientIp)
    val user = UserService.getUserById(req.headers["Authentication"])
    if (user != null) RequestCounter.registerRequestFromUser(user)
    val path = req.path
    val payload = if (path.startsWith("POST")) readJson(req.body) else null

    return when { // @formatter:off
      path == "GET /"                          -> Response(200, AdminController.getIndexHtml(), "text/html")
      path == "GET /ux-data"                   -> Response(200, AdminController.getUxData())
      path == "POST /admin" && payload == null -> throw ServerException(400, "Pass Payload for POST /admin.")
      path == "POST /admin"                    -> Response(201, AdminController.handleCommand(payload!!))
      path.contains("/api") && user == null    -> throw ServerException(400, "Pass Authentication Header when using /api interface.")
      path == "GET /api"                       -> Response(200, GameController.getDataForUser(user!!))
      path == "POST /api" && payload == null   -> throw ServerException(400, "Pass Payload for POST /api.")
      path == "POST /api "                     -> Response(201, GameController.handleDataForUser(user!!, payload!!))
      else                                     -> throw ServerException(404, "Unknown path '$path'")
    } // @formatter:on
  }
}

