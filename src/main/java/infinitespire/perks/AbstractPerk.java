package infinitespire.perks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.InputHelper;

import infinitespire.InfiniteSpire;

public abstract class AbstractPerk {
	
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
	
	public String name;
    public String id;
    public int tier;
    public PerkTreeColor tree;
    public PerkState state;
    public String desciption;
    
    private float xPos, yPos, size, origSize, hitboxSize, width, xOffset, hitboxOffset;
  
    
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
        
        size = 175f * Settings.scale;
        origSize = size;
        width = 1520f * Settings.scale;
        xOffset = Settings.WIDTH - width;
        hitboxOffset = (size - hitboxSize) / 2f;
        
        hitboxSize = 120f * Settings.scale; 
        //locked needs to be automatically false if tier = 0
	    state = PerkState.LOCKED;
	    
	    //derive this from tier and tree
	    
	    yPos = 100f;
	    xPos = 100f;
	    
	    
	    hitbox = new Hitbox(xPos + hitboxOffset, yPos + hitboxOffset, hitboxSize, hitboxSize);
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
    	

    	if(hitbox.justHovered) {
    		CardCrawlGame.sound.play("UI_HOVER");
    		this.size = origSize + 15;
    	}
    	
    	if(!hitbox.hovered) {
    		if(size > origSize) {
    			this.size -= 60 * Gdx.graphics.getDeltaTime();
    		}else if(size < origSize){
    			size = origSize;
    		}
    	}
    	
    	if(hitbox.hovered == true && InputHelper.justClickedLeft && this.state == PerkState.UNLOCKED) {
    		CardCrawlGame.sound.play("UI_CLICK_1");
    		CardCrawlGame.sound.play("UNLOCK_PING");
        	this.state = PerkState.ACTIVE;
    	}
    	
    	switch(tree) {
 		case BLUE:
 			xPos = ((width / 4f) * 3) - (size / 2f) + xOffset;
 			break;
 		case GREEN:
 			xPos = ((width * Settings.scale) / 4f) - (size / 2f) + xOffset;
 			break;
 		case RED:
 			xPos = ((width * Settings.scale) / 2f) - (size / 2f) + xOffset;
 			break;
 		default:
 			xPos = 100f;
 			break;
 	    	
 	    }
    	
    	hitboxSize = 120f * Settings.scale;
    	hitboxOffset = (size - hitboxSize) / 2f;
    	hitbox.update(xPos + hitboxOffset, yPos + hitboxOffset);
    	hitbox.transform(hitboxSize, hitboxSize);
    }

	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		Texture perkTexture = null;
		
    	perkTexture = getTextureByState();
    	
    	if(perkTexture != null)
    		sb.draw(perkTexture, xPos, yPos, size, size);
    	
    	hitbox.render(sb);
	}
	
	private Texture getTextureByState() {
		switch(state) {
		
		case LOCKED:
				return InfiniteSpire.getTexture("img/perks/" + this.tree.toString().toLowerCase() + "/locked.png");
			
		case UNLOCKED:
				return InfiniteSpire.getTexture("img/perks/" + this.tree.toString().toLowerCase()+ "/"+ this.id.toLowerCase() + ".png");
				
		case ACTIVE:
				return InfiniteSpire.getTexture("img/perks/" + this.tree.toString().toLowerCase() + "/" + this.id.toLowerCase() + "-active.png");
			
		default:
				return null;
		
		}
	}
}
