package com.blanktheevil.infinitespire.badges.abstracts

import basemod.interfaces.OnCardUseSubscriber
import com.blanktheevil.infinitespire.badges.Badge
import com.blanktheevil.infinitespire.interfaces.hooks.AfterTurnEndInterface
import com.blanktheevil.infinitespire.utils.addVoidShard
import com.megacrit.cardcrawl.cards.AbstractCard

abstract class OfAKindBadge(id: String, private val amount: Int) : Badge(id), OnCardUseSubscriber, AfterTurnEndInterface {
  private var timesPlayedInARow = 0
  private var focusedCardId = ""

  override fun giveReward() {
    addVoidShard(1)
  }

  override fun reset() {
    timesPlayedInARow = 0
    focusedCardId = ""
  }

  override fun receiveCardUsed(c: AbstractCard) {
    if (focusedCardId == c.cardID) {
      timesPlayedInARow++
      if (timesPlayedInARow == amount.minus(1)) {
        completed()
        timesPlayedInARow = 0
      }
    } else {
      timesPlayedInARow = 0
      focusedCardId = c.cardID
    }
  }

  override fun afterEndTurn() {
    reset()
  }
}