package rs.kockasystems.kivi;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Scene implements InputProcessor
{
	protected SpriteBatch batch;
	protected Stage stage;
	
	public Scene()
	{
		batch = new SpriteBatch();
		stage = new Stage();
	}
	
	public void preRender() {}
	
	public void render() { batch.begin(); }
	
	public void endRender() { batch.end(); }
	
	public void update() {}
	
	public void dispose() {}
	
	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) { return false; }
	@Override
	public boolean keyDown(final int keycode) { return false; }
	@Override
	public boolean keyUp(final int keycode) { return false; }
	@Override
	public boolean keyTyped(final char character) { return false; }
	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) { return false; }
	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer) { return false; }
	@Override
	public boolean mouseMoved(final int screenX, final int screenY) { return false; }
	@Override
	public boolean scrolled(final int amount) { return false; }
	
}
