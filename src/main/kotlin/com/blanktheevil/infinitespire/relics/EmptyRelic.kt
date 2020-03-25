package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.makeID

class EmptyRelic : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "EmptyRelic".makeID()
    private const val IMG = "empty"
    private val TIER = RelicTier.DEPRECATED
    private val SOUND = LandingSound.FLAT
  }

  override fun update() = doNothing()
}