package de.mabe.util

class JsonNode(
    val name: String,
    val value: String? = null,
    private val children: List<JsonNode> = emptyList()
) {

  operator fun get(nodeName: String): JsonNode? {
    TODO()
  }

  override fun toString(): String {
    val sb = StringBuilder()
    sb.append("$name : $value\n")
    children.forEach {
      it.toString().lines()
          .filterNot { it.isBlank() }
          .forEach { sb.append("  $it\n") }
    }
    return sb.toString()
  }
}