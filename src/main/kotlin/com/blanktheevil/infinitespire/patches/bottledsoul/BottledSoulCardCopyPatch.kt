package com.blanktheevil.infinitespire.patches.bottledsoul

import com.blanktheevil.infinitespire.extensions.inBottleSoul
import com.evacipated.cardcrawl.modthespire.lib.ByRef
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard

@Suppress("unused")
@SpirePatch(clz = AbstractCard::class, method = "makeStatEquivalentCopy")
object BottledSoulCardCopyPatch {
  @JvmStatic
  @SpireInsertPatch(rloc = 6, localvars = ["card"])
  fun applyBottledSoul(instance: AbstractCard, @ByRef cardRef: Array<AbstractCard>) {
    val card = cardRef[0]

    if (instance.inBottleSoul) {
      card.inBottleSoul = true
      card.exhaust = false
    }
  }
}