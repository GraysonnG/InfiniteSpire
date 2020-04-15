package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.powers.TheBombPower

class DeathsTouch : BlackCard(BUILDER) {
  companion object {
    val ID = "DeathsTouch".makeID()
    private const val DAMAGE = 25
    private const val UPG_DAMAGE = 10
    private val BUILDER = CardBuilder(ID)
      .img("deathstouch.png")
      .cost(3)
      .attack()
      .enemy()
      .init {
        baseDamage = DAMAGE
      }
      .upgr {
        upgradeDamage(UPG_DAMAGE)
      }
      .use { player, monster ->
        addToBot(DamageAction(
          monster,
          DamageInfo(player, damage),
          AbstractGameAction.AttackEffect.FIRE
        ))
        player?.applyPower(
          TheBombPower(player, 2, this.damage)
        )
      }
  }
}