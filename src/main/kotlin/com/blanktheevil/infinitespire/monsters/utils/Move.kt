package com.blanktheevil.infinitespire.monsters.utils

import com.megacrit.cardcrawl.monsters.AbstractMonster

class Move(
  var intent: AbstractMonster.Intent,
  var damage: Int = -1,
  var name: String? = null,
  var multiplier: Int = 0,
  var isMultiDamage: Boolean = false,
  var takeTurn: Move.(AbstractMonster) -> Unit
) {
  companion object {
    private var byteValue: Byte = 0
  }

  private val byte = getNextByte()

  private fun getNextByte(): Byte {
    return byteValue++
  }

  fun getByte(): Byte = byte

  fun modify(
    intent: AbstractMonster.Intent = this.intent,
    damage: Int = this.damage,
    name: String? = this.name,
    multiplier: Int = this.multiplier,
    isMultiDamage: Boolean = this.isMultiDamage,
    takeTurn: Move.(AbstractMonster) -> Unit = this.takeTurn
  ) {
    this.intent = intent
    this.damage = damage
    this.name = name
    this.multiplier = multiplier
    this.isMultiDamage = isMultiDamage
    this.takeTurn = takeTurn
  }
}