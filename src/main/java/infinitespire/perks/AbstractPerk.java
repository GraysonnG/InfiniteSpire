package infinitespire.perks;

import com.megacrit.cardcrawl.cards.DamageInfo;

public abstract class AbstractPerk {
	public String name;
    public String id;
    public int tier;
    public PerkTreeColor tree;
    public boolean isLocked;
    public boolean isActive;
    public String desciption;
    
    public enum PerkTreeColor
    {
        RED, 
        GREEN, 
        BLUE; 
    }
    
    public AbstractPerk(final String name, final String id, final String description, final int tier, final PerkTreeColor tree) {
        this.name = name;
        this.id = id;
        this.isLocked = true;
        this.isActive = false;
    }
    
    public void onCombatStart() {
    }
    
    public void onCombatVictory() {
    }
    
    public void onCombatLoss() {
    }
    
    public void onTurnStart() {
    }
    
    public void onDamageDelt(final DamageInfo info) {
    }
    
    public void onDamageTaken(final DamageInfo info) {
    }
    
    public void renderPerksInMenu() {
    }
}
