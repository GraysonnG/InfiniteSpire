package com.blanktheevil.infinitespire.patches.bottledsoul

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard

@SpirePatch(clz = AbstractCard::class, method = SpirePatch.CLASS)
class BottledSoulFieldPatch {
  companion object {
    @JvmField
    var isBottledSoulCard = SpireField { false }
  }
}