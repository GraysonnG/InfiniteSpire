package infinitespire.perks.green;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;

@SuppressWarnings("unused")
public class Retaliate extends AbstractPerk {
	
	public static final String NAME = "Retaliate";
    public static final String ID = "Retaliate";
    private static final String DESCRIPTION = "The first time your block is broken each turn, deal #y2 damage to all enemies.";
    private static final int TIER = 3;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
    private boolean willDealDamage;
    private int lastBlock = 0;
    private int curBlock = 0;
	
	public Retaliate() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Dodge.ID);
		willDealDamage = false;
	}

	@Override
	public void onTurnStart() {
		curBlock = AbstractDungeon.player.currentBlock;
		willDealDamage = true;
	}

	@Override
	public void onDamageTaken(DamageInfo info, int[] damageAmount) {
		if(damageAmount[0] >= 0 && lastBlock > 0 && curBlock == 0) {
			
			int[] dmg = new int[AbstractDungeon.getMonsters().monsters.size()];
			
			for(int i = 0; i < dmg.length; i++) {
				dmg[i] = 2;
			}
			
			AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(
					AbstractDungeon.player, 
					dmg, 
					DamageType.THORNS, 
					AttackEffect.FIRE));
			willDealDamage = false;
		}
		
		lastBlock = curBlock;
	}

	@Override
	public void onGainBlock(int blockAmount) {
		lastBlock = curBlock;
		curBlock += blockAmount;
	}

	@Override
	public void onLoseBlock(int amount, boolean noAnimation) {
		lastBlock = curBlock;
		curBlock -= amount;
	}
}
