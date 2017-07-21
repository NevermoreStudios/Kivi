package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;

public class KiviBall
{
	public float x = Gdx.graphics.getWidth() / 2, y = Gdx.graphics.getHeight() / 2, vx, vy;
	public KiviBall(final float vx, final float vy)
	{
		this.vx = vx;
		this.vy = vy;
	}
}
