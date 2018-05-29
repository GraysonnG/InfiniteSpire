package infinitespire;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;

import infinitespire.perks.AbstractPerk;
import infinitespire.perks.blue.*;
import infinitespire.perks.green.*;
import infinitespire.perks.red.*;
import infinitespire.screens.PerkScreen;

public class InfiniteSpire {
	public static final String VERSION = "0.0.0";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
    public static HashMap<String, Texture> imgMap = new HashMap<String, Texture>();
    public static HashMap<String, AbstractPerk> allPerks = new HashMap<String, AbstractPerk>();
    public static ArrayList<AbstractCard> finalDeck = new ArrayList<AbstractCard>();
    public static int points = 0;
    public static boolean isRerun = false;
    
    public static PerkScreen perkscreen = new PerkScreen();
    
    
    public static Texture getTexture(final String textureString) {
        if (imgMap.get(textureString) == null) {
            loadTexture(textureString);
        }
        return imgMap.get(textureString);
    }
    
    private static void loadTexture(final String textureString) {
        logger.info("InfiniteSpire | Loading Texture: " + textureString);
        imgMap.put(textureString, new Texture(textureString));
    }
    
    public static void initialize() {
        logger.info("VERSION: 0.0.0");
        logger.info("InfiniteSpire | Initialize Start...");
        logger.info("InfiniteSpire | Initializing Red Perks...");
        allPerks.put(Strengthen.ID, new Strengthen());
        allPerks.put(SpikedArmor.ID, new SpikedArmor());
        allPerks.put(PowerUp.ID, new PowerUp());
        allPerks.put(Crit1.ID, new Crit1());
        allPerks.put(Crit2.ID, new Crit2());
        logger.info("InfiniteSpire | Initializing Green Perks...");
        allPerks.put(Fortify.ID, new Fortify());
        logger.info("InfiniteSpire | Initializing Blue Perks...");
        allPerks.put(Prepared.ID, new Prepared());
        logger.info("InfiniteSpire | Initialize Complete...");
    }
}
