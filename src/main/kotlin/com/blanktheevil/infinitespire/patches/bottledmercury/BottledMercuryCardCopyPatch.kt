package com.blanktheevil.infinitespire.patches.bottledmercury

import com.blanktheevil.infinitespire.extensions.inBottleMercury
import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard

@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "makeStatEquivalentCopy")
class BottledMercuryCardCopyPatch {
  companion object {
    @JvmStatic
    @SpireInsertPatch(rloc = 6, localvars = ["card"])
    fun applyBottledMercury(instance: AbstractCard, @ByRef cardRef: Array<AbstractCard>) {
      val card = cardRef[0]

      if (instance.inBottleMercury) {
        card.inBottleMercury = true
      }
    }
  }
}