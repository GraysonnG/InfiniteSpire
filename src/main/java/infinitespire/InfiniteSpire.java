package infinitespire;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import basemod.interfaces.PostRenderSubscriber;
import infinitespire.cards.*;
import infinitespire.perks.AbstractPerk;
import infinitespire.perks.AbstractPerk.PerkState;
import infinitespire.perks.AbstractPerk.PerkTreeColor;
import infinitespire.perks.blue.*;
import infinitespire.perks.green.*;
import infinitespire.perks.red.*;
import infinitespire.relics.BottledSoul;
import infinitespire.screens.PerkScreen;

@SuppressWarnings("unused")
@SpireInitializer
public class InfiniteSpire implements PostInitializeSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber, PostRenderSubscriber {
	public static final String VERSION = "0.0.1";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
   
	public static HashMap<String, Texture> imgMap = new HashMap<String, Texture>();
    public static HashMap<String, AbstractPerk> allPerks = new HashMap<String, AbstractPerk>();
   
    public static boolean isRerun = false;
    
    public static int points = 0;
    public static int ascensionLevel = 0;
   
    public static PerkScreen perkscreen = new PerkScreen();
    
    public InfiniteSpire() {
    	BaseMod.subscribe(this);
    }
    
    public static void initialize() {
        logger.info("VERSION: 0.0.0");
        new InfiniteSpire();
        Settings.isDebug = true;
    }
    
    @Override
	public void receivePostInitialize() {
		initializePerks();
		
		Texture modBadge = getTexture("img/modbadge.png");
		BaseMod.registerModBadge(modBadge, "Infinite Spire", "Blank The Evil", "Adds a new way to play Slay the Spire, no longer stop after the 3rd boss. Keep fighting and gain perks as you climb.", null);
	}

	@Override
	public void receiveEditKeywords() {
		
	}

	@Override
	public void receiveEditCards() {
		initializeCards();
	}

	@Override
	public void receiveEditRelics() {
		initializeRelics();
	}
   
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
					if(config.getString(perk.id) != null) {
						perk.state = PerkState.valueOf(config.getString(perk.id));
					}else
					{
						perk.state = PerkState.LOCKED;
					}
				}
				points = config.getInt("points");
				ascensionLevel = config.getInt("level");
			}
			
			
		} catch (IOException e) {
			logger.error("Failed to load InfiniteSpire data!");
			e.printStackTrace();
		}
    	
    }
    
    private static void initializeRelics() {
    	logger.info("InfiniteSpire | Initializing relics...");
    	
    	String jsonString = Gdx.files.internal("local/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, jsonString);
    	
    	//RelicLibrary.add(new BottledSoul());
    }
    
    private static void initializePerks() {
    	logger.info("InfiniteSpire | Initializing perks...");
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
        allPerks.put(Retaliate.ID, new Retaliate());
        allPerks.put(Overshield.ID, new Overshield());
        allPerks.put(Bulwark.ID, new Bulwark());
        //BLUE
        allPerks.put(Prepared.ID, new Prepared());
        allPerks.put(Calculated.ID, new Calculated());
        allPerks.put(Untouchable.ID, new Untouchable());
        allPerks.put(Ancient.ID, new Ancient());
        allPerks.put(Gamble.ID, new Gamble());
        allPerks.put(MirrorImage.ID, new MirrorImage());
        //DARK
        
    }
    
    private static void initializeCards() {
    	logger.info("InfiniteSpire | Initializing cards...");
    	BaseMod.addCard(new OneForAll());
    	BaseMod.addCard(new Neurotoxin());
    }

	@Override
	public void receivePostRender(SpriteBatch sb) {
		FontHelper.renderFontLeftTopAligned(sb, FontHelper.bannerFont, "EXP: " + points, 100, 100, Color.WHITE);
	}

}
