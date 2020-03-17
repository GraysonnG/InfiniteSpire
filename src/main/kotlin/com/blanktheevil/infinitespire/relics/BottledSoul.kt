package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.models.Config
import com.megacrit.cardcrawl.cards.AbstractCard


class BottledSoul : BottleRelic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Bottled Soul".makeID()
    private val IMG = "bottledsoul-cracked"
    private val TIER = RelicTier.UNCOMMON
    private val SOUND = LandingSound.CLINK
  }

  override fun filterGridSelectBy(card: AbstractCard): Boolean = card.exhaust
  override fun isCardBottled(card: AbstractCard): Boolean = card.inBottleSoul

  override fun actionWhenUnEquipped(card: AbstractCard) {
    card.inBottleSoul = false
  }

  override fun actionWhenSelected(card: AbstractCard) {
    card.inBottleSoul = true
  }

  override fun beforeConfigSave(config: Config) {
    if (cardSelected && selectedCard != null) {
      config.bottledSoul.selectedCardID = selectedCard!!.cardID
    }
  }

  override fun afterConfigLoad(config: Config) {
    val soulSave = config.bottledSoul
    if (soulSave.selectedCardID != "") {
      player.masterDeck.group.forEach {
        if (it.cardID == soulSave.selectedCardID) {
          cardSelected = true
          selectedCard = it
          it.inBottleSoul = true
        }
      }
    }
  }

  override fun clearData(config: Config) {
    config.bottledSoul.selectedCardID = ""
  }
}