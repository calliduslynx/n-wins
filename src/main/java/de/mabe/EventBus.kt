package de.mabe

object EventBus {
  val listeners = HashMap<Class<*>, MutableList<(Any) -> Unit>>()

  inline fun <reified EVENT> on(crossinline method: (EVENT) -> Unit) {
    listeners.getOrPut(EVENT::class.java, { ArrayList() })
        .add({ method.invoke(it as EVENT) })
  }

  fun <EVENT : Any> fire(event: EVENT) {
    listeners
        .filterKeys { it.isAssignableFrom(event::class.java) }
        .forEach { _, listeners ->
          listeners.forEach { listener -> listener.invoke(event) }
        }
  }
}