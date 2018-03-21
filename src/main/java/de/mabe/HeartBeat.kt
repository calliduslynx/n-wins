package de.mabe

class HeartBeat {
  private val DELAY_IN_MS = 1000L

  fun start() {
    Thread({
      while (true) {
        Thread.sleep(DELAY_IN_MS)
        EventBus.fire(HearthBeatTickEvent())
      }
    }).start()
  }
}

class HearthBeatTickEvent