package cc.simulation.subsystems;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cc.simulation.elements.Blister;
import cc.simulation.elements.Cake;
import cc.simulation.elements.Conveyor;
import cc.simulation.elements.Mouse;
import cc.simulation.elements.Table;
import cc.simulation.state.SystemState;
import cc.simulation.utils.Rotations;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.light.Light;
import com.jme.light.PointLight;
import com.jme.light.SpotLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.util.TextureManager;

/**
 * Implementation of the Factory simulation subsystem. It is composed by all the simulation elements of the system
 * (the other 3 subsystems, the robots...). It is also in charge of the camera control and the simulation of the factory process.
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class Factory extends SimpleGame implements Observer {

	public final int CAMERA_WHOLE = 0;
	public final int CAMERA_CAKESUB = 1;
	public final int CAMERA_BLISTERSUB = 2;
	public final int CAMERA_QASUB = 3;
	
	// Mouse
	private Mouse mouse;

	// Conveyer Belts
	public CakeSubsystem cakeSub;
	public BlisterSubsystem blisterSub;
	public QualitySubsystem qualitySub;
	SystemState _state;
	Conveyor cb2, cb3;

	public Table table;
	Spatial test;
	Robot1 robot1;

	private int numcakes = 0;
	private int numBlisters = 0;

	public Box floor;
	/**
	 * Constructor
	 * Initializes the state of the system
	 */
	public Factory() {
		_state = SystemState.getInstance();
		_state.addObserver(this);
	}
	/**
	 * Drops the cakes and engraves them. Then updates the state of the three subsystems (cake, blister, quality) and
	 * of the robot.
	 */
	@Override
	protected void simpleUpdate() {

		for (int i = 0; i < numcakes; i++) {
			dropCake();
			numcakes--;
		}

		for (int i = 0; i < numBlisters; i++) {
			Engrave();
			numBlisters--;
		}

		super.simpleUpdate();

		float time = timer.getTimePerFrame();

		cakeSub.update(time);
		blisterSub.update(time);
		qualitySub.update(time);
		mouse.simpleUpdate(time, this);

		// Lo correcto seria:robot1.update(time,rootNode.getChildren());
		robot1.update(time, this);
	}
	/**
	 * Initializes all the elements that compose the simulation of the system and loads their graphical model. 
	 */
	@Override
	protected void simpleInitGame() {

		TextureState ts = display.getRenderer().createTextureState();

		Box wallback = new Box("wallback", new Vector3f(-40, 0f, -40),
				new Vector3f(60, 30f, -39.99f));
		wallback.setDefaultColor(ColorRGBA.lightGray);
		Texture t0 = TextureManager.loadTexture(getClass().getClassLoader()
				.getResource("model/texture/wallBack.jpg"),
				MinificationFilter.Trilinear, MagnificationFilter.Bilinear);
		ts.setTexture(t0);
		ts.setEnabled(true);		
		wallback.setRenderState(ts);
		wallback.updateRenderState();		
		rootNode.attachChild(wallback);

		Box wallleft = new Box("wallleft", new Vector3f(-40, 0f, -40),
				new Vector3f(-40.01f, 30f, 30));
		wallleft.setDefaultColor(ColorRGBA.lightGray);
		ts = display.getRenderer().createTextureState();
		t0 = TextureManager.loadTexture(getClass().getClassLoader()
				.getResource("model/texture/wallLeft.jpg"),
				MinificationFilter.Trilinear, MagnificationFilter.Bilinear);
		ts.setTexture(t0);
		ts.setEnabled(true);		
		wallleft.setRenderState(ts);
		wallleft.updateRenderState();
		rootNode.attachChild(wallleft);

		Box wallright = new Box("wallright", new Vector3f(60, 0f, -40),
				new Vector3f(60.01f, 30f, 30));
		wallright.setDefaultColor(ColorRGBA.lightGray);
		ts = display.getRenderer().createTextureState();
		t0 = TextureManager.loadTexture(getClass().getClassLoader()
				.getResource("model/texture/wallRight.jpg"),
				MinificationFilter.Trilinear, MagnificationFilter.Bilinear);
		ts.setTexture(t0);
		ts.setEnabled(true);		
		wallright.setRenderState(ts);
		wallright.updateRenderState();
		wallright.updateRenderState();
		rootNode.attachChild(wallright);

		floor = new Box("floor", new Vector3f(-40, 0f, -40), new Vector3f(60,
				-0.1f, 30));
		floor.setDefaultColor(ColorRGBA.brown);
		floor.updateRenderState();

		ts = display.getRenderer().createTextureState();
		t0 = TextureManager.loadTexture(getClass().getClassLoader()
				.getResource("model/texture/floor.jpg"),
				MinificationFilter.Trilinear, MagnificationFilter.Bilinear);
		ts.setTexture(t0);
		ts.setEnabled(true);
		
		floor.setRenderState(ts);
		floor.updateRenderState();

		rootNode.attachChild(floor);

		cakeSub = new CakeSubsystem();
		cakeSub.setLocalTranslation(new Vector3f(-11, 0, -7.5f));
		rootNode.attachChild(cakeSub);

		blisterSub = new BlisterSubsystem(display);
		blisterSub.setLocalTranslation(new Vector3f(-11, 0, 8.5f));
		rootNode.attachChild(blisterSub);

		qualitySub = new QualitySubsystem();
		qualitySub.setLocalTranslation(16, 0, 0);
		rootNode.attachChild(qualitySub);

		table = new Table();
		table.setLocalRotation(Rotations.rotateY(0.35f));
		table.setLocalTranslation(3.5f, 0f, -8f);

		rootNode.attachChild(table);

		robot1 = new Robot1();
		rootNode.attachChild(robot1);

		rootNode.attachChild(test);
		
		mouse = new Mouse(display);
		mouse.setLocalTranslation(15, 0, 25);
		rootNode.attachChild(mouse);

		MaterialState ms = display.getRenderer().createMaterialState();
		ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
		rootNode.setRenderState(ms);
		
		
		List<Light> luces = lightState.getLightList();
		Iterator<Light> itligh = luces.iterator();
		while(itligh.hasNext()) itligh.next().setEnabled(false);		
		
		// Lights
		SpotLight lightSub1;
		// Setting Lights
		lightSub1 = new SpotLight();
		lightSub1.setDiffuse(ColorRGBA.white);
		lightSub1.setAmbient(ColorRGBA.gray);
		lightSub1.setAngle(10);
		lightSub1.setLocation(new Vector3f(-11, 40, -1.5f));
		lightSub1.setDirection(new Vector3f(0,-1,0));
		lightSub1.setEnabled(true);
		lightState.attach(lightSub1);
		
		// Lights
		SpotLight lightSub2;
		// Setting Lights
		lightSub2 = new SpotLight();
		lightSub2.setDiffuse(ColorRGBA.white);
		lightSub2.setAmbient(ColorRGBA.gray);
		lightSub2.setAngle(20);
		lightSub2.setLocation(new Vector3f(-11, 40, 22.5f));
		lightSub2.setDirection(new Vector3f(0,-1,0));		
		lightSub2.setEnabled(true);
		lightState.attach(lightSub2);
		
		// Lights
		SpotLight lightSub3;
		lightSub3 = new SpotLight();
		lightSub3.setDiffuse(ColorRGBA.white);
		lightSub3.setAmbient(ColorRGBA.gray);
		lightSub3.setAngle(20);
		lightSub3.setLocation(new Vector3f(16, 40, 14));
		lightSub3.setDirection(new Vector3f(0,-1,0));		
		lightSub3.setEnabled(true);
		lightState.attach(lightSub3);
		
		// Lights in boxes
		SpotLight redLight = new SpotLight();
		redLight.setDiffuse(ColorRGBA.red);
		redLight.setAmbient(ColorRGBA.white);
		
		redLight.setAngle(20);
		redLight.setLocation(new Vector3f(37f, 20f, 5.5f));
		redLight.setDirection(new Vector3f(0,-1,0));
		redLight.setEnabled(true);
		lightState.attach(redLight);

		SpotLight greenLight = new SpotLight();
		greenLight.setDiffuse(ColorRGBA.green);
		greenLight.setAmbient(ColorRGBA.white);		
		greenLight.setAngle(20);
		greenLight.setLocation(new Vector3f(37f, 20f, -5.5f));
		greenLight.setDirection(new Vector3f(0,-1,0));
		greenLight.setEnabled(true);
		lightState.attach(greenLight);

		PointLight mainLight = new PointLight();
		mainLight.setDiffuse(ColorRGBA.gray);
		mainLight.setAmbient(ColorRGBA.gray);
		mainLight.setLocation(new Vector3f(0,100,0));
		mainLight.setEnabled(true);
		lightState.attach(mainLight);
		
		rootNode.updateRenderState();

		loadCamera(CAMERA_WHOLE);
	}
	/**
	 * Loads one of the four cameras that can be used in the graphical user interface
	 * @param id Identification of the camera to be loaded
	 */
	public void loadCamera(int id) {
		switch (id) {
		case CAMERA_WHOLE: // Whole system
			display.getRenderer().getCamera().setLocation(
					new Vector3f(10.4414f, 39.2627f, 49.3684f));
			display.getRenderer().getCamera().lookAt(new Vector3f(10, -1, -20),
					Vector3f.UNIT_Y);
			break;
		case CAMERA_CAKESUB: // Cake Subsystem
			display.getRenderer().getCamera().setLocation(
					new Vector3f(-4.7f, 12, 8));
			display.getRenderer().getCamera().lookAt(new Vector3f(-9, 6, -5),
					Vector3f.UNIT_Y);
			break;
		case CAMERA_BLISTERSUB: // Blister Subsystem
			display.getRenderer().getCamera().setLocation(
					new Vector3f(-6, 10, 30));
			display.getRenderer().getCamera().lookAt(new Vector3f(-10, 6, 15),
					Vector3f.UNIT_Y);
			break;
		case CAMERA_QASUB: // Quality Subsystem
			display.getRenderer().getCamera().setLocation(
					new Vector3f(31.92f, 17.168f, 30.73f));
			display.getRenderer().getCamera().lookAt(new Vector3f(25, 3, -8),
					Vector3f.UNIT_Y);
			break;
		}

	}
	/**
	 * Drops a cake and updates the state of the system 
	 */
	public void dropCake() {
		Cake cake = new Cake(cakeSub.cakes.size(), display);
		cake.setLocalTranslation(-18, 10f, -7.5f);
		
		rootNode.attachChild(cake);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
		cakeSub.cakes.add(cake);
		//combination.add(cake);
	}
	/**
	 * Engraves and updates the state of the system 
	 */
	private void Engrave() {
		Blister blister = new Blister(blisterSub.blisters.size());
		blister.setLocalTranslation(-11f, -1f, 16.8f);
		blister.setLocalScale(1.1f);
		rootNode.attachChild(blister);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
		blisterSub.blisters.add(blister);
		//combination.add(blister);
	}

	/**
	 * Updates the camera and the number of blisters and cakes
	 * @param o Observable object
	 * @param arg State of the system
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof SystemState) {
			if (_state.getId_camera() >= 0) {
				loadCamera(_state.getId_camera());
				_state.resetCamera();
			}

			if (_state.dropCake()) {
				numcakes++;
			}

			if (_state.makeBlister()) {
				numBlisters++;
			}
		}
	}

}
