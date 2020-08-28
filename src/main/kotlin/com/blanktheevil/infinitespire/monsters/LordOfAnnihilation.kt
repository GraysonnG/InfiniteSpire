package com.blanktheevil.infinitespire.monsters

import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.monsters.AbstractMonster

class LordOfAnnihilation :
    AbstractMonster(
      NAME,
      ID,
      MAX_HP,
      0f,
      0f,
      200f,
      200f,
      ""
    ) {
  companion object {
    val ID = "LordOfAnnihilation".makeID()
    private val strings = languagePack.getMonsterStrings(ID)
    private val NAME = strings.NAME
    private const val MAX_HP = 2500
  }

  private var phase = 0
  private var canRecover = false

  override fun useUniversalPreBattleAction() {
    this.canRecover = true
  }

  private fun recover() {
    if (canRecover) {
      canRecover = false
      this.currentHealth = 1
      addToTop(HealAction(this, this, MAX_HP))
      phase++
    }
  }

  override fun die() {
    if (phase >= 3)
      super.die()
    else
      recover()
  }

  override fun die(triggerRelics: Boolean) {
    if (phase >= 3)
      super.die(triggerRelics)
    else
      recover()
  }

  override fun getMove(roll: Int) {
    canRecover = true
    setMove(0.toByte(), Intent.MAGIC)
  }

  override fun takeTurn() = doNothing()
}