import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Generates a new Pine Tree for Minecraft.
 * @author Charles
 *
 */
public class PineTree implements Tree {

	private Coordinates coord;
	private int treeHeight;
	private int trunkDiameter;
	private Boolean[][] plantedTrees;
	private byte[][] topBlockArray;
	private byte[][] heightMapArray;
	private byte[][][] writeBlocks;
	
	//Pine Tree specific variables
	//Note height is the trunk height, not to the top of the crown
	Map<Integer, Integer[]> diameterHeightMap = new HashMap<Integer, Integer[]>();
	
	
	private static final boolean F = false;
	private static final boolean T = true;

	//These are all of the arrays I use to generate the leaves for the tree.
	//Go through each array at the corresponding blocks.  
	//	If the array value is True, put a leave at that value.
	//	Else leave it blank.
	
	//ringD1_H0 means that it is generating the ring 
	//	for a tree with a diameter of 1 and 
	//	at height 0 of the loop to generate leaves.
	
	private static final boolean[][] ringD1_H0 = {{F,T,T,T,F},
									 {T,T,T,T,T},
									 {T,T,F,T,T},
									 {T,T,T,T,T},
									 {F,T,T,T,F}};
	
	private static final boolean[][] ringD1_H1 = {{F,F,F,F,F},
									 {F,T,T,T,F},
								 	 {F,T,F,T,F},
								 	 {F,T,T,T,F},
									 {F,F,F,F,F}};


	private static final boolean[][] ringD2_H0 = {{F, F, T, T, T, T, F, F}, 
									  			  {F, T, T, T, T, T, T, F}, 
									  			  {T, T, T, T, T, T, T, T}, 
									  			  {T, T, T, F, F, T, T, T}, 
									  			  {T, T, T, F, F, T, T, T}, 
									  			  {T, T, T, T, T, T, T, T}, 
									  			  {F, T, T, T, T, T, T, F},
									  			  {F, F, T, T, T, T, F, F}};
	

	private static final boolean[][] ringD2_H1 = {{F, F, F, F, F, F, F, F},
												  {F, F, T, T, T, T, F, F},
												  {F, T, T, T, T, T, T, F},
												  {F, T, T, F, F, T, T, F},
												  {F, T, T, F, F, T, T, F},
												  {F, T, T, T, T, T, T, F},
												  {F, F, T, T, T, T, F, F},
												  {F, F, F, F, F, F, F, F}};

	private static final boolean[][] ringD2_H2 = {{F, F, F, F, F, F, F, F},
									 			  {F, F, F, F, F, F, F, F},
									 			  {F, F, T, T, T, T, F, F},
									 			  {F, F, T, F, F, T, F, F},
									 			  {F, F, T, F, F, T, F, F},
									 			  {F, F, T, T, T, T, F, F},
									 			  {F, F, F, F, F, F, F, F},
									 			  {F, F, F, F, F, F, F, F}};

	private static final boolean[][] crownD1_H0 = {{F,F,F,F,F},
			 						  			   {F,F,T,F,F},
			 						  			   {F,T,T,T,F},
			 						  			   {F,F,T,F,F},
			 						  			   {F,F,F,F,F}};
	
	private static final boolean[][] crownD1_H1 = {{F,F,F,F,F},
									  			   {F,F,F,F,F},
									  			   {F,F,T,F,F},
									  			   {F,F,F,F,F},
									  			   {F,F,F,F,F}};

	private static final boolean[][] crownD2_H0 = {{F, F, F, F, F, F, F, F},
									               {F, F, T, T, T, T, F, F},
									               {F, T, T, T, T, T, T, F},
									               {F, T, T, T, T, T, T, F},
									               {F, T, T, T, T, T, T, F},
									               {F, T, T, T, T, T, T, F},
									               {F, F, T, T, T, T, F, F},
									               {F, F, F, F, F, F, F, F},};

	private static final boolean[][] crownD2_H1 = {{F, F, F, F, F, F, F, F},
			  						  			   {F, F, F, F, F, F, F, F},
			  						  			   {F, F, T, T, T, T, F, F},
			  						  			   {F, F, T, T, T, T, F, F},
			  						  			   {F, F, T, T, T, T, F, F},
			  						  			   {F, F, T, T, T, T, F, F},
			  						  			   {F, F, F, F, F, F, F, F},
			  						  			   {F, F, F, F, F, F, F, F},};

	private static final boolean[][] crownD2_H2 = {{F, F, F, F, F, F, F, F},
												   {F, F, F, F, F, F, F, F},
												   {F, F, F, T, T, F, F, F},
												   {F, F, T, T, T, T, F, F},
												   {F, F, T, T, T, T, F, F},
												   {F, F, F, T, T, F, F, F},
												   {F, F, F, F, F, F, F, F},
												   {F, F, F, F, F, F, F, F},};

	private static final boolean[][] crownD2_H3 = {{F, F, F, F, F, F, F, F},
									  			   {F, F, F, F, F, F, F, F},
									  			   {F, F, F, F, F, F, F, F},
									  			   {F, F, F, T, T, F, F, F},
									  			   {F, F, F, T, T, F, F, F},
									  			   {F, F, F, F, F, F, F, F},
									  			   {F, F, F, F, F, F, F, F},
									  			   {F, F, F, F, F, F, F, F},};

	
	/**
	 * Default constructor for the Pine Tree.  
	 * Must take Coordinates of where the tree should be planted.
	 * The height array of where the tree will be planted
	 * and an array of where other trees are planted.
	 * @param c
	 * @param heightArray
	 * @param blockArray
	 * @param plantedTrees
	 */
	public PineTree(Coordinates c, byte[][] heightArray, byte[][] blockArray, Boolean[][] plantedTrees){
		this.coord = c;
		this.plantedTrees = plantedTrees;
		this.topBlockArray = blockArray;
		this.heightMapArray = heightArray;
		Integer[] diameterHeight_1 = new Integer[4];
		diameterHeight_1[0] = 20;
		diameterHeight_1[1] = 21;
		diameterHeight_1[2] = 22;
		diameterHeight_1[3] = 23;
		this.diameterHeightMap.put(1, diameterHeight_1);
		
		Integer[] diameterHeight_2 = new Integer[6];
		diameterHeight_2[0] = 25;
		diameterHeight_2[1] = 26;
		diameterHeight_2[2] = 27;
		diameterHeight_2[3] = 28;
		diameterHeight_2[4] = 29;
		diameterHeight_2[5] = 30;
		this.diameterHeightMap.put(2, diameterHeight_2);
		
		
		//Get a random diameter to generate
		Integer[] diameterArray = new Integer[this.diameterHeightMap.keySet().size()];

		Iterator<Integer> dhIter = this.diameterHeightMap.keySet().iterator();
		int i = 0;
		while(dhIter.hasNext()){
			diameterArray[i] = dhIter.next();
			i++;
		}
		
		this.trunkDiameter = diameterArray[(int)(Math.random() * diameterArray.length)];
		System.out.println("Trunk Diameter: " + this.trunkDiameter);
		
		//Get a random height associated with the diameter
		this.treeHeight = this.diameterHeightMap.get(this.trunkDiameter)
							[(int)(Math.random() * this.diameterHeightMap.get(this.trunkDiameter).length)];


		createRewriteBlockArray();
	}

	@Override
	public byte[][] getTopBlockArray(){
		return this.topBlockArray;
	}
	
	@Override
	public byte[][] getHeightMapArray(){
		return this.heightMapArray;
	}
	
	/**
	 * Creates the 3D array of the blocks to rewrite.
	 */
	private void createRewriteBlockArray(){
		if(this.trunkDiameter == 1){
			this.writeBlocks = new byte[5][5][this.treeHeight + 2];
		}
		else{
			this.writeBlocks = new byte[8][8][this.treeHeight + 5];
		}
	}
	
	@Override
	public boolean canPlant() {
		try{
		if(this.trunkDiameter == 1){
			if(this.topBlockArray[coord.getZ()][coord.getX()] != Blocks.DIRT && 
			   this.topBlockArray[coord.getZ()][coord.getX()] != Blocks.GRASS)
				return false;
		}
		if(this.trunkDiameter == 2)
			if(this.topBlockArray[coord.getZ()][coord.getX()] != Blocks.DIRT && 
				this.topBlockArray[coord.getZ()][coord.getX()] != Blocks.GRASS)
				return false;
		for(int z = this.coord.getZ() - 3; z < this.coord.getZ() + 3; z++){
			for(int x = this.coord.getX() - 3; x < this.coord.getX() +3; x++){
				
					if(this.plantedTrees[z][x])
						return false;
				
				
			}
		}
		}
		catch (IndexOutOfBoundsException e){
			return false;
		}
		return true;
	}

	@Override
	public void generateTree() {
		for(int y = 0; y < this.treeHeight; y++){
			this.generateTrunk(y);
		}
		if(this.trunkDiameter == 1){
			for(int h = 7; h < this.treeHeight; h = h + 2)
				this.generateRings(h);
		}
		if(this.trunkDiameter == 2){

			for(int h = 10; h < this.treeHeight; h = h + 3)
				this.generateRings(h);
		}
		this.generateCrown();
		
	}

	/**
	 * Generates the tree's trunk at height y.  This method is used in a loop.
	 * @param y The height of the tree in the 3D array.
	 */
	private void generateTrunk(int y){
		if(this.trunkDiameter == 1){
			if(y < this.treeHeight - 2)
				this.writeBlocks[2][2][y] = Blocks.TREE_TRUNK;
		}
		if(this.trunkDiameter == 2){
			if(y < this.treeHeight - 3){
			this.writeBlocks[3][3][y] = Blocks.TREE_TRUNK;
			this.writeBlocks[3][4][y] = Blocks.TREE_TRUNK;
			this.writeBlocks[4][3][y] = Blocks.TREE_TRUNK;
			this.writeBlocks[4][4][y] = Blocks.TREE_TRUNK;}
		}
	}
	
	/**
	 * Generates the leaf rings of the tree at the given height of the 3D array.
	 * @param y The height to generate the rings.
	 */
	private void generateRings(int y){
		if(this.trunkDiameter == 1){
			this.generateFromArray(y, ringD1_H0);
			
			y++;
			this.generateFromArray(y, ringD1_H1);
			
		}
		
		if(this.trunkDiameter == 2){
			this.generateFromArray(y, ringD2_H0);

			y++;
			this.generateFromArray(y, ringD2_H1);

			y++;
			this.generateFromArray(y, ringD2_H2);
			
			y++;
			this.generateFromArray(y, ringD2_H2);
		}
		
	}
	
	/**
	 * Generates the crown of the tree.
	 */
	private void generateCrown(){
		int relativeZ;
		int relativeX;
		int height;
		if(this.trunkDiameter == 1){
			height = this.treeHeight;
			this.generateFromArray(height, crownD1_H0);
			
			height++;
			this.generateFromArray(height, crownD1_H1);
		}
		if(this.trunkDiameter == 2){
			height = this.treeHeight;
			this.generateFromArray(height, crownD2_H0);
			height++;
			
			this.generateFromArray(height, crownD2_H1);

			height++;
			this.generateFromArray(height, crownD2_H2);
			
			height++;
			this.generateFromArray(height, crownD2_H3);
			
			height++;
			this.generateFromArray(height, crownD2_H3);
		}
	}
	
	/**
	 * Takes an array and adds in leaves corresponding to that array.
	 * Adds in the leaves at the given height.
	 * @param currY Height to add in the leaves.
	 * @param array Array to figure out if a leaf should be placed.
	 */
	private void generateFromArray(int currY, boolean[][] array){
		for(int z = 0; z < array.length; z++){
			for(int x = 0; x < array[0].length; x++){
				if(array[z][x]){
					this.writeBlocks[z][x][currY] = Blocks.LEAVES;	
				}
			}
		}
	}
	
	@Override
	public LinkedList<Coordinates> createCoordinates(){
		LinkedList<Coordinates> returnCoord = new LinkedList<Coordinates>();
		for(int z = 0; z < this.writeBlocks.length; z++){
			for(int x = 0; x < this.writeBlocks[0].length; x++){
				boolean hasData = false;
				for(int i = 0; i < this.writeBlocks[z][x].length; i++){
					if(this.writeBlocks[z][x][i] > 0)
						hasData = true;
				}
				if(hasData){
					Coordinates tempCoord;
					if(this.trunkDiameter == 1)
						tempCoord = new Coordinates(this.coord.getZ() + z - 2, this.coord.getX() + x - 2, this.coord.getY(), this.writeBlocks[z][x]);
					else
						tempCoord = new Coordinates(this.coord.getZ() + z - 3, this.coord.getX() + x - 3, this.coord.getY(), this.writeBlocks[z][x]);
					returnCoord.add(tempCoord);
				}
			}
		}
		return returnCoord;
	}
	
	@Override
	public int getTreeId() {
		// TODO Auto-generated method stub
		return Tree.PINE_TREE;
	}

	@Override
	public int getTrunkDiameter() {
		return this.trunkDiameter;
	}

}
