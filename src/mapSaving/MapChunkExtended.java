import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.jnbt.*;

/**
 * An extended version of the MapChunk. This is used to save the data to a chunk.
 * @author Charles
 *
 */
public class MapChunkExtended {
	
	String filePath;
	ByteArrayTag data;
	ListTag entities;
	int xPos;
	LongTag lastUpdate;
	int zPos;
	ByteTag terrainPopulated;
	ListTag tileEntities;
	byte[] heightMap;
	ByteArrayTag blockLight;
	byte[] blocks;
	ByteArrayTag skyLight;
	
	/**
	 * Create the mapChunk from the given file.
	 * @param mapChunkLocation The path to the mapChunk file.
	 * @throws IOException
	 */
	public MapChunkExtended(String mapChunkLocation) throws IOException{
		this.filePath = mapChunkLocation;
		FileInputStream minecraftDatStream = new FileInputStream(filePath);
		NBTInputStream minecraftTagStream = new NBTInputStream(minecraftDatStream);
		CompoundTag test = (CompoundTag) minecraftTagStream.readTag();
		//Value Level gives another compound tag
		CompoundTag levelTag = (CompoundTag)test.getValue().get("Level");
		
		System.out.println(levelTag.getValue().get("SkyLight"));
		this.data = (ByteArrayTag) levelTag.getValue().get("Data");
		this.entities = (ListTag) levelTag.getValue().get("Entities");
		this.xPos = ((IntTag) levelTag.getValue().get("xPos")).getValue();
		this.lastUpdate = (LongTag) levelTag.getValue().get("LastUpdate");
		this.zPos = ((IntTag) levelTag.getValue().get("zPos")).getValue();
		this.terrainPopulated = (ByteTag) levelTag.getValue().get("TerrainPopulated");
		this.tileEntities = (ListTag) levelTag.getValue().get("TileEntities");
		this.skyLight = (ByteArrayTag) levelTag.getValue().get("SkyLight");
		this.heightMap = ((ByteArrayTag) levelTag.getValue().get("HeightMap")).getValue();
		this.blockLight = (ByteArrayTag) levelTag.getValue().get("BlockLight");
		this.blocks = ((ByteArrayTag) levelTag.getValue().get("Blocks")).getValue();

		System.out.println(this.data);
		System.out.println(this.entities);
		System.out.println(this.xPos);
		System.out.println(this.lastUpdate);
		System.out.println(this.zPos);
		System.out.println(this.terrainPopulated);
		System.out.println(this.tileEntities);
		System.out.println(this.heightMap);
		System.out.println(this.blockLight);
		System.out.println(this.blocks);
		
		minecraftTagStream.close();
	}
	
	/**
	 * Save the current parameters to the mapChunk file.
	 */
	public void save(){
		HashMap<String, Tag> levelMap = new HashMap<String, Tag>();
		levelMap.put("Data", this.data);
		levelMap.put("Entities", this.entities);
		levelMap.put("xPos", new IntTag("xPos", this.xPos));
		levelMap.put("LastUpdate", this.lastUpdate);
		levelMap.put("zPos", new IntTag("zPos", this.zPos));
		levelMap.put("TerrainPopulated", this.terrainPopulated);
		levelMap.put("TileEntities", this.tileEntities);
		levelMap.put("HeightMap", new ByteArrayTag("HeightMap", this.heightMap));
		levelMap.put("BlockLight", this.blockLight);
		levelMap.put("Blocks", new ByteArrayTag("Blocks", this.blocks));
		levelMap.put("SkyLight", this.skyLight);
		CompoundTag levelTag = new CompoundTag("Level", levelMap);
		
		HashMap<String, Tag> levelTagMap = new HashMap<String, Tag>();
		levelTagMap.put("Level", levelTag);
		CompoundTag writeTag = new CompoundTag("Level", levelTagMap);
		
		try {
			FileOutputStream fileOStream = new FileOutputStream(this.filePath);
			NBTOutputStream minecraftOStream = new NBTOutputStream(fileOStream);
			System.out.println(this.filePath);
			minecraftOStream.writeTag(writeTag);
			minecraftOStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Copy all of the data from a given chunk to this chunk.
	 * @param fromChunk The chunk to copy from.
	 */
	public void copy(MapChunkExtended fromChunk){
		this.data = fromChunk.getData();
		//this.entities = fromChunk.getEntities();
		//this.xPos = fromChunk.getXPos();
		//this.lastUpdate = fromChunk.getLastUpdate();
		//this.zPos = fromChunk.getZPos();
		//this.terrainPopulated = fromChunk.getTerrainPopulated();
		//this.tileEntities = fromChunk.getTileEntities();
		this.heightMap = fromChunk.getHeightMap();
		this.blockLight = fromChunk.getBlockLight();
		this.blocks = fromChunk.getBlocks();
	}
	
	/**
	 * Get the block light data from this chunk.
	 * @return Block Light data.
	 */
	public ByteArrayTag getBlockLight(){
		return this.blockLight;
	}
	
	/**
	 * Get the data tag from this chunk.
	 * @return the data tag.
	 */
	public ByteArrayTag getData(){
		return this.data;
	}
	
	/**
	 * Get the entities from this chunk.
	 * @return The entities data.
	 */
	public ListTag getEntities(){
		return this.entities;
	}
	
	/**
	 * Get hte XPosition of this chunk.
	 * @return The XPosition.
	 */
	public int getXPos(){
		return this.xPos;
	}
	
	/**
	 * Get the height map array of this chunk.
	 * @return the height map data.
	 */
	public byte[] getHeightMap(){
		return this.heightMap;
	}
	
	/**
	 * Get the lastUpdated tag of this chunk..
	 * @return the last updated tag.
	 */
	public LongTag getLastUpdate(){
		return this.lastUpdate;
	}
	
	/**
	 * Get the Z position of this chunk.
	 * @return Z Position.
	 */
	public int getZPos(){
		return this.zPos;
	}
	
	/**
	 * Get the tileEntities of this chunk.
	 * @return tileEntities.
	 */
	public ListTag getTileEntities(){
		return this.tileEntities;
	}
	
	/**
	 * Get the terrainPopulated data of this chunk.
	 * @return Terrain Populated data.
	 */
	public ByteTag getTerrainPopulated(){
		return this.terrainPopulated;
	}
	
	/**
	 * Get the block data of this chunk.
	 * @return the block data.
	 */
	public byte[] getBlocks(){
		return this.blocks;
	}
	
	/**
	 * Sets the block data of this chunk.
	 * @param blocksSet the block data to set.
	 */
	public void setBlocks(byte[] blocksSet){
		this.blocks = blocksSet;
	}
	
	/**
	 * Sets the height data of this chunk.
	 * @param heightMapSet the heightmap to set.
	 */
	public void setHeightMap(byte[] heightMapSet){
		this.heightMap = heightMapSet;
	}
	
	/**
	 * Sets the height map data for this chunk from a 2D array.
	 * @param heightMapSet The 2D array to set the heighmap.
	 */
	public void setHeightMap(byte[][] heightMapSet){
		for(int z = 0; z < heightMapSet.length; z++){
			for(int x = 0; x < heightMapSet.length; x++){
				this.heightMap[z * 16 + x] = heightMapSet[z][x];
			}
		}
	}
	
	/**
	 * Adds the blocks from the coordinates payload to the given coordinates.
	 * @param blockCoord Coordinates that stores the blocks and location.
	 */
	public void addBlocks(Coordinates blockCoord){
		System.out.println("blockCoord: " + blockCoord);
		if(blockCoord.getPayloadType() == blockCoord.PAYLOAD_BLOCK){
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			this.blocks[blockCoord.getZ() * 128 + blockCoord.getX() * 2048 + blockCoord.getY()] = blockCoord.getBlock();
		}
		if(blockCoord.getPayloadType() == blockCoord.PAYLOAD_BLOCK_ARRAY){
			for(int y = 0; y < blockCoord.getBlockArray().length; y++){
				try{
					if(blockCoord.getY() + y < 128 && blockCoord.getBlockArray()[y] > 0){
						this.blocks[(blockCoord.getZ() * 128) + (blockCoord.getX() * 2048) + blockCoord.getY() + y] = blockCoord.getBlockArray()[y];
						if(this.heightMap[blockCoord.getZ() * 16 + blockCoord.getX()] < (blockCoord.getY() + y + 1))
							this.heightMap[blockCoord.getZ() * 16 + blockCoord.getX()] = (byte) (blockCoord.getY() + y + 1);
					}
				}
				catch(ArrayIndexOutOfBoundsException e){
					
				}
			}
		}
	}

	/**
	 * Raise all of the blocks by the y position in the coordinates.
	 * @param coordinates Raise blocks at the x,z coordinates y blocks.
	 */
	public void raiseBlocks(Coordinates coordinates) {
		//Note: All blocks will be raised by the y coordinates given.
		int raiseBlockHeight = coordinates.getY();
		int x = coordinates.getX();
		int z = coordinates.getZ();
		int replaceY = 0;
		int replaceWithY = 0;
        int blockHeight = getHeightMap()[(z * 16 + x)];
		this.blocks[z * 128 + x * 2048] = Blocks.ADMINIUM;
		System.out.println(raiseBlockHeight + " RAISING BLOCKS++++++++++++++++++++++++++++++++");
		this.heightMap[z * 16 + x] = (byte) (this.heightMap[z * 16 + x] + raiseBlockHeight);
		// Lowering all of the blocks.
		if(raiseBlockHeight < 0){
			replaceY = 1;
			replaceWithY = 1 + Math.abs(raiseBlockHeight);
			while(replaceWithY < 128){
				this.blocks[z * 128 + x * 2048 + replaceY] = this.blocks[z * 128 + x * 2048 + replaceWithY];
				this.blocks[z * 128 + x * 2048 + replaceWithY] = Blocks.AIR;
				replaceY++;
				replaceWithY++;
			}
		}
		
		//Raising all of the blocks.
		if(raiseBlockHeight > 0){
			replaceY = 127;
			replaceWithY = 127 - raiseBlockHeight;
			while(replaceWithY > 0){
				this.blocks[z * 128 + x * 2048 + replaceY] = this.blocks[z * 128 + x * 2048 + replaceWithY];
				replaceY--;
				replaceWithY--;
			}
			for(int y = 1; y < replaceY + 1 ; y++){
				this.blocks[z * 128 + x * 2048 + y] = Blocks.STONE;
			}
		}
	}
	
}
