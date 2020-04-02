package com.blanktheevil.infinitespire.relics.abstracts

import com.blanktheevil.infinitespire.interfaces.ActCompleteInterface
import com.blanktheevil.infinitespire.relics.Relic

abstract class CrystalRelic(
  id: String,
  img: String
) : Relic(id, img, RelicTier.UNCOMMON, LandingSound.CLINK), ActCompleteInterface {
  init {
    subscribe()
  }

  override fun onActCompleted(actId: String) {
    counter++
  }
}