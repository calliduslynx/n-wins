package de.mabe

import de.mabe.util.log
import java.time.LocalDateTime

object UserService {
  private val activeUsers = HashMap<User, LocalDateTime>()

  init {
    log("init User-Service")

    EventBus.on<UserTickEvent> {
      if (activeUsers[it.user] == null) log("User ${it.user} connected")
      activeUsers[it.user] = LocalDateTime.now()
    }

    EventBus.on<HearthBeatTickEvent> {
      val now = LocalDateTime.now()
      val toRemove = ArrayList<User>()
      activeUsers.forEach { user, time ->
        if (time.plusSeconds(10).isBefore(now)) {
          log("User $user left")
          toRemove.add(user)
        } else if (time.plusSeconds(8).isBefore(now)) {
          log("User $user struggling")
        }
      }

      toRemove.forEach { user ->
        activeUsers.remove(user)
        EventBus.fire(UserLeftEvent(user))
      }
    }
  }

  fun getUserById(userName: String?): User? {
    if (userName == null) return null
    val user = User(userName)
    EventBus.fire(UserTickEvent(user))
    return user
  }
}

class User(
    private val userName: String
) {
  override fun equals(other: Any?) = userName == (other as? User)?.userName
  override fun hashCode() = userName.hashCode()
  override fun toString() = userName
}

// ***** UserEvents
class UserTickEvent(val user: User)

class UserLeftEvent(val user: User)
