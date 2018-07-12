package infinitespire;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import basemod.BaseMod;
import basemod.interfaces.*;
import infinitespire.cards.*;
import infinitespire.events.*;
import infinitespire.perks.AbstractPerk;
import infinitespire.perks.AbstractPerk.*;
import infinitespire.perks.blue.*;
import infinitespire.perks.cursed.Timed;
import infinitespire.perks.green.*;
import infinitespire.perks.red.*;
import infinitespire.relics.*;
import infinitespire.screens.*;
import replayTheSpire.ReplayTheSpireMod;

import fruitymod.FruityMod;
import fruitymod.patches.AbstractCardEnum;

@SuppressWarnings("deprecation")
@SpireInitializer
public class InfiniteSpire implements PostCampfireSubscriber, PostInitializeSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber, PostRenderSubscriber {
	public static final String VERSION = "0.0.1";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());
   
	public static HashMap<String, Texture> imgMap = new HashMap<String, Texture>();
    public static HashMap<String, AbstractPerk> allPerks = new HashMap<String, AbstractPerk>();
    public static HashMap<String, AbstractPerk> allCurses = new HashMap<String, AbstractPerk>();
    
    public static int points = 0;
    public static int ascensionLevel = 0;
   
    public static PerkScreen perkscreen = new PerkScreen();
    
    private enum LoadType {
    	RELIC,
    	CARD,
    	KEYWORD,
    }
    
    public InfiniteSpire() {
    	BaseMod.subscribe(this);
    }
    
    public static void initialize() {
        logger.info("VERSION: 0.0.1");
        new InfiniteSpire();
        //Settings.isDebug = true;
    }
    
    @Override
	public void receivePostInitialize() {
		initializePerks();
		
		Texture modBadge = getTexture("img/modbadge.png");
		BaseMod.registerModBadge(modBadge, "Infinite Spire", "Blank The Evil", "Adds a new way to play Slay the Spire, no longer stop after the 3rd boss. Keep fighting and gain perks as you climb.", null);
    
		String eventStrings = Gdx.files.internal("local/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
		
		BaseMod.addEvent(EmptyRestSite.ID, EmptyRestSite.class, BaseMod.EventPool.ANY);
		//BaseMod.addEvent(StrangeLightPillar.ID, StrangeLightPillar.class, BaseMod.EventPool.ANY);
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
        	try {
        		loadTexture(textureString);
        	} catch (GdxRuntimeException e) {
        		logger.error("Could not find texture: " + textureString);
	        	return getTexture("img/ui/missingtexture.png");
        	}
        }
        return imgMap.get(textureString);
    }
    
    private static void loadTexture(final String textureString) throws GdxRuntimeException {
        logger.info("InfiniteSpire | Loading Texture: " + textureString);
        imgMap.put(textureString, new Texture(textureString));
    }
    
    public static void saveData() {
    	logger.info("InfiniteSpire | Saving Data...");
    	try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
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
    		if(perk.tier == 0) {
    			perk.state = PerkState.UNLOCKED;
    		}else {
    			perk.state = PerkState.LOCKED;
    		}
    	}
    	points = 0;
    	ascensionLevel = 0;
    	saveData();
    }
    
    public static void loadData() {
    	logger.info("InfiniteSpire | Loading Data...");
    	try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.load();
			
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
		
		} catch (IOException | NumberFormatException e) {
			logger.error("Failed to load InfiniteSpire data!");
			e.printStackTrace();
			clearData();
		}
    	
    }
    
    private static void initializeRelics() {
    	logger.info("InfiniteSpire | Initializing relics...");
    	
    	String jsonString = Gdx.files.internal("local/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, jsonString);
    	
		RelicLibrary.add(new GolemsMask());
		RelicLibrary.add(new LycheeNut());
		RelicLibrary.add(new Cupcake());
		RelicLibrary.add(new MagicFlask());
		RelicLibrary.add(new CubicDiamond());
		RelicLibrary.add(new MidasBlood());
		RelicLibrary.add(new BeetleShell());
		RelicLibrary.add(new BlanksBlanky());
		RelicLibrary.add(new LuckyRock());
		
		RelicLibrary.addBlue(new Freezer());
		
		RelicLibrary.addRed(new BurningSword());
		
		initializeCrossoverRelics();
		
    	//RelicLibrary.add(new BottledSoul()); //This relic is broken
    }
    
    private static void initializeCrossoverRelics() {
    	try {
			initializeReplayTheSpire(LoadType.RELIC);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect ReplayTheSpire...");
		}
    	try {
			initializeFruityMod(LoadType.RELIC);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect FruityMod...");
		}
    }
    
    @SuppressWarnings("unused")
	private static void initializeCrossoverCards() {
    	try {
			initializeReplayTheSpire(LoadType.CARD);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect ReplayTheSpire...");
		}
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
        //CURSES
        allCurses.put(Timed.ID, new Timed());
        
        loadData();
    }
    
    private static void initializeCards() {
    	logger.info("InfiniteSpire | Initializing cards...");
    	BaseMod.addCard(new OneForAll());
    	BaseMod.addCard(new Neurotoxin());
    }

	@Override
	public void receivePostRender(SpriteBatch sb) {
		
	}

	@Override
	public boolean receivePostCampfire() {
		boolean somethingSelected = true;  
		
		somethingSelected = MagicFlask.shouldFinishCampfire();
		
		return somethingSelected;
	}
	
	@SuppressWarnings("unused")
	private static void initializeReplayTheSpire(LoadType type) throws ClassNotFoundException, NoClassDefFoundError {
		Class<ReplayTheSpireMod> replayTheSpire = ReplayTheSpireMod.class;
		logger.info("InfiniteSpire | InfiniteSpire has successfully detected Replay The Spire!");
		
		if(type == LoadType.RELIC) {
			logger.info("InfiniteSpire | Initializing Relics for Replay The Spire...");
			RelicLibrary.add(new BrokenMirror());
		}
		if(type == LoadType.CARD) {
			logger.info("InfiniteSpire | Initializing Cards for Replay The Spire...");
		}
	}
	
	@SuppressWarnings("unused")
	private static void initializeFruityMod(LoadType type)throws ClassNotFoundException, NoClassDefFoundError {
		Class<FruityMod> fruityMod = FruityMod.class;
		logger.info("InfiniteSpire | InfiniteSpire has successfully detected FruityMod!");
		
		if(type == LoadType.RELIC) {
			logger.info("InfiniteSpire | Initializing Relics for FruityMod...");
			BaseMod.addRelicToCustomPool(new SpectralDust(), AbstractCardEnum.SEEKER_PURPLE.toString());
		}
		if(type == LoadType.CARD) {
			logger.info("InfiniteSpire | Initializing Cards for FruityMod...");
		}
	}

}
