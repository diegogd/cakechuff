import java.io.File;
import java.net.MalformedURLException;

import cc.simulation.state.FactoryInterface;
import cc.simulation.subsystems.Factory;

import com.jme.app.AbstractGame.ConfigShowMode;


public class DemoStart {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FactoryInterface().setVisible(true);
            }
        });
		Factory factory = new Factory();
		try {
			factory.setConfigShowMode(ConfigShowMode.AlwaysShow,new File("").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		factory.start();
	}

}
