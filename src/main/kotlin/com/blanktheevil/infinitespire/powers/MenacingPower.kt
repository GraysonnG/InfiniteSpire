package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.extensions.actionManager
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower

class MenacingPower(owner: AbstractCreature, amount: Int) : Power(
  owner,
  amount,
  BUILDER
) {
  companion object {
    val powerID = "MenacingPower".makeID()
    private val BUILDER = PowerBuilder(powerID)
      .img("realityshift.png")
      .buff()
  }

  override fun updateDesc() {
    this.description = if (amount > 1) {
      strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
    } else {
      strings.DESCRIPTIONS[2]
    }
  }

  override fun makeCopy(): AbstractPower = MenacingPower(owner, amount)

  override fun onAfterUseCard(card: AbstractCard, action: UseCardAction) {
    if (card.type == AbstractCard.CardType.ATTACK) {
      this.amount--
      this.updateDescription()
    }
    if (amount <= 0) {
      addToBot(RemoveSpecificPowerAction(owner, owner, this))
    }
  }

  override fun atEndOfRound() {
    addToBot(RemoveSpecificPowerAction(owner, owner, this))
  }

  override fun onAttack(info: DamageInfo, damageAmount: Int, target: AbstractCreature) {
    if (target is AbstractMonster) {
      if (target != owner && info.type == DamageInfo.DamageType.NORMAL) {
        flash()
        var shouldApplyToMonster = true

        actionManager.actions.forEach {
          if (it is StunMonsterPower && it.target == target) {
            shouldApplyToMonster = false
          }
        }

        if (shouldApplyToMonster) {
          addToBot(StunMonsterAction(target, owner, 1))
        }
      }
    }
  }
}