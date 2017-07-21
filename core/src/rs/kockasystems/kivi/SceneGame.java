package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class SceneGame extends SceneGameBase
{
	// Variables
	private OrthographicCamera camera;
	private TiledMap map;
	private TiledMapRenderer renderer;
	private TmxMapLoader loader;
	private float[] vertices;
	
	// Constants
	private final Vector2 displacement = new Vector2();
	
	public SceneGame()
	{
		super();
		
		// Creating variables
		camera 		= new OrthographicCamera();
		loader 		= new TmxMapLoader();
		map 		= loader.load("maps/3.tmx");
		vertices 	= ((PolylineMapObject)map.getLayers().get("Objects").getObjects().get(0)).getPolyline().getTransformedVertices();
		renderer 	= new OrthogonalTiledMapRenderer(map);
		
		// Camera setup
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		// Temporary
		label1 = 0;
		label2 = 30;
	}
	
	@Override
	public void preRender()
	{
		renderer.setView(camera);
		renderer.render();
	}
	
	public void update()
	{
		camera.translate(body.vx, body.vy);
		camera.update();
		displacement.x = Float.POSITIVE_INFINITY;
		displacement.y = Float.POSITIVE_INFINITY;
		if(!isColliding(displacement)) return;
		super.update();
	}
	
	protected void checkOver() { }
	
	private boolean isColliding(final Vector2 displacement)
	{
		for (int i = 0; i < vertices.length; i += 2) if(Intersector.intersectSegmentCircle(new Vector2(vertices[((i == 0) ? vertices.length : i) - 2], vertices[((i == 0) ? vertices.length : i) - 1]), new Vector2(vertices[i], vertices[i + 1]), new Vector2(camera.position.x, camera.position.y), (kiviTexture.getWidth() / 2) * (kiviTexture.getWidth() / 2)))
		{
			Intersector.intersectSegmentCircleDisplace(new Vector2(vertices[((i == 0) ? vertices.length : i) - 2], vertices[((i == 0) ? vertices.length : i) - 1]), new Vector2(vertices[i], vertices[i + 1]), new Vector2(camera.position.x, camera.position.y), (kiviTexture.getWidth() / 2), displacement);
			return true;
		}
		return false;
	}
	
	protected boolean collidingLeft()	{ return displacement.x > 0.5f; }
	protected boolean collidingRight()	{ return displacement.x < -0.5; }
	protected boolean collidingTop()	{ return displacement.y > 0.5f; }
	protected boolean collidingBottom()	{ return displacement.y < -0.5; }
	
	
}