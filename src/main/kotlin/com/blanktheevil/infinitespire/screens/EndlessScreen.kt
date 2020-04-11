package com.blanktheevil.infinitespire.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.ui.ColoredButton
import com.blanktheevil.infinitespire.vfx.DarkBgEffect
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.vfx.BorderFlashEffect

class EndlessScreen : Screen<EndlessScreen>() {
  companion object {
    var shouldPrompt = true
    private val strings = languagePack.getUIString("EndlessScreen".makeID()).TEXT
  }

  private val darkBgEffect = DarkBgEffect(this)

  private val rightText = strings[2]
  private val leftText = strings[3]
  private var promptText = strings[0]

  init {
    shouldPrompt = InfiniteSpire.saveData.shouldPromptEndless
    uiElements.add(
      ColoredButton(text = rightText,
        x = Settings.WIDTH.div(2f) - 150f.scale(),
        y = Settings.HEIGHT.div(2f) - 200f.scale(),
        color = Color.SKY.cpy()) {
        if (!Settings.isEndless) {
          CardCrawlGame.sound.play("UNLOCK_PING")
          AbstractDungeon.topLevelEffects.add(
            BorderFlashEffect(InfiniteSpire.PURPLE.cpy())
          )
          Settings.isEndless = true
          AbstractDungeon.topPanel.setPlayerName()
        }

        this@EndlessScreen.close()
      }
    )

    uiElements.add(
      ColoredButton(text = leftText,
        x = Settings.WIDTH.div(2f) + 150f.scale(),
        y = Settings.HEIGHT.div(2f) - 200f.scale(),
        color = Color.RED.cpy()) {
        if(Settings.isEndless) {
          Settings.isEndless = false
          AbstractDungeon.topPanel.setPlayerName()
          CardCrawlGame.sound.play("CARD_BURN")
        } else {
          shouldPrompt = false
        }

        this@EndlessScreen.close()
      }
    )
  }

  public override fun open(callback: (screen: EndlessScreen) -> Unit) {
    this.callback = callback
    show = true
    promptText = if (Settings.isEndless) {
      strings[1]
    } else {
      strings[0]
    }

    AbstractDungeon.isScreenUp = show
  }

  override fun close() {
    show = false
    callback.invoke(this)
    AbstractDungeon.isScreenUp = true
    AbstractDungeon.screen = AbstractDungeon.CurrentScreen.MAP
    AbstractDungeon.dungeonMapScreen.open(false)
  }

  override fun updateScreen() {
    if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE && show) {
      AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE
      AbstractDungeon.dungeonMapScreen.closeInstantly()
    }

    darkBgEffect.update()
  }

  override fun renderScreen(sb: SpriteBatch) {
    FontHelper.renderFontCentered(
      sb,
      FontHelper.dungeonTitleFont,
      promptText,
      Settings.WIDTH.div(2f),
      Settings.HEIGHT.div(2f).plus(100f.scale()),
      Color.WHITE.cpy()
    )

    darkBgEffect.render(sb)
  }
}