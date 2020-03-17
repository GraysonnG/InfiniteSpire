package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.interfaces.OnActComplete

abstract class CrystalRelic(
  id: String,
  img: String
) : Relic(id, img, RelicTier.UNCOMMON, LandingSound.CLINK), OnActComplete {
  init {
    subscribe()
  }

  override fun onActCompleted(actId: String) {
    counter++
  }
}