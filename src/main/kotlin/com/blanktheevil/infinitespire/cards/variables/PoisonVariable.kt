package com.blanktheevil.infinitespire.cards.variables

import basemod.abstracts.DynamicVariable
import com.blanktheevil.infinitespire.cards.Neurotoxin
import com.megacrit.cardcrawl.cards.AbstractCard

class PoisonVariable : DynamicVariable() {
  override fun isModified(card: AbstractCard?): Boolean = false
  override fun upgraded(card: AbstractCard?): Boolean = false
  override fun value(card: AbstractCard): Int =
    if (card is Neurotoxin) {
      card.poisonamt
    } else {
      0
    }

  override fun baseValue(card: AbstractCard): Int =
    if (card is Neurotoxin) {
      card.poisonamt
    } else {
      0
    }

  override fun key(): String = "inf_P"
}