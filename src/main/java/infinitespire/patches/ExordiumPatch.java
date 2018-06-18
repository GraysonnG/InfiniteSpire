package infinitespire.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.GameSavedEffect;

import infinitespire.InfiniteSpire;
import infinitespire.rooms.PerkRoom;

@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.Exordium", method="ctor")
public class ExordiumPatch {
	public static void Postfix(Exordium __instance, AbstractPlayer player, ArrayList<String> emptyList) {
		if(InfiniteSpire.isRerun) {
			AbstractDungeon.currMapNode.room = new PerkRoom();
			AbstractDungeon.nextRoom.room = new NeowRoom(false);
			
			SaveFile saveFile = new SaveFile(SaveFile.SaveType.ENTER_ROOM);
            SaveAndContinue.save(saveFile);
            AbstractDungeon.effectList.add(new GameSavedEffect());
			
			for(AbstractRelic relic : AbstractDungeon.player.relics) {
				switch(relic.tier) {
				case BOSS:
					AbstractDungeon.bossRelicPool.remove(relic.relicId);
					break;
				case COMMON:
					AbstractDungeon.commonRelicPool.remove(relic.relicId);
					break;
				case DEPRECATED:
					break;
				case RARE:
					AbstractDungeon.rareRelicPool.remove(relic.relicId);
					break;
				case SHOP:
					AbstractDungeon.shopRelicPool.remove(relic.relicId);
					break;
				case SPECIAL:
					break;
				case STARTER:
					break;
				case UNCOMMON:
					AbstractDungeon.uncommonRelicPool.remove(relic.relicId);
					break;
				default:
					break;
				
				}
			}
		}
	}
}
