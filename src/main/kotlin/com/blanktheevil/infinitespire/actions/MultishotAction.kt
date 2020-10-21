package com.blanktheevil.infinitespire.actions

import com.blanktheevil.infinitespire.vfx.MultishotVfx
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class MultishotAction(
  source: AbstractCreature,
  target: AbstractCreature,
  private val info: DamageInfo,
  private val amountOfHits: Int,
) : AbstractGameAction() {
  private var hitsDealt = 0
  private lateinit var vfx: MultishotVfx
  private var firstFrame = true

  init {
    this.source = source
    this.target = target
    this.duration = 999f
  }

  override fun update() {
    if (hitsDealt == amountOfHits) {
      this.isDone = true
    } else {
      if (firstFrame) {
        firstFrame = false
        vfx = MultishotVfx(
          source.hb,
          target.hb,
          amountOfHits * 2
        )
        AbstractDungeon.effectList.add(vfx)
      }

      if (vfx.shouldStartDamageAction() && !target.isDead && !target.isDying) {
        hitsDealt++
        this.target.damage(this.info)
        CardCrawlGame.sound.play("ATTACK_HEAVY")
      }

      if (vfx.isDone) {
        this.isDone
      }
    }
  }
}