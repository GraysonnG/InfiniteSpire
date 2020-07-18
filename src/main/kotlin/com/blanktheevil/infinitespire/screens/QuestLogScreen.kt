package com.blanktheevil.infinitespire.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.interfaces.QuestLogCloseInterface
import com.blanktheevil.infinitespire.models.QuestLog
import com.blanktheevil.infinitespire.toppanel.QuestLogButton
import com.blanktheevil.infinitespire.vfx.DarkBgEffect
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.input.InputActionSet

@Deprecated("Remove Before Release")
class QuestLogScreen(private val questLog: QuestLog, private val button: QuestLogButton) : Screen<QuestLogScreen>() {
  private val darkBgEffect = DarkBgEffect(this)

  fun init() {
    TODO("init")
  }

  override fun updateScreen() {
    if (show) {

      questLog.parallelStream()
        .forEach { it.update() }
      questLog.removeIf { it.complete }

      if (InputActionSet.cancel.isJustPressed) {
        toggle()
      }
    }

    darkBgEffect.update()
  }

  override fun renderScreen(sb: SpriteBatch) {
    darkBgEffect.render(sb)

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

  private fun renderQuestLogIcon(sb: SpriteBatch) {
    button.render(sb)
  }

  fun toggle() {
    show = !show
    AbstractDungeon.isScreenUp = show
    if (!show) {
      QuestLogCloseInterface.subscribers.forEach {
        it.onQuestLogClose(this.questLog)
      }
    }
  }

  override fun open(callback: (screen: QuestLogScreen) -> Unit) {
    this.callback = callback
    toggle()
  }

  override fun close() {
    toggle()
  }

  fun isOpen(): Boolean {
    return show
  }
}