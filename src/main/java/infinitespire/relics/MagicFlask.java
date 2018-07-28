package infinitespire.relics;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.PostCampfireSubscriber;
import infinitespire.InfiniteSpire;
import infinitespire.ui.FlaskOption;

public class MagicFlask extends Relic implements PostCampfireSubscriber{
	
	public static final String ID = "Magic Flask";
	public static final String NAME = "Magic Flask";
	
	private final Texture textureUsed;
	
	public MagicFlask() {
		super(ID, "magicflask", RelicTier.RARE, LandingSound.SOLID);
		BaseMod.subscribe(this);
		textureUsed = InfiniteSpire.getTexture("img/relics/magicflask-used.png");
		this.counter = 3;
	}
	
	@Override
	public void update() {
		super.update();
		if(counter <= 0 && img != textureUsed) {
			img = textureUsed;
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MagicFlask();
	}

	public void useFlask() {
		this.counter--;
		if(this.counter <= 0) {
			img = textureUsed;
		}
	}
	
	public boolean shouldFinishCampfire() {
		boolean somethingSelected = true;
		
		InfiniteSpire.logger.info("Setting Reopen to false");
		if(FlaskOption.shouldReOpen) {
			((RestRoom)AbstractDungeon.getCurrRoom()).campfireUI.reopen();
			((RestRoom)AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.INCOMPLETE;
			FlaskOption.shouldReOpen = false;
			somethingSelected = false;
		}
		
		boolean isStuck = true;
		@SuppressWarnings("unchecked")
		ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>) ReflectionHacks.getPrivate(((RestRoom)AbstractDungeon.getCurrRoom()).campfireUI, CampfireUI.class, "buttons");
		for(AbstractCampfireOption option : campfireButtons) {
			if(option.usable && !(option instanceof FlaskOption)) {
				isStuck = false;
				break;
			}
		}
		InfiniteSpire.logger.info("isStuck: " + isStuck);
		if(isStuck) {
			somethingSelected = true;
			AbstractDungeon.overlayMenu.proceedButton.show();
			((RestRoom)AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
		}
		return somethingSelected;
	}

	@Override
	public boolean receivePostCampfire() {
		boolean somethingSelected = true; 
		somethingSelected = shouldFinishCampfire();
		return somethingSelected;
	}
}
