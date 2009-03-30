package cc.simulation.elements;

import java.util.Observable;

import com.jme.scene.Node;

public class Sensor extends Node {

	private boolean isActived = false;
	private boolean modified = false;
	
	public boolean isActived() {
		return isActived;
	}
	public void setActived(boolean isActived) {
		if( this.isActived == isActived )
			modified = false;
		else {
			modified = true;
			this.isActived = isActived;
		}
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
