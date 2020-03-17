package com.blanktheevil.infinitespire.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.extensions.clamp
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.models.QuestLog
import com.blanktheevil.infinitespire.toppanel.QuestLogButton
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputActionSet

class QuestLogScreen(private val questLog: QuestLog, private val button: QuestLogButton) : SpireElement {
  companion object {
    const val SCREEN_BG_OPEN_TARGET = 0.65f
  }

  private var show = false
  private val bgColor = Color.BLACK.cpy().also { it.a = 0f }
  private var bgColorInterpProgress = 0f

  fun init() {
    TODO("init")
  }

  override fun update() {
    if (show) {

      questLog.parallelStream()
        .forEach { it.update() }
      questLog.removeIf { it.isDone() }

      if (InputActionSet.cancel.isJustPressed) {
        toggle()
      }
    }

    updateBgAlpha()
  }

  private fun updateBgAlpha() {
    bgColorInterpProgress += if (show) Gdx.graphics.rawDeltaTime.times(3) else -Gdx.graphics.rawDeltaTime.times(3)
    bgColorInterpProgress = bgColorInterpProgress.clamp(0f, 1f)

    bgColor.a = Interpolation.fade.apply(0f, SCREEN_BG_OPEN_TARGET, bgColorInterpProgress)
  }

  override fun render(sb: SpriteBatch) {
    renderBg(sb)

    if (show) {
      FontHelper.renderFontCentered(
        sb,
        FontHelper.buttonLabelFont,
        "Quest Log Open",
        Settings.WIDTH / 2f,
        Settings.HEIGHT / 2f,
        Color.WHITE.cpy()
      )
    }

    renderQuestLogIcon(sb)
  }

  private fun renderBg(sb: SpriteBatch) {
    with(sb) {
      color = this@QuestLogScreen.bgColor
      draw(
        ImageMaster.WHITE_SQUARE_IMG,
        0f,
        0f,
        Settings.WIDTH.toFloat(),
        Settings.HEIGHT.toFloat()
      )
    }
  }

  private fun renderQuestLogIcon(sb: SpriteBatch) {
    button.render(sb)
  }

  fun toggle() {
    show = !show
    AbstractDungeon.isScreenUp = show
  }

  fun isOpen(): Boolean {
    return show
  }
}