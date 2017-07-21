package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;

public class ScenePractice extends SceneGameBase
{
	private byte mode;
	
	// Game Modes
	private final static byte MODE_CLASSIC 			= 0;
	private final static byte MODE_TIMED 			= 1;
	private final static byte MODE_CHALLENGE 		= 2;
	private final static byte MODE_ACCURATE 		= 3;
	private final static byte MODE_ACCURATE_FAST 	= 4;
	private final static byte MODE_WALLS 			= 5;
	
	public ScenePractice(final byte mode) { this.mode = mode; }
	
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
	{
		if(mode == MODE_ACCURATE || mode == MODE_ACCURATE_FAST)label1--;
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	protected void checkOver()
	{
		if(mode == MODE_CLASSIC)label2 += Gdx.graphics.getDeltaTime();
		else if(mode == MODE_TIMED || mode == MODE_CHALLENGE || mode == MODE_ACCURATE_FAST)label2 -= Gdx.graphics.getDeltaTime();
		if(((mode == MODE_CLASSIC || mode == MODE_CHALLENGE) && label1 == 0) || ((mode == MODE_TIMED || mode == MODE_CHALLENGE || mode == MODE_ACCURATE || mode == MODE_ACCURATE_FAST || mode == MODE_WALLS) && (int)label2 == 0))
		{
			music.stop();
			music.dispose();
			System.exit(0);
		}
	}
	
	protected void onTouch()
	{
		if(mode == MODE_CLASSIC || mode == MODE_CHALLENGE)label1--;
		else if ((mode == MODE_TIMED || mode == MODE_ACCURATE || mode == MODE_WALLS))label1++;
		else if (mode == MODE_ACCURATE_FAST)label1 += 3;
	}
	
	protected boolean collidingLeft()	{ return left() < 0; }
	protected boolean collidingRight()	{ return right() >= Gdx.graphics.getWidth(); }
	protected boolean collidingTop()	{ return top() >= Gdx.graphics.getWidth(); }
	protected boolean collidingBottom()	{ return bottom() < 0; }
	
	protected void collideSide(final boolean p)
	{
		if(mode == MODE_WALLS)label2--;
		super.collideSide(p);
	}
	
	protected void collideVertical(final boolean p)
	{
		if(mode == MODE_WALLS)label2--;
		super.collideVertical(p);
	}
	
	protected void collideAngle(final boolean p)
	{
		if(mode == MODE_WALLS)label2--;
		super.collideAngle(p);
	}
	
}
