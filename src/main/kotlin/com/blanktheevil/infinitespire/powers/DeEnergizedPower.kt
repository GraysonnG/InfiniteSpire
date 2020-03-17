package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.powers.AbstractPower

class DeEnergizedPower(owner: AbstractPlayer, amount: Int) : AbstractPower() {
  companion object {
    val powerID = "DeenergizedPower".makeID()
    private val strings = CardCrawlGame.languagePack.getPowerStrings(powerID)
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

  override fun updateDescription() {
    this.description = "${strings.DESCRIPTIONS[0]}$amount${strings.DESCRIPTIONS[1]}"
  }

  override fun onEnergyRecharge() {
    this.flash()
    player.loseEnergy(amount)
  }
}