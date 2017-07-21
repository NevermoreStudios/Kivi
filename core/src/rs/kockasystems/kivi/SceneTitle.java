package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class SceneTitle extends Scene
{
	private Skin skin;
	private TextureAtlas atlas;
	private Texture titleScreen;
	private Sprite button, holder, title;
	private boolean[] animate = new boolean[4], deanimate = new boolean[4];
	private Window aboutWindow;
	private BitmapFont font;
	private CheckBox preTitle, particles;
	private Slider volume;
	
	// Constants
	private static final String[] items = {"Start", "Settings", "About"};
	private static final int BUTTON_SPEED = 10;
	private static final int HOLDER_SPEED = 8;
	private static final float TITLE_SPEED = 0.009f;
	private static final int WINDOW_SPEED = 4;
	
	public SceneTitle()
	{
		super();
		atlas = new TextureAtlas("ui/ui.atlas"); // Creating TextureAtlas
		createFont();
		createSkin();
		createWindow();
		createSettings();
		createTitle();
		createSidebar();
	}
	
	private void createSkin()
	{
		skin = new Skin(Gdx.files.internal("ui/ui.json"));
		skin.addRegions(atlas);
		skin.getFont("default-font").getData().setScale(0.33f);
	}
	
	private void createWindow()
	{
		aboutWindow	= new Window("   About", skin);
		aboutWindow.setBounds(150, 0, 480, -10);
		stage.addActor(aboutWindow);
	}
	
	private void createSettings()
	{
		preTitle 	= new CheckBox("Show Pre Title", 	skin);
		particles 	= new CheckBox("Show particles", 	skin);
		volume		= new Slider(0, 1f, 0.05f, false, 	skin);
		preTitle.setBounds(-50, Gdx.graphics.getHeight() - 40, 50, 24);
		particles.setBounds(-50, Gdx.graphics.getHeight() - 70, 50, 24);
		preTitle.setBounds(-50, Gdx.graphics.getHeight() - 100, 50, 24);
		stage.addActor(preTitle);
		stage.addActor(particles);
		stage.addActor(volume);
	}
	
	private void createTitle()
	{
		titleScreen	= new Texture("images/Title.png");
		title = new Sprite();
		title.setRegion(titleScreen);
		title.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		title.setAlpha(0);
	}

	private void createSidebar()
	{
		// Setting the animation
		animate[0] = animate[1] = animate[2] = true;
		
		// Create button and holder sprites
		button		= skin.getSprite("MenuButton");
		holder		= skin.getSprite("Holder");
		
		// Set X and Y of buttons and holder
		button.setX(- button.getWidth());
		holder.setY(- holder.getHeight());
		
	}
	
	private void createFont()
	{
		font = new BitmapFont(Gdx.files.internal("ui/text.fnt"));
		font.getData().setScale(0.2f);
		font.setColor(0, 0, 0, 1);
	}

	
	@Override
	public void update()
	{
		updateAnimation();
		updateWindow();
	}
	
	private void updateAnimation()
	{
		if(animate[0] && button.getX() < holder.getWidth() + 30f)button.setX(button.getX() + BUTTON_SPEED);
		else if(button.getX() >= holder.getWidth())
		{
			animate[0] = false;
			button.setX(button.getX() - BUTTON_SPEED / 2);
		}
		
		if(deanimate[0] && button.getX() > - button.getWidth())button.setX(button.getX() - BUTTON_SPEED);
		
		if(animate[1] && holder.getY() < 30f)holder.setY(holder.getY() + HOLDER_SPEED);
		else if(holder.getY() >= 0)
		{
			animate[1] = false;
			holder.setY(holder.getY() - HOLDER_SPEED / 2);
		}
		
		if(title.getColor().a < 0.9f && animate[2])title.setAlpha(title.getColor().a + TITLE_SPEED);
		
		if(deanimate[2] && (title.getColor().a > 0.1f))title.setAlpha(title.getColor().a - TITLE_SPEED);
		else if(deanimate[2])Kivi.changeScene(new SceneGame());
		
		if(deanimate[1] && holder.getY() > - holder.getHeight())holder.setY(holder.getY() - BUTTON_SPEED);
		
	}
	
	@Override
	public void render()
	{
		super.render();
		title.draw(batch);
		batch.draw(holder, holder.getX(), holder.getY());
		for(int i = 0; i < items.length; ++i)
		{
			batch.draw(button, button.getX(), button.getHeight() * i);
			font.draw(batch, items[i], button.getX() + 4, (float)(i + 0.5) * button.getHeight());
		}
		
		aboutWindow.draw(batch, 1f);
		stage.draw();
		font.draw(batch, "Text about the game...", aboutWindow.getX() + 10f, aboutWindow.getY() + aboutWindow.getHeight() - 15f);
	}
	
	public void updateWindow()
	{
		if(animate[3] && aboutWindow.getHeight() < 300)
		{
			deanimate[3] = false;
			aboutWindow.setHeight(aboutWindow.getHeight() + WINDOW_SPEED);
		}
		else if(deanimate[3] && aboutWindow.getHeight() > -10)
		{
			deanimate[3] = false;
			aboutWindow.setHeight(aboutWindow.getHeight() - WINDOW_SPEED);
		}
	}
	
	@Override
	public boolean touchDown(final int x, final int y, final int b, final int p)
	{
		if(animate[3])deanimate[3] = true;
		for(int i = 0; i < items.length; ++i){if(x > button.getX() && x < button.getX() + button.getWidth() && Gdx.graphics.getHeight() - y > button.getHeight() * i && Gdx.graphics.getHeight() - y < button.getHeight() * (i + 1))handleClick(i);}
		return true;
	}
	
	private void handleClick(int item)
	{
		switch(item)
		{
			case 0 :
				
				deanimate[0] = deanimate[1] = deanimate[2] = true;
				
			break;
			case 1 :
				
				
				
			break;
			case 2 :
				
				animate[3] = true;
				
			break;
		}
	}
	
}