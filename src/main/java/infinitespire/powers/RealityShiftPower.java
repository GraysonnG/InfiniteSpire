package infinitespire.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;

public class RealityShiftPower extends AbstractPower {
	public Nightmare nightmare;
	public boolean hasTriggered = false;

	public RealityShiftPower(Nightmare nightmare) {
		this.owner = nightmare;
		this.nightmare = nightmare;
		this.amount = 50;
		this.name = "Reality Shift";
		this.ID = "is_Reality_Shift";
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/realityshift.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.priority = -99999;
		this.updateDescription();
	}
	
	
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(!isPlayer){
			this.hasTriggered = false;
			this.amount = 50;
			this.updateDescription();
		}
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		this.amount -= damageAmount;
		if(this.amount <= 0 && !hasTriggered){
			nightmare.onEnoughDamageTaken();
			this.amount = 0;
			this.hasTriggered = true;
		}
		return super.onAttacked(info, damageAmount);
	}

	public void updateDescription() {
		this.description =
			"After taking #b"
				+ amount + " damage in a single turn, Nightmare will change its intent to a #r"+ nightmare.damage.get(0).base +" #rx #r"
				+ (nightmare.effectCount + 1) + " #rdamage #rattack and then shift reality, forcing you to end your turn.";
	}
}
