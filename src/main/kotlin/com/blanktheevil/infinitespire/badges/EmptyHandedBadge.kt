package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.interfaces.hooks.AfterTurnEndInterface
import com.blanktheevil.infinitespire.utils.addVoidShard

class EmptyHandedBadge : Badge(ID), AfterTurnEndInterface {
  companion object {
    val ID = "EmptyHanded".makeID()
  }

  override fun giveReward() {
    addVoidShard(1)
  }

  override fun reset() = doNothing()

  override fun afterEndTurn() {
    if (player.hand.group.size == 0) {
      completed()
    }
  }
}