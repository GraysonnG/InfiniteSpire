@file:Suppress("unused")

package com.blanktheevil.infinitespire.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.input.InputHelper

fun Float.scale(): Float = this * Settings.scale
fun Int.scale(): Float = this * Settings.scale
fun Float.clamp(min: Float, max: Float): Float = this.coerceIn(min, max)

fun Int.deltaTime(): Float = this * Gdx.graphics.rawDeltaTime
fun Float.deltaTime(): Float = this * Gdx.graphics.rawDeltaTime
fun Double.deltaTime(): Float = this.toFloat() * Gdx.graphics.rawDeltaTime

fun String.makeID(): String = "infinitespire:$this"

fun Texture.asAtlasRegion(): TextureAtlas.AtlasRegion =
  TextureAtlas.AtlasRegion(this, 0, 0, this.width, this.height)

fun Hitbox.leftClicked(): Boolean = InputHelper.justClickedLeft && this.hovered
fun Hitbox.rightClicked(): Boolean = InputHelper.justClickedRight && this.hovered


fun SpriteBatch.additiveMode() = this.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE)
fun SpriteBatch.normalMode() = this.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA)


val dungeon: AbstractDungeon get() = CardCrawlGame.dungeon
val player: AbstractPlayer get() = AbstractDungeon.player
val actionManager: GameActionManager get() = AbstractDungeon.actionManager