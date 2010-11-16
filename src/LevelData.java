/**
 * Stores all of the level's information such as spawn location, time of day, etc.
 * @author Charles
 *
 */
public class LevelData {
	private String fileLocation;
	
	/**
	 * Loads the level data from the path filePath.
	 * @param filePath The directory path of the level data.
	 */
	public LevelData(String filePath){
		this.fileLocation = filePath;
	}
	
	/**
	 * Rewrites the level file to the data contained in this class.
	 */
	public void save(){
		
	}
}
