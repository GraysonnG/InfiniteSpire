package infinitespire.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class BlackCardEffect extends AbstractGameEffect {
	public BlackCardEffect() {
		this.duration = 1.0f;

	}

	@Override
	public void update() {
		if(this.duration == 1.0f) {
			AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PURPLE.cpy(), true));
			AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.BLACK.cpy(), false));
		}

		this.duration -= Gdx.graphics.getDeltaTime();
		if(this.duration < 0.0f) {
			this.isDone = true;
		}
	}

	@Override
	public void render(SpriteBatch spriteBatch) {

	}
}
