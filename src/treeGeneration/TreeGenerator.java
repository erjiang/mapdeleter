import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generates the trees...
 * What did you expect?
 * @author Charles
 *
 */
public class TreeGenerator {
	double density;
	byte[][] blockArray;
	byte[][] heightArray;
	Boolean[][] plantedTrees;
	MapChunk[][] mapChunkIds;
	Coordinates[][] saveCoordIds;
	LinkedList<Coordinates> saveCoodList;
	int[] trees;
	LinkedList<MapChunk> mapChunkList;
	MapContainer mapContents;
	LinkedList<Coordinates> writeBlocks;
	LinkedList<SaveCommand> saveCommands;
	
	/**
	 * Constructor for the tree generator.  Takes all of the selected map chunks, the map container,
	 * the density of the trees and the tree IDs that the user wants generated.
	 * @param selectedChunks
	 * @param mapC
	 * @param density
	 * @param treesa
	 */
	public TreeGenerator(LinkedList<MapChunk> selectedChunks, MapContainer mapC, int density, int[] treesa){
		this.saveCoodList = new LinkedList<Coordinates>();
		this.writeBlocks = new LinkedList<Coordinates>();
		this.mapContents = mapC;
		this.density = (double)density / (double)100000;
		this.mapChunkList = selectedChunks;
		this.trees = treesa;
		this.createArrays();
		this.writeBlocks = this.generateTrees();
	}
	
	/**
	 * Generates all of the trees,
	 * Goes through each selected block and generates a random number.
	 * If the number is less than the density / 100,000 it will try to plant a tree.
	 * @return
	 */
	private LinkedList<Coordinates> generateTrees(){
		System.out.println();
		System.out.println("Max z: " + this.blockArray.length);
		System.out.println("Max x: " + this.blockArray[0].length);
		for(int z = 0; z < this.blockArray.length; z++){
			for(int x = 0; x < this.blockArray[0].length; x++){
				if(this.blockArray[z][x] == Blocks.GRASS || this.blockArray[z][x] == Blocks.DIRT){
					System.out.println("Plant At: " + z + " | " + x);
					boolean selectedBlock = this.mapContents.getChunkSelected(this.mapChunkIds[z / 16][x / 16]).isBlockSelected(z % 16, x % 16);
					if(Math.random() <= this.density){
						Tree tempTree = this.getTreeFromId(this.randomizeTree(), new Coordinates(z, x, this.heightArray[z][x]));
						if(tempTree.canPlant() && selectedBlock){
							tempTree.generateTree();
							this.blockArray = tempTree.getTopBlockArray();
							this.heightArray = tempTree.getHeightMapArray();
							LinkedList<Coordinates> tempSaveCoord = tempTree.createCoordinates();
							Iterator<Coordinates> iterCo = tempSaveCoord.iterator();
							while(iterCo.hasNext()){
								Coordinates tempCoord = iterCo.next();
								try{
									System.out.println(tempCoord.getY());
									if(this.saveCoordIds[tempCoord.getZ()][tempCoord.getX()] == null){
										this.saveCoordIds[tempCoord.getZ()][tempCoord.getX()] = tempCoord;
									}
									else{
										Coordinates mergedCoord = this.mergeCoordinates(tempCoord, this.saveCoordIds[tempCoord.getZ()][tempCoord.getX()]);
										this.saveCoordIds[tempCoord.getZ()][tempCoord.getX()] = mergedCoord;
									}
								}
								catch(ArrayIndexOutOfBoundsException e){
									
								}
							}
							
						}
					}
				}
			}
		}
		return writeBlocks;
	}
	
	/**
	 * If two coordinates have the save X and Z axis, this will merge them.
	 * @param a Coordinate A to merge.
	 * @param b Coordinate B to merge.
	 * @return The merged Coordinates.
	 */
	private Coordinates mergeCoordinates(Coordinates a, Coordinates b){
		int tempZ = a.getZ();
		int tempX = a.getX();
		int tempY = b.getY();
		if(a.getY() < b.getY())
			tempY = a.getY();

		byte[] tempBlocks;
		int tempOffset = Math.abs(a.getY() - b.getY());
		if(a.getY() < b.getY()){
			if(b.getBlockArray().length + tempOffset < a.getBlockArray().length)
				tempBlocks = new byte[a.getBlockArray().length];
			else
				tempBlocks = new byte[b.getBlockArray().length + tempOffset];
		}
		else{
			if(a.getBlockArray().length + tempOffset < b.getBlockArray().length)
				tempBlocks = new byte[b.getBlockArray().length];	
			else
				tempBlocks = new byte[a.getBlockArray().length + tempOffset];
		}
		for(int i = 0; i < tempBlocks.length; i++){
			tempBlocks[i] = -1;
		}
		
		
		
			for(int i = 0; i < tempBlocks.length; i++){
				if(a.getY() < b.getY()){
					if(i < a.getBlockArray().length){
						if(tempBlocks[i] == -1)
							tempBlocks[i] = a.getBlockArray()[i];
					}
					if(i < b.getBlockArray().length + tempOffset && tempBlocks[i] == -1){
						if(tempBlocks[i] == -1)
							tempBlocks[i] = b.getBlockArray()[i - tempOffset];
					}
				}
				else{
					if(i < b.getBlockArray().length){
						if(tempBlocks[i] == -1)
							tempBlocks[i] = b.getBlockArray()[i];
					}
					if(i < a.getBlockArray().length + tempOffset && tempBlocks[i] == -1){
						if(tempBlocks[i] == -1)
						tempBlocks[i] = a.getBlockArray()[i - tempOffset];
					}
				}
			}
		
		Coordinates returnCoord = new Coordinates(tempZ, tempX, tempY, tempBlocks);
		return returnCoord;
	}
	
	/**
	 * Creates a tree type given and ID and coordinates.
	 * @param id The ID of the tree to generate.
	 * @param c The coordinates where the tree will be planted.
	 * @return
	 */
	private Tree getTreeFromId(int id, Coordinates c){
		switch(id){
		case Tree.PINE_TREE:
			return new PineTree(c, this.heightArray, this.blockArray, this.plantedTrees);
		default: 
			return new PineTree(c, this.heightArray, this.blockArray, this.plantedTrees);
		}
	}
	
	/**
	 * Picks a random tree id to generate.
	 * Currently there is only one tree ID which is a Pine Tree :/
	 * @return The ID of a tree type.
	 */
	private int randomizeTree(){
		int rnd = (int) (Math.random() * (double)this.trees.length);
		return this.trees[rnd];
	}
	
	/**
	 * Get the blocks to write to the mapChunks
	 * @return The blocks to write to the mapChunks.
	 */
	public LinkedList<Coordinates> getWriteBlocks(){
		return this.writeBlocks;
	}
	
	/**
	 * Get a LinkedList of all of the save commands to save.
	 * @return SaveCommands to save.
	 */
	public LinkedList<SaveCommand> getBlocksToSave(){
		LinkedList<SaveCommand> returnCommand = new LinkedList<SaveCommand>();
		Iterator<Coordinates> iter = this.saveCoodList.iterator();
		for(int z = 0; z < this.saveCoordIds.length; z++){
			for(int x = 0; x < this.saveCoordIds[0].length; x++){
				if(this.saveCoordIds[z][x] != null){
					Coordinates tempCoord = this.saveCoordIds[z][x];
					int chuckZ = tempCoord.getZ() / 16;
					int chunkX = tempCoord.getX() / 16;
					if(this.mapChunkIds[chuckZ][chunkX] != null){
						MapChunk tempChunk = this.mapChunkIds[chuckZ][chunkX];
						tempCoord.setX(tempCoord.getX() % 16);
						tempCoord.setZ(tempCoord.getZ() % 16);
						SaveCommand tempCo = new SaveCommand(SaveCommand.ADD_BLOCKS_COMMAND, tempChunk.getFilePath(), tempCoord);
						returnCommand.add(tempCo);
					}
				
				}
			}
			
		}
		return returnCommand;
	}
	
	/**
	 * Sets the new top block of each block in each chunk where needed..
	 */
	public void rewriteChunks(){
		for(int z = 0; z < this.mapChunkIds.length; z++){
			for(int x = 0; x < this.mapChunkIds[0].length; x++){
				if(this.mapChunkIds[z][x] != null){
					MapChunk tempChunk = this.mapChunkIds[z][x];
					for(int blockZ = 0; blockZ < 16; blockZ++){
						for(int blockX = 0; blockX < 16; blockX++){
							tempChunk.setTopBlock(this.blockArray[z * 16 + blockZ][x * 16 + blockX], 
												  this.heightArray[z * 16 + blockZ][x * 16 + blockX], blockZ, blockX);
							System.out.println("Leaf: " + (this.blockArray[z * 16 + blockZ][x * 16 + blockX] == Blocks.LEAVES));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Calculates a new height map for the chunks.
	 */
	public void generateHeightMapArray(){
		for(int z = 0; z < this.blockArray.length; z++){
			for(int x = 0; x < this.blockArray[0].length; x++){
				if(this.blockArray[z][x] != -1 && this.mapChunkIds[z / 16][x / 16] != null){
					this.mapChunkIds[z / 16][x / 16].setTopBlock(this.blockArray[z][x], this.heightArray[z][x], z%16, x%16);
					System.out.println("Block: " + z + " | " + x);
				}
			}
		}
	}
	
	/**
	 * Instantiates all of the arrays the tree generater uses.
	 */
	private void createArrays(){
		int xSmall = Integer.MAX_VALUE;
		int zSmall = Integer.MAX_VALUE;
		int xLarge = Integer.MIN_VALUE;
		int zLarge = Integer.MIN_VALUE;
		Iterator<MapChunk> iterA = this.mapChunkList.iterator();
		while(iterA.hasNext()){
			MapChunk tempChunk = iterA.next();
			if(tempChunk.getZPos() < zSmall)
	    		zSmall = tempChunk.getZPos();
	    	if(tempChunk.getZPos() > zLarge)
	    		zLarge = tempChunk.getZPos();
	    	if(tempChunk.getXPos() < xSmall)
	    		xSmall = tempChunk.getXPos();
	    	if(tempChunk.getXPos() > xLarge)
	    		xLarge = tempChunk.getXPos();
		}
		this.blockArray = new byte[(zLarge - zSmall + 1) * 16][(xLarge - xSmall + 1) * 16];
		this.heightArray = new byte[(zLarge - zSmall + 1) * 16][(xLarge - xSmall + 1) * 16];
		this.plantedTrees = new Boolean[(zLarge - zSmall + 1) * 16][(xLarge - xSmall + 1) * 16];
		this.saveCoordIds = new Coordinates[(zLarge - zSmall + 1) * 16][(xLarge - xSmall + 1) * 16];
		this.mapChunkIds = new MapChunk[(zLarge - zSmall + 1)][(xLarge - xSmall + 1)];
		
		for(int z = 0; z < this.mapChunkIds.length; z++){
			for(int x = 0; x < this.mapChunkIds[0].length; x++){
				this.mapChunkIds[z][x] = null;
				for(int localZ = 0; localZ < 16; localZ++){
					for(int localX = 0; localX < 16; localX++){
						this.blockArray[z * 16 + localZ][x * 16 + localX] = -1;
						this.heightArray[z * 16 + localZ][x * 16 + localX] = -1;
						this.saveCoordIds[z * 16 + localZ][x * 16 + localX] = null;
						this.plantedTrees[z * 16 + localZ][x * 16 + localX] = true;
					}
				}
			}
		}
		
		int count = 0;
		Iterator<MapChunk> iterB = this.mapChunkList.iterator();
		while(iterB.hasNext()){
			MapChunk tempChunk = iterB.next();
			int arrayZ = tempChunk.getZPos() - zSmall;
			int arrayX = tempChunk.getXPos() - xSmall;
			this.mapChunkIds[arrayZ][arrayX] = tempChunk;
			for(int z = 0; z < 16; z++){
				for(int x = 0; x < 16; x++){
					System.out.println("Creating block z: " + (arrayZ + z) + " | x: " + (arrayX + x));
					this.blockArray[arrayZ * 16 + z][arrayX * 16 + x] = tempChunk.getTopBlockArray()[z][x];
					this.heightArray[arrayZ * 16 + z][arrayX * 16 + x] = tempChunk.getHeightMap()[z][x];
					this.plantedTrees[arrayZ * 16 + z][arrayX *16 + x] = false;
				}
			}
			count++;
		}
	}
}
