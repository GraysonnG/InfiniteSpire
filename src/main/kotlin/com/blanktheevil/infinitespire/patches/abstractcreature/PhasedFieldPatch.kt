package com.blanktheevil.infinitespire.patches.abstractcreature

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.megacrit.cardcrawl.core.AbstractCreature

@Suppress("unused")
@SpirePatch(clz = AbstractCreature::class, method = SpirePatch.CLASS)
object PhasedFieldPatch {
  @JvmField
  var phased = SpireField { false }
}