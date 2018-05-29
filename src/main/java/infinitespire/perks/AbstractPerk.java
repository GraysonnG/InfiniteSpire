package infinitespire.perks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;

import infinitespire.InfiniteSpire;

public abstract class AbstractPerk {
	public String name;
    public String id;
    public int tier;
    public PerkTreeColor tree;
    public PerkState state;
    public String desciption;
    
    private float xPos, yPos;
    
   	private Hitbox hitbox;
    
    public enum PerkTreeColor
    {
        RED, 
        GREEN, 
        BLUE; 
    }
    
    public enum PerkState {
    	LOCKED,
    	UNLOCKED,
    	ACTIVE
    }
    
    public AbstractPerk(String name, String id, String description, int tier, PerkTreeColor tree) {
        this.name = name;
        this.id = id;
        this.desciption = description;
        this.tier = tier;
        this.tree = tree;
        
        
        //locked needs to be automatically false if tier = 0
	    state = PerkState.LOCKED;
	    
	    //derive this from tier and tree
	    switch(tree) {
		case BLUE:
			xPos = (((1520f * Settings.scale) / 4f) * 3) - (175f / 2f);
			break;
		case GREEN:
			xPos = ((1520f * Settings.scale) / 4f) - (175f / 2f);
			break;
		case RED:
			xPos = ((1520f * Settings.scale) / 2f) - (175f / 2f);
			break;
		default:
			xPos = 100f;
			break;
	    	
	    }
	    
	    yPos = 100f;
	    
	    
	    hitbox = new Hitbox(xPos, yPos, 120f, 120f);
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
    
    public void update() {
    	hitbox.update();
    }

	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		Texture perkTexture = null;
		
    	perkTexture = getTextureByState();
    	
    	if(perkTexture != null)
    		sb.draw(perkTexture, xPos, yPos);
    	
    	hitbox.render(sb);
	}
	
	private Texture getTextureByState() {
		switch(state) {
		
		case LOCKED:
				return InfiniteSpire.getTexture("img/perks/" + this.tree.toString().toLowerCase() + "/locked.png");
			
		case UNLOCKED:
				return InfiniteSpire.getTexture("img/perks/" + this.tree.toString().toLowerCase()+ "/"+ this.id + ".png");
				
		case ACTIVE:
				return InfiniteSpire.getTexture("img/perks/" + this.tree.toString().toLowerCase() + "/" + this.id + "-active.png");
			
		default:
				return null;
		
		}
	}
}
