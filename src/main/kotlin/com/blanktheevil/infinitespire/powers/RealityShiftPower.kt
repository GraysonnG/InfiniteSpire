package com.blanktheevil.infinitespire.powers

import basemod.interfaces.CloneablePowerInterface
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.monsters.Nightmare
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.powers.AbstractPower

class RealityShiftPower(val nightmare: Nightmare, amount: Int) : AbstractPower(), CloneablePowerInterface {
  companion object {
    val powerID = "RealityShiftPower".makeID()
    val STRINGS = languagePack.getPowerStrings(powerID)
  }

  private val maxAmount = amount
  private var hasTriggered = false

  init {
    owner = nightmare
    this.amount = amount
    this.name = STRINGS.NAME
    this.ID = powerID
    this.img = Textures.powers.get("realityshift.png")
    this.type = PowerType.BUFF
    this.isTurnBased = true
    this.priority = Int.MIN_VALUE
    this.updateDescription()
  }

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
    with(STRINGS) {
      description = StringBuilder()
        .append(DESCRIPTIONS[0])
        .append(amount.toString())
        .append(DESCRIPTIONS[1])
        .append(getAttackStr())
        .append(DESCRIPTIONS[2])
        .append(nightmare.effectAmount + 1)
        .append(DESCRIPTIONS[3])
        .toString()
    }
  }

  override fun makeCopy(): AbstractPower {
    return RealityShiftPower(this.owner as Nightmare, this.maxAmount)
  }
}