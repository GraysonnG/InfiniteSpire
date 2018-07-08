package infinitespire.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.campfire.CampfireDigEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireTokeEffect;

import infinitespire.relics.BlanksBlanky;
import infinitespire.relics.Relic;

public class EmptyRestSite extends AbstractImageEvent {

	public static final String ID = "BlanksBlanky";
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
		this.imageEventText.setDialogOption(OPTIONS[1]); //Smith
		this.imageEventText.setDialogOption(OPTIONS[2]); //Toke
		if(AbstractDungeon.player.hasRelic("Regal Pillow")) {
			this.imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[4], "b"));
		} else {
			this.imageEventText.setDialogOption(OPTIONS[3]); //Dig
		}
		state = State.RESTING;
	}

	@Override
	protected void buttonEffect(int buttonPressed) {
		switch(state) {
		case RESTING:
			switch(buttonPressed) {
			case 0:
				this.playSleepJingle();
				AbstractDungeon.player.heal(healAmount);
				break;
			case 1:
				AbstractDungeon.effectList.add(new CampfireSmithEffect());
				break;
			case 2:
				AbstractDungeon.effectList.add(new CampfireTokeEffect());
				break;
			case 3:
				AbstractDungeon.effectList.add(new CampfireDigEffect());
				break;
			case 4:
				findAndReplaceRegalPillow();
				break;
			default: 
				break;
			}
			this.imageEventText.clearAllDialogs();
			this.imageEventText.setDialogOption(OPTIONS[5]);
		case LEAVING:
			this.imageEventText.clearAllDialogs();
			this.imageEventText.setDialogOption(OPTIONS[5]);
			openMap();
		}
	}
	
	private void findAndReplaceRegalPillow() {
		for(int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
			AbstractRelic relic = AbstractDungeon.player.relics.get(i);
			if(relic.relicId.equals("Regal Pillow")) {
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
