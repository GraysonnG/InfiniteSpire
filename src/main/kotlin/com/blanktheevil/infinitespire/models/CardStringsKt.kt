package com.blanktheevil.infinitespire.models

import com.megacrit.cardcrawl.cards.AbstractCard.*

class CardStringsKt(
  var NAME: String,
  var DESCRIPTION: String,
  var UPGRADE_DESCRIPTION: String?,
  var EXTENDED_DESCRIPTION: List<String>?,
  var COST: Int?,
  var TYPE: CardType?,
  var TARGET: CardTarget?
) {
  companion object {
    fun getDefault(): CardStringsKt = CardStringsKt(
      "[MISSING_TITLE]",
      "[MISSING_DESCRIPTION]",
      "[MISSING_DESCRIPTION+]",
      listOf("[MISSING_0]", "[MISSING_1]", "[MISSING_2]"),
      0,
      CardType.SKILL,
      CardTarget.NONE
    )
  }
}