package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.badges.abstracts.OfAKindBadge
import com.blanktheevil.infinitespire.extensions.makeID

class FourOfAKindBadge : OfAKindBadge(ID, 4) {
  companion object {
    val ID = "FourOfAKind".makeID()
  }
}