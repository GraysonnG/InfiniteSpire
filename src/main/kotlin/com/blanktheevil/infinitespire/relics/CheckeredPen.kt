package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo

class CheckeredPen : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "CheckeredPen".makeID()
    private const val IMG = "checkeredpen"
    private val TIER = RelicTier.UNCOMMON
    private val SOUND = LandingSound.CLINK
  }

  override fun onUseCard(targetCard: AbstractCard?, useCardAction: UseCardAction?) {
    addToBot(
      DamageRandomEnemyAction(
        DamageInfo(
          null,
          1,
          DamageInfo.DamageType.THORNS
        ),
        AbstractGameAction.AttackEffect.BLUNT_LIGHT
      )
    )
  }
}