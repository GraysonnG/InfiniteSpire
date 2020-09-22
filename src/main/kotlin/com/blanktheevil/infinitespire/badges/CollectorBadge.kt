package com.blanktheevil.infinitespire.badges

import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.interfaces.hooks.AfterTurnEndInterface
import com.blanktheevil.infinitespire.utils.addVoidShard

class CollectorBadge : Badge(ID), AfterTurnEndInterface {
  companion object {
    val ID = "Collector".makeID()
  }

  private var badgesThisTurn = 0

  override fun giveReward() = addVoidShard(2)
  override fun afterEndTurn() = reset()

  override fun reset() {
    badgesThisTurn = 0
  }

  override fun badgeCompletedHook() {
    badgesThisTurn++

    if (badgesThisTurn >= 5) {
      completed()
    }
  }
}