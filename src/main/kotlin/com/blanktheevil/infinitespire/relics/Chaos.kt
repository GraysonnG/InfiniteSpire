package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.Circlet

class Chaos : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "Chaos".makeID()
    private val IMG = "chaos"
    private val TIER = RelicTier.SHOP
    private val SOUND = LandingSound.MAGICAL
  }

  private val relicsToReplace = mutableListOf<AbstractRelic>()
  private var screenStateAtEquip: AbstractDungeon.CurrentScreen? = null

  override fun onEquip() {
    screenStateAtEquip = AbstractDungeon.screen
    player.relics.apply {
      forEach {
        relicsToReplace.add(it)
        it.onUnequip()
      }
      val s = size
      clear()
      for (i in 0 until s) {
        add(EmptyRelic())
      }
    }
  }

  private fun allRelicsReplaced(): Boolean {
    player.relics.forEach {
      if (it is EmptyRelic) {
        return false
      }
    }
    return true
  }

  private fun getRandomRelic(): AbstractRelic =
    mutableListOf<AbstractRelic>().let {
      it.addAll(RelicLibrary.commonList)
      it.addAll(RelicLibrary.uncommonList)
      it.addAll(RelicLibrary.rareList)
      it.addAll(RelicLibrary.shopList)
      it.addAll(RelicLibrary.bossList)
      it.addAll(RelicLibrary.starterList)
      it.addAll(RelicLibrary.specialList)
      (it.getRandomItem(AbstractDungeon.relicRng) ?: Circlet()).makeCopy()
    }

}