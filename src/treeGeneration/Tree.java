import java.util.LinkedList;

/**
 * A Template for creating a tree.
 * Use the template so it is very easy to add in new tree types.
 * @author Charles
 *
 */
public interface Tree {
	/**
	 * Get the tree's trunk diameter.
	 * @return Trunk Diameter.
	 */
	public int getTrunkDiameter();
	/**
	 * Get the specified Tree ID.  
	 * Must be unique for each tree type.
	 */
	public int getTreeId();
	/**
	 * Checks to see if the tree is able to plant at the current position.
	 * @return True if the tree can be generated.
	 */
	public boolean canPlant();
	/**
	 * Generate the tree.
	 */
	public void generateTree();
	/**
	 * Get the new blocks that are at the top of the height map.
	 * @return The new highest blocks.
	 */
	public byte[][] getTopBlockArray();
	/**
	 * Get a linked list of coordinates of the blocks to add.
	 * @return The Top Block array.
	 */
	public LinkedList<Coordinates> createCoordinates();
	/**
	 * Gets the new height map.
	 * @return New height map.
	 */
	public byte[][] getHeightMapArray();
	
	public static final int PINE_TREE = 0;
	
}
