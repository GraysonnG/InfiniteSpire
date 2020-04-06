package com.blanktheevil.infinitespire.powers

import basemod.interfaces.CloneablePowerInterface
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.isPlatedPower
import com.blanktheevil.infinitespire.extensions.makeID
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.stances.AbstractStance

class PlatedPower<T : AbstractPower>(private val simulatedPower: T, amount: Int) : AbstractPower(),
    CloneablePowerInterface {
  companion object {
    val POWER_ID = "PlatedPower".makeID()
  }

  private val amountToLose = simulatedPower.amount

  init {
    this.owner = simulatedPower.owner
    this.ID = POWER_ID + ":${simulatedPower.ID}"
    this.name = "Plated " + simulatedPower.name
    this.amount = amount
    simulatedPower.amount *= amount
    simulatedPower.isPlatedPower = true
    loadRegion("platedarmor")
    updateDescription()
  }

  override fun updateDescription() {
    simulatedPower.updateDescription()
    this.description = "Gain the effect of $amount #y${simulatedPower.name}" +
        " NL NL Receiving unblocked attack damage reduces #yPlated #y${simulatedPower.name} by #b$amountToLose."
  }

  override fun atEndOfTurnPreEndTurnCards(isPlayer: Boolean) {
    flash()
    simulatedPower.atEndOfTurnPreEndTurnCards(isPlayer)
  }

  override fun wasHPLost(info: DamageInfo?, damageAmount: Int) {
    if (info?.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0) {
      this.addToBot(ReducePowerAction(owner, owner, this.ID, 1))
    }
    simulatedPower.wasHPLost(info, damageAmount)
  }

  override fun reducePower(reduceAmount: Int) {
    super.reducePower(reduceAmount)
    simulatedPower.amount -= amountToLose
  }

  override fun stackPower(stackAmount: Int) {
    super.stackPower(stackAmount)
    simulatedPower.amount += amountToLose
  }

  override fun renderIcons(sb: SpriteBatch, x: Float, y: Float, c: Color) {
    super.renderIcons(sb, x, y, c)
    simulatedPower.renderIcons(sb, x, y, c)
  }

  override fun makeCopy(): AbstractPower = PlatedPower(simulatedPower, amount)

  override fun onDamageAllEnemies(damage: IntArray?) {
    simulatedPower.onDamageAllEnemies(damage)
  }

  override fun onAttackToChangeDamage(info: DamageInfo?, damageAmount: Int): Int {
    return simulatedPower.onAttackToChangeDamage(info, damageAmount)
  }

  override fun onAfterCardPlayed(usedCard: AbstractCard?) {
    simulatedPower.onAfterCardPlayed(usedCard)
  }

  override fun onUseCard(card: AbstractCard?, action: UseCardAction?) {
    simulatedPower.onUseCard(card, action)
  }

  override fun onGainCharge(chargeAmount: Int) {
    simulatedPower.onGainCharge(chargeAmount)
  }

  override fun onDeath() {
    simulatedPower.onDeath()
  }

  override fun duringTurn() {
    simulatedPower.duringTurn()
  }

  override fun onSpecificTrigger() {
    simulatedPower.onSpecificTrigger()
  }

  override fun atDamageReceive(damage: Float, damageType: DamageInfo.DamageType?): Float {
    return simulatedPower.atDamageReceive(damage, damageType)
  }

  override fun atDamageReceive(damage: Float, damageType: DamageInfo.DamageType?, card: AbstractCard?): Float {
    return simulatedPower.atDamageReceive(damage, damageType, card)
  }

  override fun onChannel(orb: AbstractOrb?) {
    simulatedPower.onChannel(orb)
  }

  override fun onPlayerGainedBlock(blockAmount: Float): Int {
    return simulatedPower.onPlayerGainedBlock(blockAmount)
  }

  override fun onPlayerGainedBlock(blockAmount: Int): Int {
    return simulatedPower.onPlayerGainedBlock(blockAmount)
  }

  override fun onRemove() {
    simulatedPower.onRemove()
  }

  override fun atDamageFinalGive(damage: Float, type: DamageInfo.DamageType?): Float {
    return simulatedPower.atDamageFinalGive(damage, type)
  }

  override fun atDamageFinalGive(damage: Float, type: DamageInfo.DamageType?, card: AbstractCard?): Float {
    return simulatedPower.atDamageFinalGive(damage, type, card)
  }

  override fun onEvokeOrb(orb: AbstractOrb?) {
    simulatedPower.onEvokeOrb(orb)
  }

  override fun onEnergyRecharge() {
    simulatedPower.onEnergyRecharge()
  }

  override fun atDamageFinalReceive(damage: Float, type: DamageInfo.DamageType?): Float {
    return simulatedPower.atDamageFinalReceive(damage, type)
  }

  override fun atDamageFinalReceive(damage: Float, type: DamageInfo.DamageType?, card: AbstractCard?): Float {
    return simulatedPower.atDamageFinalReceive(damage, type, card)
  }

  override fun modifyBlock(blockAmount: Float): Float {
    return simulatedPower.modifyBlock(blockAmount)
  }

  override fun modifyBlock(blockAmount: Float, card: AbstractCard?): Float {
    return simulatedPower.modifyBlock(blockAmount, card)
  }

  override fun atEndOfRound() {
    simulatedPower.atEndOfRound()
  }

  override fun onApplyPower(power: AbstractPower?, target: AbstractCreature?, source: AbstractCreature?) {
    simulatedPower.onApplyPower(power, target, source)
  }

  override fun onAttack(info: DamageInfo?, damageAmount: Int, target: AbstractCreature?) {
    simulatedPower.onAttack(info, damageAmount, target)
  }

  override fun onAttacked(info: DamageInfo?, damageAmount: Int): Int {
    return simulatedPower.onAttacked(info, damageAmount)
  }

  override fun atEnergyGain() {
    simulatedPower.atEnergyGain()
  }

  override fun atEndOfTurn(isPlayer: Boolean) {
    simulatedPower.atEndOfTurn(isPlayer)
  }

  override fun onPlayCard(card: AbstractCard?, m: AbstractMonster?) {
    simulatedPower.onPlayCard(card, m)
  }

  override fun atStartOfTurnPostDraw() {
    simulatedPower.atStartOfTurnPostDraw()
  }

  override fun onHeal(healAmount: Int): Int {
    return simulatedPower.onHeal(healAmount)
  }

  override fun onScry() {
    simulatedPower.onScry()
  }

  override fun onGainedBlock(blockAmount: Float) {
    simulatedPower.onGainedBlock(blockAmount)
  }

  override fun onInitialApplication() {
    simulatedPower.onInitialApplication()
  }

  override fun onInflictDamage(info: DamageInfo?, damageAmount: Int, target: AbstractCreature?) {
    simulatedPower.onInflictDamage(info, damageAmount, target)
  }

  override fun atDamageGive(damage: Float, type: DamageInfo.DamageType?): Float {
    return simulatedPower.atDamageGive(damage, type)
  }

  override fun atDamageGive(damage: Float, type: DamageInfo.DamageType?, card: AbstractCard?): Float {
    return simulatedPower.atDamageGive(damage, type, card)
  }

  override fun onDrawOrDiscard() {
    simulatedPower.onDrawOrDiscard()
  }

  override fun onLoseHp(damageAmount: Int): Int {
    return simulatedPower.onLoseHp(damageAmount)
  }

  override fun canPlayCard(card: AbstractCard?): Boolean {
    return simulatedPower.canPlayCard(card)
  }

  override fun onVictory() {
    simulatedPower.onVictory()
  }

  override fun atStartOfTurn() {
    simulatedPower.atStartOfTurn()
  }

  override fun onExhaust(card: AbstractCard?) {
    simulatedPower.onExhaust(card)
  }

  override fun triggerMarks(card: AbstractCard?) {
    simulatedPower.triggerMarks(card)
  }

  override fun onAttackedToChangeDamage(info: DamageInfo?, damageAmount: Int): Int {
    return simulatedPower.onAttackedToChangeDamage(info, damageAmount)
  }

  override fun onCardDraw(card: AbstractCard?) {
    simulatedPower.onCardDraw(card)
  }

  override fun onChangeStance(oldStance: AbstractStance?, newStance: AbstractStance?) {
    simulatedPower.onChangeStance(oldStance, newStance)
  }

  override fun onAfterUseCard(card: AbstractCard?, action: UseCardAction?) {
    simulatedPower.onAfterUseCard(card, action)
  }
}