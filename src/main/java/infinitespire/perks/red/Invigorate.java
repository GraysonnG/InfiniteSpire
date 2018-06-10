package infinitespire.perks.red;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import infinitespire.InfiniteSpire;
import infinitespire.perks.AbstractPerk;

public class Invigorate extends AbstractPerk {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	public static final String NAME = "Invigorate";
    public static final String ID = "Invigorate";
    private static final String DESCRIPTION = "Strength is twice as effective.";
    private static final int TIER = 4;
    private static final PerkTreeColor TREE_COLOR = PerkTreeColor.RED;
	
	public Invigorate() {
		super(NAME, ID, DESCRIPTION, TIER, TREE_COLOR, Crit2.ID);
	}
	
	public void onAddPower(AbstractPower p) {
		//AbstractPlayer player = AbstractDungeon.player;
		
		if((p instanceof StrengthPower || p instanceof LoseStrengthPower) && p.owner == AbstractDungeon.player) {
			logger.info("Invigorate: "+ p.ID + ": " + p.amount);
			p.amount *= 2;
		}
	}
	
}
