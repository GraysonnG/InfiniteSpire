package infinitespire.abstracts;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import infinitespire.InfiniteSpire;

public abstract class Blight extends AbstractBlight {

	public static BlightStrings STRINGS;

	public Blight(String id, String textureString) {
		super(id, "", "", "", true);

		STRINGS = CardCrawlGame.languagePack.getBlightString(id);
		description = getDescription();
		name = STRINGS.NAME;

		img = InfiniteSpire.Textures.getRelicTexture(textureString + ".png");
		outlineImg = InfiniteSpire.Textures.getRelicTexture(textureString + "-outline.png");

		this.tips.clear();
		this.tips.add(new PowerTip(name, description));
	}

	private static String getDescription() {
		return STRINGS.DESCRIPTION[0];
	}
}
