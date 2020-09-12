package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.badges.abstracts.KillBadge
import com.blanktheevil.infinitespire.extensions.makeID

class DoubleKillBadge : KillBadge(ID, 2) {
  companion object {
    val ID = "DoubleKill".makeID()
  }
}