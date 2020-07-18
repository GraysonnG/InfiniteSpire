package com.blanktheevil.infinitespire.actions

import com.blanktheevil.infinitespire.extensions.dealDamage
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.vfx.BlackStrikeLargeVfx
import com.blanktheevil.infinitespire.vfx.BlackStrikeSmallVfx
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import kotlin.math.ceil

class BlackStrikeDamageAction(target: AbstractCreature, damage: Int) : AbstractGameAction()  {
  companion object {
    private const val DELAY = 0.5f
  }

  private val hb: Hitbox = target.hb
  private val halfDamage = ceil(damage.toFloat().div(2f)).toInt()

  init {
    duration = Settings.ACTION_DUR_FAST
    this.source = player
    this.target = target
  }

  override fun update() {
    if (duration == Settings.ACTION_DUR_FAST) {
      for (j in 0 until halfDamage) {
        player.dealDamage(target, 1, AttackEffect.NONE)
        addToBot(object : AbstractGameAction() {
          override fun update() {
            if (!this@BlackStrikeDamageAction.target.isDying && !this@BlackStrikeDamageAction.target.isDead) {
              AbstractDungeon.effectList.add(
                BlackStrikeSmallVfx(hb)
              )
            }
            this.isDone = true
          }
        })
      }
      addToBot(object : AbstractGameAction() {
        override fun update() {
          if (!this@BlackStrikeDamageAction.target.isDying && !this@BlackStrikeDamageAction.target.isDead) {
            AbstractDungeon.effectList.add(
              BlackStrikeLargeVfx(hb)
            )
            addToBot(Wait(DELAY))
            player.dealDamage(this@BlackStrikeDamageAction.target, halfDamage, AttackEffect.NONE)
          }
          this.isDone = true
        }
      })
    }

    tickDuration()
  }
}