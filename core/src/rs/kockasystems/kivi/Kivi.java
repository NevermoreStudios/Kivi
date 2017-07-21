package rs.kockasystems.kivi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Kivi extends ApplicationAdapter
{
	public static Scene scene;
	
	@Override
	public void create ()
	{
		Config.init();
		scene = firstScene();												// Initializing first scen
		Gdx.input.setInputProcessor(scene);	// Setting the processor class for input
	}
	
	private Scene firstScene() { return Config.preTitle ? new ScenePreTitle() : new SceneTitle(); }
	
	@Override
	public void render ()
	{
		scene.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.preRender();
        scene.render();
        scene.endRender();
	}
	
	@Override
	public void dispose() { scene.dispose(); }
	
	public static void changeScene(final Scene newScene)
	{
		scene.dispose();
		scene = newScene;
		Gdx.input.setInputProcessor(scene);
	}
	

}
