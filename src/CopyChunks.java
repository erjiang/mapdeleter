import java.util.Iterator;
import java.util.LinkedList;

/**
 * Stores the chunks that the user wants to paste.
 * Is made when "Copy Selected Chunks" button is hit.
 * @author Charles
 *
 */
public class CopyChunks {

	LinkedList<MapChunk> copyChunks;
	int[][] copyChunksArray;
	
	int zSmall;
	int zLarge;
	int xSmall;
	int xLarge;
	
	/**
	 * Creates the CopyChunks with the given linked list of MapChunks.
	 * @param chunksToCopy - A list of chunks to copy.
	 */
	public CopyChunks(LinkedList<MapChunk> chunksToCopy){
		this.copyChunks = new LinkedList<MapChunk>();
		Iterator<MapChunk> iter = chunksToCopy.iterator();

    	this.zSmall = Integer.MAX_VALUE;
    	this.zLarge = Integer.MIN_VALUE;
    	this.xSmall = Integer.MAX_VALUE;
    	this.xLarge = Integer.MIN_VALUE;
    	
    	//Find out what position is the largest and smallest.
    	//   In both X and Z directions.
    	while(iter.hasNext()){
    		MapChunk temp = iter.next();
    		this.copyChunks.add(temp);
    		if(temp.getZPos() < this.zSmall)
    			this.zSmall = temp.getZPos();
    		if(temp.getZPos() > this.zLarge)
    			this.zLarge = temp.getZPos();
    		if(temp.getXPos() < this.xSmall)
    			this.xSmall = temp.getXPos();
    		if(temp.getXPos() > this.xLarge)
    			this.xLarge = temp.getXPos();
    	}
    	
    	//Set up an array of the indexes for the MapChunk list.
    	this.copyChunksArray = new int[zLarge - zSmall + 1][xLarge - xSmall + 1];
    	
    	//Set -1 as the default value for the array.
    	for(int z = 0; z < this.copyChunksArray.length; z++){
    		for(int x = 0; x < this.copyChunksArray[0].length; x++){
    			this.copyChunksArray[z][x] = -1;
    		}
    	}
    	
    	int count = 0;
    	//Iterate through the MapChunk list and add the index to the array.
    	Iterator<MapChunk> iterA = this.copyChunks.iterator();
    	while(iterA.hasNext()){
    		MapChunk tempA = iterA.next();
    		System.out.println("tempA: " + (tempA.getZPos() - this.zSmall));
    		this.copyChunksArray[tempA.getZPos() - this.zSmall][tempA.getXPos() - this.xSmall] = count;
    		count++;
    	}
	}
	
	/**
	 * A simple test to see if the current selected chunks can hold the copied chunks.
	 * Returns true if it can hold the copied chunks, false otherwise.
	 * @param pasteArea - 2D Array of booleans corresponding to the selected chunks.
	 * @return True if the pasteArea can hold the copied chunks.  False otherwise.
	 */
	public boolean canPaste(boolean[][] pasteArea){
		//Check to see if the two sizes are correct.
		if(pasteArea.length < this.copyChunksArray.length || pasteArea[0].length < this.copyChunksArray[0].length)
			return false;
		// Check to see if the selected chunks correspond correctly to the copied chunks.
		for(int z = 0; z < this.copyChunksArray.length; z++){
			for(int x = 0; x < this.copyChunksArray[0].length; x++){
				if(!pasteArea[z][x] && this.copyChunksArray[z][x] >= 0)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the 2D MapChunk array of the copied chunks.
	 * @return The 2D MapChunk array of the copied chunks.
	 */
	public MapChunk[][] getChunks(){
		//Make the array.
		MapChunk[][] returnArray = new MapChunk[this.copyChunksArray.length][this.copyChunksArray[0].length];
		
		//Fill the array.
		for(int z = 0; z < this.copyChunksArray.length; z++){
			for(int x = 0; x < this.copyChunksArray[0].length; x++){
				if(this.copyChunksArray[z][x] == -1){
					returnArray[z][x] = null;
				}
				else{
					returnArray[z][x] = this.copyChunks.get(this.copyChunksArray[z][x]);
				}
			}
		}
		return returnArray;
	}
}
