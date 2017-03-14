package cubes;
/**
 * A cube of Blocks that can fit pieces to it's side
 * 
 * @author Matthias Hutter
 */
public class Cube {
	/**
	 * empty constructor.
	 * removes every Piece from this cube
	 */
	public Cube (){
		clear();
	}
	/**
	 * copy constructor for cube instances
	 * 
	 * @param aCube the cube to be duplicated
	 */
	public Cube (Cube aCube){ 
		System.arraycopy(aCube.pieces, 0, pieces, 0, aCube.pieces.length);
	}
	/**
	 * initializes the cube
	 */
	public void clear(){
		for (int i=0; i<5; ++i)
			for (int j=0; j<5; ++j)
				for (int k=0; k<5; ++k)
					pieces[i][j][k]=new Block();
	}
	/**
	 * Returns the faces of the cube as a Piece reference
	 * 
	 * @param side the side to be returned
	 * 0 = bottom
	 * 1 = front
	 * 2 = left
	 * 3 = back
	 * 4 = right
	 * 5 = top
	 * @return the handle to the new Piece
	 */
	public Piece getSide(int side){
		//Coordinates of block: [x][y][z]	
		Piece newPiece = new Piece();
		switch (side){
		case 0://bottom, xy area at [0][0][0] 
			for (int i = 0; i < 5; ++i)
				for (int j = 0; j < 5; ++j)
					newPiece.block[i][j]=pieces[4-i][j][0];
			break;
		case 1://front, zy area at [4][0][0]
			for (int i = 0; i < 5; ++i)
				for (int j = 0; j < 5; ++j)
					newPiece.block[i][j]=pieces[4][j][4-i];
			break;
		case 2://left, zx area at [0][0][0]
			for (int i = 0; i < 5; ++i)
				for (int j = 0; j < 5; ++j)
					newPiece.block[i][j]=pieces[j][0][4-i];
			break;
		case 3://back, zy area at [0][0][0]
			for (int i = 0; i < 5; ++i)
				for (int j = 0; j < 5; ++j)
					newPiece.block[i][j]=pieces[0][4-j][4-i];
			break;	
		case 4://right, zx area at [0][4][0]
			for (int i=0; i<5; ++i)
				for (int j=0; j<5; ++j)
					newPiece.block[i][j]=pieces[4-j][4][4-i];
			break;
		case 5://top,  yx area at [0][0][4]
			for (int i=0; i<5; ++i)
				for (int j=0; j<5; ++j)
					newPiece.block[i][j]=pieces[i][j][4];
			break;	
		};
		return newPiece;
	}
	public boolean fits(Piece newPiece, int side){
		Piece sidePiece = getSide(side);
		for (int i=0; i<5 ;++i)
			for (int j=0;j<5;++j)
				if (sidePiece.block[i][j].set && newPiece.block[i][j].set) return false;
		return true;
	}
	public boolean hasPiece(Piece newPiece, int side){
		Piece sidePiece = getSide(side);
		for (int i=0; i<5 ;++i)
			for (int j=0;j<5;++j)
				if (!sidePiece.block[i][j].set && newPiece.block[i][j].set) return false;
		return true;
	}	

	public boolean addPiece(Piece newPiece, int side){
		if (!fits(newPiece, side)) return false;
		Piece sidePiece = getSide(side);
		for (int i=0; i<5 ;++i)
			for (int j=0;j<5;++j)
				sidePiece.block[i][j].set = sidePiece.block[i][j].set || newPiece.block[i][j].set;
		return true;
	}
	public boolean removePiece(Piece newPiece, int side){
		if (!hasPiece(newPiece,side)) return false;
		Piece sidePiece = getSide(side);
		for (int i=0; i<5 ;++i)
			for (int j=0;j<5;++j)
				sidePiece.block[i][j].set = sidePiece.block[i][j].set && !newPiece.block[i][j].set;
		return true;
	}
	//PRIVATE
	private Block[][][] pieces = new Block[5][5][5]; //Coordinates of block: [x][y][z]	
}
