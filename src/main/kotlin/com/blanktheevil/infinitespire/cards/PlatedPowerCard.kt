package com.blanktheevil.infinitespire.cards

import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.powers.PlatedPower
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.*
import com.megacrit.cardcrawl.powers.watcher.MantraPower

class PlatedPowerCard : BlackCard(ID, IMG) {
  companion object {
    val ID = "Oblivion".makeID()
    private val IMG = Textures.cards.getString("beta.png")
  }

  override fun update() {
    if (!InfiniteSpire.powerSelectScreen.show && !InfiniteSpire.targetMonsterScreen.show) {
      super.update()
    }
  }

  override fun use(p: AbstractPlayer?, m: AbstractMonster?) {
    InfiniteSpire.powerSelectScreen.open(
      listOf(
        StrengthPower(null, 1),
        EnvenomPower(null, 1),
        ThornsPower(null, 1),
        MantraPower(null, 1),
        VulnerablePower(null, 1, false),
        WeakPower(null, 1, false),
        PoisonPower(null, player, 1),
        LockOnPower(null, 1)
      )
    ) { powerScreen ->
      InfiniteSpire.targetMonsterScreen.open() { monsterSelector ->
        val power = powerScreen.selectedPower!!
        power.owner = monsterSelector.hoveredCreature!!
        monsterSelector.hoveredCreature!!.applyPower(
          PlatedPower(power, 5)
        )
        powerScreen.selectedPower = null
      }
    }
  }
}