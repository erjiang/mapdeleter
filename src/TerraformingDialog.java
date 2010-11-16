import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Dialog for the Terraforming function.
 * @author Charles
 *
 */
public class TerraformingDialog extends JDialog implements ActionListener, ChangeListener {

	private MapEditor parent;
	private Container mainContainer;
	private JSlider heightSlider;
	private LinkedList<MapChunk> selectedChunks;
	private LinkedList<MapChunk> selectedChunksOld;
	private byte heightChange;
	private byte heighestBlock;
	private byte lowestBlock;
	private JLabel changeInHeightLabel;
	private JLabel lowestBlockLabel;
	private JLabel heighestBlockLabel;
	private MapContainer mapData;
	private static final long serialVersionUID = -9174236473875308560L;
	
	/**
	 * Constructor for the terraforming.
	 * @param frame Parent frame to attach to.
	 * @param mapDat The Map Data to modifying
	 */
	public TerraformingDialog(JFrame frame, MapContainer mapDat){
		super(frame, "Terraform", false);
		MapDeleter t = (MapDeleter)frame;
		this.parent = t.getMapEditor();
		this.selectedChunksOld = new LinkedList<MapChunk>();
		this.mapData = mapDat;
		
		this.selectedChunks = this.mapData.getSelectedChunks();
		this.heighestBlock = 0;
		this.lowestBlock = 127;
		
		this.mainContainer = getContentPane();
		this.mainContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.mainContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		this.setSize(285, 320);

		JLabel titleLabel = new JLabel("Terraform:");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		this.mainContainer.add(titleLabel, c);
		
		c = new GridBagConstraints();
		JLabel descriptionLabel = new JLabel("Block's change in height.\n Note: Sea level is 64");
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		this.mainContainer.add(descriptionLabel, c);
		
		c = new GridBagConstraints();
		JLabel spaceTop = new JLabel();
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = .4;
		c.fill = GridBagConstraints.VERTICAL;
		this.mainContainer.add(spaceTop, c);
		
		c = new GridBagConstraints();
		this.heighestBlockLabel =  new JLabel("Highest Block: 52");
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = .2;
		c.weighty = .025;
		this.mainContainer.add(heighestBlockLabel, c);
		
		c = new GridBagConstraints();
		this.lowestBlockLabel =    new JLabel("Lowest  Block: 52");
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = .2;
		c.weighty = .025;
		this.mainContainer.add(lowestBlockLabel, c);
		
		c = new GridBagConstraints();
		this.changeInHeightLabel = new JLabel("Height Change: 0");
		c.gridx = 0;
		c.gridy = 5;
		c.weightx = .2;
		c.weighty = .025;
		this.mainContainer.add(changeInHeightLabel, c);
		
		c = new GridBagConstraints();
		JButton previewButton = new JButton("Preview");
		previewButton.addActionListener(this.parent);
		c.gridx = 0;
		c.gridy = 6;
		c.weightx = .2;
		c.weighty = .025;
		this.mainContainer.add(previewButton, c);
		
		c = new GridBagConstraints();
		JLabel spaceBottom = new JLabel();
		c.gridx = 0;
		c.gridy = 7;
		c.weighty = .4;
		this.mainContainer.add(spaceBottom, c);
		
		c = new GridBagConstraints();
		this.heightSlider = new JSlider(JSlider.VERTICAL, -127, 127, 0);
		c.gridx = 1;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = .8;
		c.weightx = .8;
		c.gridheight = 6;
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(-127, new JLabel("-127"));
		labelTable.put(-64, new JLabel("-64"));
		labelTable.put(0, new JLabel("0"));
		labelTable.put(64, new JLabel("64"));
		labelTable.put(127, new JLabel("127"));
		this.heightSlider.setLabelTable(labelTable);
		this.heightSlider.setPaintLabels(true);
		this.heightSlider.setMajorTickSpacing(32);
		this.heightSlider.setMinorTickSpacing(16);
		this.heightSlider.addChangeListener(this);
		this.mainContainer.add(this.heightSlider, c);
		
		c = new GridBagConstraints();
		JButton commitButton = new JButton("Commit");
		commitButton.addActionListener(this.parent);
		c.gridx = 0;
		c.gridy = 8;
		c.weightx = .5;
		c.weighty = 0;
		this.mainContainer.add(commitButton,c);
		
		c = new GridBagConstraints();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 8;
		c.weightx = .5;
		c.weighty = 0;
		this.mainContainer.add(cancelButton, c);
		
		c = new GridBagConstraints();
		JLabel spaceBottomMain = new JLabel(" ");
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 2;
		c.weighty = .2;
		this.mainContainer.add(spaceBottomMain, c);
		
		this.initialize();
	}
	
	/**
	 * Set up the varables and stuff upon loading.
	 */
	private void initialize(){
		Iterator<MapChunk> iter = this.selectedChunks.iterator();
		MapChunk tempChunk;
		//Go through each mapChunk
		while(iter.hasNext()){
			tempChunk = iter.next();
			this.selectedChunksOld.add(new MapChunk(tempChunk));
			byte[][] tempHeight = tempChunk.getHeightMap();
			MapChunkSelected tempSelected = this.mapData.getChunkSelected(tempChunk);
			for(int z = 0; z < tempHeight.length; z++){
				for(int x = 0; x < tempHeight[0].length; x++){
					//Find the highest and lowest block.
					if(tempSelected.isBlockSelected(z, x)){
						if(this.heighestBlock < tempHeight[z][x]){
							this.heighestBlock = new Byte(tempHeight[z][x]);
						}
						if(this.lowestBlock > tempHeight[z][x]){
							this.lowestBlock = new Byte(tempHeight[z][x]);
						}
					}
				}
			}
		}
		this.heighestBlockLabel.setText("Highest Block: " + this.heighestBlock);
		  this.lowestBlockLabel.setText("Lowest Block : " + this.lowestBlock);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e);
	}
	
	/**
	 * Get the value/height of the slider.
	 * @return Return the value of the slider
	 */
	public byte getChangeHeight(){
		return (byte)this.heightSlider.getValue();
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		System.out.println(this.heightSlider.getValue());
		String tempStr = "";
		this.heightChange = (byte) this.heightSlider.getValue();
		tempStr = new String(this.heightChange + "");
		while(tempStr.length() < 5)
			tempStr = "  " + tempStr;
		this.changeInHeightLabel.setText("Height Change: " + tempStr);
		if(this.heighestBlock + this.heightChange > 127)
			this.heighestBlockLabel.setText("Highest Block: 127");
		else if(this.heighestBlock + this.heightChange < 0)
			this.heighestBlockLabel.setText("Highest Block: 0");
		else
			this.heighestBlockLabel.setText("Highest Block: " + (byte)(this.heighestBlock + this.heightChange));
		
		if(this.lowestBlock + this.heightChange > 127)
			this.lowestBlockLabel.setText("Lowest Block : 127");
		else if(this.lowestBlock + this.heightChange < 0)
			this.lowestBlockLabel.setText("Lowest Block : 0");
		else
			this.lowestBlockLabel.setText("Lowest Block : " + (byte)(this.lowestBlock + this.heightChange));
	}
	
	/**
	 * Creates a preview of the terraform that the user wants to do.
	 */
	public void setPreviewHeights(){
		Iterator<MapChunk> chunkIter = this.selectedChunks.iterator();
		Iterator<MapChunk> oldChunkIter = this.selectedChunksOld.iterator();
		while(chunkIter.hasNext() && oldChunkIter.hasNext()){
			MapChunk tempChunk = chunkIter.next();
			MapChunk oldTempChunk = oldChunkIter.next();
			MapChunkSelected tempSelected = this.mapData.getChunkSelected(tempChunk);
			byte[][] oldHeightMap = oldTempChunk.getHeightMap();
			byte[][] oldHeightMapTopo = oldTempChunk.getHeightMapTopo();
			byte[][] newHeightMap = new byte[oldHeightMap.length][oldHeightMap[0].length];
			byte[][] newHeightMapTopo = new byte[oldHeightMapTopo.length][oldHeightMapTopo[0].length];
			for(int z = 0; z < oldHeightMap.length; z++){
				for(int x = 0; x < oldHeightMap[0].length; x++){
					if(tempSelected.isBlockSelected(z, x)){
						if(oldHeightMap[z][x] + this.heightSlider.getValue() > 127)
							newHeightMap[z][x] = 127;
						else if(oldHeightMap[z][x] + this.heightSlider.getValue() < 0)
							newHeightMap[z][x] = 0;
						else
							newHeightMap[z][x] = (byte) (oldHeightMap[z][x] + this.heightSlider.getValue());
						
						if(oldHeightMapTopo[z][x] + this.heightSlider.getValue() > 127)
							newHeightMapTopo[z][x] = 127;
						else if(oldHeightMapTopo[z][x] + this.heightSlider.getValue() < 0)
							newHeightMapTopo[z][x] = 0;
						else
							newHeightMapTopo[z][x] = (byte) (oldHeightMapTopo[z][x] + this.heightSlider.getValue());
					}	
					else{
						newHeightMap[z][x] = oldHeightMap[z][x];
						newHeightMapTopo[z][x] = oldHeightMapTopo[z][x];
					}
				}
			}
			tempChunk.setHeightMap(newHeightMap);
			tempChunk.setHeightMapTopo(newHeightMapTopo);
		}
	}

	/**
	 * Create the save commands for modifying the map.
	 * @return LinkedList of SaveCommands to modify the height.
	 */
	public LinkedList<SaveCommand> getSaveCommands() {
		LinkedList<SaveCommand> returnCommands = new LinkedList<SaveCommand>();
		Iterator<MapChunk> saveIter = this.selectedChunks.iterator();
		while(saveIter.hasNext()){
			MapChunk tempChunk = saveIter.next();
			MapChunkSelected tempSelected = this.mapData.getChunkSelected(tempChunk);
			byte[][] tempHeightMap = tempChunk.getHeightMap();
			for(int z = 0; z < tempHeightMap.length; z++){
				for(int x = 0; x < tempHeightMap[0].length; x++){
					if(tempSelected.isBlockSelected(z, x)){
						Coordinates returnCoord = new Coordinates(z, x, this.heightSlider.getValue());;
						if((int)tempHeightMap[z][x] == 127){
							returnCoord = new Coordinates(z, x, 127 - tempHeightMap[z][x]);
							System.out.println("Value greater than 127");
							System.out.println("TempHeight: " + tempHeightMap[z][x] + " | Value Slider: " + this.heightSlider.getValue());
						}
						if((int)tempHeightMap[z][x] == 0){
							System.out.println("Value less than 1");
							returnCoord = new Coordinates(z, x, 1 - tempHeightMap[z][x]);
						}
						System.out.println("Height Slider: " + this.heightSlider.getValue());
						
						returnCommands.add(new SaveCommand(SaveCommand.CHANGE_HEIGHT_COMMAND, 
															tempChunk.getFilePath(),
															returnCoord));
						System.out.println(returnCoord.getY() + "  RETURN COORD +++++++++");
					}
				}
			}
		}
		return returnCommands;
	}

}
