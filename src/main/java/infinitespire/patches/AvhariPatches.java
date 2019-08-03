package infinitespire.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.vfx.FlameAnimationEffect;
import infinitespire.avhari.AvhariRoom;

import java.util.ArrayList;

public class AvhariPatches {

	private static ArrayList<FlameAnimationEffect> fEffects = new ArrayList<>();
	private static float flameVfxTimer = 0f;


	@SpirePatch(clz = MapRoomNode.class, method = "renderEmeraldVfx")
	public static class MapRoomNodeFlameEffectPatch {
		@SpirePrefixPatch
		public static void renderVFX(MapRoomNode __instance, SpriteBatch sb) {
			if(__instance.room instanceof AvhariRoom) {
				float scale = (float) ReflectionHacks.getPrivate(__instance, MapRoomNode.class, "scale");

				sb.setColor(Color.WHITE.cpy());
				for (FlameAnimationEffect e: fEffects) {
					e.render(sb, scale);
				}
			}
		}
	}
	@SpirePatch(clz = MapRoomNode.class, method = "updateEmerald")
	public static class UpdateParticlesFix {
		@SpirePrefixPatch
		public static void update(MapRoomNode __instance) {
			if(__instance.room instanceof AvhariRoom) {
				flameVfxTimer -= Gdx.graphics.getDeltaTime();
				if(flameVfxTimer < 0.0f) {
					flameVfxTimer = MathUtils.random(0.2f, 0.4f);
					fEffects.add(new FlameAnimationEffect(__instance.hb));
				}

				fEffects.removeIf((effect) -> {
					if(effect.isDone){
						effect.dispose();
						return true;
					}
					return false;
				});

				for(FlameAnimationEffect e : fEffects) {
					e.update();
				}
			}
		}
	}
}
