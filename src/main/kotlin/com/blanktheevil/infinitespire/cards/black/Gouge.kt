package com.blanktheevil.infinitespire.cards.black

import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.powers.ArtifactPower
import com.megacrit.cardcrawl.powers.VulnerablePower
import com.megacrit.cardcrawl.powers.WeakPower

@Suppress("unused")
class Gouge : BlackCard(BUILDER) {
  companion object {
    val ID = "Gouge".makeID()
    private const val DAMAGE = 5
    private const val UPG_DAMAGE = 4
    private const val MAGIC = 2
    private const val UPG_MAGIC = 1
    private val ATTACK_EFFECT = AbstractGameAction.AttackEffect.SLASH_DIAGONAL
    private val BUILDER = CardBuilder(id = ID)
      .img("gouge.png")
      .black()
      .attack()
      .enemy()
      .cost(0)
      .use { p, m ->
        addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), ATTACK_EFFECT))
        addToBot(RemoveSpecificPowerAction(m, p, ArtifactPower.POWER_ID))
        m?.applyPower(WeakPower(m, magicNumber, false))
        m?.applyPower(VulnerablePower(m, magicNumber, false))
      }
      .init {
        baseDamage = DAMAGE
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
      }
      .upgr {
        upgradeDamage(UPG_DAMAGE)
        upgradeMagicNumber(UPG_MAGIC)
      }
  }
}