import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

/**
 * The main class for the Map Deleter Project.
 * This project is for editing infinite maps for the game Minecraft created by Notch.
 * 
 * @author Charles Stebbins
 *
 */
public class MapDeleter extends JFrame implements ActionListener{

	/**
	 * The generated serialVersion
	 */
	private static final long serialVersionUID = -3232794473698861728L;
	
	/**
	 * The class which controls editing the maps.
	 */
	private MapEditor mapEdit;
    
	/**
     * Displays the progress of a specific task.
     */
	private JProgressBar mapLoadProgress;
    
	/**
     * The area for displaying the rendered map.
     */
	private JScrollPane pictureScrollPane;

	/**
	 * Constructor for the MapDeleter.  Creates and shows the window with a default image in it.
	 * @param title The string to be displayed in the top left of the window.
	 */
	public MapDeleter(String title){
		super(title);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(695, 602));

		//Make the mapEdit with an empty image.
        this.mapEdit = new MapEditor(new ImageIcon(), this);
        //Get the image to use.

        JMenuBar mainMenuBar = new JMenuBar();
		
        //File Menu Bar - Holds user option for FileIO and window modification.
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileMenuOpen = new JMenuItem("Open (World Directory)");
		JMenuItem fileMenuSave = new JMenuItem("Save");
		JMenuItem fileMenuSavePNG = new JMenuItem("Save as PNG");
		JMenuItem fileMenuExit = new JMenuItem("Exit"); 
		
		fileMenu.add(fileMenuOpen);
		fileMenu.add(fileMenuSave);
		fileMenu.add(fileMenuSavePNG);
		fileMenu.addSeparator();
		fileMenu.add(fileMenuExit);
		mainMenuBar.add(fileMenu);

		fileMenuOpen.addActionListener(this.mapEdit);
		fileMenuSave.addActionListener(this.mapEdit);
		fileMenuSavePNG.addActionListener(this.mapEdit);
		fileMenuExit.addActionListener(this);
		
		// Level Data Menu Bar - Holds options for editing the level.
		// TODO - Implement.
		JMenu levelMenu = new JMenu("Level Data");
		JMenuItem levelMenuSetSpawn = new JMenuItem("Set Spawn to Selection");
		JMenuItem levelMenuSetMapInfo = new JMenuItem("Set Map Info");
		levelMenu.add(levelMenuSetSpawn);
		levelMenu.add(levelMenuSetMapInfo);
		//mainMenuBar.add(levelMenu);
		
		// Map Modification Menu - Hold options to edit the map.
		JMenu mapMenu = new JMenu("Map Modification");
		JMenuItem mapMenuGenerateTrees = new JMenuItem("Generate Trees");
		JMenuItem mapMenuTerraformSelected = new JMenuItem("Terraform Selection");
		JMenuItem mapMenuCopySelection = new JMenuItem("Copy Selection");
		JMenuItem mapMenuPasteSelection = new JMenuItem("Paste Selection");
		JMenuItem mapMenuDeleteSelection = new JMenuItem("Delete Selection");
		mapMenu.add(mapMenuCopySelection);
		mapMenu.add(mapMenuPasteSelection);
		mapMenu.add(mapMenuDeleteSelection);
		mapMenu.addSeparator();
		mapMenu.add(mapMenuGenerateTrees);
		mapMenu.addSeparator();
		mapMenu.add(mapMenuTerraformSelected);
		mainMenuBar.add(mapMenu);
		
		//adding actionListeners to the mapMenu
		mapMenuCopySelection.addActionListener(this.mapEdit);
		mapMenuPasteSelection.addActionListener(this.mapEdit);
		mapMenuDeleteSelection.addActionListener(this.mapEdit);
		mapMenuGenerateTrees.addActionListener(this.mapEdit);
		mapMenuTerraformSelected.addActionListener(this.mapEdit);
		
		// View Modes Menu - Changes how the user interacts with the map viewer.
		JMenu viewMenu = new JMenu("View Modes");
		ButtonGroup mapDisplayType = new ButtonGroup();
		JRadioButtonMenuItem coloredView = new JRadioButtonMenuItem("View Colored Map");
		JRadioButtonMenuItem topoView = new JRadioButtonMenuItem("View Topographic Map");
		coloredView.setSelected(true);
		topoView.setSelected(false);
		
		mapDisplayType.add(coloredView);
		mapDisplayType.add(topoView);
		viewMenu.add(coloredView);
		viewMenu.add(topoView);
		viewMenu.addSeparator();
		
		coloredView.addActionListener(this.mapEdit);
		topoView.addActionListener(this.mapEdit);
		
		//Radio Buttons for the different selections
		ButtonGroup mapSelectType = new ButtonGroup();
		JRadioButtonMenuItem chunkSelect = new JRadioButtonMenuItem("Chunk Selection");
		JRadioButtonMenuItem blockSelect = new JRadioButtonMenuItem("Block Selection");
		chunkSelect.setSelected(true);
		blockSelect.setSelected(false);
		mapSelectType.add(chunkSelect);
		mapSelectType.add(blockSelect);
		viewMenu.add(chunkSelect);
		viewMenu.add(blockSelect);
		mainMenuBar.add(viewMenu);
		
		//Set up Action Listeners
		chunkSelect.addActionListener(this.mapEdit);
		blockSelect.addActionListener(this.mapEdit);
		
		viewMenu.addActionListener(this.mapEdit);
		fileMenu.addActionListener(this.mapEdit);
		mapMenu.addActionListener(this.mapEdit);

		this.setJMenuBar(mainMenuBar);
       
		
		// Setting up the top buttons.
        GridBagConstraints c = new GridBagConstraints();		

        JButton invertSelectionButton = new JButton("Invert Selection");
        invertSelectionButton.addActionListener(this.mapEdit);
        invertSelectionButton.setName("invertSelection");
        c.gridx = 0;
        c.gridy = 0;
        add(invertSelectionButton, c);
        
        
        JToggleButton fillButton = new JToggleButton("Fill Selection");
        fillButton.addActionListener(this.mapEdit);
        fillButton.setName("fillSelection");
        c.gridx = 1;
        c.gridy = 0;
        add(fillButton, c);

        
        JButton unselectButton = new JButton("Unselect All");
        unselectButton.addActionListener(this.mapEdit);
        unselectButton.setName("unselectAll");
        c.gridx = 2;
        c.gridy = 0;
        add(unselectButton, c);
        
        JButton selectButton = new JButton("Select All");
        selectButton.addActionListener(this.mapEdit);
        selectButton.setName("selectAll");
        c.gridx = 3;
        c.gridy = 0;
        add(selectButton, c);
        
       
        c = new GridBagConstraints();
        
        this.mapLoadProgress = new JProgressBar(0, 100);
        this.mapLoadProgress.setStringPainted(true);
        this.mapLoadProgress.setString("");
        this.mapLoadProgress.setSize(150, 20);
        c.gridx = 4;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        add(mapLoadProgress);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.BOTH;

        pictureScrollPane = new JScrollPane(mapEdit);
        pictureScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        pictureScrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        this.mapEdit.loadMap("");
        //Set up the scroll pane.
        pictureScrollPane.setPreferredSize(new Dimension(300, 250));
        pictureScrollPane.setViewportBorder(
                BorderFactory.createLineBorder(Color.black));
        
        this.repaint();
        add(pictureScrollPane, c);
        
	}



    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void main(String[] args) {
    	MapDeleter frame = new MapDeleter("Map Deleter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //tempScroll.add(points);
        frame.update(null);        
        frame.setSize(695, 602);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }
    
    /*
     * This actionPerformed listener handles events meant for the JFrame,
     * rather than events meant for MapEditor ("the brains").
     */
    @Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Exit") {
			System.exit(0);
		}
		else {
			System.err.println("[WARN] MapDeleter JFrame received unknown action: "+e.getActionCommand());
		}
	}

	/**
     * Sets the progress bar to be filled with a blue bar to a given value.
     * @param value The new value of the progress bar in percent.  50 is 50% of the progress bar filled.
     * @param s - The text to overlay the progress bar.
     */
	public void setProgressBarValue(int value, String s){
		this.mapLoadProgress.setValue(value);
		this.mapLoadProgress.setString(s);
		this.mapLoadProgress.setForeground(new Color(0, 0, 216));
		if(this.mapLoadProgress.getString().compareTo(s) != 0){
			this.mapLoadProgress.setString(s);
		}
		this.mapLoadProgress.update(this.mapLoadProgress.getGraphics());
		
	}
	
	/**
	 * Sets the progress bar to be filled with a red bar.
	 * @param s Text to overlay the progress bar.
	 */
	public void progressBarFailed(String s){
		this.mapLoadProgress.setValue(100);
		this.mapLoadProgress.setString(s);
		this.mapLoadProgress.setForeground(new Color(200, 20, 20));
		this.mapLoadProgress.setString(s);
		this.mapLoadProgress.update(this.mapLoadProgress.getGraphics());
	}
	
	/**
	 * Sets the progress bar to be filled with a green bar.
	 * @param s Text to overlay the progress bar.
	 */
	public void progressBarComplete(String s){
		this.mapLoadProgress.setValue(100);
		this.mapLoadProgress.setString(s);
		this.mapLoadProgress.setForeground(new Color(32, 173, 42));
		this.mapLoadProgress.setString(s);
		this.mapLoadProgress.update(this.mapLoadProgress.getGraphics());
	}
	
	public void scrollPaneUpdate() {

		this.pictureScrollPane.revalidate();/*
		this.update(this.pictureScrollPane.getGraphics());
		this.update(this.getGraphics());
		this.repaint();*/
	}
	
	/**
	 * Returns the map editor that is being used to edit the current map.
	 * @return The map editor used for editing the current map.
	 * @see MapEditor
	 */
	public MapEditor getMapEditor(){
		return this.mapEdit;
	}
}
