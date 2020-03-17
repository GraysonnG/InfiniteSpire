package com.blanktheevil.infinitespire.patches.bottledmercury

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard

@SpirePatch(clz = AbstractCard::class, method = SpirePatch.CLASS)
class BottledMercuryFieldPatch {
  companion object {
    @JvmField
    var isBottledMercuryCard = SpireField { false }
  }
}