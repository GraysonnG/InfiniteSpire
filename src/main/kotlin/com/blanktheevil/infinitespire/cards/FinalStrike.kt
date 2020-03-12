package com.blanktheevil.infinitespire.cards

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.red.PerfectedStrike
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster
import kotlin.math.floor
import kotlin.streams.toList

@Suppress("unused")
class FinalStrike : BlackCard(ID, IMG) {
  companion object {
    val ID = "FinalStrike".makeID()
    private val IMG = Textures.cards.getString("finalstrike.png")

    fun isStrike(card: AbstractCard): Boolean {
      return if (PerfectedStrike.isStrike(card) || card.tags.contains(CardTags.STRIKE)) {
        true
      } else card.cardID.toLowerCase().contains("strike")
    }
  }

  init {
    baseDamage = 10
    baseMagicNumber = 5
    magicNumber = 5
    this.tags.add(CardTags.STRIKE)
  }

  private fun getStrikeDamage(): Float {
    val groups = mutableListOf<CardGroup>()
    var count = 0

    with(player) {
      groups.add(hand)
      groups.add(drawPile)
      groups.add(discardPile)
    }

    groups.forEach {cGroup ->
      count += cGroup.group.stream()
        .filter { isStrike(it) }
        .toList().size
    }

    return count.times(magicNumber.toFloat())
  }

  override fun applyPowers() {
    var tmp: Float = this.baseDamage.toFloat()
    tmp += getStrikeDamage()
    with (player) {
      powers.forEach { it.atDamageGive(tmp, damageTypeForTurn) }
      powers.forEach { it.atDamageFinalGive(tmp, damageTypeForTurn) }
    }

    tmp = if (tmp < 0f) 0f else tmp
    isDamageModified = baseDamage != tmp.toInt()
    damage = floor(tmp).toInt()
  }

  override fun calculateCardDamage(mo: AbstractMonster?) {
    var tmp: Float = this.baseDamage.toFloat()
    tmp += getStrikeDamage()
    with (player) {
      powers.forEach { it.atDamageGive(tmp, damageTypeForTurn) }
      powers.forEach { it.atDamageFinalGive(tmp, damageTypeForTurn) }
    }

    if (mo != null) {
      with(mo) {
        powers.forEach { it.atDamageReceive(tmp, damageTypeForTurn) }
        powers.forEach { it.atDamageFinalReceive(tmp, damageTypeForTurn) }
      }
    }

    tmp = if (tmp < 0f) 0f else tmp
    isDamageModified = baseDamage != tmp.toInt()
    damage = floor(tmp).toInt()
  }

  override fun use(p: AbstractPlayer?, m: AbstractMonster?) {
    addToBot(WaitAction(0.8f))
    addToBot(DamageAction(m, DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE))
    TODO("final strike vfx above wait action")
  }

  override fun onUpgrade() {
    upgradeDamage(5)
  }
}