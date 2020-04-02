package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.CriticalPower
import com.blanktheevil.infinitespire.relics.abstracts.Relic
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.monsters.AbstractMonster

class BurningSword : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Burning Sword".makeID()
    private val IMG = "burningsword"
    private val TIER = RelicTier.BOSS
    private val SOUND = LandingSound.MAGICAL
  }

  var attacksPlayed = 0

  override fun atTurnStart() {
    attacksPlayed = 0
    player.applyPower(CriticalPower(player))
  }

  override fun onPlayCard(card: AbstractCard?, monster: AbstractMonster?) {
    if(card != null && card.type == AbstractCard.CardType.ATTACK && attacksPlayed == 0) {
      addToBot(
        MakeTempCardInHandAction(Burn())
      )
      attacksPlayed++
    }
  }
}