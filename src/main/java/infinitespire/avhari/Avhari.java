package infinitespire.avhari;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import infinitespire.InfiniteSpire;

public class Avhari {
	AnimatedNpc anim;

	public Avhari(float x, float y) {
		anim = new AnimatedNpc(
			(Settings.WIDTH / 2f) - (150f * Settings.scale),
			450f * Settings.scale,
			InfiniteSpire.createPath("ui/avhari/character/Avhari.atlas"),
			InfiniteSpire.createPath("ui/avhari/character/Avhari.json"),
			"idle");
		anim.skeleton.getRootBone().setScale(0.5f);
	}

	public void render(SpriteBatch sb) {
		anim.render(sb);
	}
}
