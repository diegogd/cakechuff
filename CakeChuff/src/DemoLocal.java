import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cc.automatons.BlisterAutomaton;
import cc.automatons.CakeAutomaton;
import cc.automatons.MasterAutomaton;
import cc.automatons.QCAutomaton;
import cc.simulation.state.ControlInterface;
import cc.simulation.subsystems.Factory;

import com.jme.app.AbstractGame.ConfigShowMode;
public class DemoLocal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MasterAutomaton master;
		CakeAutomaton a1;
		BlisterAutomaton a2;
		QCAutomaton a3;
		java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ControlInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ControlInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ControlInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(ControlInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                new ControlInterface().setVisible(true);
            }
        });
		master=new MasterAutomaton( "localhost",9009,9008,"localhost",9000,9001,"localhost",9003,9002,"localhost",9005,9004);
		Factory factory = new Factory();
		try {
			factory.setConfigShowMode(ConfigShowMode.AlwaysShow,new File("").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		a1=new CakeAutomaton(9001,9000,"localhost");
		a2 = new BlisterAutomaton(9003,9002,"localhost");
		a3=new QCAutomaton(9005,9004,"llocalhost");

		factory.start();
	}

}
