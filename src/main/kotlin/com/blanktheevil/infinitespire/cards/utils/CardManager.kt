package com.blanktheevil.infinitespire.cards.utils

import basemod.AutoAdd
import basemod.BaseMod
import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.cards.black.FinalStrike
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
