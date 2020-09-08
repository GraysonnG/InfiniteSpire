package com.blanktheevil.infinitespire.cards.utils

import basemod.AutoAdd
import basemod.BaseMod
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.cards.variables.PoisonVariable
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.blanktheevil.infinitespire.patches.utils.filters.NotPackageFilter
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.unlock.UnlockTracker

object CardManager {
  private val blackCards = mutableListOf<BlackCard>()

  private fun addCard(card: Card) {
    if (card is BlackCard) {
      addBlackCard(card)
    } else {
      CardLibrary.add(card)
    }
  }

  fun addAllCards() {
    addCustomVariables()
    AutoAdd(InfiniteSpire.modid)
      .packageFilter(Card::class.java)
      .filter(NotPackageFilter(CardManager::class.java))
      .any(Card::class.java) { info, card ->
        InfiniteSpire.logger.info("Added Card: ${card.cardID}")
        addCard(card)
        if (info.seen) {
          UnlockTracker.markCardAsSeen(card.cardID)
          card.isSeen = true
        }
      }
  }

  private fun addCustomVariables() {
    BaseMod.addDynamicVariable(PoisonVariable())
  }

  @JvmStatic
  fun addBlackCard(card: BlackCard) {
    CardLibrary.add(card)
    blackCards.add(card)
  }

  @JvmStatic
  fun getRandomBlackCard(): BlackCard =
    blackCards.getRandomItem(AbstractDungeon.cardRandomRng).makeCopy() as BlackCard

  @JvmStatic
  fun getBlackCardList(amount: Int): List<BlackCard> {
    fun getUniqueCard(list: List<BlackCard>, depth: Int = 0): BlackCard {
      var card = getRandomBlackCard()
      list.forEach {
        if (depth < 100 && it.cardID == card.cardID) {
          card = getUniqueCard(list, depth + 1)
        }
      }
      return card
    }

    val cards = mutableListOf<BlackCard>()
    for (i in 0 until amount) {
      cards.add(getUniqueCard(cards))
    }

    return cards
  }

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
