package com.blanktheevil.infinitespire.powers

import basemod.interfaces.CloneablePowerInterface
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.powers.AbstractPower

class BeetleShellPower(player: AbstractPlayer) : AbstractPower(), CloneablePowerInterface {
  companion object {
    val powerID = "BeetleShellPower".makeID()
    private val strings = CardCrawlGame.languagePack.getPowerStrings(powerID)
  }

  init {
    owner = player
    amount = 1
    name = strings.NAME
    ID = powerID
    img = Textures.powers.get("beetleshell.png")
    type = PowerType.BUFF
    updateDescription()
    priority = 6
  }

  override fun updateDescription() {
    this.description = strings.DESCRIPTIONS[0]
  }

  override fun modifyBlock(blockAmount: Float): Float = blockAmount.times(2f)

  override fun makeCopy(): AbstractPower = BeetleShellPower(this.owner as AbstractPlayer)
}