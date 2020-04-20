package com.blanktheevil.infinitespire.powers

import basemod.interfaces.CloneablePowerInterface
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.monsters.Nightmare
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.powers.AbstractPower

class RealityShiftPower(val nightmare: Nightmare, amount: Int) :
    AbstractPower(),
    CloneablePowerInterface {
  companion object {
    val powerID = "RealityShiftPower".makeID()
    val BUILDER = PowerBuilder(powerID)
      .img("realityshift.png")
      .isTurnBased()
      .buff()
      .priority(Int.MIN_VALUE)
  }

  val strings = languagePack.getPowerStrings(powerID)

  init {
    this.owner = nightmare
    this.amount = amount
    this.name = strings.NAME
    this.ID = powerID
    this.img = BUILDER.img
    this.type = BUILDER.type
    this.priority = BUILDER.priority
    updateDescription()
  }

  private val maxAmount = amount
  private var hasTriggered = false

  override fun atEndOfTurn(isPlayer: Boolean) {
    if (!isPlayer) {
      hasTriggered = false
      this.amount = maxAmount
      this.updateDescription()
    }
  }

  override fun onAttacked(info: DamageInfo, damageAmount: Int): Int {
    this.amount -= damageAmount
    if (this.amount <= 0 && !hasTriggered) {
      this.amount = 0
      nightmare.triggerRealityShiftAttack()
      this.hasTriggered = true
    }

    return super.onAttacked(info, damageAmount)
  }

  private fun getAttackStr(): Int {
    val dmg = nightmare.damage[0]
    dmg.applyPowers(nightmare, player)
    return dmg.output
  }

  override fun updateDescription() {
    with(strings) {
      description = StringBuilder()
        .append(DESCRIPTIONS[0])
        .append(amount.toString())
        .append(DESCRIPTIONS[1])
        .append(getAttackStr())
        .append(DESCRIPTIONS[2])
        .append(nightmare.attackAmount + 1)
        .append(DESCRIPTIONS[3])
        .toString()
    }
  }

  override fun makeCopy(): AbstractPower {
    return RealityShiftPower(this.owner as Nightmare, this.maxAmount)
  }
}