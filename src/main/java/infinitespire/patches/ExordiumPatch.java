package infinitespire.patches;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import infinitespire.InfiniteSpire;
import infinitespire.rooms.PerkRoom;

@SpirePatch(cls="com.megacrit.cardcrawl.dungeons.Exordium", method="ctor")
public class ExordiumPatch {
	public static void Postfix(Exordium __instance, AbstractPlayer player, ArrayList<String> emptyList) {
		if(InfiniteSpire.isRerun) {
			AbstractDungeon.currMapNode.room = new PerkRoom();
			for(AbstractRelic relic : AbstractDungeon.player.relics) {
				switch(relic.tier) {
				case BOSS:
					AbstractDungeon.bossRelicPool.remove(relic.relicId);
					break;
				case COMMON:
					break;
				case DEPRECATED:
					break;
				case RARE:
					break;
				case SHOP:
					break;
				case SPECIAL:
					break;
				case STARTER:
					break;
				case UNCOMMON:
					break;
				default:
					break;
				
				}
			}
		}
	}
}
