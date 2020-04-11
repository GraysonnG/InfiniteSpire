package com.blanktheevil.infinitespire.screens

import basemod.BaseMod
import basemod.interfaces.PostUpdateSubscriber
import basemod.interfaces.RenderSubscriber
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Bezier
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.*
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.GameCursor
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper
import java.util.*

class TargetMonsterScreen : Screen<TargetMonsterScreen>(), RenderSubscriber, PostUpdateSubscriber {

  var hoveredCreature: AbstractCreature? = null
  var startPoint = Vector2(0f, 0f)
  var controlPoint = Vector2(0f, 0f)
  var endPoint = Vector2(0f, 0f)
  var arrowScale = 0f
  var arrowScaleTimer = 0f
  var points = ArrayList<Vector2>()

  init {
    BaseMod.subscribe(this)
    for (i in 0 until 20) {
      points.add(Vector2())
    }
  }

  override fun receivePostUpdate() {
    super.update()
    if (!show) return
    update()
  }

  override fun updateScreen() {
    updateTargetMode()
  }

  private fun updateTargetMode() {
    this.hoveredCreature = null

    if (AbstractDungeon.getMonsters().monsters.isEmpty() ||
      AbstractDungeon.getMonsters().areMonstersBasicallyDead() ||
      player.isDying ||
      player.isDead
    ) close()

    this.startPoint = Vector2(Settings.WIDTH.div(2f), Settings.HEIGHT.div(2f))
    this.endPoint = Vector2(InputHelper.mX.toFloat(), InputHelper.mY.toFloat())
    this.controlPoint = Vector2(endPoint.x.minus(startPoint.x).div(2f), InputHelper.mY.toFloat())

    AbstractDungeon.getMonsters().monsters.forEach {
      if (it.hb.hovered && !it.isDying) {
        this.hoveredCreature = it
      }
    }

    if (player.hb.hovered) {
      this.hoveredCreature = player
    }

    if (InputHelper.justClickedLeft) {
      InputHelper.justClickedLeft = false
      if (hoveredCreature != null) {
        callback.invoke(this)
        close()
      }
    }
  }

  override fun close() {
    AbstractDungeon.isScreenUp = false
    GameCursor.hidden = false
    show = false
  }

  public override fun open(callback: (screen: TargetMonsterScreen) -> Unit) {
    this.callback = callback
    AbstractDungeon.isScreenUp = true
    hoveredCreature = null
    show = true
    GameCursor.hidden = true
    points.clear()
    for (i in 0 until 20) {
      points.add(Vector2())
    }
  }

  override fun receiveRender(sb: SpriteBatch) {
    if (!show) return
    render(sb)
  }

  override fun renderScreen(sb: SpriteBatch) {
    renderTargetingUi(sb)
    if (this.hoveredCreature != null) {
      this.hoveredCreature!!.renderReticle(sb)
    }
  }

  private fun renderTargetingUi(sb: SpriteBatch) {
    if (this.hoveredCreature == null) {
      this.arrowScale = Settings.scale
      this.arrowScaleTimer = 0f
      sb.color = Color(1f, 1f, 1f, 1f)
    } else {
      this.arrowScaleTimer += deltaTime
      arrowScaleTimer = arrowScaleTimer.clamp(0f, 1f)

      this.arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2f, this.arrowScaleTimer)
      sb.color = Color(1f, 0.2f, 0.3f, 1f)
    }
    val tmp = Vector2(this.controlPoint.x - InputHelper.mX, this.controlPoint.y - InputHelper.mY)
    tmp.nor()

    drawCurvedLine(sb, startPoint, endPoint, controlPoint)

    sb.draw(
      ImageMaster.TARGET_UI_ARROW,
      InputHelper.mX.minus(128f),
      InputHelper.mY.minus(128f),
      128f,
      128f,
      256f,
      256f,
      arrowScale,
      arrowScale,
      tmp.angle().plus(90f),
      0,
      0,
      256,
      256,
      false,
      false
    )
  }

  private fun drawCurvedLine(sb: SpriteBatch, start: Vector2, end: Vector2, control: Vector2) {
    var radius = 7f.scale()

    for (i in points.indices) {
      val point = points[i]

      points[i] = Bezier.quadratic(point, i.div(20f), start, control, end, Vector2()) as Vector2
      radius += 0.4f.scale()

      val angle = if (i != 0) {
        val temp = Vector2(points[i - 1].x.minus(point.x), points[i - 1].y.minus(point.y))
        temp.nor().angle() + 90f
      } else {
        val temp = Vector2(controlPoint.x.minus(point.x), controlPoint.y.minus(point.y))
        temp.nor().angle() + 270f
      }

      sb.draw(
        ImageMaster.TARGET_UI_CIRCLE,
        point.x - 64f,
        point.y - 64f,
        64f,
        64f,
        128f,
        128f,
        radius.div(18f),
        radius.div(18f),
        angle,
        0,
        0,
        128,
        128,
        false,
        false
      )
    }
  }
}