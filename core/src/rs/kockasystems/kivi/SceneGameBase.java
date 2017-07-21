package rs.kockasystems.kivi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class SceneGameBase extends Scene
{
	// Variables
	protected Texture kiviTexture;
	protected Sprite ball;
	protected KiviBall body;
	protected Music music;
	protected float label1, label2;
	protected List<Effect> effects;
	protected BitmapFont font;
	
	// Constants
	protected final static float ACCELERATION = 0.001f;
	protected final static Random rnd = new Random();
	
	public SceneGameBase()
	{
		super();
		kiviTexture = new Texture("Ball.png");
		ball 		= new Sprite();
		effects 	= new ArrayList<Effect>();
		font		= new BitmapFont();
		body		= new KiviBall(rnd.nextFloat(), rnd.nextFloat());
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/game.ogg"));
		music.setVolume(Config.soundVolume);
		music.play();
		
		ball.setRegion(kiviTexture);
		ball.setBounds((Gdx.graphics.getWidth() - kiviTexture.getWidth()) / 2, (Gdx.graphics.getHeight() - kiviTexture.getHeight()) / 2, kiviTexture.getWidth(), kiviTexture.getHeight());
	}
	
	public void render()
	{
		super.render();
		ball.draw(batch);
		font.draw(batch, String.valueOf((int)label1), Gdx.graphics.getWidth() / 2 - 10, Gdx.graphics.getHeight() / 10);
		font.draw(batch, String.valueOf((int)label2), Gdx.graphics.getWidth() / 2 - 10, Gdx.graphics.getHeight() / 10 + 20);
		if((Config.enableParticles || Config.soundVolume > 0) && effects.size() > 0)for(int i=0; i<effects.size(); ++i)
		{
			if(effects.get(i).started())effects.get(i).update(batch, Gdx.graphics.getDeltaTime());
			else if(effects.get(i).stopped())
			{
				effects.get(i).stop();
				effects.remove(i);
			}
		}
	}
	
	public void update()
	{
		super.update();
		updateBall();
		checkOver();
	}
	
	protected void updateBall()
	{
		final boolean COLLIDING_LEFT 	= collidingLeft();
		final boolean COLLIDING_RIGHT 	= collidingRight();
		final boolean COLLIDING_TOP		= collidingTop();
		final boolean COLLIDING_BOTTOM	= collidingBottom();
		
		if(COLLIDING_LEFT || COLLIDING_RIGHT) collideSide(COLLIDING_LEFT);
		if(COLLIDING_TOP || COLLIDING_BOTTOM) collideVertical(COLLIDING_BOTTOM);
		if(!COLLIDING_LEFT && !COLLIDING_TOP && !COLLIDING_BOTTOM && !COLLIDING_RIGHT) collideAngle(COLLIDING_BOTTOM);
	}
	
	protected boolean collidingLeft()	{ return false; }
	protected boolean collidingRight()	{ return false; }
	protected boolean collidingTop()	{ return false; }
	protected boolean collidingBottom()	{ return false; }
	
	protected void collideSide(final boolean left)
	{
		if(Config.enableParticles || Config.soundVolume > 0)effects.add(new Effect("bounce", (left ? left() : right()), body.y));
		body.vx = -body.vx;
	}
	
	protected void collideVertical(final boolean bottom)
	{
		if(Config.enableParticles || Config.soundVolume > 0)effects.add(new Effect("bounce", body.x, (bottom ? bottom() : top() )));
		body.vy = -body.vy;
	}
	
	protected void collideAngle(final boolean bottom)
	{
		body.vx = -body.vx;
		body.vy = -body.vy;
		if(Config.enableParticles || Config.soundVolume > 0)effects.add(new Effect("bounce", body.x, (bottom ? bottom() : top() )));
	}
	
	protected float top()	{ return body.y - (ball.getHeight() / 2); }
	protected float bottom(){ return body.y + (ball.getHeight() / 2); }
	protected float left()	{ return body.x - (ball.getWidth() 	/ 2); }
	protected float right()	{ return body.x + (ball.getWidth() 	/ 2); }
	
	protected void checkOver() { }
	
	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
	{	
		if(screenX > body.x - (kiviTexture.getWidth() / 2) && screenX < body.x + (kiviTexture.getWidth() / 2) && Gdx.graphics.getHeight() - screenY > body.y - (kiviTexture.getHeight() / 2) && Gdx.graphics.getHeight() - screenY < body.y + (kiviTexture.getHeight() / 2))if(dist(screenX, Gdx.graphics.getHeight() - screenY, body.x, body.y) <= kiviTexture.getHeight() / 2)
		{
			if(Config.enableParticles)effects.add(new Effect("click", body.x, body.y));
			onTouch();
			body.vx =                            screenX > body.x ? body.vx > 0 ? -body.vx : body.vx : body.vx > 0 ? body.vx : -body.vx;
			body.vy = Gdx.graphics.getHeight() - screenY > body.y ? body.vy > 0 ? -body.vy : body.vy : body.vy > 0 ? body.vy : -body.vy;
			body.vx = body.vx > 0 ? body.vx + ACCELERATION : body.vx - ACCELERATION;
			body.vy = body.vy > 0 ? body.vy + ACCELERATION : body.vy - ACCELERATION;
		}
		return true;	
	}
	
	protected void onTouch(){  }
	
	protected float dist(final float x1, final float y1, final float x2, final float y2){ return (float)Math.sqrt((double)(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2))); }
	
}
