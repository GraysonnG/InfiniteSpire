package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.interfaces.hooks.AfterTurnEndInterface
import com.blanktheevil.infinitespire.utils.addVoidShard

class SurvivorBadge : Badge(ID), AfterTurnEndInterface {
  companion object {
    val ID = "Survivor".makeID()
  }

  override fun giveReward() = addVoidShard(1)
  override fun reset() = doNothing()

  override fun afterEndTurn() {
    if (player.currentHealth == 1 && player.maxHealth > 0) {
      completed()
    }
  }
}