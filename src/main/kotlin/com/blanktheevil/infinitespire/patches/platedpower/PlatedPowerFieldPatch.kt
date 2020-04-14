package com.blanktheevil.infinitespire.patches.platedpower

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.powers.AbstractPower

@SpirePatch(clz = AbstractPower::class, method = SpirePatch.CLASS)
object PlatedPowerFieldPatch {
  @JvmField
  var isPlatedPower = SpireField { false }
}