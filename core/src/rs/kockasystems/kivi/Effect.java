package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Effect
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
		if(Config.soundVolume > 0) sound.setVolume(sound.play(), Config.soundVolume);
		if(Config.enableParticles)effect.start();
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
