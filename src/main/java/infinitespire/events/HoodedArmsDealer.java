package infinitespire.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import infinitespire.InfiniteSpire;
import infinitespire.helpers.CardHelper;
import infinitespire.helpers.QuestHelper;

import java.util.ArrayList;

public class HoodedArmsDealer extends AbstractImageEvent {

	public static final String ID = InfiniteSpire.createID("Hooded Arms Dealer");
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;
	private static final String NAME = eventStrings.NAME;

	private int goldCount;
	private int hpCount;
	private State state;
	
	private enum State {
		PAYING,
		LEAVING
	}
	
	public HoodedArmsDealer() {
		super(NAME, DESCRIPTIONS[0], "img/infinitespire/events/emptyrestsite.jpg");
		this.goldCount = QuestHelper.makeRandomCost(500);
		this.hpCount = QuestHelper.makeRandomCost(AbstractDungeon.player.maxHealth / 4);
		this.state = State.PAYING;
		
		boolean goldDisabled = AbstractDungeon.player.gold >= this.goldCount;
		boolean hpDisabled = AbstractDungeon.player.currentHealth > this.hpCount;
		
		String goldOption = goldDisabled ? 
						OPTIONS[0] + goldCount + OPTIONS[3] + OPTIONS[7]: 
						OPTIONS[6] + goldCount + OPTIONS[3];
		String hpOption = hpDisabled ?
						OPTIONS[1] + hpCount + OPTIONS[4] + OPTIONS[7]:
						OPTIONS[6] + hpCount + OPTIONS[4];
		
		this.imageEventText.setDialogOption(goldOption , !goldDisabled);
		this.imageEventText.setDialogOption(hpOption, !hpDisabled);
		this.imageEventText.setDialogOption(OPTIONS[2] + OPTIONS[7]);
		this.imageEventText.setDialogOption(OPTIONS[5]);
	}
	
	@Override
	public void update() {
		super.update();
		if(!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.forPurge) {
			CardCrawlGame.sound.play("CARD_EXHAUST");
			for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                    AbstractDungeon.player.masterDeck.removeCard(c);
            }
			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			this.pickCard();
		}
	}
	
	@Override
	protected void buttonEffect(int buttonPressed) {
		switch(state){
		case LEAVING:
			tryLeave();
			break;
		case PAYING:
			switch(buttonPressed) {
			case 0:
				AbstractDungeon.player.loseGold(goldCount);
				this.pickCard();
				break;
			case 1:
				AbstractDungeon.player.damage(new DamageInfo(null, hpCount, DamageInfo.DamageType.HP_LOSS));
				this.pickCard();
				break;
			case 2:
				for(int i = 0; i < 2; i++) {
					if(AbstractDungeon.player.hasRelic(Omamori.ID)){
						Omamori relic = (Omamori) AbstractDungeon.player.getRelic(Omamori.ID);
						if(relic.counter > 0) {
							relic.use();
							continue;
						}
					}

					AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
						AbstractDungeon.returnRandomCurse(),
						Settings.WIDTH / 2f,
						Settings.HEIGHT/ 2f));
				}

				this.pickCard();
				break;
			case 3:
				break;
			}
			this.imageEventText.clearAllDialogs();
			this.imageEventText.setDialogOption(OPTIONS[5]);
			this.state = State.LEAVING;
			this.imageEventText.updateBodyText(DESCRIPTIONS[(buttonPressed == 3 ? 2 : 1)]);
			break;
		}
	}

	private void pickCard() {
		ArrayList<AbstractCard> randomBlackCards = CardHelper.getBlackRewardCards();
		AbstractDungeon.cardRewardScreen.open(randomBlackCards, null, "Select a Card.");
	}
	
	private void tryLeave() {
		this.imageEventText.clearAllDialogs();
		this.imageEventText.setDialogOption(OPTIONS[5]);
		this.openMap();
	}

}
