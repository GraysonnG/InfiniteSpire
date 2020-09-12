package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.badges.abstracts.OfAKindBadge
import com.blanktheevil.infinitespire.extensions.makeID

class ThreeOfAKindBadge : OfAKindBadge(ID, 3) {
  companion object {
    val ID = "ThreeOfAKind".makeID()
  }
}