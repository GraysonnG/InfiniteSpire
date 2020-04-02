package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.models.SaveData
import com.blanktheevil.infinitespire.relics.abstracts.BottleRelic
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.FocusPower
import com.megacrit.cardcrawl.powers.StrengthPower

class BottledMercury : BottleRelic(ID, IMG, TIER, SOUND) {
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
          player.applyPower(StrengthPower(player, 1))
        AbstractCard.CardType.SKILL ->
          player.applyPower(DexterityPower(player, 1))
        AbstractCard.CardType.POWER ->
          player.applyPower(FocusPower(player, 2), amount = 2)
        else -> doNothing()
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

  override fun beforeConfigSave(saveData: SaveData) {
    if (cardSelected && selectedCard != null) {
      saveData.bottledMercury.selectedCardID = selectedCard!!.cardID
    }
  }

  override fun afterConfigLoad(saveData: SaveData) {
    if (saveData.bottledMercury.selectedCardID != "") {
      player.masterDeck.group.forEach {
        if (it.cardID == saveData.bottledMercury.selectedCardID) {
          cardSelected = true
          selectedCard = it
          it.inBottleMercury = true
        }
      }
    }
  }

  override fun clearData(saveData: SaveData) {
    saveData.bottledMercury.selectedCardID = ""
  }
}