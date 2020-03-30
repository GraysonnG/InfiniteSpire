package com.blanktheevil.infinitespire.patches.abstractpower

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.powers.AbstractPower

@SpirePatch(clz = AbstractPower::class, method = SpirePatch.CLASS)
class PlatedPowerFieldPatch {
  companion object {
    @JvmField
    var isPlatedPower = SpireField { false }
  }
}