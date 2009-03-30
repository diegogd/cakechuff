import java.nio.FloatBuffer;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;


public class Balls extends SimpleGame {
	
	private Sphere s;
	private Box floor;
	private Box convBelt1, convBelt2, convBelt3;
	private Node sideWall;
	private float v1=50f,v2=45f,v3=28f;
	private Vector3f ballVelocity;
	
	@Override
	protected void simpleUpdate() {
		// TODO Auto-generated method stub
		super.simpleUpdate();
		
		
		if(sideWall.hasCollision(convBelt1, false))
		{
			v1 *= -1f;
		}
		if(sideWall.hasCollision(convBelt2, false))
		{
			v2 *= -1f;
		}
		if(sideWall.hasCollision(convBelt3, false))
		{
			v3 *= -1f;
		}
		if(s.hasCollision(sideWall.getChild("wallS"), false) ||
		   s.hasCollision(sideWall.getChild("wallN"), false)
		   || s.hasCollision(convBelt2, false)
		   || s.hasCollision(convBelt3, false))
		{					
			ballVelocity.z *= -1f;
		}
		
		if(s.hasCollision(sideWall.getChild("wallW"), false) ||
		   s.hasCollision(sideWall.getChild("wallE"), false)
		   || s.hasCollision(convBelt1, false))
		{					
			ballVelocity.x *= -1f;
		}
		
		convBelt1.getLocalTranslation().x += v1*timer.getTimePerFrame();
		convBelt2.getLocalTranslation().x += 0.74*v2*timer.getTimePerFrame();
		convBelt3.getLocalTranslation().x += 0.50*v3*timer.getTimePerFrame();
		s.getLocalTranslation().addLocal(ballVelocity.mult(timer.getTimePerFrame()));
	}

	@Override
	protected void simpleInitGame() {
		
		ballVelocity = new Vector3f(30f, 0f, 25f);
		
		// TODO Auto-generated method stub
		floor = new Box("floor", new Vector3f(-20, -0.1f,-20), new Vector3f(20,0f,20));
		floor.setDefaultColor(ColorRGBA.green);
		floor.updateRenderState();
		// floor.setModelBound(new BoundingBox());
		// floor.updateModelBound();
		// TextureState ts = display.getRenderer().createTextureState();
		rootNode.attachChild(floor);
		
		s = new Sphere("s1", 20, 20, 1f);
		s.setDefaultColor(ColorRGBA.blue);
		s.updateRenderState();
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		s.translatePoints(0, 1f, 0);
		rootNode.attachChild(s);
		
		convBelt1 = new Box("convBelt1", new Vector3f(-2, 0f, -0.5f), new Vector3f(2, 2f, 0.5f));
		convBelt1.translatePoints(-2, 0, -1);
		convBelt1.setModelBound(new BoundingBox());
		convBelt1.updateModelBound();
		convBelt1.setSolidColor(ColorRGBA.red);
		convBelt1.updateRenderState();
		rootNode.attachChild(convBelt1);
		
		convBelt2 = new Box("convBelt2", new Vector3f(-2, 0f, -0.5f), new Vector3f(2, 2f, 0.5f));
		convBelt2.translatePoints(-2, 0, 1);
		convBelt2.setModelBound(new BoundingBox());
		convBelt2.updateModelBound();
		convBelt2.setDefaultColor(ColorRGBA.orange);
		convBelt2.updateRenderState();
		rootNode.attachChild(convBelt2);
		rootNode.updateRenderState();
		
		convBelt3 = new Box("convBelt3", new Vector3f(-2, 0f, -0.5f), new Vector3f(2, 2f, 0.5f));
		convBelt3.translatePoints(2, 0, 0);
		convBelt3.setModelBound(new BoundingBox());
		convBelt3.updateModelBound();
		convBelt3.setDefaultColor(ColorRGBA.blue);
		convBelt3.updateRenderState();
		rootNode.attachChild(convBelt3);
		rootNode.updateRenderState();
		
		sideWall = new Node("Walls");
		rootNode.attachChild(sideWall);
		
		Box wall = new Box("wallN", new Vector3f(-20,0, -20), new Vector3f(20, 1,-19.9f));
		wall.setDefaultColor(ColorRGBA.red);
		wall.setModelBound(new BoundingBox());
		wall.updateModelBound();
		wall.updateRenderState();
		sideWall.attachChild(wall);
		
		wall = new Box("wallS", new Vector3f(-20,0,20), new Vector3f(20,1, 20.1f));
		wall.setDefaultColor(ColorRGBA.red);
		wall.updateRenderState();
		wall.setModelBound(new BoundingBox());
		wall.updateModelBound();
		sideWall.attachChild(wall);
		
		wall = new Box("wallW", new Vector3f(-20,0,-20), new Vector3f(-19.9f,1, 20f));
		wall.setDefaultColor(ColorRGBA.red);
		wall.updateRenderState();
		wall.setModelBound(new BoundingBox());
		wall.updateModelBound();
		sideWall.attachChild(wall);
		
		wall = new Box("wallE", new Vector3f(20,0,-20), new Vector3f(20.1f,1, 20f));
		wall.setDefaultColor(ColorRGBA.red);
		wall.updateRenderState();
		wall.setModelBound(new BoundingBox());
		wall.updateModelBound();
		sideWall.attachChild(wall);
		
		MaterialState ms = display.getRenderer().createMaterialState();
		ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
		rootNode.setRenderState(ms);
		
		display.getRenderer().getCamera().setLocation(new Vector3f(50, 75, 50));
		display.getRenderer().getCamera().lookAt(new Vector3f(0,-1,-5), Vector3f.UNIT_Y);
	}

}
