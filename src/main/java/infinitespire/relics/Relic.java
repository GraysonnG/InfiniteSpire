package infinitespire.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;

public abstract class Relic extends AbstractRelic {
	
	public Relic(String setId, String textureID, RelicTier tier, LandingSound sfx) {
		super(setId, "", tier, sfx);
		Texture texture = InfiniteSpire.getTexture("img/relics/" + textureID + ".png");
		Texture outline = InfiniteSpire.getTexture("img/relics/" + textureID + "-outline.png");
		img = texture;
		largeImg = texture;
		outlineImg = outline;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
}
