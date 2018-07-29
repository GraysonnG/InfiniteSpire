package infinitespire.powers;

import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;

public class RealityShiftPower extends AbstractPower {
	public RealityShiftPower(Nightmare nightmare) {
		this.owner = nightmare;
		this.amount = 50;
		this.name = "Reality Shift";
		this.ID = "is_Reality_Shift";
		this.img = InfiniteSpire.getTexture("img/powers/realityshift.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.updateDescription();
	}
	
	
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(!isPlayer){
			this.amount = 50;
		}
	}



	public void updateDescription() {
		this.description = "After taking #b" + amount + " damage in a single turn, Nighmare will shift reality forcing you to end your turn.";
	}
}
