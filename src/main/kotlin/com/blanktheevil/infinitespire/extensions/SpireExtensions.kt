@file:Suppress("unused")

package com.blanktheevil.infinitespire.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.blanktheevil.infinitespire.InfiniteSpire
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.rooms.AbstractRoom

fun Float.scale(): Float = this * Settings.scale
fun Int.scale(): Float = this * Settings.scale
fun Float.clamp(min: Float, max: Float): Float = this.coerceIn(min, max)

fun Int.deltaTime(): Float = this * Gdx.graphics.rawDeltaTime
fun Float.deltaTime(): Float = this * Gdx.graphics.rawDeltaTime
fun Double.deltaTime(): Float = this.toFloat() * Gdx.graphics.rawDeltaTime

fun String.makeID(): String = "${InfiniteSpire.modid}:$this"

fun Texture.asAtlasRegion(
  x: Int = 0,
  y: Int = 0,
  w: Int = this.width,
  h: Int = this.height
): TextureAtlas.AtlasRegion =
  TextureAtlas.AtlasRegion(this, x, y, w, h)

fun Hitbox.leftClicked(): Boolean = InputHelper.justClickedLeft && this.hovered
fun Hitbox.rightClicked(): Boolean = InputHelper.justClickedRight && this.hovered


fun SpriteBatch.additiveMode() = this.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE)
fun SpriteBatch.normalMode() = this.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)

fun AbstractCreature.applyPower(power: AbstractPower, amount: Int = 0, source: AbstractCreature = this, top: Boolean = false) {
  if (!top) {
    actionManager.addToBottom(
      ApplyPowerAction(
        this,
        source,
        power,
        amount
      )
    )
  } else {
    actionManager.addToTop(
      ApplyPowerAction(
        this,
        source,
        power,
        amount
      )
    )
  }
}

fun <T> List<T>.getRandomItem(random: Random = Random()): T? {
  return if (this.isNotEmpty()) {
    this[random.random(this.size - 1)]
  } else null
}

fun doNothing() {}

val dungeon: AbstractDungeon get() = CardCrawlGame.dungeon
val currentRoom: AbstractRoom? get() = AbstractDungeon.getCurrRoom()
val player: AbstractPlayer get() = AbstractDungeon.player
val actionManager: GameActionManager get() = AbstractDungeon.actionManager
val scale: Float get() = Settings.scale