package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.badges.abstracts.KillBadge
import com.blanktheevil.infinitespire.extensions.makeID

class QuadraKillBadge : KillBadge(ID, 4) {
  companion object {
    val ID = "QuadraKill".makeID()
  }
}