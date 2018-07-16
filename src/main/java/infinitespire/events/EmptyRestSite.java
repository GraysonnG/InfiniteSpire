package infinitespire.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.RegalPillow;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import infinitespire.relics.BlanksBlanky;
import infinitespire.relics.Relic;

public class EmptyRestSite extends AbstractImageEvent {

	public static final String ID = "Empty Rest Site";
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Empty Rest Site");
	private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
	private static final String[] OPTIONS = eventStrings.OPTIONS;
	private int healAmount;
	private State state;
	
	private enum State {
		RESTING,
		LEAVING
	}
	
	public EmptyRestSite() {
		super("Empty Rest Site", DESCRIPTIONS[0], "img/events/emptyrestsite.jpg");
		
		this.healAmount = (int)(AbstractDungeon.player.maxHealth * 0.3f);
		if(AbstractDungeon.player.hasRelic("Regal Pillow")) {
			healAmount += 15;
		}
		
		this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[0] + " " + healAmount + " HP.", "g"));
		this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[1], "g")); //Smith
		this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[2], "g")); //Toke
		if(AbstractDungeon.player.hasRelic("Regal Pillow")) {
			this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[4], "b"));
		} else {
			this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[3], "g")); //Dig
		}
		state = State.RESTING;
	}
	
	

	@Override
	public void update() {
		super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.forUpgrade) {
            final AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            c.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.forPurge) {
        	CardCrawlGame.sound.play("CARD_EXHAUST");
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0), Settings.WIDTH / 2, Settings.HEIGHT / 2));
            AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
	}



	@Override
	protected void buttonEffect(int buttonPressed) {
		switch(state) {
		case RESTING:
			switch(buttonPressed) {
			case 0:
				this.playSleepJingle();
				AbstractDungeon.player.heal(healAmount);
				imageEventText.updateBodyText(DESCRIPTIONS[1]);
				break;
			case 1:
				imageEventText.updateBodyText(DESCRIPTIONS[2]);
				AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, "Upgrade a Card.", true, false, false, false);
				
				break;
			case 2:
				imageEventText.updateBodyText(DESCRIPTIONS[3]);
				AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, "Remove a card.", false, false, false, true);
				break;
			case 3:
				imageEventText.updateBodyText(DESCRIPTIONS[4]);
				CardCrawlGame.sound.play("SHOVEL");
	            AbstractDungeon.getCurrRoom().rewards.clear();
	            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier())));
	            AbstractDungeon.combatRewardScreen.open();
				break;
			case 4:
				imageEventText.updateBodyText(DESCRIPTIONS[5]);
				this.playSleepJingle();
				findAndReplaceRegalPillow();
				break;
			default: 
				break;
			}
			this.imageEventText.clearAllDialogs();
			this.imageEventText.setDialogOption(OPTIONS[5]);
			this.state = State.LEAVING;
            AbstractDungeon.getCurrRoom().phase = RoomPhase.COMPLETE;
			break;
		case LEAVING:
			this.imageEventText.clearAllDialogs();
			this.imageEventText.clearRemainingOptions();
			openMap();
			break;
		}
	}
	
	private void findAndReplaceRegalPillow() {
		for(int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
			AbstractRelic relic = AbstractDungeon.player.relics.get(i);
			if(relic.relicId.equals(RegalPillow.ID)) {
				Relic blanksBlanky = new BlanksBlanky();
				blanksBlanky.instantObtain(AbstractDungeon.player, i, false);
				blanksBlanky.playLandingSFX();
				blanksBlanky.flash();
			}
		}
	}
	
	private void playSleepJingle() {
        final int roll = MathUtils.random(0, 2);
        final String id = AbstractDungeon.id;
        switch (id) {
            case "Exordium": {
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_1-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_1-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_1-3");
                break;
            }
            case "TheCity": {
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_2-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_2-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_2-3");
                break;
            }
            case "TheBeyond": {
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_3-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_3-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_3-3");
                break;
            }
        }
    }
}
