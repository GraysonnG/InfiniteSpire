package com.blanktheevil.infinitespire.monsters

import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.applyPower
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.monsters.utils.Move
import com.blanktheevil.infinitespire.monsters.utils.setMove
import com.blanktheevil.infinitespire.powers.VenomPower
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.*
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.ChangeStateAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class Voidling(xOffset: Float = 0.0f, yOffset: Float = 0.0f) : Monster(
  strings.NAME,
  ID,
  32,
  0.0f,
  0.0f,
  200f,
  150f,
  null,
  xOffset,
  yOffset
) {
  companion object {
    val ID = "Voidling".makeID()
    val strings = languagePack.getMonsterStrings(ID)
  }

  private var tackleDamage = 17
  private var fangAttack = 9
  private var fangPoison = 2
  private var venomShotAmt = 7
  private var blockAmt = 16
  private val maxHP = 67
  private val minHP = 58
  private val ascHPOffset = 6

  val TACKLE = Move(Intent.ATTACK, tackleDamage) {
    addToBot(
      ChangeStateAction(it, "ATTACK")
    )
    addToBot(
      DamageAction(player, it.damage[getByte().toInt()], AbstractGameAction.AttackEffect.BLUNT_HEAVY)
    )
  }
  val DEFEND = Move(Intent.DEFEND) {
    addToBot(GainBlockAction(it, it, blockAmt))
  }
  val VENOM_SHOT = Move(Intent.ATTACK_DEBUFF, 1) {
    log.info("MoveByte: ${getByte()}")
    addToBot(
      DamageAction(player, it.damage[getByte().toInt()], AbstractGameAction.AttackEffect.BLUNT_HEAVY)
    )
    player.applyPower(
      VenomPower(player, it, venomShotAmt),
      source = it
    )
  }
  val FANG_ATTACK = Move(Intent.ATTACK_DEBUFF, fangAttack, multiplier = 2, isMultiDamage = true) {
    for (i in 0 until 2) {
      addToBot(
        DamageAction(player, it.damage[getByte().toInt()], AbstractGameAction.AttackEffect.SLASH_VERTICAL)
      )
      player.applyPower(
        VenomPower(player, it, fangPoison),
        source = it
      )
    }
  }

  init {
    if (AbstractDungeon.ascensionLevel > 7)
      setHp(minHP.plus(ascHPOffset), maxHP.plus(ascHPOffset))
    else
      setHp(minHP, maxHP)


    if (AbstractDungeon.ascensionLevel > 2) {
      TACKLE.modify (damage = tackleDamage + 2)
      venomShotAmt += 2
      FANG_ATTACK.modify(damage = fangAttack + 2)
    }

    registerMove(TACKLE)
    registerMove(DEFEND)
    registerMove(VENOM_SHOT)
    registerMove(FANG_ATTACK)

    loadAnimation(
      Textures.monsters.getString("voidling/Voidling.atlas", true),
      Textures.monsters.getString("voidling/Voidling.json", true),
      1.0f
    )

    state.setAnimation(0, "idle", true).also {
      it.time = it.endTime.times(MathUtils.random())
    }
  }

  override fun changeState(stateName: String) {
    when (stateName) {
      "ATTACK" -> {
        state.setAnimation(0, "attack", false)
        state.addAnimation(0, "idle", true, 0.0f)
      }
      else -> {
      }
    }
  }

  override fun damage(info: DamageInfo) {
    super.damage(info)
    if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
      state.setAnimation(0, "oof", false)
      state.addAnimation(0, "idle", true, 0.0f)
    }
  }

  override fun getMove(moveInt: Int) {
    when {
      moveInt < 30 || GameActionManager.turn == 0 -> when {
        !lastMove(VENOM_SHOT.getByte()) -> setMove(VENOM_SHOT)
        AbstractDungeon.ascensionLevel >= 17 -> setMove(FANG_ATTACK)
        else -> setMove(TACKLE)
      }
      moveInt < 45 -> setMove(DEFEND)
      else -> setMove(TACKLE)
    }
  }
}