package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.*
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.Circlet

class Chaos : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Chaos".makeID()
    private val IMG = "chaos"
    private val TIER = RelicTier.SHOP
    private val SOUND = LandingSound.MAGICAL
  }

  private var screenStateAtEquip: AbstractDungeon.CurrentScreen? = null
  private var relicsAdded = 0

  override fun onEquip() {
    screenStateAtEquip = AbstractDungeon.screen
    relicsAdded = 0

    for (i in 0 until player.relics.size.minus(1)) {
      player.relics[i].onUnequip()
      player.relics[i] = EmptyRelic()
    }
  }

  override fun update() {
    super.update()
    while (CardCrawlGame.isInARun() && !allRelicsReplaced() && !hasScreenChanged()) {
      getRandomChaosRelic().also {
        it.removeFromPools()
        it.instantObtain(player, relicsAdded, true)
        relicsAdded++
      }
    }
  }

  private fun allRelicsReplaced(): Boolean = player.relics.none { it is EmptyRelic }

  private fun hasScreenChanged(): Boolean = screenStateAtEquip != AbstractDungeon.screen

  private fun getRandomChaosRelic(): AbstractRelic =
    mutableListOf<AbstractRelic>().let {
      it.addAll(allRelics)
      it.removeIf { relic -> relic is Chaos }
      player.relics.forEach { listRelic ->
        it.removeIf { relic -> relic.relicId == listRelic.relicId }
      }
      it.getRandomItem(AbstractDungeon.relicRng).makeCopy()
    }
}