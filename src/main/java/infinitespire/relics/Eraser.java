package infinitespire.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.rooms.NightmareEliteRoom;

public class Eraser extends Relic implements ClickableRelic {
	public static final String ID = InfiniteSpire.createID("Eraser");
	private static RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
	private boolean restartedThisCombat = false;

	public Eraser(){
		super(ID, "eraser", RelicTier.UNCOMMON, LandingSound.FLAT);
		this.counter = 3;
	}

	@Override
	public void onEnterRoom(AbstractRoom room) {
		restartedThisCombat = false;
	}

	@Override
	public void update() {
		super.update();
		if(isClickable() && !this.pulse){
			this.flash();
			this.beginPulse();
			this.pulse = true;
		}
	}

	@Override
	public void onVictory() {
		this.stopPulse();
	}

	public String getUpdatedDescription() {
		return "[#2aecd7]Right [#2aecd7]click [#2aecd7]during [#2aecd7]combat [#2aecd7]to [#2aecd7]activate.[]"
			+ " NL [#2aecd7]Usable [#2aecd7]only [#2aecd7]once [#2aecd7]per [#2aecd7]combat.[] NL " + DESCRIPTIONS[0];
	}

	@Override
	public void onRightClick() {
		if(isClickable()) {
			restartCombat();
			restartedThisCombat = true;
			counter--;
		}
	}

	private boolean isClickable(){
		return (!restartedThisCombat &&
			!AbstractDungeon.loading_post_combat &&
			AbstractDungeon.getCurrMapNode() != null &&
			AbstractDungeon.getCurrRoom() != null &&
			AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite &&
			!(AbstractDungeon.getCurrRoom() instanceof NightmareEliteRoom) &&
			this.counter > 0);
	}

	public void restartCombat()
	{
		System.out.println("Remaking combat vs " + AbstractDungeon.lastCombatMetricKey);
		AbstractRoom room = AbstractDungeon.getCurrRoom();

		AbstractDungeon.fadeIn();
		AbstractDungeon.player.resetControllerValues();
		AbstractDungeon.effectList.clear();
		AbstractDungeon.topLevelEffects.clear();
		AbstractDungeon.topLevelEffectsQueue.clear();
		AbstractDungeon.effectsQueue.clear();
		AbstractDungeon.dungeonMapScreen.dismissable = true;
		AbstractDungeon.dungeonMapScreen.map.legend.isLegendHighlighted = false;

		AbstractDungeon.player.orbs.clear();
		AbstractDungeon.player.animX = 0.0f;
		AbstractDungeon.player.animY = 0.0f;
		AbstractDungeon.player.hideHealthBar();
		AbstractDungeon.player.hand.clear();
		AbstractDungeon.player.powers.clear();
		AbstractDungeon.player.drawPile.clear();
		AbstractDungeon.player.discardPile.clear();
		AbstractDungeon.player.exhaustPile.clear();
		AbstractDungeon.player.limbo.clear();
		AbstractDungeon.player.loseBlock(true);
		AbstractDungeon.player.damagedThisCombat = 0;
		GameActionManager.turn = 1;

		AbstractDungeon.actionManager.monsterQueue.clear();
		AbstractDungeon.actionManager.monsterAttacksQueued = true;


		for (AbstractRelic r : AbstractDungeon.player.relics) {
			r.onEnterRoom(room);
		}

		AbstractDungeon.actionManager.clear();

		// Reroll encounter to be a different elite
		room.monsters = MonsterHelper.getEncounter(getNewEliteGroupKey(AbstractDungeon.lastCombatMetricKey));
		room.monsters.init();

		// Prepare monsters
		for (AbstractMonster m : room.monsters.monsters) {
			m.showHealthBar();
			m.usePreBattleAction();
			m.useUniversalPreBattleAction();
		}

		AbstractDungeon.player.preBattlePrep();

		// From AbstractRoom.update(). Most of what happens at start of combat
		AbstractDungeon.actionManager.turnHasEnded = true;
		AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAndEnableControlsAction(AbstractDungeon.player.energy.energyMaster));

		AbstractDungeon.player.applyStartOfCombatPreDrawLogic();
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.gameHandSize));

		AbstractDungeon.actionManager.addToBottom(new EnableEndTurnButtonAction());
		AbstractDungeon.overlayMenu.showCombatPanels();
		AbstractDungeon.player.applyStartOfCombatLogic();
		AbstractDungeon.player.applyStartOfTurnRelics();
		AbstractDungeon.player.applyStartOfTurnPostDrawRelics();
		AbstractDungeon.player.applyStartOfTurnCards();
		AbstractDungeon.player.applyStartOfTurnPowers();
		AbstractDungeon.player.applyStartOfTurnOrbs();

		this.stopPulse();

		buffMonsters();
	}

	private void buffMonsters() {
		for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m, 0.25f, true));
		}
		switch (AbstractDungeon.mapRng.random(0, 3)) {
			case 0: {
				for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, AbstractDungeon.actNum + 1), AbstractDungeon.actNum + 1));
				}
				break;
			}
			case 1: {
				for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new AngryPower(m, 1), AbstractDungeon.actNum));
				}
				break;
			}
			case 2: {
				for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, AbstractDungeon.actNum * 2 + 2), AbstractDungeon.actNum * 2 + 2));
				}
				break;
			}
			case 3: {
				for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new RegenerateMonsterPower(m, 1 + AbstractDungeon.actNum * 2), 1 + AbstractDungeon.actNum * 2));
				}
				break;
			}
		}
	}

	private String getNewEliteGroupKey(String lastCombatMetricKey){
		String key = AbstractDungeon.eliteMonsterList.get(AbstractDungeon.miscRng.random(AbstractDungeon.eliteMonsterList.size() - 1));
		if(key.equals(lastCombatMetricKey)){
			return getNewEliteGroupKey(lastCombatMetricKey);
		}else{
			return key;
		}
	}

	@Override
	public String[] CLICKABLE_DESCRIPTIONS() {
		return new String[0];
	}
}
