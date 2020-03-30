package com.blanktheevil.infinitespire.utils

import com.blanktheevil.infinitespire.cards.BlackCard
import com.blanktheevil.infinitespire.cards.FinalStrike
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary

class CardHelper {
  companion object {
    private val blackCards = mutableListOf<BlackCard>()

    fun addBlackCard(card: BlackCard) {
      CardLibrary.add(card)
      blackCards.add(card)
    }

    fun getRandomBlackCard(): BlackCard =
      (blackCards.getRandomItem(AbstractDungeon.cardRandomRng) ?: FinalStrike()).makeCopy() as BlackCard
  }
}