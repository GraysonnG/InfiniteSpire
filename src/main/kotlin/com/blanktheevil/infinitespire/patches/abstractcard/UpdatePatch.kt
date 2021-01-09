package com.blanktheevil.infinitespire.patches.abstractcard

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.cards.AbstractCard

@SpirePatch(clz = AbstractCard::class, method = "update")
object UpdatePatch {
  @SpirePrefixPatch
  @JvmStatic
  fun fixHitbox(card: AbstractCard) {
    card.hb.update()
  }
}