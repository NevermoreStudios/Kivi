package rs.kockasystems.kivi;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Kivi extends ApplicationAdapter implements InputProcessor
{
	// Variables
	private SpriteBatch batch;
	private Texture kiviTexture;
	private KiviBall ball;
	private BitmapFont font;
	private final Random rnd = new Random();
	private Music music;
	private final float ACCELERATION = 0.1f;
	private byte mode;
	private float soundVolume;
	private boolean enableParticles;
	private float label1, label2;
	
	// Game Modes
	private final byte MODE_CLASSIC 		= 0;
	private final byte MODE_TIMED 			= 1;
	private final byte MODE_CHALLENGE 		= 2;
	private final byte MODE_ACCURATE 		= 3;
	private final byte MODE_ACCURATE_FAST 	= 4;
	private final byte MODE_WALLS 			= 5;
	
	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		kiviTexture = new Texture("Ball.png");
		ball = new KiviBall((rnd.nextFloat() - 0.5f) * 10, (rnd.nextFloat() - 0.5f) * 10);
		font = new BitmapFont();
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
		//music.play();
		
		mode = MODE_WALLS;
		
		label1 = 0;
		label2 = 30;
		
		
		Gdx.input.setInputProcessor(this);
		
	}
	
	@Override
	public void render ()
	{
		if(mode == MODE_CLASSIC)label2 += Gdx.graphics.getDeltaTime();
		else if(mode == MODE_TIMED || mode == MODE_CHALLENGE || mode == MODE_ACCURATE_FAST)label2 -= Gdx.graphics.getDeltaTime();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ball.update();
		batch.begin();
			ball.draw(batch);
			font.draw(batch, String.valueOf((int)label1), Gdx.graphics.getWidth() / 2 - 10, Gdx.graphics.getHeight() / 10);
			font.draw(batch, String.valueOf((int)label2), Gdx.graphics.getWidth() / 2 - 10, Gdx.graphics.getHeight() / 10 + 20);
		batch.end();
		if(((mode == MODE_CLASSIC || mode == MODE_CHALLENGE) && label1 == 0) || ((mode == MODE_TIMED || mode == MODE_CHALLENGE || mode == MODE_ACCURATE || mode == MODE_ACCURATE_FAST || mode == MODE_WALLS) && (int)label2 == 0))
		{
			music.stop();
			music.dispose();
			System.exit(0);
		}
	}
	
	public class KiviBall
	{
		
		public float x = Gdx.graphics.getWidth() / 2, y = Gdx.graphics.getHeight() / 2, vx, vy;
		
		public KiviBall(float vx, float vy)
		{
			this.vx = vx;
			this.vy = vy;
		}
		
		public void draw(SpriteBatch batch)
		{
			batch.draw(kiviTexture, x - (kiviTexture.getWidth() / 2), y - (kiviTexture.getHeight() / 2));
		}
		
		public void update()
		{
			x += vx;
			y += vy;
			if(x + (kiviTexture.getWidth() / 2) >= Gdx.graphics.getWidth() || x - (kiviTexture.getWidth() / 2) < 0)
			{
				vx = -vx;
				if(mode == MODE_WALLS)label2--;
			}
			if(y + (kiviTexture.getHeight() / 2) >= Gdx.graphics.getHeight() || y - (kiviTexture.getHeight() / 2) < 0){
				vy = -vy;
				if(mode == MODE_WALLS)label2--;
			}
		}
		
	}
	
	class Effect
	{
		private String name;
		private float x, y;
		private ParticleEffect effect;
		private Sound sound;
		
		public Effect(final String name, final float x, final float y)
		{
			this.name = name;
			this.x = x;
			this.y = y;
			sound = Gdx.audio.newSound(Gdx.files.internal("effects/" + name + "/" + name + ".wav"));
			effect = new ParticleEffect();
			effect.setPosition(x, y);
		}
		
		public void play()
		{
			if(soundVolume > 0)sound.play();
			effect.start();
		}
	}
	
	class EffectsThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				
			}
		}
	}
	
	
	// Unused
	@Override
	public boolean keyDown(int keycode) { return false; }
	@Override
	public boolean keyUp(int keycode) { return false; }
	@Override
	public boolean keyTyped(char character) { return false; }
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }
	@Override
	public boolean scrolled(int amount) { return false; }
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(mode == MODE_ACCURATE || mode == MODE_ACCURATE_FAST)label1--;
		if(screenX > ball.x - (kiviTexture.getWidth() / 2) && screenX < ball.x + (kiviTexture.getWidth() / 2) && Gdx.graphics.getHeight() - screenY > ball.y - (kiviTexture.getHeight() / 2) && Gdx.graphics.getHeight() - screenY < ball.y + (kiviTexture.getHeight() / 2))if(dist(screenX, Gdx.graphics.getHeight() - screenY, ball.x, ball.y) <= kiviTexture.getHeight() / 2)
		{
			if(mode == MODE_CLASSIC || mode == MODE_CHALLENGE)label1--;
			else if ((mode == MODE_TIMED || mode == MODE_ACCURATE || mode == MODE_WALLS))label1++;
			else if (mode == MODE_ACCURATE_FAST)label1 += 3;
			ball.vx =                            screenX > ball.x ? ball.vx > 0 ? -ball.vx : ball.vx : ball.vx > 0 ? ball.vx : -ball.vx;
			ball.vy = Gdx.graphics.getHeight() - screenY > ball.y ? ball.vy > 0 ? -ball.vy : ball.vy : ball.vy > 0 ? ball.vy : -ball.vy;
			ball.vx = ball.vx > 0 ? ball.vx + ACCELERATION : ball.vx - ACCELERATION;
			ball.vy = ball.vy > 0 ? ball.vy + ACCELERATION : ball.vy - ACCELERATION;
		}
		return true;
	}
	
	private float dist(final float x1, final float y1, final float x2, final float y2){return (float)Math.sqrt((double)(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));}
	
}
