/**
 * A coordinate system which supports 2D and 3D as well as a payload.
 * @author Charles
 *
 */
public class Coordinates {
	private int z;
	private int x;
	private int y;
	private byte block;
	private byte[] blocks;
	private int payloadType;
	
	public static int PAYLOAD_NONE = 0;
	public int PAYLOAD_BLOCK = 1;
	public int PAYLOAD_BLOCK_ARRAY = 2;
	
	/**
	 * Coordinates for a 2D plane without a payload.
	 * 
	 * @param za - Z Coordinate
	 * @param xa - X Coordinate
	 */
	public Coordinates(int za, int xa){
		this.z = za;
		this.x = xa;
		this.payloadType = PAYLOAD_NONE;
	}
	
	/**
	 * Coordinates for a 3D plane without a payload.
	 * @param za - Z Coordinate
	 * @param xa - X Coordinate
	 * @param ya - Y Coordinate
	 */
	public Coordinates(int za, int xa, int ya){
		this.z = za;
		this.x = xa;
		this.y = ya;
		this.payloadType = PAYLOAD_NONE;
	}
	
	/**
	 * Coordinates for a 3D plane with a payload of a single block.
	 * @param za - Z Coordinate
	 * @param xa - X Coordinate
	 * @param ya - Y Coordinate
	 * @param block - a byte corresponding to a Minecraft block.
	 * @see Blocks
	 */
	public Coordinates(int za, int xa, int ya, byte block){
		this.z = za;
		this.x = xa;
		this.y = ya;
		this.block = block;
		this.payloadType = PAYLOAD_BLOCK;
	}
	
	/**
	 * Coordinates for a 3D plane with a payload of a single block.
	 * @param za - Z Coordinate
	 * @param xa - X Coordinate
	 * @param ya - Y Coordinate
	 * @param blocks - an array of bytes corresponding to Minecraft blocks.
	 * @see Blocks
	 */
	public Coordinates(int za, int xa, int ya, byte[] blocks){
		this.z = za;
		this.x = xa;
		this.y = ya;
		this.blocks = blocks;
		this.payloadType = PAYLOAD_BLOCK_ARRAY;
	}
	
	/**
	 * Returns the Z Coordinate
	 * @return - Z Coordinate
	 */
	public int getZ(){
		return this.z;
	}
	
	/**
	 * Sets the Z Coordinate
	 * @param newZ - Z Coordinate to be set
	 */
	public void setZ(int newZ){
		this.z = newZ;
	}
	
	/**
	 * Returns the X Coordinate.
	 * @return - X Coordinate
	 */
	public int getX(){
		return this.x;
	}
	
	/**
	 * Sets the X Coordinate.
	 * @param newX - X Coordinate.
	 */
	public void setX(int newX){
		this.x = newX;
	}
	
	/**
	 * Returns the Y Coordinate.
	 * @return - Y Coordinate.
	 */
	public int getY(){
		return this.y;
	}
	
	/**
	 * Sets the Y Coordinate.
	 * @param newY - Y Coordinate.
	 */
	public void setY(int newY){
		this.y = newY;
	}
	
	/**
	 * Returns the payload if it is a block array.  Else returns null.
	 * @return - pay load - Block Array
	 */
	public byte[] getBlockArray(){
		if(this.payloadType != PAYLOAD_BLOCK_ARRAY)
			return null;
		return this.blocks;
	}
	
	/**
	 * Returns the pay load if it is a block.  Else returns 0;
	 * @return - pay load - Block
	 */
	public byte getBlock(){
		if(this.payloadType != PAYLOAD_BLOCK){
			return 0;
		}
		return this.block;
	}
	
	/**
	 * Returns the type of the pay load.
	 * @return - pay load type.
	 */
	public int getPayloadType(){
		return this.payloadType;
	}
}
