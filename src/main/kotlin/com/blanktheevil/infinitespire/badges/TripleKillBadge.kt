package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.badges.abstracts.KillBadge
import com.blanktheevil.infinitespire.extensions.makeID

class TripleKillBadge : KillBadge(ID, 3) {
  companion object {
    val ID = "TripleKill".makeID()
  }
}