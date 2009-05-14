package cc.simulation.subsystems;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import cc.simulation.elements.Blister;
import cc.simulation.elements.Cake;
import cc.simulation.elements.Conveyor;
import cc.simulation.elements.ConveyorBlister;
import cc.simulation.elements.ConveyorCake;
import cc.simulation.elements.ConveyorQuality;
import cc.simulation.elements.LightSensor;
import cc.simulation.elements.Packet;
import cc.simulation.elements.Table;
import cc.simulation.state.SystemState;
import cc.simulation.utils.Rotations;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.AxisRods;
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
	Vector<Spatial> cakes,blisters,packets, combination;
	
	Table table;
	
	Spatial test;
	
	Robot1 robot1;
	
	//AxisRods axis;
	
	
	private int numcakes = 0;
	private int numBlisters = 0,numPackets=0;
	
	private Box floor;
	
	public Factory() {
		_state = SystemState.getInstance();
		_state.addObserver(this);
		cakes = new Vector<Spatial>();
		blisters = new Vector<Spatial>();
		packets = new Vector<Spatial>();
		combination = new Vector<Spatial>();
	}
	
	@Override
	protected void simpleUpdate() {
		
		for(int i=0; i < numcakes; i++){
			dropCake();
			numcakes--;
		}
		
		for(int i=0; i < numBlisters; i++){
			Engrave();
			numBlisters--;
		}
		
		for(int i=0; i < numPackets; i++){
			Wrap();
			numPackets--;
		}
		
		// TODO Auto-generated method stub
		super.simpleUpdate();
		
		float time = timer.getTimePerFrame();
		
		cakeSub.update(cakes, time);
		
		blisterSub.update(blisters,time);
		
		qualitySub.update(combination,time);
		
//		robot1.moveToSub1(time);
		
		//Lo correcto seria:robot1.update(time,rootNode.getChildren());
		robot1.update(time,combination);
		
//		axis.setLocalRotation( new Quaternion().fromAngleAxis( display.getRenderer().getCamera().getDirection().angleBetween(axis.getLocalRotation().getRotationColumn(0)), display.getRenderer().getCamera().getDirection()) );
//		axis.setLocalTranslation(display.getRenderer().getCamera().getLocation().getX(),
//				display.getRenderer().getCamera().getLocation().getY(),
//				display.getRenderer().getCamera().getLocation().getZ()-2
//		);
		
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
		
		table = new Table();
		table.setLocalRotation(Rotations.rotateY(-0.785f));
		table.setLocalTranslation(4f,2.2f,-8f);
		
		rootNode.attachChild(table);
		
		robot1 = new Robot1();
//		robot.pickUpCake();
		rootNode.attachChild(robot1);
		
//		test = new Box("test", new Vector3f(-0.5f, 0f,-0.5f), new Vector3f(0.5f,1f,0.5f));
//		test.setModelBound(new BoundingBox());
//		test.updateModelBound();
//		test.setLocalTranslation(new Vector3f(-4.5f,4f,-7f));
//		test.setLocalScale(2.0f);
//		test.updateRenderState();
//		combination.add(test);
				
		rootNode.attachChild(test);
		
//		
//		Node prueba = new Node();
//		rootNode.attachChild(prueba);
//		
//		test.removeFromParent();
//		prueba.attachChild(test);
//		
//		test.removeFromParent();
//		rootNode.attachChild(test);
		
		
		
//		//Create an right handed axisrods object with a scale of 1/2
//		axis = new AxisRods("rods", true, 0.5f);
//		//Attach ar to the node we want to visualize
//		rootNode.attachChild(axis);
		
		MaterialState ms = display.getRenderer().createMaterialState();
		ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
		rootNode.setRenderState(ms);
		
		combination.add(table);
		combination.add(qualitySub);
		
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
		combination.add(cake);
	}
	
	private void Engrave() {
		Blister blister = new Blister(blisters.size());
		blister.setLocalTranslation(-13f, 0f, 16.8f);
		blister.setLocalScale(1.1f);
		rootNode.attachChild(blister);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
		blisters.add(blister);
		combination.add(blister);
	}
	
	private void Wrap() {
		Packet packet = new Packet(packets.size());
		packet.setLocalTranslation(15f, 10.2f, -8.1f);
		packet.setLocalScale(-1.1f);
		packet.updateRenderState();
		rootNode.attachChild(packet);
		rootNode.updateWorldBound();
		rootNode.updateRenderState();
		packets.add(packet);
		combination.add(packet);
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
			
			if(_state.makeBlister())
			{
				numBlisters++;
			}
			if(_state.makePacket())
			{
				numPackets++;
			}
		}
	}

}
