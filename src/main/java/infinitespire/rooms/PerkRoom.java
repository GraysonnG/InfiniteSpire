package infinitespire.rooms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;

import infinitespire.InfiniteSpire;
import infinitespire.perks.PerkMerchant;

public class PerkRoom extends AbstractRoom {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
	PerkMerchant merchant;
	
	public PerkRoom() {
		this.phase = RoomPhase.COMPLETE;
		
		AbstractDungeon.dialog.clear();
		
		for (final AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof InfiniteSpeechBubble) {
                ((InfiniteSpeechBubble)e).dismiss();
            }
        }
		
		merchant = new PerkMerchant();
	}
	
	public void setPerkMerchant(PerkMerchant merchant) {
		this.merchant = merchant;
	}

	@Override
	public CardRarity getCardRarity(int roll) {
		return AbstractCard.CardRarity.RARE;
	}

	@Override
	public void onPlayerEntry() {
		AbstractDungeon.overlayMenu.proceedButton.setLabel("Finish.");
	}
	
	@Override
	public void update() {
		super.update();
		
		if(this.merchant != null) this.merchant.update();
	}
	
	public void render(SpriteBatch sb) {
		if(this.merchant != null) this.merchant.render(sb);
		
		super.render(sb);
		this.renderTips(sb);
	}
}
