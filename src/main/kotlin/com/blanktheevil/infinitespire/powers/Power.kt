package com.blanktheevil.infinitespire.powers

import basemod.interfaces.CloneablePowerInterface
import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower

abstract class Power(
  owner: AbstractCreature,
  amount: Int,
  id: String,
  img: Texture,
  priority: Int,
  type: PowerType = PowerType.BUFF,
  isTurnBased: Boolean = false,
  isPostAction: Boolean = false,
  canGoNegative: Boolean = false,
  val strings: PowerStrings = languagePack.getPowerStrings(id)
) : AbstractPower(), CloneablePowerInterface {
  constructor(owner: AbstractCreature, amount: Int, builder: PowerBuilder): this(
    owner,
    amount,
    builder.id,
    builder.img,
    builder.priority,
    builder.type,
    builder.isTurnBased,
    builder.isPostAction,
    builder.canGoNegative,
    builder.strings
  )

  init {
    this.owner = owner
    this.amount = amount
    this.name = strings.NAME
    this.ID = id
    this.img = img
    this.type = type
    this.priority = priority
    this.isTurnBased = isTurnBased
    this.isPostActionPower = isPostAction
    this.canGoNegative = canGoNegative
    this.updateDescription()
  }

  abstract fun updateDesc()

  override fun updateDescription() {
    updateDesc()
  }
}