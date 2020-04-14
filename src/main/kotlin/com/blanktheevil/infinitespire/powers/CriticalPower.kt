package com.blanktheevil.infinitespire.powers

import basemod.interfaces.CloneablePowerInterface
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.powers.AbstractPower

class CriticalPower(player: AbstractPlayer): AbstractPower(), CloneablePowerInterface {
  companion object {
    val powerID = "CriticalPower".makeID()
    private val STRINGS = languagePack.getPowerStrings(powerID)
  }

  init {
    owner = player
    amount = -1
    name = STRINGS.NAME
    ID = powerID
    img = Textures.powers.get("crit.png")
    type = PowerType.BUFF
    updateDescription()
  }

  override fun updateDescription() {
    this.description = STRINGS.DESCRIPTIONS[0]
  }

  override fun atDamageFinalGive(damage: Float, type: DamageInfo.DamageType?): Float =
    if (type == DamageInfo.DamageType.NORMAL) damage.times(2f) else damage

  override fun onAfterUseCard(card: AbstractCard?, action: UseCardAction?) {
    if (card != null && card.type == AbstractCard.CardType.ATTACK) {
      addToTop(
        RemoveSpecificPowerAction(owner, owner, powerID)
      )
    }
  }

  override fun makeCopy(): AbstractPower {
    return CriticalPower(this.owner as AbstractPlayer)
  }
}