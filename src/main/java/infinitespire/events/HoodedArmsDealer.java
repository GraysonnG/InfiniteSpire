package infinitespire.events;

import java.util.ArrayList;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import infinitespire.helpers.CardHelper;
import infinitespire.helpers.QuestHelper;

public class HoodedArmsDealer extends AbstractImageEvent {

	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Hooded Arms Dealer");
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;
	public static final String ID = eventStrings.NAME;
	private int goldCount;
	private int hpCount;
	private State state;
	
	private enum State {
		PAYING,
		LEAVING
	}
	
	public HoodedArmsDealer() {
		super(ID, DESCRIPTIONS[0], "img/infinitespire/events/emptyrestsite.jpg");
		this.goldCount = QuestHelper.makeRandomCost(500);
		this.hpCount = AbstractDungeon.player.maxHealth / 4;
		this.state = State.PAYING;
		
		boolean goldDisabled = AbstractDungeon.player.gold >= this.goldCount;
		boolean hpDisabled = AbstractDungeon.player.currentHealth > this.hpCount;
		
		String goldOption = goldDisabled ? 
						OPTIONS[0] + goldCount + OPTIONS[3] : 
						OPTIONS[6] + goldCount + OPTIONS[3];
		String hpOption = hpDisabled ?
						OPTIONS[1] + hpCount + OPTIONS[4] :
						OPTIONS[6] + hpCount + OPTIONS[4];
		
		this.imageEventText.setDialogOption(FontHelper.colorString(goldOption, "y"), goldDisabled);
		this.imageEventText.setDialogOption(FontHelper.colorString(hpOption, "r"), hpDisabled);
		this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[2], "b"));
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
				if(AbstractDungeon.player.gold >= this.goldCount) {
					AbstractDungeon.player.loseGold(goldCount);
					this.pickCard();
				}
				
				break;
			case 1:
				if(AbstractDungeon.player.currentHealth > this.hpCount) {
					AbstractDungeon.player.damage(new DamageInfo(null, hpCount, DamageInfo.DamageType.HP_LOSS));
					this.pickCard();
				}
				this.pickCard();
				break;
			case 2:
				AbstractDungeon.gridSelectScreen.open(
						CardGroup.getGroupWithoutBottledCards(
								AbstractDungeon.player.masterDeck.getPurgeableCards()), 2, 
								"Remove 2 cards.", false, false, false, true);
				break;
			case 3:
				break;
			}
			this.imageEventText.clearAllDialogs();
			this.imageEventText.setDialogOption(OPTIONS[5]);
			this.state = State.LEAVING;
			this.imageEventText.updateBodyText(DESCRIPTIONS[(buttonPressed == 3 ? 3 : 2)]);
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
