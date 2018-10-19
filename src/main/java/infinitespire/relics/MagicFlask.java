package infinitespire.relics;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.PostCampfireSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;
import infinitespire.ui.FlaskOption;

import java.util.ArrayList;

public class MagicFlask extends Relic implements PostCampfireSubscriber {
	
	public static final String ID = InfiniteSpire.createID("Magic Flask");
	public static final String NAME = "Magic Flask";
	
	private final Texture textureUsed;
	
	public MagicFlask() {
		super(ID, "magicflask", RelicTier.RARE, LandingSound.SOLID);
		BaseMod.subscribe(this);
		textureUsed = InfiniteSpire.getTexture("img/infinitespire/relics/magicflask-used.png");
		this.counter = 3;

		if(InfiniteSpire.isHubrisLoaded){
			this.tips.add(new PowerTip("Synergy",
				"If you have #yBottled #yHeart, when resting #yBottled #yHeart gains 7 charges."));
		}
	}

	@Override
	public String getUpdatedDescription() {
		if(InfiniteSpire.isHubrisLoaded){
			return DESCRIPTIONS[0] + " " + DESCRIPTIONS[1];
		} else {
			return super.getUpdatedDescription();
		}
	}

	@Override
	public void update() {
		super.update();
		if(counter <= 0 && img != textureUsed) {
			img = textureUsed;
		}else if(img == textureUsed && counter > 0) {
			img = InfiniteSpire.getTexture("img/infinitespire/relics/magicflask.png");
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MagicFlask();
	}

	public void useFlask() {
		this.counter--;
		if(InfiniteSpire.isHubrisLoaded) {
			if (AbstractDungeon.player.hasRelic(com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart.ID)) {
				com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart bHeart = (com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart)
					AbstractDungeon.player.getRelic(com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart.ID);
				bHeart.setCounter(bHeart.counter + 7);
			}
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
		return shouldFinishCampfire();
	}

	@Override
	public void onVictory() {
		if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
			counter += 2;
			this.flash();
		}
	}
}
