package com.blanktheevil.infinitespire.powers.util

import com.badlogic.gdx.graphics.Texture
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.localization.PowerStrings
import com.megacrit.cardcrawl.powers.AbstractPower

class PowerBuilder(
  val id: String,
  val strings: PowerStrings = languagePack.getPowerStrings(id),
  var img: Texture = Textures.missingTexture,
  var priority: Int = 0,
  var type: AbstractPower.PowerType = AbstractPower.PowerType.BUFF,
  var isTurnBased: Boolean = false,
  var isPostAction: Boolean = false,
  var canGoNegative: Boolean = false
) {
  fun img(path: String): PowerBuilder = this.apply { img = Textures.powers.get(path) }
  fun priority(value: Int): PowerBuilder = this.apply { priority = value }
  fun buff(): PowerBuilder = this.apply { type = AbstractPower.PowerType.BUFF }
  fun debuff(): PowerBuilder = this.apply { type = AbstractPower.PowerType.DEBUFF }
  fun isTurnBased(): PowerBuilder = this.apply { isTurnBased = true }
  fun isPostAction(): PowerBuilder = this.apply { isPostAction = true }
  fun canGoNegative(): PowerBuilder = this.apply { canGoNegative = true }
}