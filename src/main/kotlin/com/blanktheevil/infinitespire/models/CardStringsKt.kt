package com.blanktheevil.infinitespire.models

import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import com.megacrit.cardcrawl.cards.AbstractCard.CardType

class CardStringsKt(
  val NAME: String = "[MISSING_TITLE]",
  val DESCRIPTION: String = "[MISSING_DESCRIPTION]",
  val UPGRADE_DESCRIPTION: String = "[MISSING_DESCRIPTION+]",
  val EXTENDED_DESCRIPTION: List<String> = listOf("[MISSING_0]", "[MISSING_1]", "[MISSING_2]"),
  val COST: Int = 0,
  val TYPE: CardType = CardType.SKILL,
  val TARGET: CardTarget = CardTarget.NONE
)