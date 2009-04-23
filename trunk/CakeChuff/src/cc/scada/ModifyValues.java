package cc.scada;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
/**
 * Example of components laid out in a grid
 */
public class ModifyValues extends javax.swing.JFrame {
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JTextField ivjJTextField1 = null;
	private JButton ivjJButton11 = null;
	private JLabel ivjJLabel11 = null;
	private JLabel ivjJLabel12 = null;
	public ModifyValues() {
		super();
		initialize();
	}

	/**
	 * Return the JButton1 property value.
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getJButton1() {
		if (ivjJButton1 == null) {
			ivjJButton1 = new javax.swing.JButton();
			ivjJButton1.setName("JButton1");
			ivjJButton1.setBounds(new Rectangle(63, 191, 87, 26));
			ivjJButton1.setText("OK");
		}
		return ivjJButton1;
	}

	/**
	 * Return the JFrameContentPane property value.
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			ivjJLabel12 = new JLabel();
			ivjJLabel12.setBounds(new Rectangle(16, 118, 108, 28));
			ivjJLabel12.setName("JLabel1");
			ivjJLabel12.setText("    SUBSYSTEM 3");
			ivjJLabel12.setFont(new Font("Dialog", Font.BOLD, 12));
			ivjJLabel11 = new JLabel();
			ivjJLabel11.setBounds(new Rectangle(16, 65, 108, 30));
			ivjJLabel11.setName("JLabel1");
			ivjJLabel11.setText("    SUBSYSTEM 2");
			ivjJLabel11.setFont(new Font("Dialog", Font.BOLD, 12));
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setLayout(null);
			ivjJFrameContentPane.add(getJLabel1(), null);
			ivjJFrameContentPane.add(getJButton1(), null);
			ivjJFrameContentPane.add(getJTextField1(), null);
			ivjJFrameContentPane.add(getIvjJButton11(), null);
			ivjJFrameContentPane.add(ivjJLabel11, null);
			ivjJFrameContentPane.add(ivjJLabel12, null);
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Return the JLabel1 property value.
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (ivjJLabel1 == null) {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setBounds(new Rectangle(16, 12, 104, 32));
			ivjJLabel1.setFont(new Font("Dialog", Font.BOLD, 12));
			ivjJLabel1.setText("    SUBSYSTEM 1");
		}
		return ivjJLabel1;
	}

	/**
	 * Return the JTextField1 property value.
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getJTextField1() {
		if (ivjJTextField1 == null) {
			ivjJTextField1 = new javax.swing.JTextField();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setBounds(new Rectangle(168, 144, 133, 28));
			ivjJTextField1.setText("JTextField");
		}
		return ivjJTextField1;
	}

	/**
	 * Initialize the class.
	 */
	private void initialize() {

		this.setName("JFrame1");
		this
				.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(45, 25, 317, 273);
		this.setTitle("Modify Values");
		this.setContentPane(getJFrameContentPane());

	}

	/**
	 * This method initializes ivjJButton11	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getIvjJButton11() {
		if (ivjJButton11 == null) {
			ivjJButton11 = new JButton();
			ivjJButton11.setBounds(new Rectangle(176, 191, 81, 26));
			ivjJButton11.setText("Cancel");
			ivjJButton11.setName("JButton1");
		}
		return ivjJButton11;
	}
}
