package cc.scada;

import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.Font;
/**
 * An example that shows a JToolbar, as well as a JList, JTable, JSplitPane and JTree
 */
public class UserInterface extends javax.swing.JFrame {
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JToolBar ivjJToolBar = null;
	private javax.swing.JButton ivjJButton = null;
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JButton ivjJButton2 = null;
	private javax.swing.JPanel ivjJPanel = null;
	private javax.swing.JScrollPane ivjJScrollPane = null;
	private javax.swing.JList ivjJList = null;
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private javax.swing.JTable ivjJTable = null;
	private javax.swing.JComboBox ivjJComboBox = null;
	private javax.swing.JMenuBar ivjJMenuBar = null;
	private javax.swing.JMenu ivjJMenu = null;
	private javax.swing.JMenuItem ivjJMenuItem = null;
	private javax.swing.JMenu ivjJMenu2 = null;
	private javax.swing.JMenuItem ivjJMenuItem2 = null;
	private JMenuItem jMenuItem = null;
	private JMenu jMenu = null;
	private JMenuItem jMenuItem1 = null;
	private JMenuItem jMenuItem2 = null;
	private JMenuItem jMenuItem3 = null;
	private JPanel jPanel = null;
	private JButton jButton = null;
	private JButton Stop = null;
	private JButton Start = null;
	private JMenuItem jMenuItem4 = null;
	private JMenuItem jMenuItem5 = null;
	public UserInterface() {
		super();
		initialize();
	}

	/**
	 * Return the JFrameContentPane property value.
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			ivjJFrameContentPane = new javax.swing.JPanel();
			java.awt.BorderLayout layBorderLayout_3 = new java.awt.BorderLayout();
			ivjJFrameContentPane.setLayout(layBorderLayout_3);
			ivjJFrameContentPane.add(getIvjJToolBar(),
					java.awt.BorderLayout.NORTH);
			ivjJFrameContentPane.add(getIvjJPanel(),
					java.awt.BorderLayout.CENTER);
			ivjJFrameContentPane.setName("JFrameContentPane");
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Initialize the class.
	 */
	private void initialize() {
		this.setContentPane(getJFrameContentPane());
		this.setJMenuBar(getIvjJMenuBar());
		this.setName("JFrame1");
		this.setTitle("CakeChuff User Interface");
		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(23, 36, 526, 301);
	}

	/**
	 * This method initializes ivjJToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private javax.swing.JToolBar getIvjJToolBar() {
		if (ivjJToolBar == null) {
			ivjJToolBar = new javax.swing.JToolBar();
			ivjJToolBar.add(getIvjJButton());
			ivjJToolBar.add(getIvjJButton1());
			ivjJToolBar.add(getIvjJButton2());
			ivjJToolBar.add(getIvjJComboBox());
		}
		return ivjJToolBar;
	}

	/**
	 * This method initializes ivjJButton
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getIvjJButton() {
		if (ivjJButton == null) {
			ivjJButton = new javax.swing.JButton();
			ivjJButton.setText("One");
		}
		return ivjJButton;
	}

	/**
	 * This method initializes ivjJButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getIvjJButton1() {
		if (ivjJButton1 == null) {
			ivjJButton1 = new javax.swing.JButton();
			ivjJButton1.setText("Two");
		}
		return ivjJButton1;
	}

	/**
	 * This method initializes ivjJButton2
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getIvjJButton2() {
		if (ivjJButton2 == null) {
			ivjJButton2 = new javax.swing.JButton();
			ivjJButton2.setText("Three");
		}
		return ivjJButton2;
	}

	/**
	 * This method initializes ivjJPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getIvjJPanel() {
		if (ivjJPanel == null) {
			ivjJPanel = new javax.swing.JPanel();
			java.awt.GridLayout layGridLayout_4 = new java.awt.GridLayout();
			layGridLayout_4.setRows(2);
			layGridLayout_4.setColumns(2);
			ivjJPanel.setLayout(layGridLayout_4);
			ivjJPanel.add(getIvjJScrollPane(), null);
			ivjJPanel.add(getJPanel(), null);
			ivjJPanel.add(getIvjJScrollPane1(), null);
		}
		return ivjJPanel;
	}

	/**
	 * This method initializes ivjJScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getIvjJScrollPane() {
		if (ivjJScrollPane == null) {
			ivjJScrollPane = new javax.swing.JScrollPane();
			ivjJScrollPane.setViewportView(getIvjJList());
			ivjJScrollPane
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			ivjJScrollPane
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		}
		return ivjJScrollPane;
	}

	/**
	 * This method initializes ivjJList
	 * 
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getIvjJList() {
		if (ivjJList == null) {
			ivjJList = new javax.swing.JList();
		}
		return ivjJList;
	}

	/**
	 * This method initializes ivjJTable
	 * 
	 * @return javax.swing.JTable
	 */
	private javax.swing.JTable getIvjJTable() {
		if (ivjJTable == null) {
			ivjJTable = new javax.swing.JTable();
		}
		return ivjJTable;
	}

	/**
	 * This method initializes ivjJScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getIvjJScrollPane1() {
		if (ivjJScrollPane1 == null) {
			ivjJScrollPane1 = new javax.swing.JScrollPane();
			ivjJScrollPane1.setViewportView(getIvjJTable());
		}
		return ivjJScrollPane1;
	}

	/**
	 * This method initializes ivjJComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getIvjJComboBox() {
		if (ivjJComboBox == null) {
			ivjJComboBox = new javax.swing.JComboBox();
		}
		return ivjJComboBox;
	}

	/**
	 * This method initializes ivjJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private javax.swing.JMenuBar getIvjJMenuBar() {
		if (ivjJMenuBar == null) {
			ivjJMenuBar = new javax.swing.JMenuBar();
			ivjJMenuBar.add(getIvjJMenu());
			ivjJMenuBar.add(getJMenu());
		}
		return ivjJMenuBar;
	}

	/**
	 * This method initializes ivjJMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getIvjJMenu() {
		if (ivjJMenu == null) {
			ivjJMenu = new javax.swing.JMenu();
			ivjJMenu.add(getIvjJMenu2());
			ivjJMenu.setText("Simulation");
			ivjJMenu.add(getIvjJMenuItem());
			ivjJMenu.add(getJMenuItem3());
		}
		return ivjJMenu;
	}

	/**
	 * This method initializes ivjJMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getIvjJMenuItem() {
		if (ivjJMenuItem == null) {
			ivjJMenuItem = new javax.swing.JMenuItem();
			ivjJMenuItem.setText("Modify values");
		}
		return ivjJMenuItem;
	}

	/**
	 * This method initializes ivjJMenu2
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getIvjJMenu2() {
		if (ivjJMenu2 == null) {
			ivjJMenu2 = new javax.swing.JMenu();
			ivjJMenu2.add(getIvjJMenuItem2());
			ivjJMenu2.setText("Refill");
			ivjJMenu2.add(getJMenuItem());
			ivjJMenu2.add(getJMenuItem1());
			ivjJMenu2.add(getJMenuItem2());
		}
		return ivjJMenu2;
	}

	/**
	 * This method initializes ivjJMenuItem2
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getIvjJMenuItem2() {
		if (ivjJMenuItem2 == null) {
			ivjJMenuItem2 = new javax.swing.JMenuItem();
			ivjJMenuItem2.setText("Cakes");
		}
		return ivjJMenuItem2;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem() {
		if (jMenuItem == null) {
			jMenuItem = new JMenuItem();
			jMenuItem.setText("Chocolate");
		}
		return jMenuItem;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Reports");
			jMenu.add(getJMenuItem5());
			jMenu.add(getJMenuItem4());
		}
		return jMenu;
	}

	/**
	 * This method initializes jMenuItem1	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem1() {
		if (jMenuItem1 == null) {
			jMenuItem1 = new JMenuItem();
			jMenuItem1.setText("Caramel");
		}
		return jMenuItem1;
	}

	/**
	 * This method initializes jMenuItem2	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem2() {
		if (jMenuItem2 == null) {
			jMenuItem2 = new JMenuItem();
			jMenuItem2.setText("Plastic Film");
		}
		return jMenuItem2;
	}

	/**
	 * This method initializes jMenuItem3	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem3() {
		if (jMenuItem3 == null) {
			jMenuItem3 = new JMenuItem();
			jMenuItem3.setText("Reset default values");
		}
		return jMenuItem3;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getJButton(), null);
			jPanel.add(getStop(), null);
			jPanel.add(getStart(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(7, 7, 244, 49));
			jButton.setFont(new Font("Dialog", Font.BOLD, 14));
			jButton.setText("EMERGENCY STOP");
		}
		return jButton;
	}

	/**
	 * This method initializes Stop	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getStop() {
		if (Stop == null) {
			Stop = new JButton();
			Stop.setBounds(new Rectangle(57, 59, 91, 40));
			Stop.setText("Stop");
		}
		return Stop;
	}

	/**
	 * This method initializes Start	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getStart() {
		if (Start == null) {
			Start = new JButton();
			Start.setBounds(new Rectangle(153, 59, 87, 40));
			Start.setFont(new Font("Dialog", Font.BOLD, 12));
			Start.setText("Start");
		}
		return Start;
	}

	/**
	 * This method initializes jMenuItem4	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem4() {
		if (jMenuItem4 == null) {
			jMenuItem4 = new JMenuItem();
			jMenuItem4.setText("Get PDF summary report");
		}
		return jMenuItem4;
	}

	/**
	 * This method initializes jMenuItem5	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItem5() {
		if (jMenuItem5 == null) {
			jMenuItem5 = new JMenuItem();
			jMenuItem5.setText("Show reports");
		}
		return jMenuItem5;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="0,0"
