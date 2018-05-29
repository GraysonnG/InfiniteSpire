package infinitespire.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class SpikedArmorPower extends AbstractPower{
	public SpikedArmorPower(AbstractCreature owner, int amount) {
		this.owner = owner;
		this.amount = amount;
		this.name = "Spiked Armor";
		this.ID = "is_SpikePower";
		this.img = InfiniteSpire.getTexture("img/powers/spike.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}
	
	
	
	@Override
	public void onGainedBlock(float blockAmount) {
		int[] damage = new int[AbstractDungeon.getMonsters().monsters.size()];
		for(int i = 0; i < damage.length; i++) {
			damage[i] = amount;
		}
		
		AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(owner, damage, DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL));
	}



	public void updateDescription() {
		this.description = "When you gain block, deal " + amount + "damage to all enemies.";
	}
}
