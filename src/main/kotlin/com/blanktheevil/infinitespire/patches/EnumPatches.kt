@file:Suppress("unused")

package com.blanktheevil.infinitespire.patches

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.rewards.RewardItem

class EnumPatches {
  class CardColor {
    companion object {
      @SpireEnum
      lateinit var INFINITE_BLACK: AbstractCard.CardColor
    }
  }

  class CardRarity {
    companion object {
      @SpireEnum
      lateinit var BLACK: AbstractCard.CardRarity
    }
  }

  class LibColor {
    companion object {
      @SpireEnum
      lateinit var INFINITE_BLACK: CardLibrary.LibraryType
    }
  }

  class RewardType {
    companion object {
      @SpireEnum
      lateinit var BLACK_CARD: RewardItem.RewardType

      @SpireEnum
      lateinit var QUEST: RewardItem.RewardType

      @SpireEnum
      lateinit var INTEREST: RewardItem.RewardType

      @SpireEnum
      lateinit var VOID_SHARD: RewardItem.RewardType
    }
  }
}