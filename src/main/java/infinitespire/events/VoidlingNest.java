package infinitespire.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;
import infinitespire.monsters.Voidling;
import infinitespire.rewards.BlackCardRewardItem;

public class VoidlingNest extends AbstractImageEvent {
	public static final String ID = InfiniteSpire.createID("VoidlingNest");
	private static final EventStrings strings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String[] DESCRIPTIONS = strings.DESCRIPTIONS;
	private static final String[] OPTIONS = strings.OPTIONS;
	private static final String NAME = strings.NAME;
	private State state;

	public enum State {
		INTRO,
		FIGHT,
		LEAVE,
		POST_COMBAT
	}

	public VoidlingNest() {
		super(NAME, DESCRIPTIONS[0], "img/infinitespire/events/prismoflight.jpg");
		imageEventText.setDialogOption(OPTIONS[0]);
		state = State.INTRO;
	}

	@Override
	protected void buttonEffect(int i) {
		switch(state) {
			case INTRO:
				switch(i) {
					case 0:
						this.state = State.FIGHT;
						this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
						this.imageEventText.updateDialogOption(0, OPTIONS[1]);
						this.imageEventText.setDialogOption(OPTIONS[2]);
						return;
				}
				return;
			case FIGHT:
				switch(i){
					case 0: //Attack
						this.state = State.POST_COMBAT;
						AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(Voidling.SPECIAL_ENCOUNTER_ID);
						AbstractDungeon.getCurrRoom().rewards.clear();
						AbstractDungeon.getCurrRoom().rewardAllowed = false;
						this.enterCombatFromImage();
						AbstractDungeon.lastCombatMetricKey = Voidling.SPECIAL_ENCOUNTER_ID;
						return;
					case 1: //Sprint
						this.openMap();
						return;
				}
				return;
			case POST_COMBAT:
				AbstractDungeon.getCurrRoom().rewardAllowed = true;
				switch(i){
					case 0:
						this.state = State.LEAVE;
						AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(Nightmare.ID + "_Alpha");
						AbstractDungeon.getCurrRoom().rewards.clear();
						AbstractDungeon.getCurrRoom().rewards.add(new BlackCardRewardItem());
						AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
						AbstractDungeon.getCurrRoom().addGoldToRewards(100);
						AbstractDungeon.getCurrRoom().eliteTrigger = true;
						this.enterCombatFromImage();
						AbstractDungeon.lastCombatMetricKey = Nightmare.ID + "_Alpha";
						return;
					case 1:
						this.openMap();
						return;
				}
				return;
			case LEAVE:
				this.openMap();
				break;
		}
	}

	public void reopen() {
		if (this.state != State.LEAVE) {
			AbstractDungeon.resetPlayer();
			AbstractDungeon.player.drawX = (float) Settings.WIDTH * 0.25F;
			AbstractDungeon.player.preBattlePrep();
			this.enterImageFromCombat();
			this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
			this.imageEventText.updateDialogOption(0, OPTIONS[3]);
			this.imageEventText.updateDialogOption(1, OPTIONS[4]);
		}

	}
}
