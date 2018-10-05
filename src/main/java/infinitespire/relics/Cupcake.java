package infinitespire.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

public class Cupcake extends Relic {
	public static final String ID = InfiniteSpire.createID("Cupcake");
	public static final String NAME = "Cupcake";
	
	private static final int CARD_LIMIT = 6;
	
	public Cupcake() {
		super(ID, "cupcake", RelicTier.BOSS, LandingSound.MAGICAL);
	}
	
	public String getUpdatedDescription() {
		StringBuilder retVal = new StringBuilder();
		AbstractPlayer.PlayerClass pc = AbstractPlayer.PlayerClass.IRONCLAD;
		if(AbstractDungeon.player != null) {
			pc = AbstractDungeon.player.chosenClass;
		} 
		
		switch(pc) {
		case IRONCLAD:
			retVal.append(DESCRIPTIONS[1]);
			break;
		case THE_SILENT:
			retVal.append(DESCRIPTIONS[2]);
			break;
		case DEFECT:
			retVal.append(DESCRIPTIONS[3]);
			break;
		default:
			retVal.append(DESCRIPTIONS[1]);
			break;
		}
		retVal.append(DESCRIPTIONS[0]);
		return retVal.toString();
	}

	@Override
	public AbstractRelic makeCopy() {
		return new Cupcake();
	}

	@Override
	public void onEquip() {
		EnergyManager energy = AbstractDungeon.player.energy;
		++energy.energyMaster;
	}

	@Override
	public void onUnequip() {
		EnergyManager energy = AbstractDungeon.player.energy;
		--energy.energyMaster;
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		this.counter++;
		if(counter == CARD_LIMIT) {
			for (final AbstractCard c2 : AbstractDungeon.player.hand.group) {
                if (c2.costForTurn >= 0) {
                    c2.costForTurn += 1;
                    c2.isCostModifiedForTurn = true;
                    if(c2.costForTurn == 1 && c2.freeToPlayOnce == true) {
                    	c2.freeToPlayOnce = false;
                    }
                }
            }
		}
	}
	
	@Override
	public void onVictory() {
		counter = 0;
	}

	@Override
	public void atTurnStart() {
		counter = 0;
	}

	@Override
	public void onCardDraw(AbstractCard drawnCard) {
		if(counter >= CARD_LIMIT && drawnCard.cost >= 0) {
			drawnCard.setCostForTurn(drawnCard.cost + 1);
		}
	}
}
