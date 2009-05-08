/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FactoryInterface.java
 *
 * Created on 27-mar-2009, 0:13:19
 */

package cc.simulation.state;

import java.beans.DesignMode;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cc.simulation.elements.LightSensor;
import cc.simulation.elements.TouchSensor;

/**
 * 
 * @author diego
 */
public class FactoryInterface extends javax.swing.JFrame implements Observer {

	// State
	private SystemState _systemState = null;
	private CakeSubsystemState _cakeSubState = null;
	private BlisterSubsystemState _blisterSubState = null;
	private QualitySubsystemState _qualitySubState = null;
	private Robot1State _robot1State = null;

	/** Creates new form FactoryInterface */
	public FactoryInterface() {
		// ^ Just notify no observer
		_systemState = SystemState.getInstance();
		_cakeSubState = CakeSubsystemState.getInstance();
		_cakeSubState.addObserver(this);
		_blisterSubState = BlisterSubsystemState.getInstance();
		_blisterSubState.addObserver(this);
		_qualitySubState = QualitySubsystemState.getInstance();
		_qualitySubState.addObserver(this);
		_robot1State = Robot1State.getInstance();
		_robot1State.addObserver(this);

		initComponents();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
			this.pack();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeLabelColor(JLabel label, boolean detected) {
		if (detected) {
			label.setText("Detection");
			label.setForeground(new java.awt.Color(0, 204, 0));
		} else {
			label.setText("None");
			label.setForeground(new java.awt.Color(204, 0, 0));
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel2 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabelSensor4 = new javax.swing.JLabel();
		jLabelSensor5 = new javax.swing.JLabel();
		jSpinnerConveyor2 = new javax.swing.JSpinner();
		jLabel11 = new javax.swing.JLabel();
		jButtonEngrave = new javax.swing.JButton();
		jButtonCut = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jSpinner1 = new javax.swing.JSpinner();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabelSensor1 = new javax.swing.JLabel();
		jLabelSensor2 = new javax.swing.JLabel();
		jLabelSensor3 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jSpinnerValve1 = new javax.swing.JSpinner();
		jLabel14 = new javax.swing.JLabel();
		jSpinnerValve2 = new javax.swing.JSpinner();
		jPanel3 = new javax.swing.JPanel();
		jLabel7 = new javax.swing.JLabel();
		jLabelQA1 = new javax.swing.JLabel();
		jLabelQA2 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabelQA3 = new javax.swing.JLabel();
		jLabelQA4 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jSpinnerConveyor3 = new javax.swing.JSpinner();
		jLabel12 = new javax.swing.JLabel();
		jButtonTakeCake = new javax.swing.JButton();
		jButtonTakeBlister = new javax.swing.JButton();
		jButtonTakePackage = new javax.swing.JButton();
		jButtonCamera1 = new javax.swing.JButton();
		jButtonCamera2 = new javax.swing.JButton();
		jButtonCamera3 = new javax.swing.JButton();
		jButtonCamera4 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		jPanel2.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Blister Subsystem",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(
								0, 0, 0))); // NOI18N

		jLabel6.setText("Touch Sensor");

		jLabel5.setText("Light Sensor1");

		jLabelSensor4.setForeground(new java.awt.Color(204, 0, 0));
		jLabelSensor4.setText("None");

		jLabelSensor5.setForeground(new java.awt.Color(204, 0, 0));
		jLabelSensor5.setText("None");

		jSpinnerConveyor2
				.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						jSpinnerConveyor2StateChanged(evt);
					}
				});

		jLabel11.setText("Conveyor Speed:");

		jButtonEngrave.setText("Engrave");
		jButtonEngrave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonEngraveActionPerformed(evt);
			}
		});

		jButtonCut.setText("Cut");
		jButtonCut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCutActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel6)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelSensor5))
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel5)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelSensor4))
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel2Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING,
																								false)
																						.addComponent(
																								jButtonEngrave,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								jLabel11,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addGroup(
																				jPanel2Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jButtonCut,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								jSpinnerConveyor2,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								70,
																								javax.swing.GroupLayout.PREFERRED_SIZE))))
										.addContainerGap(12, Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel2Layout
										.createSequentialGroup()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel11)
														.addComponent(
																jSpinnerConveyor2,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonEngrave)
														.addComponent(
																jButtonCut))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												16, Short.MAX_VALUE)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel5)
														.addComponent(
																jLabelSensor4))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jLabelSensor5)
														.addComponent(jLabel6))));

		jPanel1.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Cake Subsystem",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(
								0, 0, 0))); // NOI18N

		jLabel1.setText("Conveyor Speed:");

		jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jSpinner1StateChanged(evt);
			}
		});

		jLabel2.setText("Light Sensor1");

		jLabel3.setText("Light Sensor2");

		jLabel4.setText("Touch Sensor");

		jLabelSensor1.setForeground(new java.awt.Color(204, 0, 0));
		jLabelSensor1.setText("None");

		jLabelSensor2.setForeground(new java.awt.Color(204, 0, 0));
		jLabelSensor2.setText("None");

		jLabelSensor3.setForeground(new java.awt.Color(204, 0, 0));
		jLabelSensor3.setText("None");

		jLabel13.setText("Valve 1 (secs):");

		jSpinnerValve1
				.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						jSpinnerValve1StateChanged(evt);
					}
				});

		jLabel14.setText("Valve 2 (secs):");

		jSpinnerValve2
				.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						jSpinnerValve2StateChanged(evt);
					}
				});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jSpinner1))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel4)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelSensor3))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel3)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelSensor2))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel2)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelSensor1))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel13)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jSpinnerValve1))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel14)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jSpinnerValve2,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				81,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(20, Short.MAX_VALUE)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel1)
														.addComponent(
																jSpinner1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel13)
														.addComponent(
																jSpinnerValve1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel14)
														.addComponent(
																jSpinnerValve2,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel2)
														.addComponent(
																jLabelSensor1))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel3)
														.addComponent(
																jLabelSensor2))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jLabelSensor3)
														.addComponent(jLabel4))
										.addGap(22, 22, 22)));

		jPanel3.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Quality Subsystem",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(
								0, 0, 0))); // NOI18N

		jLabel7.setText("Quality 1:");

		jLabelQA1.setForeground(new java.awt.Color(204, 0, 0));
		jLabelQA1.setText("None");

		jLabelQA2.setForeground(new java.awt.Color(204, 0, 0));
		jLabelQA2.setText("None");

		jLabel8.setText("Quality 2:");

		jLabel9.setText("Quality 3:");

		jLabelQA3.setForeground(new java.awt.Color(204, 0, 0));
		jLabelQA3.setText("None");

		jLabelQA4.setForeground(new java.awt.Color(204, 0, 0));
		jLabelQA4.setText("None");

		jLabel10.setText("Quality 4:");

		jSpinnerConveyor3
				.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						jSpinnerConveyor3StateChanged(evt);
					}
				});

		jLabel12.setText("Conveyor Speed:");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout
				.setHorizontalGroup(jPanel3Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel3Layout
										.createSequentialGroup()
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel7)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelQA1))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel8)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelQA2))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel9)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelQA3))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel10)
																		.addGap(
																				18,
																				18,
																				18)
																		.addComponent(
																				jLabelQA4))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel12)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jSpinnerConveyor3,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				70,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap(18, Short.MAX_VALUE)));
		jPanel3Layout
				.setVerticalGroup(jPanel3Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel3Layout
										.createSequentialGroup()
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel12)
														.addComponent(
																jSpinnerConveyor3,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel7)
														.addComponent(jLabelQA1))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel8)
														.addComponent(jLabelQA2))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel9)
														.addComponent(jLabelQA3))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel10)
														.addComponent(jLabelQA4))
										.addGap(22, 22, 22)));

		jButtonTakeCake.setText("Take Cake");
		jButtonTakeCake.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonTakeCakeActionPerformed(evt);
			}
		});

		jButtonTakeBlister.setText("Take Blister");
		jButtonTakeBlister
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jButtonTakeBlisterActionPerformed(evt);
					}
				});

		jButtonTakePackage.setText("Take Package");
		jButtonTakePackage
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jButtonTakePackageActionPerformed(evt);
					}
				});

		jButtonCamera1.setText("Camera 1");
		jButtonCamera1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCamera1ActionPerformed(evt);
			}
		});

		jButtonCamera2.setText("Camera 2");
		jButtonCamera2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCamera2ActionPerformed(evt);
			}
		});

		jButtonCamera3.setText("Camera 3");
		jButtonCamera3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCamera3ActionPerformed(evt);
			}
		});

		jButtonCamera4.setText("Camera 4");
		jButtonCamera4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCamera4ActionPerformed(evt);
			}
		});

		jButton1.setText("Drop CAKE");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jButton1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																192,
																Short.MAX_VALUE)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonCamera1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				34,
																				Short.MAX_VALUE)
																		.addComponent(
																				jButtonCamera2))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonCamera3)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				34,
																				Short.MAX_VALUE)
																		.addComponent(
																				jButtonCamera4))
														.addComponent(
																jPanel1,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jPanel2,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																jButtonTakePackage,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																192,
																Short.MAX_VALUE)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																layout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonTakeCake,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				97,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jButtonTakeBlister,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				89,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																jPanel3,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonCamera1)
														.addComponent(
																jButtonCamera2))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonCamera3)
														.addComponent(
																jButtonCamera4))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jButton1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanel1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												165,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanel2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonTakeCake)
														.addComponent(
																jButtonTakeBlister,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																23,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jButtonTakePackage)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanel3,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {
		_cakeSubState.setConveyor_velocity(Float.parseFloat(""
				+ jSpinner1.getValue()));
	}

	private void jButtonTakeBlisterActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonTakeBlisterActionPerformed
		// TODO add your handling code here:

		_robot1State.setGoToState(7);

	}// GEN-LAST:event_jButtonTakeBlisterActionPerformed
	
	private void jButtonEngraveActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonTakeBlisterActionPerformed
		// TODO add your handling code here:
		_blisterSubState.setEngraver_secs(8);
		_systemState.setMakeBlister();

	}// GEN-LAST:event_jButtonTakeBlisterActionPerformed
	
	private void jButtonCutActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonTakeBlisterActionPerformed
		// TODO add your handling code here:
		_blisterSubState.setCutter_secs(8);
	}// GEN-LAST:event_jButtonTakeBlisterActionPerformed

	private void jButtonTakeCakeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonTakeCakeActionPerformed
		// TODO add your handling code here:

		_robot1State.setGoToState(5);

	}// GEN-LAST:event_jButtonTakeCakeActionPerformed
	
	private void jButtonTakePackageActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonTakeCakeActionPerformed
		// TODO add your handling code here:

		_robot1State.setGoToState(8);

	}// GEN-LAST:event_jButtonTakeCakeActionPerformed

	private void jButtonCamera1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCamera1ActionPerformed
		_systemState.setId_camera(1);
	}// GEN-LAST:event_jButtonCamera1ActionPerformed

	private void jButtonCamera2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCamera2ActionPerformed
		_systemState.setId_camera(2);
	}// GEN-LAST:event_jButtonCamera2ActionPerformed

	private void jButtonCamera3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCamera3ActionPerformed
		_systemState.setId_camera(3);
	}// GEN-LAST:event_jButtonCamera3ActionPerformed

	private void jButtonCamera4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCamera4ActionPerformed
		_systemState.setId_camera(0);
	}// GEN-LAST:event_jButtonCamera4ActionPerformed

	private void jSpinnerValve1StateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSpinnerValve1StateChanged
		_cakeSubState.setValve1_open_secs(Float.parseFloat(""
				+ jSpinnerValve1.getValue()));
		// jSpinnerValve1.setValue(0);
	}// GEN-LAST:event_jSpinnerValve1StateChanged

	private void jSpinnerValve2StateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSpinnerValve2StateChanged
		_cakeSubState.setValve2_open_secs(Float.parseFloat(""
				+ jSpinnerValve2.getValue()));
		// jSpinnerValve2.setValue(0);
	}// GEN-LAST:event_jSpinnerValve2StateChanged

	private void jSpinnerConveyor2StateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSpinnerConveyor2StateChanged
		_blisterSubState.setConveyor_velocity(Float.parseFloat(""
				+ jSpinnerConveyor2.getValue()));
	}// GEN-LAST:event_jSpinnerConveyor2StateChanged

	private void jSpinnerConveyor3StateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_jSpinnerConveyor3StateChanged
		_qualitySubState.setConveyor_velocity(Float.parseFloat(""
				+ jSpinnerConveyor3.getValue()));
	}// GEN-LAST:event_jSpinnerConveyor3StateChanged

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		_systemState.setDropCake();

	}// GEN-LAST:event_jButton1ActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FactoryInterface().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButtonCamera1;
	private javax.swing.JButton jButtonCamera2;
	private javax.swing.JButton jButtonCamera3;
	private javax.swing.JButton jButtonCamera4;
	private javax.swing.JButton jButtonCut;
	private javax.swing.JButton jButtonEngrave;
	private javax.swing.JButton jButtonTakeBlister;
	private javax.swing.JButton jButtonTakeCake;
	private javax.swing.JButton jButtonTakePackage;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel jLabelQA1;
	private javax.swing.JLabel jLabelQA2;
	private javax.swing.JLabel jLabelQA3;
	private javax.swing.JLabel jLabelQA4;
	private javax.swing.JLabel jLabelSensor1;
	private javax.swing.JLabel jLabelSensor2;
	private javax.swing.JLabel jLabelSensor3;
	private javax.swing.JLabel jLabelSensor4;
	private javax.swing.JLabel jLabelSensor5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JSpinner jSpinner1;
	private javax.swing.JSpinner jSpinnerConveyor2;
	private javax.swing.JSpinner jSpinnerConveyor3;
	private javax.swing.JSpinner jSpinnerValve1;
	private javax.swing.JSpinner jSpinnerValve2;

	// End of variables declaration//GEN-END:variables

	@Override
	public void update(Observable arg0, Object arg1) {
		// Update subsystem 1
		if (arg0 instanceof CakeSubsystemState) {
			if (arg1 instanceof LightSensor) {
				if (((LightSensor) arg1).getName().equalsIgnoreCase("sensor1")) {
					changeLabelColor(jLabelSensor1, ((LightSensor) arg1)
							.isActived());
				} else {
					changeLabelColor(jLabelSensor2, ((LightSensor) arg1)
							.isActived());
				}
			} else if (arg1 instanceof TouchSensor) {
				changeLabelColor(jLabelSensor3, ((TouchSensor) arg1)
						.isActived());
			}
		} else if (arg0 instanceof BlisterSubsystemState) {
			// TODO: pendiente identificar sensores
			if (arg1 instanceof LightSensor) {
					changeLabelColor(jLabelSensor4, ((LightSensor) arg1)
							.isActived());
				
			} else if (arg1 instanceof TouchSensor) {
				changeLabelColor(jLabelSensor5, ((TouchSensor) arg1)
						.isActived());
			}
		} else if (arg0 instanceof QualitySubsystemState) {
			// TODO: pendiente identificar sensores
		}

	}

}
