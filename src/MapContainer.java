import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Contains all of the MapChunks and is in charge of mass modifying the chunks.
 * @author Charles
 *
 */
public class MapContainer {
	private String mapDir = "";
	private int currentDirNum;
	private int progress;
	private ArrayList<MapChunk> mapChunkList;
	private int[][] mapChunkIds;
	private MapChunkSelected[][] mapChunkSelected;
	private int xSmallestChunk;
	private int xLargestChunk;
	private int zSmallestChunk;
	private int zLargestChunk;
	private int mapWidth;
	private int mapHeight;
	
	/**
	 * Takes the directory of the fileName and traverses through finding all of the map chunks.
	 * @param fileName Directory holding the folders for the map chunks.
	 * @param parent The MapDeleter class.
	 */
	public MapContainer(String fileName, MapDeleter parent){
		this.progress = 0;
		this.mapDir = fileName;
		this.mapChunkList = new ArrayList<MapChunk>();
		File worldDir = new File(this.mapDir);
		
		//Start traversing through all of the directories.
		int count=0;
		for(File file : worldDir.listFiles()){
			//Set the progress bar depending on how many folders have been traversed.
			this.progress = (int)((double)count / (double) worldDir.listFiles().length * 100);
			parent.setProgressBarValue(this.progress, "Loading World");
			count++;
			//Traverse the directories.
			traverseDir(file);
		}
		parent.progressBarComplete("Loaded Map");
		
		//Make an array for each mapChunkID
		this.mapChunkIds = new int[this.zLargestChunk - this.zSmallestChunk + 1]
			                      [this.xLargestChunk - this.xSmallestChunk + 1];
		
		//Make an array for each MapChunkSelected.
		this.mapChunkSelected = new MapChunkSelected[this.zLargestChunk - this.zSmallestChunk + 1]
		  			                       [this.xLargestChunk - this.xSmallestChunk + 1];
		
		//Load the arrays with default values.
		for(int z=0; z<this.mapChunkIds.length; z++){
			for(int x=0; x<this.mapChunkIds[0].length; x++){
				this.mapChunkIds[z][x] = -1;
				this.mapChunkSelected[z][x] = new MapChunkSelected();
			}
		}
		

		this.mapWidth = this.mapChunkIds[0].length;
		this.mapHeight = this.mapChunkIds.length;
		
		//Load the actual values into the mapChunkIds array.
		for(int i=0; i<this.mapChunkList.size(); i++){
			this.mapChunkIds[this.mapChunkList.get(i).getZPos() - this.zSmallestChunk]
			                [this.mapChunkList.get(i).getXPos() - this.xSmallestChunk] = i;
		}
				
	}
	
	/**
	 * Traverses through each sub directory and file.
	 * If it finds a MapChunk file it will add the MapChunk to the list.
	 * @param dir
	 */
	private void traverseDir(File dir){
		//Found a directory, TRAVERSE!!!!!
		// QUICKFIX: Do not transverse Nether Dir
		String d = dir.getAbsolutePath();
    	if(dir.isDirectory() && ! dir.getName().equals("DIM-1")){
    		for(File file : dir.listFiles()){
    			traverseDir(file);
    		}
    	}
    	else{
    		//WOOHOO A FILE, ADD IT!!!!
    		if(dir.getParent().toString().compareTo(this.mapDir) != 0){
	    		try
	    		{
	    			MapChunk currentChunk = new MapChunk(dir.toString());
	    		if(this.xSmallestChunk > currentChunk.getXPos())
	    			this.xSmallestChunk = currentChunk.getXPos();
	    		if(this.xLargestChunk < currentChunk.getXPos()){
	    			this.xLargestChunk = currentChunk.getXPos();
	    		}
	    		if(this.zSmallestChunk > currentChunk.getZPos()){
	    			this.zSmallestChunk = currentChunk.getZPos();
	    		}
	    		if(this.zLargestChunk < currentChunk.getZPos()){
	    			this.zLargestChunk = currentChunk.getZPos();
	    		}
	    		System.out.println(currentChunk);
	    		this.mapChunkList.add(currentChunk);
	    		}
	    		catch(IOException e){
	    			//It wasn't what we thought it was ;-;
	    			System.out.println(e);
	    		}
	    		catch(Exception e){
	    			//OH SHIT OH SHIT OH SHIT.
	    		}
    	
    		}
    	}
    }
		
	/**
	 * Get the color corresponding to the given block.
	 * @param blockCode The block to get the color of.
	 * @return A color of the block.
	 */
    public Color getTopBlockColor(byte blockCode) {
		Color returnColor;
		returnColor = new Color(0, 0, 0);
    	switch(blockCode){
			case Blocks.AIR:
				returnColor = new Color(0,0,0);
				break;
			case Blocks.STONE:
				returnColor = new Color(126, 126, 126);
				break;
			case Blocks.GRASS:
				returnColor = new Color(123, 191, 56);
				break;
			case Blocks.DIRT:
				returnColor = new Color(128, 62, 0);
				break;
			case Blocks.COBBLE_STONE:
				returnColor = new Color(56, 56, 56);
				break;
			case Blocks.WOOD:
				returnColor = new Color(84, 40, 0);
				break;
			case Blocks.SAPLING:
				returnColor = new Color(76, 255, 0);
				break;
			case Blocks.ADMINIUM:
				returnColor = new Color(45, 45, 45);
				break;
			case Blocks.WATER:
				returnColor = new Color(25, 103, 191);
				break;
			case Blocks.STILL_WATER:
				returnColor = new Color(25, 103, 191);
				break;
			case Blocks.LAVA:
				returnColor = new Color(255, 90, 0);
				break;
			case Blocks.STILL_LAVA:
				returnColor = new Color(255, 90, 0);
				break;
			case Blocks.SAND:
				returnColor = new Color(255, 232, 157);
				break;
			case Blocks.GRAVEL:
				returnColor = new Color(163, 153, 121);
				break;
			case Blocks.GOLD:
				returnColor = new Color(255, 186, 0);
				break;
			case Blocks.IRON:
				returnColor = new Color(255, 137, 137);
				break;
			case Blocks.COAL:
				returnColor = new Color(0, 0, 0);
				break;
			case Blocks.TREE_TRUNK:
				returnColor = new Color(134, 86, 23);
				break;
			case Blocks.LEAVES:
				returnColor = new Color(3, 65, 0);
				break;
			case Blocks.SPONGE:
				returnColor = new Color(223, 221, 23);
				break;
			case Blocks.GLASS:
				break;
			case Blocks.RED_CLOTH:
				returnColor = new Color(255, 0, 0);
				break;
			case Blocks.ORANGE_CLOTH:
				break;
			case Blocks.YELLOW_CLOTH:
				break;
			case Blocks.LIGHT_GREEN_CLOTH:
				break;
			case Blocks.GREEN_CLOTH:
				break;
			case Blocks.AQUA_GREEN_CLOTH:
				break;
			case Blocks.CYAN_CLOTH:
				break;
			case Blocks.BLUE_CLOTH:
				break;
			case Blocks.PURPLE_CLOTH:
				break;
			case Blocks.INDIGO_CLOTH:
				returnColor = new Color(75, 0, 130);
				break;
			case Blocks.VIOLET_CLOTH:
				returnColor = new Color(138, 43, 226);
				break;
			case Blocks.MAGENTA_CLOTH:
				returnColor = new Color(255, 0, 255);
				break;
			case Blocks.PINK_CLOTH:
				returnColor = new Color(255, 192, 203);
				break;
			case Blocks.BLACK_CLOTH:
				returnColor = new Color(0, 0, 0);
				break;
			case Blocks.GRAY_CLOTH:
				returnColor = new Color(192, 192, 192);
				break;
			case Blocks.WHITE_CLOTH:
				returnColor = new Color(255, 255, 255);
				break;
			case Blocks.YELLOW_FLOWER:
				break;
			case Blocks.RED_ROSE:
				break;
			case Blocks.BROWN_MUSHROOM:
				break;
			case Blocks.RED_MUSHROOM:
				break;
			case Blocks.GOLD_BLOCK:
				returnColor = new Color(246, 212, 45);
				break;
			case Blocks.IRON_BLOCK:
				returnColor = new Color(211, 211, 211);
				break;
			case Blocks.DOUBLE_STAIR:
				returnColor = new Color(168, 168, 168);
				break;
			case Blocks.STAIR:
				returnColor = new Color(26, 26, 26);
				break;
			case Blocks.BRICK:
				returnColor = new Color(113, 55, 39);
				break;
			case Blocks.TNT:
				break;
			case Blocks.BOOKCASE:
				break;
			case Blocks.MOSSY_COBBLE_STONE:
				break;
			case Blocks.OBSIDION:
				break;
			case Blocks.TORCH:
				returnColor = new Color(247, 204, 0);
				break;
			case Blocks.FIRE_BLOCK:
				break;
			case Blocks.MOB_SPAWNER:
				break;
			case Blocks.STAIR_WOOD:
				break;
			case Blocks.CHEST:
				break;
			case Blocks.GEAR:
				break;
			case Blocks.DIAMOND_ORE:
				break;
			case Blocks.DIAMOND_BLOCK:
				break;
			case Blocks.CRAFTING_TABLE:
				break;
			case Blocks.CROPS:
				break;
			case Blocks.SOIL:
				returnColor = new Color(127, 85, 51);
				break;
			case Blocks.FURNACE:
				break;
			case Blocks.BURNING_FURNACE:
				break;
			case Blocks.BLANK_SIGNS:
				break;
			case Blocks.WOODEN_DOOR_BOTTOM:
				break;
			case Blocks.LADDER:
				break;
			case Blocks.MINECART_RAIL:
				break;
			case Blocks.COBBLE_STONE_STAIR:
				returnColor = new Color(26, 26, 26);
				break;
			case Blocks.WALL_SIGN:
				break;
			case Blocks.LEVER:
				break;
			case Blocks.COBBLE_STONE_PRESSURE_PLATE:
				break;
			case Blocks.IRON_DOOR:
				break;
			case Blocks.WOODEN_PRESSURE_PLATE:
				break;
			case Blocks.REDSTONE_ORE:
				break;
			case Blocks.REDSTONE_ORE_2:
				break;
			case Blocks.REDSTONE_TORCH_OFF:
				break;
			case Blocks.REDSTONE_TORCH_ON:
				break;
			case Blocks.STONE_BUTTON:
				break;
			case Blocks.SNOW:
				returnColor = new Color(200, 200, 200);
				break;
			case Blocks.ICE:
				returnColor = new Color(55, 133, 211);
				break;
			case Blocks.SNOW_BLOCK:
				returnColor = new Color(227, 243, 243);
				break;
			case Blocks.CACTI:
				break;
			case Blocks.CLAY:
				returnColor = new Color(161, 166, 182);
				break;
			case Blocks.REED:
				break;
			case Blocks.PUMPKIN:
				break;
			case Blocks.NETHERSTONE:
				returnColor = new Color(115, 60, 60);
				break;
			case Blocks.SLOWSAND:
				returnColor = new Color(103, 79, 65);
				break;
			case Blocks.LIGHTSTONE:
				returnColor = new Color(249, 212, 156);
				break;
			case Blocks.PORTAL:
				returnColor = new Color(45, 45, 45);
				break;
			case Blocks.JACK_O_LANTERN:
				break;
			default: 
				System.out.println("Not found color for block " + blockCode);
				break;
		}
		return returnColor;
	}
	
    /**
     * Creates an image based on the loaded MapChunks.
     * @return Image of the map.
     */
	public Image createImage(){
		BufferedImage tempImage = new BufferedImage((this.xLargestChunk - this.xSmallestChunk + 1) * 16,
													(this.zLargestChunk - this.zSmallestChunk + 1) * 16,
													BufferedImage.TYPE_INT_RGB);
		
		for(int z = 0; z < this.mapChunkIds.length; z++){
			for(int x = 0; x < this.mapChunkIds[0].length; x++){
				if(this.mapChunkIds[z][x] != -1){
					byte[][] tempBlocks = this.mapChunkList.get(this.mapChunkIds[z][x]).getTopBlockArray();
					byte[][] tempHeight = this.mapChunkList.get(this.mapChunkIds[z][x]).getHeightMap();
					for(int mz = 0; mz < tempBlocks.length; mz++){
						for(int mx = 0; mx < tempBlocks[0].length; mx++){
							
							
							Color tempColor = this.getTopBlockColor(tempBlocks[mz][mx]);
	    					int tempRed = tempColor.getRed();
							int tempGrn = tempColor.getGreen();
							int tempBlu = tempColor.getBlue();
							
							//Create a height map if the block is grass
	    					if(tempBlocks[mz][mx] == Blocks.GRASS){
	    						int subtractInt = 127 - tempHeight[mz][mx];
	    						tempRed = (int) (tempRed- subtractInt * 1.5);
	    						tempGrn = (int) (tempGrn- subtractInt * 1.5);
	    						tempBlu = (int) (tempBlu- subtractInt * 1.5);
	    						if(tempRed < 0)
	    							tempRed = 0;
		    	    			if(tempGrn < 0)
		    	    				tempGrn = 0;
		    	    			if(tempBlu < 0)
		    	    				tempBlu = 0;
		    	    		}
	    					if(tempRed < 0)
								tempRed = 0;
	    	    			if(tempGrn < 0)
	    	    				tempGrn = 0;
	    	    			if(tempBlu < 0)
	    	    				tempBlu = 0;
	    	    			if(tempRed > 255)
								tempRed = 255;
	    	    			if(tempGrn > 255)
	    	    				tempGrn = 255;
	    	    			if(tempBlu > 255)
	    	    				tempBlu = 255;
							tempColor = new Color(tempRed, tempGrn, tempBlu);
							
							tempImage.setRGB(x * 16 + mx, z * 16 + mz, tempColor.getRGB());
							
						}
					}
				}
				else{
					for(int mz = 0; mz < 16; mz++){
						for(int mx = 0; mx < 16; mx++){
							tempImage.setRGB(x * 16 + mx, z * 16 + mz, new Color(240,240,240).getRGB());
						}
					}
				}
			}
		}
		return tempImage;
	}
	
	/**
	 * Checks to see if a mapChunk is selected at the given position.
	 * @param z Z-Position of the chunk.
	 * @param x X-Position of the chunk.
	 * @return True if the chunk is selected. False if not.
	 */
	public boolean isChunkSelected(int z, int x){
		try{
			return this.mapChunkSelected[z][x].isChunkSelected();
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Checks to see if the mapChunk has at least one block selected.
	 * @param z Z-Position of the chunk.
	 * @param x X-Position of the chunk.
	 * @return True if there is one or more blocks selected, false otherwise.
	 */
	public boolean hasBlocksSelected(int z, int x){
		try{
			return this.mapChunkSelected[z][x].hasBlocksSelected();
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Checks to see if a specific block is seleceted.  Z and X are in blocks not chunks.
	 * @param z - Z-Position of the block (Z-Pos of the chunk * 16 + Z-Pos of the block.)
	 * @param x - X-Position of the block (X-Pos of the chunk * 16 + X-Pos of the block.)
	 * @return True if that block is selected.  False otherwise.
	 */
	public boolean isBlockSelected(int z, int x){
		try{
			return this.mapChunkSelected[z / 16][x / 16].isBlockSelected(z % 16, x % 16);
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}

	/**
	 * Get the MapChunk at the given coordinates.
	 * @param z Z-Position of the chunk.
	 * @param x X-Position of the chunk.
	 * @return MapChunk at (Z,X)
	 */
	public MapChunk getChunk(int z, int x){
		if(this.mapChunkIds[z][x] >= 0)
			return this.mapChunkList.get(this.mapChunkIds[z][x]);
		return null;
	}
	
	/**
	 * Get MapChunk based on the index in the list.
	 * @param index Index of the MapChunk.
	 * @return MapChunk with given index.
	 */
	public MapChunk getChunk(int index){
		return this.mapChunkList.get(index);
	}
	
	/**
	 * The number of chunks on the X-Axis
	 * @return The number of chunks.
	 */
	public int getMapWidth(){
		return this.mapWidth;
	}
	
	/**
	 * The number of chunks on the Z-Axis
	 * @return The number of chunks.
	 */
	public int getMapHeight(){
		return this.mapHeight;
	}
	
	/**
	 * Check if a chunk exists at the given coordinates.
	 * @param z Z-Position to check.
	 * @param x X-Position to check.
	 * @return True if the chunk exists, false otherwise.
	 */
	public boolean containsChunk(int z, int x){
		try{
			return (this.mapChunkIds[z][x] >= 0);
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Remove a chunk from the array at the given position.
	 * Note: This does not delete the chunk from the file system.
	 * @param z Z-Position.
	 * @param x X-Position.
	 */
	public void deleteChunk(int z, int x){
		this.mapChunkIds[z][x] = -1;
		this.mapChunkSelected[z][x].unselectChunk();
	}
	
	/**
	 * Find the Coordinates of a given chunk.
	 * @param chunk Chunk to find the coordinates of.
	 * @return Coordinates of the chunk.
	 */
	public Coordinates locateChunk(MapChunk chunk){
		return new Coordinates(chunk.getZPos() - this.zSmallestChunk, chunk.getXPos() - this.xSmallestChunk);
	}
	
	/**
	 * Return the number of chunks the 0,0 position had to be moved down to form the image.
	 * @return Return the number of chunks the 0,0 position had to be moved down to form the image.
	 */
	public int getZOffset(){
		return (this.zSmallestChunk);
	}
	
	/**
	 * Return the number of chunks the 0,0 position had to be moved right to form the image.
	 * @return Return the number of chunks the 0,0 position had to be moved right to form the image.
	 */
	public int getXOffset(){
		return (this.xSmallestChunk);
	}
	
	/**
	 * Get all of the chunks that are currently selected.
	 * @return LinkedList of selected MapChunks.
	 */
	public LinkedList<MapChunk> getSelectedChunks(){
		LinkedList<MapChunk> returnList = new LinkedList<MapChunk>();
		for(int z = 0; z < this.mapHeight; z++){
			for(int x = 0; x < this.mapWidth; x++){
				if(this.containsChunk(z, x) && this.mapChunkSelected[z][x].hasBlocksSelected()){
					returnList.add(this.getChunk(z, x));
				}
			}
		}
		return returnList;
	}
	
	/**
	 * Get all of the IDs corresponding to the currently selected chunks.
	 * @return LinkedList of IDs
	 */
	public LinkedList<Coordinates> getSelectedIds() {
		LinkedList<Coordinates> returnList = new LinkedList<Coordinates>();
		for(int z = 0; z < this.mapHeight; z++){
			for(int x = 0; x < this.mapWidth; x++){
				if(this.containsChunk(z, x) && this.mapChunkSelected[z][x].isChunkSelected()){
					returnList.add(new Coordinates(z, x));
				}
			}
		}
		return returnList;
	}
	
	/**
	 * Get the array of all of the MapChunkSelected.
	 * @return MapChunkSelected array.
	 */
	public MapChunkSelected[][] getSelectedArray(){
		return this.mapChunkSelected;
	}

	/**
	 * Selects or unselects a MapChunk
	 * @param z Z-Position of the Chunk
	 * @param x X-Position of the Chunk
	 * @param b True to select the chunk, false otherwise.
	 */
	public void setSelected(int z, int x, boolean b) {
		if (z >= 0 && z < mapChunkSelected.length && 
		    x >= 0 && x < this.mapChunkSelected[0].length) {	
			this.mapChunkSelected[z][x].setChunkSelected(b);
		}
	}

	/**
	 * Selects or unselects a specific block.
	 * @param z Z-Position of the Block (Z-Pos of the chunk * 16 + internal position of the block).
	 * @param x X-Position of the Block (X-Pos of the chunk * 16 + internal position of the block).
	 * @param b True to select the block, false otherwise.
	 */
	public void setSelectedBlock(int z, int x, boolean b) {
		this.mapChunkSelected[z / 16][x / 16].setBlockSelected(z % 16, x % 16, b);
	}
	
	/**
	 * Get all of the chunks.
	 * @return All of the chunks.
	 */
	public ArrayList<MapChunk> getChunkList(){
		return this.mapChunkList;
	}
	
	/**
	 * Inverts the selected data of the chunk at a given position.
	 * @param z Z-Position of the chunk.
	 * @param x X-Position of the chunk.
	 */
	public void invertChunk(int z, int x){
		this.mapChunkSelected[z][x].invert();
	}
	
	/**
	 * Selects all of the blocks in the MapChunks directly or indirectly 
	 * connected with a block in the linked list.
	 * @param z Z-Position of the MapChunk
	 * @param x X-Position of the MapChunk
	 * @param coord The coordinates of the blocks to start filling at.
	 * @return A LinkedList of the blocks taht were filled at the edge of the chunk.
	 */
	public LinkedList<Coordinates> fillMapChunkSelected(int z, int x, LinkedList<Coordinates> coord){
			return this.mapChunkSelected[z][x].fillBlocks(coord);
	}
	
	/**
	 * Get the MapChunkSelected instance corresponding to the given MapChunk.
	 * @param chunk MapChunk to get the corresponding MapChunkSelected from.
	 * @return The Corresponding MapChunkSelected.
	 */
	public MapChunkSelected getChunkSelected(MapChunk chunk){
		Coordinates foundC = this.locateChunk(chunk);
		return mapChunkSelected[foundC.getZ()][foundC.getX()];
	}
}
