package com.blanktheevil.infinitespire.monsters.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.log
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect

val attackIntents = mutableListOf<AbstractMonster.Intent>().also {
  it.add(AbstractMonster.Intent.ATTACK)
  it.add(AbstractMonster.Intent.ATTACK_DEBUFF)
  it.add(AbstractMonster.Intent.ATTACK_BUFF)
  it.add(AbstractMonster.Intent.ATTACK_DEFEND)
}

fun AbstractMonster.setMove(
  move: Move,
) {
  val errorMsg = "ENEMY MOVE ${move.name} IS SET INCORRECTLY! REPORT TO DEV"

  if (move.intent in attackIntents && move.damage < 0) {
    for (i in 0 until 8) {
      AbstractDungeon.effectsQueue.add(
        TextAboveCreatureEffect(
          MathUtils.random(
            Settings.WIDTH.times(0.25f),
            Settings.WIDTH.times(0.75f)
          ),
          MathUtils.random(
            Settings.HEIGHT.times(0.25f),
            Settings.HEIGHT.times(0.75f)
          ),
          errorMsg,
          Color.RED.cpy()
        )
      )
    }
    log.info(errorMsg)
  }

  this.setMove(move.name, move.getByte(), move.intent, move.damage, move.multiplier, move.isMultiDamage)
}