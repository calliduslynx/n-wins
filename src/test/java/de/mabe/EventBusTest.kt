package de.mabe

import org.junit.Assert
import org.junit.Test

private open class E2 : E1()
private open class E3 : E2()
private open class E1 {
  override fun toString() = this::class.java.simpleName
}

class EventBusTest {
  private val ticks = ArrayList<String>()
  private fun assertAndReset(expected: String) {
    Assert.assertEquals(expected, ticks.joinToString(","))
    ticks.clear()
  }

  @Test fun `Bus funktionert`() {

    EventBus.on { event: E1 -> ticks.add("E1:$event") }
    EventBus.on { event: E2 -> ticks.add("E2:$event") }
    EventBus.on { event: E3 -> ticks.add("E3a:$event") }
    EventBus.on { event: E3 -> ticks.add("E3b:$event") }

    EventBus.fire(E1())
    assertAndReset("E1:E1")
    
    EventBus.fire(E2())
    assertAndReset("E2:E2,E1:E2")

    EventBus.fire(E3())
    assertAndReset("E2:E3,E1:E3,E3a:E3,E3b:E3")
  }

}