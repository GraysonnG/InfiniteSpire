package com.blanktheevil.infinitespire.creatures.utils

import com.blanktheevil.infinitespire.patches.abstractcreature.PhasedFieldPatch
import com.megacrit.cardcrawl.core.AbstractCreature

var AbstractCreature.isPhased: Boolean
  get() = PhasedFieldPatch.phased.get(this) as Boolean
  set(value) = PhasedFieldPatch.phased.set(this, value)