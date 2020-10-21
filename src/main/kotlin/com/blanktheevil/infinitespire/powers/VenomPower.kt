package com.blanktheevil.infinitespire.powers

import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.interfaces.hooks.OnMonsterDeathInterface
import com.blanktheevil.infinitespire.textures.Textures
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.rooms.AbstractRoom

class VenomPower(
  owner: AbstractCreature,
  val source: AbstractCreature,
  amount: Int
) :
  TwoAmountPower(),
  HealthBarRenderPower,
  NonStackablePower,
  OnMonsterDeathInterface {
  companion object {
    val powerID = "VenomPower".makeID()
    private val strings = languagePack.getPowerStrings(powerID)
  }

  init {
    subscribe()
    this.owner = owner
    this.amount = amount
    this.amount2 = 0
    this.type = PowerType.BUFF
    this.name = strings.NAME
    this.ID = powerID
    this.img = Textures.powers.get("venom.png")
    this.updateDescription()
  }

  override fun updateDescription() {
    description = StringBuilder(strings.DESCRIPTIONS[0])
      .append(amount)
      .append(strings.DESCRIPTIONS[1])
      .append(source.name)
      .append(strings.DESCRIPTIONS[2])
      .append(amount2)
      .append(strings.DESCRIPTIONS[3])
      .toString()
  }

  override fun atEndOfTurn(isPlayer: Boolean) {
    if (isPlayer && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead() && this.amount > 0) {
      flashWithoutSound()
      addToBot(LoseHPAction(owner, owner, amount))
      amount2 += amount
      amount--
      updateDescription()
    }
  }

  override fun onMonsterDeath(monster: AbstractMonster) {
    if (source == monster) {
      addToBot(HealAction(owner, owner, this.amount2))
      this.amount = 0
      this.amount2 = 0
    }
  }

  override fun update(slot: Int) {
    if (amount > 0) {
      capAmount()
    }
    super.update(slot)
  }

  private fun capAmount() {
    when {
      owner.currentHealth == 1 -> {
        amount = 0
      }
      amount >= owner.currentHealth -> {
        amount = owner.currentHealth.minus(1)
      }
    }
    updateDescription()
  }

  override fun isStackable(power: AbstractPower): Boolean = (power is VenomPower && power.source == this.source)
  override fun getColor(): Color = Color.valueOf("#24007f")
  override fun getHealthBarAmount(): Int = amount
}