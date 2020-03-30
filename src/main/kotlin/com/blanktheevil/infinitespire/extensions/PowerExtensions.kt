package com.blanktheevil.infinitespire.extensions

import com.blanktheevil.infinitespire.patches.abstractpower.PlatedPowerFieldPatch
import com.megacrit.cardcrawl.powers.AbstractPower

var AbstractPower.isPlatedPower: Boolean
  get() = PlatedPowerFieldPatch.isPlatedPower.get(this) as Boolean
  set(value) = PlatedPowerFieldPatch.isPlatedPower.set(this, value)