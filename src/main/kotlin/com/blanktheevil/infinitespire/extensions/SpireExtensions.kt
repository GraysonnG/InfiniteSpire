@file:Suppress("unused")

package com.blanktheevil.infinitespire.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.InfiniteSpire
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.localization.LocalizedStrings
import com.megacrit.cardcrawl.map.MapEdge
import com.megacrit.cardcrawl.map.MapRoomNode
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect
import java.util.*

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

val TextureAtlas.AtlasRegion.width
  get() = this.packedWidth.toFloat()
val TextureAtlas.AtlasRegion.height
  get() = this.packedHeight.toFloat()
val TextureAtlas.AtlasRegion.halfWidth
  get() = this.packedWidth.div(2f)
val TextureAtlas.AtlasRegion.halfHeight
  get() = this.packedHeight.div(2f)

fun AbstractCreature.applyPower(
  power: AbstractPower,
  amount: Int = power.amount,
  source: AbstractCreature = this,
  top: Boolean = false
) {
  with(ApplyPowerAction(
    this,
    source,
    power,
    amount
  )) {
    if (!top) {
      actionManager.addToBottom(this)
    } else {
      actionManager.addToTop(this)
    }
  }
}

fun AbstractCreature.applyHeal(
  amount: Int,
  source: AbstractCreature = this,
  top: Boolean = false
) {
  if (!top) {
    actionManager.addToBottom(
      HealAction(this, source, amount)
    )
  } else {
    actionManager.addToTop(
      HealAction(this, source, amount)
    )
  }
}

fun AbstractRelic.removeFromPools() {
  AbstractDungeon.commonRelicPool.remove(this.relicId)
  AbstractDungeon.uncommonRelicPool.remove(this.relicId)
  AbstractDungeon.rareRelicPool.remove(this.relicId)
  AbstractDungeon.shopRelicPool.remove(this.relicId)
  AbstractDungeon.bossRelicPool.remove(this.relicId)
}

fun Hitbox.move(vector2: Vector2) {
  this.x = vector2.x
  this.y = vector2.y
  this.cX = this.x.plus(this.width.div(2f))
  this.cY = this.y.plus(this.height.div(2f))
}

fun <T> List<T>.getRandomItem(random: Random = Random()): T =
  if (this.isNotEmpty()) this[random.random(this.lastIndex)] else throw NullPointerException()

fun <T> List<T>.forEachIndexedReversed(action: (index: Int, T) -> Unit) {
  for (i in lastIndex..0) {
    action.invoke(i, this.elementAt(i))
  }
}

fun MapRoomNode.connectToNode(dst: MapRoomNode) {
  addEdge(
    MapEdge(
      x,
      y,
      offsetX,
      offsetY,
      dst.x,
      dst.y,
      dst.offsetX,
      dst.offsetY,
      false
    )
  )
}

fun endPlayerTurn() {
  actionManager.cardQueue.clear()
  player.limbo.group.forEach {
    AbstractDungeon.effectList.add(ExhaustCardEffect(it))
  }
  player.limbo.group.clear()
  player.releaseCard()
  AbstractDungeon.overlayMenu.endTurnButton.disable(true)
}

fun addToTop(action: AbstractGameAction) = AbstractDungeon.actionManager.addToTop(action)
fun addToBot(action: AbstractGameAction) = AbstractDungeon.actionManager.addToBottom(action)

fun AbstractCreature?.dealDamage(
  target: AbstractCreature,
  amount: Int = 0,
  effect: AbstractGameAction.AttackEffect = AbstractGameAction.AttackEffect.NONE,
  type: DamageType = DamageType.NORMAL,
  source: AbstractCreature? = this,
  toTop: Boolean = false
) {
  if (toTop) {
    addToTop(
      DamageAction(target, DamageInfo(
        source, amount, type
      ), effect)
    )
  } else {
    addToBot(
      DamageAction(target, DamageInfo(
        source, amount, type
      ), effect)
    )
  }
}

fun AbstractCreature?.dealThornsDamage(
  target: AbstractCreature,
  amount: Int = 0,
  effect: AbstractGameAction.AttackEffect = AbstractGameAction.AttackEffect.NONE,
  source: AbstractCreature? = this,
  toTop: Boolean = false
) = dealDamage(target, amount, effect, DamageType.THORNS, source, toTop)

fun AbstractCreature?.dealHPDamage(
  target: AbstractCreature,
  amount: Int = 0,
  effect: AbstractGameAction.AttackEffect = AbstractGameAction.AttackEffect.NONE,
  source: AbstractCreature? = this,
  toTop: Boolean = false
) = dealDamage(target, amount, effect, DamageType.HP_LOSS, source, toTop)

val allRelics: List<AbstractRelic>
  get() = mutableListOf<AbstractRelic>().also {
    it.addAll(RelicLibrary.commonList)
    it.addAll(RelicLibrary.uncommonList)
    it.addAll(RelicLibrary.rareList)
    it.addAll(RelicLibrary.shopList)
    it.addAll(RelicLibrary.bossList)
    it.addAll(RelicLibrary.starterList)
    it.addAll(RelicLibrary.specialList)
  }

fun doNothing() {}

val dungeon: AbstractDungeon get() = CardCrawlGame.dungeon
val currentRoom: AbstractRoom? get() = AbstractDungeon.getCurrRoom()
val player: AbstractPlayer get() = AbstractDungeon.player
val actionManager: GameActionManager get() = AbstractDungeon.actionManager
val effectsQueue: ArrayList<AbstractGameEffect> get() = AbstractDungeon.effectsQueue
val scale: Float get() = Settings.scale
val languagePack: LocalizedStrings get() = CardCrawlGame.languagePack
val deltaTime: Float get() = Gdx.graphics.rawDeltaTime