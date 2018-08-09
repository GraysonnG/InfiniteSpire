package infinitespire.relics;

import java.util.Scanner;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;

public abstract class Relic extends AbstractRelic {
	
	@SpireEnum
	public static RelicTier QUEST;
	
	public Relic(String setId, String textureID, RelicTier tier, LandingSound sfx) {
		super(setId, "", tier, sfx);
		Texture texture = InfiniteSpire.getTexture("img/infinitespire/relics/" + textureID + ".png");
		Texture outline = InfiniteSpire.getTexture("img/infinitespire/relics/" + textureID + "-outline.png");
		img = texture;
		largeImg = texture;
		outlineImg = outline;
		this.description = getUpdatedDescription();
		this.initializeRelicTips();
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	private void initializeRelicTips(){
		Scanner desc = new Scanner(this.description);
        while (desc.hasNext()) {
            String s = desc.next();
            if (s.charAt(0) == '#') {
                s = s.substring(2);
            }
            s = s.replace(',', ' ');
            s = s.replace('.', ' ');
            s = s.trim();
            s = s.toLowerCase();
            boolean alreadyExists = false;
            if (GameDictionary.keywords.containsKey(s)) {
                s = GameDictionary.parentWord.get(s);
                for (final PowerTip t : this.tips) {
                    if (t.header.toLowerCase().equals(s)) {
                        alreadyExists = true;
                        break;
                    }
                }
                if (alreadyExists) {
                    continue;
                }
                this.tips.add(new PowerTip(TipHelper.capitalize(s), GameDictionary.keywords.get(s)));
            }
        }
        desc.close();
	}
}
