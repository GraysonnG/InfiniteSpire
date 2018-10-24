package infinitespire.effects.simpleVFX;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect;

public class ColoredFloatyEffect extends DeathScreenFloatyEffect {


	public ColoredFloatyEffect(Color c){
		super();
		this.color = c.cpy();
	}

	public void render(SpriteBatch sb) {
		if(!color.equals(Color.BLACK)){
			sb.setBlendFunction(770,1);
		}

		super.render(sb);

		sb.setBlendFunction(770, 771);
	}
}
