package com.blanktheevil.infinitespire.patches.puzzlecube

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.cards.AbstractCard

@SpirePatch(clz = AbstractCard::class, method = SpirePatch.CLASS)
class PuzzleCubeFieldPatch {
  companion object {
    @JvmField
    var isPuzzleCubeCard = SpireField { false }
  }
}