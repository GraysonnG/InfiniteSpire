@file:Suppress("unused")

package com.blanktheevil.infinitespire.patches

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.rewards.RewardItem

object EnumPatches {
  object CardColor {
    @SpireEnum
    lateinit var INFINITE_BLACK: AbstractCard.CardColor
  }
  object CardRarity {
    @SpireEnum
    lateinit var BLACK: AbstractCard.CardRarity
  }
  object LibColor {
    @SpireEnum
    lateinit var INFINITE_BLACK: CardLibrary.LibraryType
  }
  object RewardType {
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