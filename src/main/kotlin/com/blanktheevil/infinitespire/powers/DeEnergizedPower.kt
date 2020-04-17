package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.powers.AbstractPower

class DeEnergizedPower(owner: AbstractPlayer, amount: Int) : Power(
  owner,
  amount,
  BUILDER
) {
  companion object {
    val powerID = "DeenergizedPower".makeID()
    private val BUILDER = PowerBuilder(powerID)
      .debuff()
      .img("energydown.png")
  }

  init {
    name = strings.NAME
    ID = powerID
    this.owner = owner
    this.amount = amount
    this.updateDescription()
    this.img = Textures.powers.get("energydown.png")
    this.type = PowerType.DEBUFF
  }

  override fun updateDesc() {
    this.description = "${strings.DESCRIPTIONS[0]}$amount${strings.DESCRIPTIONS[1]}"
  }

  override fun onEnergyRecharge() {
    this.flash()
    player.loseEnergy(amount)
  }

  override fun makeCopy(): AbstractPower = DeEnergizedPower(this.owner as AbstractPlayer, this.amount)
}