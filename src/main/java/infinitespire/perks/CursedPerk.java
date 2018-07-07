package infinitespire.perks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

import infinitespire.InfiniteSpire;

@Deprecated
public class CursedPerk extends AbstractPerk{
	
	public enum CursedPerkRarity {
		COMMON,
		UNCOMMON,
		RARE
	}
	
	private final CursedPerkRarity rarity;
	
	public CursedPerk(String name, String id, String description, int tier, CursedPerkRarity rarity) {
		super(name, id, description, tier, PerkTreeColor.CURSED);
		this.state = PerkState.UNLOCKED;
		this.rarity = rarity;
	}

	public void update(boolean allowClick){
    	super.update(allowClick);
    	
    	xPos = Settings.WIDTH / 2f;
    	yPos = Settings.HEIGHT / 2f;
    	
    	//determine the xOffset and yOffset here
    	switch(this.rarity) {
		case COMMON:
			
			break;
		case RARE:
			
			break;
		case UNCOMMON:
			
			break;
		default:
			InfiniteSpire.logger.info("How did you set a rarity that does not exist!");
			break;
    	}
    	
    	hitboxSize = 120f * Settings.scale;
    	hitboxOffset = (size - hitboxSize) / 2f;
    	hitbox.update(xPos - hitboxOffset, yPos - hitboxOffset);
    }
	
	public void render(SpriteBatch sb, boolean allowPurchace) {
		sb.setColor(Color.WHITE);
		
		Texture perkTexture = null;
		
		perkTexture = InfiniteSpire.getTexture("img/perks/cursed/" + this.id.toLowerCase() + ".png");
		
		if(perkTexture != null) {
			sb.draw(perkTexture, xPos, yPos, size, size);
		}
		
		renderCost(sb, allowPurchace);
		
		if(hitbox.hovered) {
			renderTip(sb);
		}
		
		hitbox.render(sb);
	}
}
