package cubes;
/**
 * A matrix of blocks that can be rotated in 3D.
 * 
 * @author Matthias Hutter
 */
public class Piece {
	public Block[][] block = new Block[5][5];
	/**
	 * Empty constructor
	 */
	public Piece(){
		init();
	}
	/**
	 * Copy constructor for boolean data
	 */
	public Piece(boolean[][] blockData){
		init();
		for (int i = 0; i<5;++i)
			for (int j = 0; j<5;++j)
				block[i][j].set = blockData[i][j];
	}
	/**
	 * Copy constructor for Block data
	 */
	public Piece(Block[][] blockData){
		init();
		block = blockData;
	}
	/**
	 * Copy constructor for a Piece instance
	 */
	public Piece(Piece pieceInstance){//copy constructor
		init();
		for (int i = 0; i<5;++i)
			for (int j = 0; j<5;++j)
				block[i][j].set = pieceInstance.block[i][j].set;
	}

	public Piece rotateClockwise(){
		transpose();
		flipHorizontal();
		return this;
	}
	public Piece rotateCounterClockwise(){
		transpose();
		flipVertical();
		return this;
	}
	public Piece flipVertical(){
		Piece temp = new Piece (); //stores block references
		for (int i = 0; i<5;++i)
			for (int j = 0; j<5;++j)
				temp.block[i][j] = block[4-i][j];
		block = temp.block;	
		return this;	
	}
	public Piece flipHorizontal(){
		Piece temp = new Piece (); //stores block references
		for (int i = 0; i<5;++i)
			for (int j = 0; j<5;++j)
				temp.block[i][j] = block[i][4-j];
		block = temp.block;	
		return this;	
	}
	
	//PRIVATE
	/**
	 * initializes block
	 */
	private final void init(){
		for (int i = 0; i<5;++i)
			for (int j = 0; j<5;++j)
				block[i][j] = new Block();
	}
	/**
	 * swaps coordinates of block
	 */
	private Piece transpose(){
		Piece temp = new Piece (); //stores block references
		for (int i = 0; i<5;++i)
			for (int j = 0; j<5;++j)
				temp.block[i][j] = block[j][i];
		block = temp.block;
		return this;
	}

	
}
