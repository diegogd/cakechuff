package cc.simulation.subsystems;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.Cake;
import cc.simulation.elements.Conveyor;
import cc.simulation.elements.ConveyorBlister;
import cc.simulation.elements.ConveyorCake;
import cc.simulation.elements.ConveyorQuality;
import cc.simulation.elements.LightSensor;
import cc.simulation.state.SystemState;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.util.TextureManager;



public class Factory extends SimpleGame implements Observer{
	
	final int CAMERA_WHOLE = 0;
	final int CAMERA_CAKESUB = 1;
	final int CAMERA_BLISTERSUB = 2;
	final int CAMERA_QASUB = 3;
	
	// Factory Elements
	
	// Conveyer Belts
	CakeSubsystem cakeSub;
	BlisterSubsystem blisterSub;
	QualitySubsystem qualitySub;
	SystemState _state;
	Conveyor cb2, cb3;
	Vector<Spatial> cakes;
	
	
	private int numcakes = 0;
	
	private Box floor;
	
	public Factory() {
		_state = SystemState.getInstance();
		_state.addObserver(this);
		cakes = new Vector<Spatial>();
	}
	
	@Override
	protected void simpleUpdate() {
		
		for(int i=0; i < numcakes; i++){
			dropCake();
			numcakes--;
		}
		
		// TODO Auto-generated method stub
		super.simpleUpdate();
		
		float time = timer.getTimePerFrame();
		
		cakeSub.update(cakes, time);
		
		blisterSub.update(time);
	}

	@Override
	protected void simpleInitGame() {
		
		// TODO Auto-generated method stub
		floor = new Box("floor", new Vector3f(-40, 0f,-40), new Vector3f(60,-0.1f,60));
		floor.setDefaultColor(ColorRGBA.brown);
		floor.updateRenderState();
		
		// floor.setModelBound(new BoundingBox());
		// floor.updateModelBound();
		// TextureState ts = display.getRenderer().createTextureState();
		
		
		TextureState ts = display.getRenderer().createTextureState();

			Texture t0 =
					TextureManager.loadTexture(
					getClass().getClassLoader().getResource("model/texture/floor2.png"),
					MinificationFilter.Trilinear,
					MagnificationFilter.Bilinear);
			t0.setScale(new Vector3f(10f, 10f, 1f));
			t0.setWrap(Texture.WrapMode.Repeat);
			ts.setTexture(t0);
			ts.setEnabled(true);
			floor.setRenderState(ts);
			floor.updateRenderState();
		
		rootNode.attachChild(floor);
			
		cakeSub = new CakeSubsystem();
		cakeSub.setLocalTranslation(new Vector3f(-11,0,-8.5f));
		rootNode.attachChild(cakeSub);
		
		blisterSub = new BlisterSubsystem();
		blisterSub.setLocalTranslation(new Vector3f(-11,0,8.5f));
		rootNode.attachChild(blisterSub);
		
		qualitySub = new QualitySubsystem();
		qualitySub.setLocalTranslation(18, 0, 0);
		rootNode.attachChild(qualitySub);
		
		MaterialState ms = display.getRenderer().createMaterialState();
		ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
		rootNode.setRenderState(ms);
		
		loadCamera(CAMERA_CAKESUB);
	}
	
	public void loadCamera(int id){
		switch(id){
		case CAMERA_WHOLE: // Whole system
			display.getRenderer().getCamera().setLocation(new Vector3f(50, 75, 50));
			display.getRenderer().getCamera().lookAt(new Vector3f(0,-1,-5), Vector3f.UNIT_Y);
			break;
		case CAMERA_CAKESUB: // Cake Subsystem
			display.getRenderer().getCamera().setLocation(new Vector3f(-6, 10, 3));
			display.getRenderer().getCamera().lookAt(new Vector3f(-10,6,-5), Vector3f.UNIT_Y);
			break;
		case CAMERA_BLISTERSUB: // Blister Subsystem
			display.getRenderer().getCamera().setLocation(new Vector3f(-6, 10, 30));
			display.getRenderer().getCamera().lookAt(new Vector3f(-10,6,15), Vector3f.UNIT_Y);
			break;
		case CAMERA_QASUB: // Quality Subsystem
			break;
		}
		
	}
	
	public void dropCake(){
		Cake cake = new Cake(cakes.size());
		cake.setLocalTranslation(-18, 10f, -8.5f);
		rootNode.attachChild(cake);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
		cakes.add(cake);
	}

	@Override
	public void update(Observable o, Object arg) {
		if( o instanceof SystemState)
		{
			if(_state.getId_camera() >= 0){
				loadCamera(_state.getId_camera());
				_state.resetCamera();
			}
			
			if(_state.dropCake())
			{
				numcakes++;
			}
		}
	}

}
