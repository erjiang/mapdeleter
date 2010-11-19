import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Stores of which blocks in a chunk is selected, and if the chunk is selected as a whole.
 * @author Charles
 *
 */
public class MapChunkSelected {

	private boolean[][] selectedBlocks = new boolean[16][16];
	private boolean chunkSelected;
	private boolean blockSelected;
	private int numSelectedBlocks;
	
	@Override
	public String toString() {
		return "MapChunkSelected [blockSelected=" + blockSelected
				+ ", chunkSelected=" + chunkSelected + ", numSelectedBlocks="
				+ numSelectedBlocks + ", selectedBlocks="
				+ Arrays.toString(selectedBlocks) + "]";
	}
	
	/**
	 * Creates the default map chunk.  Nothing is selected.
	 */
	public MapChunkSelected(){
		this.chunkSelected = false;
		this.blockSelected = false;
		this.numSelectedBlocks = 0;
		
		for(int z = 0; z < 16; z++){
			for(int x = 0; x < 16; x++){
				this.selectedBlocks[z][x] = false;
			}
		}
	}
	
	/**
	 * Selects the whole chunk.
	 */
	public void selectChunk(){
		for(byte z = 0; z < 16; z++){
			for(byte x = 0; x < 16; x++){
				selectedBlocks[z][x] = true;
			}
		}
		this.chunkSelected = true;
		this.blockSelected = true;
		this.numSelectedBlocks = 256;
	}
	
	/**
	 * Unselects the whole chunk.
	 */
	public void unselectChunk(){
		for(byte z = 0; z < 16; z++){
			for(byte x = 0; x < 16; x++){
				selectedBlocks[z][x] = false;
			}
		}
		this.chunkSelected = false;
		this.blockSelected = false;
		this.numSelectedBlocks = 0;
	}

	/**
	 * Selects a specific block in the chunk.
	 * @param z Z-Position of the block in the 16x16 array.
	 * @param x X-Position of the block in the 16x16 array.
	 */
	public void selectBlock(int z, int x){
		if(!selectedBlocks[z][x]){
			selectedBlocks[z][x] = true;
			blockSelected = true;
			this.numSelectedBlocks++;
			if(this.numSelectedBlocks == 256)
				this.chunkSelected = true;
		}
	}
	
	/**
	 * Unselect a specific block in the chunk.
	 * @param z Z-Position of the block in the 16x16 array.
	 * @param x X-Position of the block in the 16x16 array.
	 */
	public void unselectBlock(int z, int x){
		if(this.selectedBlocks[z][x]){
			selectedBlocks[z][x] = false;
			this.numSelectedBlocks--;
			if(this.numSelectedBlocks == 0){
				this.chunkSelected = false;
				this.blockSelected = false;
			}
			this.chunkSelected = false;
		}
	}
	
	/**
	 * Returns true if the whole chunk is selected.  False otherwise.
	 * @return True if the whole chunk is selected.  False otherwise.
	 */
	public boolean isChunkSelected(){
		return this.chunkSelected;
	}
	
	/**
	 * Returns true if there is at least one block in the chunk selected.  False otherwise.
	 * @return True if there is at least one block in the chunk selected.  False otherwise. 
	 */
	public boolean hasBlocksSelected(){
		return this.blockSelected;
	}
	
	/**
	 * Checks if the specific block is selected.
	 * @param z Z-Position of the block in the 16x16 array.
	 * @param x X-Position of the block in the 16x16 array.
	 * @return True if the block is selected. False otherwise.
	 */
	public boolean isBlockSelected(int z, int x){
		try{
			return this.selectedBlocks[z][x];
		}
		catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	/**
	 * Selects or unselects the whole chunk the whole chunk.
	 * @param b true if to select the chunk.  False otherwise.
	 */
	public void setChunkSelected(boolean b){
		if(b)
			this.selectChunk();
		else
			this.unselectChunk();
	}
	
	/**
	 * Sets a specific block to be selected or unselected.
	 * @param z - Z-Position of the block in the 16x16 grid. 
	 * @param x - X-Position of the block in the 16x16 grid.
	 * @param b - If true then select the block,  false unselect the block.
	 */
	public void setBlockSelected(int z, int x, boolean b){
		if(b)
			this.selectBlock(z, x);
		else
			this.unselectBlock(z, x);
			
	}
	
	/**
	 * Changes all of the selected blocks to unselected and unselected to selected.
	 */
	public void invert(){
		this.numSelectedBlocks = 0;
		this.blockSelected = false;
		this.chunkSelected = false;
		for(int z = 0; z < this.selectedBlocks.length; z++){
			for(int x = 0; x < this.selectedBlocks[0].length; x++){
				if(this.selectedBlocks[z][x]){
					this.selectedBlocks[z][x] = false;
				}
				else{
					this.selectedBlocks[z][x] = true;
					this.numSelectedBlocks++;
					this.blockSelected = true;
				}
			}
		}
		if(this.numSelectedBlocks == 256)
			this.chunkSelected = true;
	}
	
	/**
	 * Change the unselected blocks to selected as long as they are indirectly or directly touching
	 *   a block with the coordinates provided. 
	 * @param blockList The Coordinates to start filling at.
	 * @return A linked list of Coordinates that are on the outside edge of the 16x16 array.
	 */
	public LinkedList<Coordinates> fillBlocks(LinkedList<Coordinates> blockList){
		LinkedList<Coordinates> returnList = new LinkedList<Coordinates>();
		LinkedList<Coordinates> fillStack = new LinkedList<Coordinates>();
		
		Iterator<Coordinates> biter = blockList.iterator();
		int currZ;
		int currX;
		boolean popMe = false;
		while(biter.hasNext()){
			Coordinates currC = biter.next();
			fillStack.addLast(currC);
			
			//My really weird iterative solution to finding all of the blocks connected.
			//Goes to a block and then checks if the block North, South, East, and West are selected.
			//If one is not selected loop through and check that block.
			while(true){
				popMe = true;
				if(fillStack.size() == 0)
					break;
				currZ = fillStack.getLast().getZ();
				currX = fillStack.getLast().getX();
				if(this.isBlockSelected(currZ, currX)){
					fillStack.removeLast();
				}
				else{
					selectBlock(currZ, currX);
					if(currZ == 0 || currZ == 15 || currX == 0 || currX == 15)
						returnList.add(fillStack.getLast());
					//Check and go North
					if(!this.isBlockSelected(currZ - 1, currX) && currZ - 1 >= 0){
						fillStack.addLast(new Coordinates(currZ - 1, currX));
						popMe = false;
					}
					//Check and go East
					if(!this.isBlockSelected(currZ, currX + 1) && currX + 1 < 16){
						fillStack.addLast(new Coordinates(currZ, currX + 1));
						popMe = false;
					}
					//Check and go South
					if(!this.isBlockSelected(currZ + 1, currX) && currZ + 1 < 16){
						fillStack.addLast(new Coordinates(currZ + 1, currX));
						popMe = false;
					}
					//Check and go West
					if(!this.isBlockSelected(currZ, currX - 1) && currX - 1 >= 0){
						fillStack.addLast(new Coordinates(currZ, currX - 1));
						popMe = false;
					}
					if(popMe)
						fillStack.removeLast();
				}
			}
		}
		return returnList;
	}
}
