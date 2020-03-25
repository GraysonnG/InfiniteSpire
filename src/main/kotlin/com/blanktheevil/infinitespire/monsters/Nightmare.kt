package com.blanktheevil.infinitespire.monsters

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.models.SaveData
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.TintEffect
import java.util.*
import kotlin.math.round
import kotlin.math.sin

class Nightmare(val isAlpha: Boolean = false) :
    AbstractMonster(
      NAME,
      ID,
      MAX_HP,
      HB_X,
      HB_Y,
      HB_W,
      HB_H,
      IMG_URL
    ), Savable {
  companion object {
    val ID = "Nightmare".makeID()
    private val ALPHA_ID = "NightmareAlpha".makeID()
    private val STRINGS = languagePack.getMonsterStrings(ID)
    private val ALPHA_STRINGS = languagePack.getMonsterStrings(ALPHA_ID)
    private val NAME = STRINGS.NAME
    private val MOVES = STRINGS.MOVES
    private val DIALOG = STRINGS.DIALOG
    private const val MAX_HP = 200
    private const val HB_X = 0f
    private const val HB_Y = -10f
    private const val HB_W = 160f
    private const val HB_H = 300f
    private const val IMG_URL = ""
    private var timesDefeated = 0
    private var timesNotReceivedBlackCard = 0
  }

  private var attackDmg = 5
  private var blockAmount: Int
  private var debuffDmg: Int
  private var slamDmg: Int
  private var effectTime = 0f

  var effectAmount: Int

  init {
    img = Textures.monsters.get("nightmare/nightmare-1.png")
    var hpAmount = MAX_HP
    if (isAlpha) {
      this.name = ALPHA_STRINGS.NAME
      hpAmount += 100
    }

    if (CardCrawlGame.playerName == "fiiiiilth" || Math.random() < 0.01) {
      this.name = "Niiiiightmare"
    }

    this.debuffDmg = 10

    if (AbstractDungeon.bossCount > 1) {
      hpAmount = hpAmount.times(1.5f).toInt()
      blockAmount = 30
      effectAmount = 4
      slamDmg = 25
    } else {
      blockAmount = 20
      effectAmount = 2
      slamDmg = 15
    }

    if (Settings.hasSapphireKey) hpAmount += 50

    this.setHp(hpAmount)
    this.damage.add(DamageInfo(this, attackDmg))
    this.damage.add(DamageInfo(this, slamDmg))
    this.damage.add(DamageInfo(this, debuffDmg))
    this.tint = TintEffect()
    subscribe()
  }

  override fun die() {
    super.die()
    timesDefeated++
    timesNotReceivedBlackCard++
  }

  override fun render(sb: SpriteBatch) {
    if (isAlpha) {
      val portalTexture = Textures.screen.get("portal.png").asAtlasRegion()
      sb.color = Color.WHITE.cpy()
      sb.draw(
        portalTexture,
        0f,
        0f,
        hb.cX - portalTexture.packedWidth.div(2),
        hb.cY - portalTexture.packedHeight.div(2),
        portalTexture.packedWidth.toFloat(),
        portalTexture.packedHeight.toFloat(),
        1.5f,
        1.6f,
        effectTime
      )
    }
    this.tint.changeColor(Color.WHITE.cpy())
    super.render(sb)
  }

  override fun update() {
    super.update()
    effectTime += Gdx.graphics.rawDeltaTime
    drawY += sin(effectTime).div(10.scale())

    if (round(effectTime * 10).toInt() % 3 == 0) {
      val rand = Random()
      val num1 = rand.nextInt(3)
      val num2 = rand.nextInt(3)
      if (num1 == 0) {
        this.img = Textures.monsters.get(
          "nightmare/nightmare-${num2 + 1}.png"
        )
      }
    }

    powers.stream()
      .filter { it.ID == "" }
      .distinct()
      .forEach { it.updateDescription() }
  }

  fun triggerRealityShiftAttack() {
    this.effectAmount++
    if (AbstractDungeon.overlayMenu.endTurnButton.enabled) {
      // do the thing
    }
  }

  override fun getMove(p0: Int) {
    TODO("Not yet implemented")
  }

  override fun takeTurn() {
    TODO("Not yet implemented")
  }

  override fun beforeConfigSave(saveData: SaveData) {
    saveData.nightmareData.timesDefeated = timesDefeated
    saveData.nightmareData.timesNotReceivedBlackCard = timesNotReceivedBlackCard
  }

  override fun afterConfigLoad(saveData: SaveData) {
    timesDefeated = saveData.nightmareData.timesDefeated
    timesNotReceivedBlackCard = saveData.nightmareData.timesNotReceivedBlackCard
  }

  override fun clearData(saveData: SaveData) {
    timesDefeated = 0
    timesNotReceivedBlackCard = 0
  }
}