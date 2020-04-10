package com.blanktheevil.infinitespire.utils

import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.cards.black.FinalStrike
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary

class CardHelper {
  companion object {
    private val blackCards = mutableListOf<BlackCard>()

    fun addCard(card: Card) {
      if (card is BlackCard) {
        addBlackCard(card)
      } else {
        CardLibrary.add(card)
      }
    }

    @JvmStatic
    fun addBlackCard(card: BlackCard) {
      CardLibrary.add(card)
      blackCards.add(card)
    }

    @JvmStatic
    fun getRandomBlackCard(): BlackCard =
      (blackCards.getRandomItem(AbstractDungeon.cardRandomRng) ?: FinalStrike()).makeCopy() as BlackCard
  }
}