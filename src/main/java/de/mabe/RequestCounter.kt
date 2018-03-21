package de.mabe

object RequestCounter {

  /**
   * @throws ServerException
   */
  fun registerRequestFromIp(ip: String) {
    val numberOfRequests = incRequestCounter()
    if (numberOfRequests > Configuration.`Maximale Requests pro Minute pro IP`)
      throw ServerException(500, "Zu viele Requests von IP '$ip'")
  }

  private fun incRequestCounter(): Int {
    return 1 // TODO
  }

  /**
   * @throws ServerException
   */
  fun registerRequestFromUser(user: User) {
    // TODO
  }

}
