package com.blanktheevil.infinitespire.extensions

import com.blanktheevil.infinitespire.patches.bottledmercury.BottledMercuryFieldPatch
import com.blanktheevil.infinitespire.patches.bottledsoul.BottledSoulFieldPatch
import com.blanktheevil.infinitespire.patches.puzzlecube.PuzzleCubeFieldPatch
import com.megacrit.cardcrawl.cards.AbstractCard

val attack = AbstractCard.CardType.ATTACK
val skill = AbstractCard.CardType.SKILL
val power = AbstractCard.CardType.POWER

var AbstractCard.inBottleSoul: Boolean
  get() = BottledSoulFieldPatch.isBottledSoulCard.get(this) as Boolean
  set(value) = BottledSoulFieldPatch.isBottledSoulCard.set(this, value)

var AbstractCard.inBottleMercury: Boolean
  get() = BottledMercuryFieldPatch.isBottledMercuryCard.get(this) as Boolean
  set(value) = BottledMercuryFieldPatch.isBottledMercuryCard.set(this, value)

var AbstractCard.inPuzzleCube: Boolean
  get() = PuzzleCubeFieldPatch.isPuzzleCubeCard.get(this) as Boolean
  set(value) = PuzzleCubeFieldPatch.isPuzzleCubeCard.set(this, value)

fun AbstractCard.isBottled(): Boolean =
  inBottleFlame || inBottleLightning || inBottleTornado || inBottleMercury || inBottleSoul