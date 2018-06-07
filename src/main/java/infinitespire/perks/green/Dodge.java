package infinitespire.perks.green;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.perks.AbstractPerk;

public class Dodge extends AbstractPerk {
	
	public static final String NAME = "Dodge";
    public static final String ID = "Dodge";
    private static final String DESCRIPTION = "Whenever you take #b7 or less unblocked #yAttack damage, reduce it to #y1";
    private static final int TIER = 2;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
	
	public Dodge() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Reinforce.ID);
	}

	@Override
	public void onDamageTaken(DamageInfo info, int[] damageAmount) {
		AbstractPlayer player = AbstractDungeon.player;
		
		int damage = damageAmount[0];
		
		System.out.println("Player taking " + damageAmount + " damage.");
		
		if(damage > player.currentBlock) {
			int hpDamage = damage - player.currentBlock;
			if(hpDamage <= 7) {
				damage = 1;
			}
		}
		damageAmount[0] = damage;
	}
	
	
}
