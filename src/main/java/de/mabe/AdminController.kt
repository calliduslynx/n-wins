package de.mabe

import de.mabe.util.JsonNode

object AdminController {
  private val indexHtml = AdminController.javaClass.getResourceAsStream("/index.html").bufferedReader().readText()

  fun getIndexHtml(): String = indexHtml

  fun getUxData(): String {
    TODO()
  }

  fun handleCommand(payload: JsonNode) {
  }

}
