package cubes;
/**
 * Solves a puzzle (list of Piece) using a Cube
 * 
 * @author Matthias Hutter 
 */
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Solver {
	/**
	 * Constructor
	 * 
	 * @param aPuzzle the puzzle to solve
	 * @param aCube the Cube used to solve the puzzle
	 */
	public Solver(List<Piece> aPuzzle, Cube aCube){
		puzzle = new ArrayList<>(aPuzzle);
		cube = aCube;
	}
	/**
	 * Solves the puzzle.
	 * Does nothing if the puzzle doesn't have 6 pieces
	 */
	public void solve(){
		if (puzzle.size() == 6) {
			clear();
			solveRecursive(puzzle,0);	
		}
	}
	
	/**
	 * Outputs solution to a file in an unfolded format
	 * @param aPath the file to write to
	 */
	public void toFile(String aPath){
		try { 
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(aPath), "utf-8"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
			
		for (LinkedHashMap<Integer,Piece> nextSolution : solutions){
			writeLine("Solution:");
			printSolution(nextSolution);
		}
		try { 
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//PRIVATE
	/**
	 * Writes a String as a line to the file specified by writer
	 * 
	 * @param line the line to be written
	 */
	private void writeLine(String line){
		try {
			writer.write(line);
			writer.newLine(); //depends on system;
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}
	/**
	 * Writes a StringBuilder as a line to the file specified by writer
	 * 
	 * @param line line the line to be written
	 */
	private void writeLine(StringBuilder line){
		writeLine(line.toString());
	}
	/**
	 * Writes a line to the file specified by writer
	 */
	private void newLine(){
		writeLine("");
	}
	private List<Piece> puzzle;
	private Cube cube;
	private LinkedHashMap<Integer,Piece> currentSolution;
	private ArrayList<LinkedHashMap<Integer, Piece>> solutions;
	private BufferedWriter writer = null;
	/**
	 * Removes all pieces from cube and all solutions
	 */
	private void clear(){
		cube.clear();
		currentSolution = new LinkedHashMap<>();
		solutions = new ArrayList<>();
	}
	/**
	 * Recursively solves the puzzle
	 * 
	 * @param puzzle the remaining pieces of the puzzle
	 * @param side the current side of the Cube to test
	 */
	private void solveRecursive(List<Piece> puzzle,int side){
		int numPieces = puzzle.size();
		if (numPieces == 6){ //put first piece at the bottom (removes a significantly number of duplicates)
			cube.addPiece(puzzle.get(0),0);
			recursion(puzzle,puzzle.get(0),0,0);
		}
		else{
			for (int i = 0; i < numPieces; ++i){
				Piece testPiece = new Piece(puzzle.get(i));
				if (cube.addPiece(testPiece,side)) recursion(puzzle,testPiece,i,side);
				for (int j = 0; j < 3; ++j){
					if (cube.addPiece(testPiece.rotateClockwise(),side))recursion(puzzle,testPiece,i,side);
				} 
				if (cube.addPiece(testPiece.flipHorizontal(),side)) recursion(puzzle,testPiece,i,side);
				for (int j = 0; j < 3; ++j){
					if (cube.addPiece(testPiece.rotateClockwise(),side))recursion(puzzle,testPiece,i,side);
				} 
			}
		}
	}
	/**
	 * Put piece on a side, try to find a solution for the rest of the puzzle
	 * 
	 * @param puzzle remaining pieces of the puzzle
	 * @param piece the Piece to add
	 * @param pieceIndex the index of the piece in the puzzle
	 * @param side the side to add the Piece to
	 */
	private void recursion(List<Piece> puzzle, Piece piece, int pieceIndex, int side){
		pop(puzzle,piece,pieceIndex,side);
		if (puzzle.isEmpty()){
			if (!isDuplicate(currentSolution))
				solutions.add(duplicateSolution(currentSolution));
		}
		else  solveRecursive(puzzle,side+1);
		push(puzzle,piece,pieceIndex,side);	
	}
	/**
	 * Take Piece from puzzle, put it on cube
	 * 
	 * @param puzzle remaining pieces of the puzzle
	 * @param piece the Piece to add
	 * @param pieceIndex the index of the piece in the puzzle
	 * @param side the side to add the Piece to
	 */
	private void pop(List<Piece> puzzle, Piece piece, int pieceIndex, int side){
		//put puzzle piece on cube
		currentSolution.put(side, piece);
		puzzle.remove(pieceIndex);
	}
	/**
	 * Take Piece from cube, put it back to the puzzle
	 * 
	 * @param puzzle remaining pieces of the puzzle
	 * @param piece the Piece to remove
	 * @param pieceIndex the index of the piece in the puzzle
	 * @param side the side to remove the Piece from
	 */
	private void push(List<Piece> puzzle, Piece piece, int pieceIndex, int side){
		cube.removePiece(piece, side);
		currentSolution.remove(side, piece);
		puzzle.add(piece);
	}
	/**
	 * Checks if a solution was found previously
	 * 
	 * @param solution the solution to be checked
	 * @return true if the solution is a duplicate to another solution found previously
	 */
	private boolean isDuplicate(LinkedHashMap<Integer,Piece> solution){
		for (LinkedHashMap<Integer,Piece> nextSolution : solutions){
			if (isIdentical(solution,nextSolution)) return true; 
			LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(solution));
			for (int i = 0; i < 3; ++i){
				if (isIdentical(yaw(duplicate),nextSolution)) return true;
			}		
		}
		return false;
	}
	/**
	 * Checks if two solutions have identical values
	 * 
	 * @param solution1 the solution to be compared 
	 * @param solution2 the solution to be checked
	 * @return true if solution1 and solution2 have identical values
	 */
	private static boolean isIdentical(LinkedHashMap<Integer,Piece> solution1, LinkedHashMap<Integer,Piece> solution2){
		if (solution1 == solution2) return true; 
		for (int key : solution1.keySet()) {
			if (!solution2.containsKey(key)) return false; // missing piece
			if (solution1.get(key) == solution2.get(key)) continue; //identical piece instance
			else{
				for (int i = 0; i < 5; ++i)
					for (int j = 0; j < 5; ++j)
						if (solution1.get(key).block[i][j].set != solution2.get(key).block[i][j].set) return false; //value mismatch, try next solution
			}
		}
		return true;
	}
	/**
	 * Duplicates a solution
	 * 
	 * @param solution the solution to be duplicated
	 * @return handle to the duplicate solution
	 */
	private static LinkedHashMap<Integer,Piece> duplicateSolution(LinkedHashMap<Integer,Piece> solution){
		LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>();
		for (Map.Entry<Integer, Piece> entry : solution.entrySet()) {
			Piece newPiece = new Piece(entry.getValue());//duplication
			duplicate.put(entry.getKey(), newPiece);
		}
		return duplicate;
	}
	/**
	 * Changes solution as if the Cube were rotated around the z-axis
	 * (Other rotations aren't necessary because the first piece is always put at the bottom)
	 * 
	 * @param solution the solution to rotate
	 * @return handle to the rotated solution
	 */
	private static LinkedHashMap<Integer,Piece> yaw(LinkedHashMap<Integer,Piece> solution){
		LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(solution));
		//rotate pieces
		duplicate.get(0).rotateClockwise();
		duplicate.get(5).rotateCounterClockwise();
		//switch piece position
		solution.get(0).block = duplicate.get(0).block;
		solution.get(1).block = duplicate.get(2).block;
		solution.get(2).block = duplicate.get(3).block;
		solution.get(3).block = duplicate.get(4).block;
		solution.get(4).block = duplicate.get(1).block;
		solution.get(5).block = duplicate.get(5).block;	
		return solution;
	}	
	/**
	 * Writes solution to a file specified by writer in an unfolded form
	 * 
	 * @param solution the solution to write
	 */
	private  void printSolution(LinkedHashMap<Integer,Piece> solution) {
		for (int i = 0; i < 5; ++i){
			StringBuilder buff = new StringBuilder();
			for (int j = 0; j < 5; ++j){
				if (solution.get(4).block[j][i].set) buff.append('o');
				else buff.append(' ');	
			}
			for (int j = 0; j < 5; ++j){
				if (solution.get(0).block[i][4-j].set) buff.append('o');
				else buff.append(' ');	
			}
			for (int j = 0; j < 5; ++j){
				if (solution.get(2).block[4-j][4-i].set) buff.append('o');
				else buff.append(' ');	
			}
			writeLine(buff);
		}
		
		for (int i = 0; i < 5; ++i){
			StringBuilder buff = new StringBuilder();
			buff.append("     ");
			for (int j = 0; j < 5; ++j){
				if (solution.get(3).block[4-i][j].set) buff.append('o');
				else buff.append(' ');	
			}
			buff.append("     ");
			writeLine(buff);
		}
		for (int i = 0; i < 5; ++i){
			StringBuilder buff = new StringBuilder();
			buff.append("     ");
			for (int j = 0; j < 5; ++j){
				if (solution.get(5).block[i][4-j].set) buff.append('o');
				else buff.append(' ');	
			}
			buff.append("     ");
			writeLine(buff);
		}
		for (int i = 0; i < 5; ++i){
			StringBuilder buff = new StringBuilder();
			buff.append("     ");
			for (int j = 0; j < 5; ++j){
				if (solution.get(1).block[i][4-j].set) buff.append('o');
				else buff.append(' ');	
			}
			buff.append("     ");
			writeLine(buff);
		}
		newLine();
	}
	
	//Obsolete Test-functions
	/*

	private static LinkedHashMap<Integer,Piece> reflectX(LinkedHashMap<Integer,Piece> solution){
		//TODO: test
		//mirror area = x

		LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(solution));
		for (int i = 0; i< 5; ++i)
			duplicate.get(i).flipVertical();
		for (int i = 1; i<4; ++i)
			solution.get(i).block = duplicate.get(i).block;
		solution.get(0).block = duplicate.get(5).block;
		solution.get(5).block = duplicate.get(0).block;
		return solution;
	}
	private boolean isSymmetric(){
		//TODO: test
		// checks for *432 full octahedral symmetry.  (cube and octahedron are isomorphic)
		LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(currentSolution));
		return (isRotationalSymmetric(duplicate)||isRotationalSymmetric(reflectX(duplicate)));
	}
	
	private  boolean isRotationalSymmetric(LinkedHashMap<Integer,Piece> solution){
		// checks for 432 chiral octahedral symmetry. 
		// roll = x, pitch = y, yaw = z [used decompositions]
		//
		//Possible duplicates (24 possibilities):
		//I: identity
		//II: 6 rotations by 90, 3 rotations by 180 degrees, axes = opposing faces [x,y,z; 2x, 2y, 2z; 3x, 3y, 3z]
		//IV: 6 rotations by 180 degrees, axes = opposing edges [2x y,2x z, 2y x,2y z, 2z x, 2z y]
		//V:  4 rotations by 120, 4 rotations by 240 degrees, axes = opposing corners [x z, y z, 3x z, 3y z; 3z x, 3z y, 3z 3x, 3z 3y]
		for (LinkedHashMap<Integer,Piece> nextSolution : solutions){
			if (isIdentical(solution,nextSolution)) return true; // (I)
			LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(solution));
			for (int i = 0; i < 4; ++i){ 
				roll(duplicate);
				if (isIdentical(duplicate,nextSolution)) return true; //(II)
				if (i==2){ //(III)
					LinkedHashMap<Integer,Piece> duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(pitch(duplicate2),nextSolution)) return true;
					duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(yaw(duplicate2),nextSolution)) return true;
				}
				else if (i==1 || i==3){ //(IV)
					LinkedHashMap<Integer,Piece> duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(yaw(duplicate2),nextSolution)) return true;			
				}
			}
			duplicate = new LinkedHashMap<>(duplicateSolution(solution));
			for (int i = 0; i < 4; ++i){ 
				pitch(duplicate);
				if (isIdentical(duplicate,nextSolution)) return true; //(II)
				if (i==2){ //(III)
					LinkedHashMap<Integer,Piece> duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(roll(duplicate2),nextSolution)) return true;
					duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(yaw(duplicate2),nextSolution)) return true;
				}
				else if (i==1 || i==3){ //(IV)
					LinkedHashMap<Integer,Piece> duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(yaw(duplicate2),nextSolution)) return true;			
				}
			}
			duplicate = new LinkedHashMap<>(duplicateSolution(currentSolution));
			for (int i = 0; i < 4; ++i){ 
				yaw(duplicate);
				if (isIdentical(duplicate,nextSolution)) return true; //(II)
				if (i==2){ //(III)
					LinkedHashMap<Integer,Piece> duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(roll(duplicate2),nextSolution)) return true;
					duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(pitch(duplicate2),nextSolution)) return true;
				}
				else if (i==3){ //(IV)
					LinkedHashMap<Integer,Piece> duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(roll(duplicate2),nextSolution)) return true;
					if (isIdentical(roll(roll(duplicate2)),nextSolution)) return true;
					duplicate2 = new LinkedHashMap<>(duplicateSolution(duplicate));
					if (isIdentical(pitch(duplicate2),nextSolution)) return true;
					if (isIdentical(pitch(pitch(duplicate2)),nextSolution)) return true;
				}
			}
		}
		return false;	
	}
		private static LinkedHashMap<Integer,Piece> roll(LinkedHashMap<Integer,Piece> solution){
		LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(solution));
		//rotate pieces
		duplicate.get(0).rotateCounterClockwise();
		duplicate.get(1).rotateCounterClockwise();
		duplicate.get(2).rotateCounterClockwise();
		duplicate.get(3).rotateClockwise();
		//switch piece position
		solution.get(0).block = duplicate.get(2).block;
		solution.get(1).block = duplicate.get(1).block;
		solution.get(2).block = duplicate.get(5).block;
		solution.get(3).block = duplicate.get(3).block;
		solution.get(4).block = duplicate.get(0).block;
		solution.get(5).block = duplicate.get(4).block;
		return solution;
	}	
	private static LinkedHashMap<Integer,Piece> pitch(LinkedHashMap<Integer,Piece> solution){
		LinkedHashMap<Integer,Piece> duplicate = new LinkedHashMap<>(duplicateSolution(solution));
		//rotate pieces
		duplicate.get(0).rotateClockwise().rotateClockwise();
		duplicate.get(2).rotateClockwise();
		duplicate.get(3).rotateClockwise().rotateClockwise();
		duplicate.get(4).rotateCounterClockwise();
		//switch piece position
		solution.get(0).block = duplicate.get(1).block;
		solution.get(1).block = duplicate.get(5).block;
		solution.get(2).block = duplicate.get(2).block;
		solution.get(3).block = duplicate.get(0).block;
		solution.get(4).block = duplicate.get(4).block;
		solution.get(5).block = duplicate.get(3).block;
		return solution;
	}
	*/
}
