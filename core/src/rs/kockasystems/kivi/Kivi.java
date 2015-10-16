package rs.kockasystems.kivi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class Kivi extends ApplicationAdapter implements InputProcessor
{
	// Variables
	private SpriteBatch batch;
	private Texture kiviTexture;
	private KiviBall ball;
	private BitmapFont font;
	private final Random rnd = new Random();
	private Music music;
	private final float ACCELERATION = 0.001f;
	private byte mode;
	private float soundVolume;
	private boolean enableParticles;
	private float label1, label2;
	private List<Effect> effects;
	private OrthographicCamera camera;
	private TiledMap map;
	private TiledMapRenderer renderer;
	private TmxMapLoader loader;
	private float[] vertices;
	
	// Game Modes
	private final byte MODE_CLASSIC 		= 0;
	private final byte MODE_TIMED 			= 1;
	private final byte MODE_CHALLENGE 		= 2;
	private final byte MODE_ACCURATE 		= 3;
	private final byte MODE_ACCURATE_FAST 	= 4;
	private final byte MODE_WALLS 			= 5;
	private final byte MODE_MAP				= 6;
	
	@Override
	public void create ()
	{
		batch = new SpriteBatch();
		kiviTexture = new Texture("Ball.png");
		ball = new KiviBall((rnd.nextFloat() - 0.5f) * 5, (rnd.nextFloat() - 0.5f) * 5);
		font = new BitmapFont();
		effects = new ArrayList<Effect>();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		loader = new TmxMapLoader();
		map = loader.load("maps/2.tmx");
		vertices = ((PolylineMapObject)map.getLayers().get("Objects").getObjects().get(0)).getPolyline().getTransformedVertices();
		renderer = new OrthogonalTiledMapRenderer(map);
		
		soundVolume = 1f;
		enableParticles = true;
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
		//music.play();
		
		mode = MODE_MAP;
		
		label1 = 0;
		label2 = 30;
		
		
		Gdx.input.setInputProcessor(this);
		
	}
	
	@Override
	public void render ()
	{
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(mode == MODE_CLASSIC)label2 += Gdx.graphics.getDeltaTime();
		else if(mode == MODE_TIMED || mode == MODE_CHALLENGE || mode == MODE_ACCURATE_FAST)label2 -= Gdx.graphics.getDeltaTime();
        
		ball.update();
		camera.update();
        renderer.setView(camera);
        renderer.render();
        
		batch.begin();
			
			ball.draw(batch);
			font.draw(batch, String.valueOf((int)label1), Gdx.graphics.getWidth() / 2 - 10, Gdx.graphics.getHeight() / 10);
			font.draw(batch, String.valueOf((int)label2), Gdx.graphics.getWidth() / 2 - 10, Gdx.graphics.getHeight() / 10 + 20);
			if((enableParticles || soundVolume > 0) && effects.size() > 0)for(int i=0; i<effects.size(); ++i)
			{
				if(effects.get(i).started())effects.get(i).update(batch, Gdx.graphics.getDeltaTime());
				else if(effects.get(i).stopped())
				{
					effects.get(i).stop();
					effects.remove(i);
				}
			}
			
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
			batch.draw(kiviTexture, left(), top());
		}
		
		public void update()
		{
			x += vx;
			y += vy;
			camera.translate(vx, vy);
			
			final boolean COLLIDING_LEFT 	= mode == MODE_MAP ? isColliding() 	: left() < 0;
			final boolean COLLIDING_RIGHT 	= mode == MODE_MAP ? isColliding() 	: right() >= Gdx.graphics.getWidth();
			final boolean COLLIDING_TOP		= mode == MODE_MAP ? isColliding() 	: top() >= Gdx.graphics.getWidth();
			final boolean COLLIDING_BOTTOM	= mode == MODE_MAP ? isColliding() 	: bottom() < 0;
			if(COLLIDING_LEFT || COLLIDING_RIGHT)
			{
				if(enableParticles || soundVolume > 0)effects.add(new Effect("bounce", (COLLIDING_LEFT ? left() : right()), y));
				vx = -vx;
				if(mode == MODE_WALLS)label2--;
			}
			if(COLLIDING_TOP || COLLIDING_BOTTOM)
			{
				if(enableParticles || soundVolume > 0)effects.add(new Effect("bounce", x, (COLLIDING_BOTTOM ? bottom() : top() )));
				vy = -vy;
				if(mode == MODE_WALLS)label2--;
			}
		}
		
		private boolean isColliding()
		{
		    for (int i = 0; i < vertices.length; i += 2)
		    {
		        if (i == 0){ if (Intersector.intersectSegmentCircle(new Vector2(vertices[vertices.length-2], vertices[vertices.length-1]), new Vector2(vertices[i] / 2, vertices[i + 1] / 2), new Vector2(x, y), (kiviTexture.getWidth() / 2) * (kiviTexture.getWidth() / 2)))return true; }
		        else if (Intersector.intersectSegmentCircle(new Vector2(vertices[i - 2], vertices[i - 1]), new Vector2(vertices[i], vertices[i + 1]), new Vector2(x, y), (kiviTexture.getWidth() / 2) * (kiviTexture.getWidth() / 2))){
		        	System.out.println(new Vector2(vertices[i - 2], 800 - vertices[i - 1]) + " " + new Vector2(vertices[i], 800 - vertices[i + 1]) + " " + new Vector2(x, y));
		        	return true;
		        }
		    }
			return false;
		}
		
		private float top(){ return y - (kiviTexture.getHeight() / 2); }
		private float bottom(){ return y + (kiviTexture.getHeight() / 2); }
		private float left(){ return x - (kiviTexture.getWidth() / 2); }
		private float right(){ return x + (kiviTexture.getWidth() / 2); }
		
	}
	
	class Effect
	{
		private ParticleEffect effect;
		private Sound sound;
		private boolean started;
		
		public Effect(final String name, final float x, final float y)
		{
			sound = Gdx.audio.newSound(Gdx.files.internal("effects/" + name + "/" + name + ".wav"));
			effect = new ParticleEffect();
			effect.load(Gdx.files.internal("effects/" + name + "/" + name + ".particle"), Gdx.files.internal("images"));
			effect.setPosition(x, y);
			started = true;
			if(soundVolume > 0) sound.setVolume(sound.play(), soundVolume);
			if(enableParticles)effect.start();
		}
		
		public void update(SpriteBatch batch, float delta)
		{
			started = !effect.isComplete();
			effect.draw(batch, delta);
		}
		
		public void stop()
		{
			effect.dispose();
			sound.dispose();
		}
		
		public boolean started() { return started; }
		public boolean stopped() { return effect.isComplete(); }
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
			if(enableParticles)effects.add(new Effect("click", ball.x, ball.y));
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
