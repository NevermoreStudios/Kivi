package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ScenePreTitle extends Scene
{
	// Variables
	private Sprite logo;
	private Texture logoTexture;
	private boolean animate;
	
	// Constants
	private static final float SPEED = 0.01f;
	
	public ScenePreTitle()
	{
		super();
		logoTexture = new Texture("images/logo.png");
		logo 		= new Sprite();
		logo.setRegion(logoTexture);
		logo.setAlpha(0);
		logo.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		animate = true;
	}
	
	public void render()
	{
		super.render();
		logo.draw(batch);
	}
	
	public void update()
	{
		super.update();
		if(logo.getColor().a < 1f && animate)
		{
			logo.setAlpha(logo.getColor().a + SPEED);
			if(logo.getColor().a > 0.9f)animate = false;
		}
		else if(logo.getColor().a > 0)logo.setAlpha(logo.getColor().a - SPEED);
		else Kivi.changeScene(new SceneTitle());
	}
	
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
	{
		animate = false;
		return true;
	}
	public boolean keyDown(final int a)
	{
		animate = false;
		return true;
	}
	
}
