package com.blanktheevil.infinitespire.relics

import basemod.AutoAdd
import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.relics.abstracts.Relic

@AutoAdd.Ignore
class EmptyRelic : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "EmptyRelic".makeID()
    private const val IMG = "empty"
    private val TIER = RelicTier.DEPRECATED
    private val SOUND = LandingSound.FLAT
  }

  override fun update() = doNothing()
}