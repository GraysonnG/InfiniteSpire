package com.blanktheevil.infinitespire.utils

import basemod.AutoAdd
import basemod.BaseMod
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.cards.black.FinalStrike
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.unlock.UnlockTracker

class CardManager {
  companion object {
    private val blackCards = mutableListOf<BlackCard>()

    private fun addCard(card: Card) {
      if (card is BlackCard) {
        addBlackCard(card)
      } else {
        CardLibrary.add(card)
      }
    }

    fun addAllCards() {
      AutoAdd(InfiniteSpire.modid)
        .packageFilter(Card::class.java)
        .any(Card::class.java) { info, card ->
          InfiniteSpire.logger.info("Added Card: ${card.cardID}")
          addCard(card)
          if (info.seen) {
            UnlockTracker.markCardAsSeen(card.cardID)
          }
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

    @JvmStatic
    fun addBlackCardColor() {
      BaseMod.addColor(
        EnumPatches.CardColor.INFINITE_BLACK,
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Color.BLACK.cpy(),
        Textures.cards.getString("ui/512/boss-attack.png", true),
        Textures.cards.getString("ui/512/boss-skill.png", true),
        Textures.cards.getString("ui/512/boss-power.png", true),
        Textures.cards.getString("ui/512/boss-orb.png", true),
        Textures.cards.getString("ui/1024/boss-attack.png", true),
        Textures.cards.getString("ui/1024/boss-skill.png", true),
        Textures.cards.getString("ui/1024/boss-power.png", true),
        Textures.cards.getString("ui/1024/boss-orb.png", true)
      )
    }
  }
}