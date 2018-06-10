package infinitespire.perks.green;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import infinitespire.perks.AbstractPerk;

public class Retaliate extends AbstractPerk {
	
	public static final String NAME = "Retaliate";
    public static final String ID = "Retaliate";
    private static final String DESCRIPTION = "The first time your block is broken each turn, deal #y2 damage to all enemies.";
    private static final int TIER = 3;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.GREEN;
    private boolean willDealDamage;
	
	public Retaliate() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Dodge.ID);
	}
	
	@Override
	public void onTurnStart() {
		willDealDamage = true;
	}
	
	//this is broken as usual
	@Override
	public void onLoseBlock(int amount, boolean noAnimation) {
		
		if(!willDealDamage)
			return;
		
		
		AbstractPlayer player = AbstractDungeon.player;
		int[] dmg = new int[AbstractDungeon.getMonsters().monsters.size()];
		
		for(int i = 0; i < dmg.length; i++) {
			dmg[i] = 2;
		}
		
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(player, dmg, DamageType.THORNS, AttackEffect.FIRE));
		willDealDamage = false;
	}
}
