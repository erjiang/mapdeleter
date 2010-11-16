import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * Dialog to display the options for generating trees.
 * @author Charles
 *
 */
public class TreeGeneratorDialog extends JDialog implements ActionListener {

	private MapEditor parent;
	private Container mainContainer;
	private static final long serialVersionUID = 8546315463187L;
	
	private JLabel generateTreeDialog;
	private JButton generateTreeButton;
	private JButton cancelButton;
	private JSlider densitySlider;
	
	/**
	 * Constructor, takes the frame to hook onto.
	 * @param frame The frame to hook to.
	 */
	public TreeGeneratorDialog(JFrame frame){
		super(frame,  "Tree Generator", false);
		MapDeleter t = (MapDeleter)frame;
		this.parent = t.getMapEditor();
		
		this.mainContainer = getContentPane();
		this.mainContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.mainContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		
		this.setSize(350, 150);
		this.generateTreeDialog = new JLabel("Click the button to generate trees!");
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.ipady = 20;
		this.mainContainer.add(this.generateTreeDialog, c);
		
		c = new GridBagConstraints();
		
		
		c = new GridBagConstraints();
				
		this.densitySlider = new JSlider(JSlider.HORIZONTAL, 5000, 1);
		this.densitySlider.setMajorTickSpacing(1000);
		this.densitySlider.setMinorTickSpacing(1000);
		this.densitySlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("Scarce"));
		labelTable.put(5000, new JLabel("Very Dense"));
		labelTable.put(2500, new JLabel("Dense"));
		this.densitySlider.setLabelTable(labelTable);
		this.densitySlider.setPaintLabels(true);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		this.mainContainer.add(this.densitySlider, c);
		
		c = new GridBagConstraints();
		
		this.generateTreeButton = new JButton("Generate");
		this.generateTreeButton.setName("generateTrees");
		this.generateTreeButton.addActionListener(this);
		this.generateTreeButton.addActionListener(this.parent);
		c.gridx = 0;
		c.gridy = 2;
		this.mainContainer.add(this.generateTreeButton, c);
		
		c = new GridBagConstraints();
		
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 2;
		this.mainContainer.add(this.cancelButton, c);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().compareTo("Cancel") == 0){
			this.setVisible(false);
		}
		
	}
	
	/**
	 * Get the density/value of the slider.
	 * @return the density of the trees.
	 */
	public int getDensity(){
		return this.densitySlider.getValue();
	}

}
