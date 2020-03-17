package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.models.Config
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.FocusPower
import com.megacrit.cardcrawl.powers.StrengthPower

class BottledMercury: BottleRelic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "BottledMercury".makeID()
    private val IMG = "bottledMercury"
    private val TIER = RelicTier.UNCOMMON
    private val SOUND = LandingSound.CLINK
  }

  override fun onPlayCard(card: AbstractCard, monster: AbstractMonster?) {
    if (card.inBottleMercury) {
      flash()
      actionManager.addToBottom(RelicAboveCreatureAction(player, this))
      when (card.type) {
        AbstractCard.CardType.ATTACK ->
          actionManager.addToBottom(ApplyPowerAction(player, player, StrengthPower(player, 1), 1))
        AbstractCard.CardType.SKILL ->
          actionManager.addToBottom(ApplyPowerAction(player, player, DexterityPower(player, 1), 1))
        AbstractCard.CardType.POWER ->
          actionManager.addToBottom(ApplyPowerAction(player, player, FocusPower(player, 2), 2))
        else -> { /* do nothing */ }
      }
    }
  }



  override fun filterGridSelectBy(card: AbstractCard): Boolean = true
  override fun isCardBottled(card: AbstractCard): Boolean = card.inBottleMercury

  override fun actionWhenSelected(card: AbstractCard) {
    card.inBottleMercury = true
  }

  override fun actionWhenUnEquipped(card: AbstractCard) {
    card.inBottleMercury = false
  }

  override fun beforeConfigSave(config: Config) {
    if (cardSelected && selectedCard != null) {
      config.bottledMercury.selectedCardID = selectedCard!!.cardID
    }
  }

  override fun afterConfigLoad(config: Config) {
    if (config.bottledMercury.selectedCardID != "") {
      player.masterDeck.group.forEach {
        if (it.cardID == config.bottledMercury.selectedCardID) {
          cardSelected = true
          selectedCard = it
          it.inBottleMercury = true
        }
      }
    }
  }

  override fun clearData(config: Config) {
    config.bottledMercury.selectedCardID = ""
  }
}