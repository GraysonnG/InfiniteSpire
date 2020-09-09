package com.blanktheevil.infinitespire.monsters

import actlikeit.savefields.BehindTheScenesActNum
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.interfaces.Savable
import com.blanktheevil.infinitespire.models.SaveData
import com.blanktheevil.infinitespire.monsters.utils.Move
import com.blanktheevil.infinitespire.monsters.utils.setMove
import com.blanktheevil.infinitespire.powers.RealityShiftPower
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.*
import com.blanktheevil.infinitespire.vfx.BlackCardVfx
import com.blanktheevil.infinitespire.vfx.particles.BlackCardParticle
import com.blanktheevil.infinitespire.vfx.particlesystems.BlackCardParticleSystem
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.actions.common.RollMoveAction
import com.megacrit.cardcrawl.blights.Spear
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.powers.WeakPower
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect
import java.util.*
import kotlin.math.roundToInt

class Nightmare :
    Monster(
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
    private val STRINGS = languagePack.getMonsterStrings(ID)
    private val NAME = STRINGS.NAME
    private val MOVES = STRINGS.MOVES
    private const val MAX_HP = 550
    private const val MIN_HP = 520
    private const val ASC_HP = 50
    private const val BLOCK_ON_TRIGGER = 35
    private const val HB_X = 0f
    private const val HB_Y = -10f
    private const val HB_W = 160f
    private const val HB_H = 300f
    private const val IMG_URL = ""
    private var timesDefeated = 0
    private var timesNotReceivedBlackCard = 0
  }

  var attackAmount = 5
  private var attackDamage = 5
  private var slamDamage = 30
  private var debuffAmount = 3
  private var buffAmount = 1
  private var blockAmount = 35
  private var realityShiftAmount = 50
  private var firstTurn = true
  private var spriteTimer = 0f
  private var sprintIndex = 0
  private var particleSystem = BlackCardParticleSystem(
    8,
    {
      val point = VFXManager.generateRandomPointAlongEdgeOfCircle(
        hb.cX,
        hb.cY,
        200f.scale()
      )

      BlackCardParticle(
        point,
        1f,
        true,
        VFXManager.getVelocityToPoint(
          Vector2(hb.cX, hb.cY),
          point
        ).scl(0.75f.div(scale))
      )
    },
    { !isDying && !isDead }
  )
  private val realityShiftMove = Move(Intent.MAGIC, name = MOVES[0]) {
    it.applyPower(
      RealityShiftPower(it as Nightmare, 50)
    )
  }
  private val multiStrikeMove = Move(Intent.ATTACK, attackDamage, MOVES[1], attackAmount, true) {
    addToBot(
      AnimateFastAttackAction(it)
    )
    for (i in 0 until this.multiplier) {
      addToBot(DamageAction(
        player,
        it.damage[this.getByte().toInt()],
        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
        true
      ))
    }
  }
  private val debuffBlockMove = Move(Intent.DEFEND_DEBUFF, name = MOVES[3]) {
    addToBot(GainBlockAction(it, blockAmount))
      for (i in 0 until debuffAmount) {
        player.applyPower(
          WeakPower(player, 1, true),
          source = it
        )
        player.applyPower(
          WeakPower(player, 1, true),
          source = it
        )
      }
  }
  private val slamMove = Move(Intent.ATTACK_BUFF, slamDamage, name = MOVES[2]) {
    addToBot(DamageAction(
          player,
          it.damage[this.getByte().toInt()],
          AbstractGameAction.AttackEffect.BLUNT_HEAVY
        ))
        applyPower(
          StrengthPower(it, buffAmount)
        )
  }

  init {
    type = EnemyType.BOSS

    if (AbstractDungeon.ascensionLevel > 7)
      setHp(MIN_HP.plus(ASC_HP), MAX_HP.plus(ASC_HP))
    else
      setHp(MIN_HP, MAX_HP)

    if (AbstractDungeon.ascensionLevel > 2) {
      multiStrikeMove.modify(damage = attackDamage + 1)
      slamMove.modify(damage = slamDamage + 5)
      this.debuffAmount += 1
    }

    if (CardCrawlGame.isInARun() && player.hasBlight(Spear.ID)) {
      realityShiftAmount *= 1 + player.getBlight(Spear.ID).counter
    }

    registerMove(realityShiftMove)
    registerMove(multiStrikeMove)
    registerMove(debuffBlockMove)
    registerMove(slamMove)

    this.img = Textures.monsters.get("nightmare/nightmare-1.png")
  }

  fun triggerRealityShiftAttack() {
    AbstractDungeon.effectList.add(
      PowerBuffEffect(
        this.hb.cX - this.animX,
        this.hb.cY + this.hb.height / 2f,
        "")
    )
    AbstractDungeon.effectList.add(
      BlackCardVfx()
    )
    addToTop(GainBlockAction(this, this, BLOCK_ON_TRIGGER))

    this.setMove(multiStrikeMove)
    this.createIntent()
    endPlayerTurn()
  }

  override fun damage(info: DamageInfo) {
    super.damage(info)
    if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
      particleSystem.addParticles(100) {
        val point = VFXManager.generateRandomPointAlongEdgeOfCircle(
          hb.cX,
          hb.cY,
          200f.scale()
        )

        BlackCardParticle(
          point,
          1f,
          true,
          VFXManager.getVelocityAwayFromPoint(
            Vector2(hb.cX, hb.cY),
            point
          ).scl(0.75f.div(scale))
        )
      }
    }
  }

  override fun update() {
    spriteTimer += deltaTime

    if (spriteTimer.times(10f).roundToInt().rem(3) == 0) {
      if ((Random()).nextInt(3) == 0) {
        if (++sprintIndex > 2) {
          sprintIndex = 0
        }
        img = Textures.monsters.get(
          "nightmare/nightmare-${sprintIndex + 1}.png"
        )
      }
    }

    particleSystem.update()

    super.update()
  }

  override fun render(sb: SpriteBatch) {
    particleSystem.render(sb)
    super.render(sb)
  }

  override fun dispose() = doNothing()

  override fun die() {
    var actNum = BehindTheScenesActNum.getActNum()
    BehindTheScenesActNum.setActNum(--actNum)
    log.info("Set Current Act Num: $actNum")
    super.die()
  }

  override fun getMove(roll: Int) {
    if (firstTurn) {
      this.setMove(realityShiftMove)
      firstTurn = false
      return
    }
    when {
      roll < 29 -> this.setMove(multiStrikeMove)
      roll > 69 -> this.setMove(debuffBlockMove)
      else -> this.setMove(slamMove)
    }
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