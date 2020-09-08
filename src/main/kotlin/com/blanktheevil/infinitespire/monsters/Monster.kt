package com.blanktheevil.infinitespire.monsters

import com.blanktheevil.infinitespire.monsters.utils.Move
import com.blanktheevil.infinitespire.monsters.utils.attackIntents
import com.megacrit.cardcrawl.actions.common.RollMoveAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.monsters.AbstractMonster

abstract class Monster(
  name: String,
  id: String,
  maxHealth: Int,
  hb_x: Float,
  hb_y: Float,
  hb_w: Float,
  hb_h: Float,
  imgUrl: String? = null,
  offsetX: Float = 0f,
  offsetY: Float = 0f,
  ignoreBlights: Boolean = true) :
  AbstractMonster(
    name,
    id,
    maxHealth,
    hb_x,
    hb_y,
    hb_w,
    hb_h,
    imgUrl,
    offsetX,
    offsetY,
    ignoreBlights) {
  private var moveByteIndex: Byte = 0

  private val moves = mutableListOf<Move>()
  fun registerMove(move: Move) {
    move.setByte(moveByteIndex++)
    moves.add(move)

    if (move.intent in attackIntents) {
      this.damage.add(DamageInfo(this, move.damage))
    }
  }

  override fun takeTurn() {
    moves.first { it.getByte() == nextMove }.also {
      it.takeTurn.invoke(it, this)
    }

    addToBot(RollMoveAction(this))
  }
}