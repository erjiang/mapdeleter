import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


import org.jnbt.*;

/**
 * Stores a version of a Minecraft block chunk which is a 16x16 area of blocks.
 * This version only stores the information needed to view and do simple edits on the chunk.
 * @author Charles
 *
 */
public class MapChunk {
	private byte[][] heightMap = new byte[16][16];
	private byte[][] topBlocks = new byte[16][16];
	private byte[][] heightMapTopo = new byte[16][16];
	private int xPos;
	private int zPos;
	private String filePath;
	
	/**
	 * Loads the file found at filePatha.
	 * @param filePatha The directory and file path where the file is stored.
	 * @throws IOException Thrown when there is no file at the filePatha
	 */
	public MapChunk(String filePatha) throws IOException{
		//TODO Make less of a memory print.
		this.filePath = filePatha;
		FileInputStream minecraftDatStream = new FileInputStream(filePath);
		NBTInputStream minecraftTagStream = new NBTInputStream(minecraftDatStream);

		//The level gives a compound tag
		CompoundTag test = (CompoundTag) minecraftTagStream.readTag();
		//Value Level gives another compound tag
		CompoundTag levelTag = (CompoundTag)test.getValue().get("Level");
		
		System.out.println(this.filePath);
		//Set up the tags.
		IntTag tempLevelxPos = (IntTag)levelTag.getValue().get("xPos"); 
		IntTag tempLevelzPos = (IntTag)levelTag.getValue().get("zPos"); 
		ByteArrayTag tempLevelHeightMap = (ByteArrayTag)levelTag.getValue().get("HeightMap");
		ByteArrayTag tempLevelBlocks = (ByteArrayTag)levelTag.getValue().get("Blocks");
		this.xPos = tempLevelxPos.getValue();
		this.zPos = tempLevelzPos.getValue();			

		//Create heightMap, heightMapTopo, and topBlock arrays.
		for(int z=0; z<16; z++){
			for(int x=0; x<16; x++){
				//topBlocks[z][x] = tempLevelBlocks.getValue()[(x * 16) + z * 16 * 16 + (tempLevelHeightMap.getValue()[(z * 16 + x)]) - 1];
				int currentHeight = tempLevelHeightMap.getValue()[(z * 16 + x)];
				if(currentHeight < 0){
					this.heightMap[z][x] = (byte) 127;
					this.heightMapTopo[z][x] = (byte) 127;
					this.topBlocks[z][x] = tempLevelBlocks.getValue()[z * 128 + x * 2048 + 127];
				}
				else{
					this.heightMap[z][x] = (byte)currentHeight;
					this.heightMapTopo[z][x] = (byte)currentHeight;
					if(tempLevelBlocks.getValue().length > z * 128 + x * 2048 + currentHeight && tempLevelBlocks.getValue()[z * 128 + x * 2048 + currentHeight] == 50)
						this.topBlocks[z][x] = 50;
					else
						this.topBlocks[z][x] = tempLevelBlocks.getValue()[z * 128 + x * 2048 + currentHeight - 1];
					
					byte y = (byte)currentHeight;
					//Ensure that the Leaves, Tree Trunks And air will not be a factor in heightMapTopo
					while((tempLevelBlocks.getValue()[z * 128 + x * 2048 + y - 1] == Blocks.LEAVES ||
							tempLevelBlocks.getValue()[z * 128 + x * 2048 + y - 1] == Blocks.TREE_TRUNK ||
							tempLevelBlocks.getValue()[z * 128 + x * 2048 + y - 1] == Blocks.AIR) &&
							y >= 0){
						y--;
					}
					this.heightMapTopo[z][x] = y;
				}
			}
		}		
		minecraftTagStream.close();
	}
	
	/**
	 * Copy constructor for the MapChunk
	 * @param chunk The MapChunk to get the data from.
	 */
	public MapChunk(MapChunk chunk) {
		for(int z = 0; z < this.heightMap.length; z++){
			for(int x = 0; x < this.heightMap[0].length; x++){
				this.heightMap[z][x] = new Byte(chunk.getHeightMap()[z][x]);
				this.topBlocks[z][x] = new Byte(chunk.getTopBlockArray()[z][x]);
			}
		}
		this.xPos = new Integer(chunk.getXPos());
		this.zPos = new Integer(chunk.getZPos());
		this.filePath = new String(chunk.filePath);
	}

	/**
	 * Returns the X Position of the MapChunk
	 * @return X Position
	 */
	public int getXPos(){
		System.out.print(this.xPos + " | ");
		return this.xPos;
	}

	/**
	 * Returns the 2D byte array of the height map.
	 * @return Height Map.
	 */
	public byte[][] getHeightMap(){
		return this.heightMap;
	}
	
	/**
	 * Returns the 2D byte array of the height map Topography version.
	 * @return The 2D array of the height map Topography version.
	 */
	public byte[][] getHeightMapTopo(){
		return this.heightMapTopo;
	}
	
	/**
	 * Sets the new X Position.  I don't see why this should be used...
	 * @param x X Position
	 */
	public void setXPos(int x){
		this.xPos = x;
	}
	
	/**
	 * Get the Z Position of the chunk.
	 * @return Z Position
	 */
	public int getZPos(){
		return this.zPos;
	}
	
	/**
	 * Sets the Z Position of the chunk.
	 * @param z Z Position.
	 */
	public void setZPos(int z){
		this.zPos = z;
	}
	
	/**
	 * Sets a new top block.
	 * @param block New top block.
	 * @param height Height of the new top block.
	 * @param z Z-Position in the 16x16 array of blocks.
	 * @param x X-Position in the 16x16 array of blocks.
	 */
	public void setTopBlock(byte block, byte height, int z, int x){
		this.topBlocks[z][x] = block;
		this.heightMap[z][x] = height;
	}
	
	/**
	 * Sets the new height map for this MapChunk.
	 * @param setMap New height map.
	 */
	public void setHeightMap(byte[][] setMap){
		for(int z = 0; z < setMap.length; z++){
			for(int x = 0; x < setMap[0].length; x++){
				this.heightMap[z][x] = new Byte(setMap[z][x]);
			}
		}
	}
	
	/**
	 * Sets the new topBlockMap which is the highest block of the map.
	 * @param setMap new topBlockMap.
	 */
	public void setTopBlockMap(byte[][] setMap){
		for(int z = 0; z < setMap.length; z++){
			for(int x = 0; x < setMap[0].length; x++){
				this.topBlocks[z][x] = new Byte(setMap[z][x]);
			}
		}
	}
	
	/**
	 * Sets the new height map topography version for this chunk.
	 * @param setTopo New height map topography version.
	 */
	public void setHeightMapTopo(byte[][] setTopo){
		for(int z = 0; z < setTopo.length; z++)
			for(int x = 0; x < setTopo[0].length; x++ )
				this.heightMapTopo[z][x] = new Byte(setTopo[z][x]);
	}
	
	/**
	 * Returns the top block array.
	 * @return Top Block Array
	 */
	public byte[][] getTopBlockArray(){
		return this.topBlocks;
	}
	
	/**
	 * Returns the file path where this MapChunk came from.
	 * @return File Path of the map chunk.
	 */
	public String getFilePath(){
		return this.filePath;
	}
	
	/**
	 * A string representation of this MapChunk.  Just shows the Z and X coordinates.
	 */
	public String toString(){
		return new String("MapChunk (Z|X): " + this.zPos + " | " + this.xPos);
	}
}

