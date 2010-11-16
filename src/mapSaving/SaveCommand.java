/**
 * SaveCommand stores the data that needs to be saved.
 * @author Charles
 *
 */
public class SaveCommand {
	
	public static int COPY_COMMAND = 0;
	public static int DELETE_COMMAND = 1;
	public static int ADD_BLOCKS_COMMAND = 2;
	public static int CHANGE_HEIGHT_COMMAND = 3;
	
	private String chunkA;
	private String chunkB;
	private Coordinates coord;
	private int command;
	
	/**
	 * Constructor that takes a command and two file paths.
	 * Mainly used for copying chunks.
	 * @param commandA Command to save.
	 * @param a File Path for the first chunk.
	 * @param b File Path for the second chunk.
	 */
	public SaveCommand(int commandA, String a, String b){
		this.chunkA = a;
		this.chunkB = b;
		this.command = commandA;
	}
	
	/**
	 * Constructor that takes a command and one file path.
	 * Mainly used for deleting a chunk.
	 * @param commandA Command to save.
	 * @param a File path for the chunk.
	 */
	public SaveCommand(int commandA, String a){
		this.chunkA = a;
		this.command = commandA;
	}
	
	/**
	 * Constructor that takes a command, a file path, and a coordinate.
	 * Mainly used for adding blocks or raising the height.
	 * @param commandA Command to save.
	 * @param a File path for the chunk.
	 * @param c Coordinates to locate what to modify.
	 */
	public SaveCommand(int commandA, String a, Coordinates c){
		this.command = commandA;
		this.chunkA = a;
		this.coord = c;
	}
	
	/**
	 * Get the command to save.
	 * @return command to save.
	 */
	public int getCommand(){
		return this.command;
	}
	
	/**
	 * Gets the first chunk in the save command.
	 * Null if there is no chunk.
	 * @return The first chunk in the save command.
	 */
	public String getChunkA(){
		return this.chunkA;
	}
	
	/**
	 * Gets the second chunk in the save command.
	 * Null if there is no chunk.
	 * @return The second chunk in the save command.
	 */
	public String getChunkB(){
		return this.chunkB;
	}
	
	/**
	 * Gets the coordinates in the save command.
	 * Null if there are no coordinates.
	 * @return The coordinates in the save command.
	 */
	public Coordinates getCoordinates(){
		return this.coord;
	}
}
