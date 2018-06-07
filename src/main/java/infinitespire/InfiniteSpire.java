package infinitespire;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import infinitespire.perks.AbstractPerk;
import infinitespire.perks.AbstractPerk.PerkState;
import infinitespire.perks.AbstractPerk.PerkTreeColor;
import infinitespire.perks.blue.*;
import infinitespire.perks.green.*;
import infinitespire.perks.red.*;
import infinitespire.screens.PerkScreen;

public class InfiniteSpire {
	public static final String VERSION = "0.0.0";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
   
	public static HashMap<String, Texture> imgMap = new HashMap<String, Texture>();
    public static HashMap<String, AbstractPerk> allPerks = new HashMap<String, AbstractPerk>();
   
    public static boolean isRerun = false;
    
    public static int points = 0;
    public static int ascensionLevel = 0;
   
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
    
    public static void saveData() {
    	logger.info("InfiniteSpire | Saving Data...");
    	try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.setBool("rerun", isRerun);
			config.setInt("points", points);
			config.setInt("level", ascensionLevel);
			for(AbstractPerk perk : allPerks.values()) {
				config.setString(perk.id, perk.state.toString());
			}
			
			config.save();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void clearData() {
    	logger.info("InfiniteSpire | Clearing Saved Data...");
    	for(AbstractPerk perk : allPerks.values()) {
    		if(!(perk.tier == 0)) {
    			perk.state = PerkState.LOCKED;
    		}else {
    			if(perk.tree != PerkTreeColor.BLUE)
    				perk.state = PerkState.UNLOCKED;
    		}
    	}
    	points = 0;
    	isRerun = false;
    	ascensionLevel = 0;
    	saveData();
    }
    
    public static void loadData() {
    	logger.info("InfiniteSpire | Loading Data...");
    	try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.load();
			
			isRerun = config.getBool("rerun");
			
			if(isRerun) {
				for(AbstractPerk perk : allPerks.values()) {
					perk.state = PerkState.valueOf(config.getString(perk.id));
				}
				points = config.getInt("points");
				ascensionLevel = config.getInt("level");
			}
			
			
		} catch (IOException e) {
			logger.error("Failed to load InfiniteSpire data!");
			e.printStackTrace();
		}
    	
    }
    
    public static void initialize() {
        logger.info("VERSION: 0.0.0");
        logger.info("InfiniteSpire | Initialize Start...");
        logger.info("InfiniteSpire | Initializing allPerks...");
        //RED
        allPerks.put(Strengthen.ID, new Strengthen());
        allPerks.put(SpikedArmor.ID, new SpikedArmor());
        allPerks.put(PowerUp.ID, new PowerUp());
        allPerks.put(Crit1.ID, new Crit1());
        allPerks.put(Crit2.ID, new Crit2());
        allPerks.put(Invigorate.ID, new Invigorate());
        //GREEN
        allPerks.put(Fortify.ID, new Fortify());
        allPerks.put(Reinforce.ID, new Reinforce());
        allPerks.put(Dodge.ID, new Dodge());
        //BLUE
        allPerks.put(Prepared.ID, new Prepared());
        logger.info("InfiniteSpire | Initialize Complete...");
    }
}
