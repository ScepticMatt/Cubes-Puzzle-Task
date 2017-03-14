package cubes;
/**
 * Solve the cube challenges
 * 
 * @author Matthias Hutter
 */
import java.util.ArrayList;
import java.util.List;

public class MainClass {
	/**
	 * Copies the puzzle information into a lists of pieces, fits them on a  cube 
	 * and outputs the possible completed cubes to text files cube in an unfolded form
	 * @param args
	 */
	public static void main(String[] args) {
		Cube cube = new Cube(); //test-cube for puzzles
		List<Piece> puzzle; //stack of puzzle pieces
		Solver solver;
		int index = 0;
		for (boolean[][][] puzzleData : PUZZLES){
			index++;
			puzzle = new ArrayList<>(); 
			for (boolean[][] pieceData : puzzleData){
				puzzle.add(new Piece(pieceData));
			}
			solver = new Solver(puzzle,cube);
			solver.solve();
			StringBuilder filename = new StringBuilder("solutions_for_puzzle_").append(index).append(".txt");
			solver.toFile(filename.toString());
		}
	}
	
	//raw data of puzzles
	static final boolean PUZZLES[][][][]=
		{
			{//blue puzzle
				{//piece 1
					{false,false,true,false,false},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{false,false,true,false,false},
				},
				{//piece 2
					{true,false,true,false,true},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{true,false,true,false,true},
				},
				{//piece 3
					{false,false,true,false,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{false,false,true,false,false},
				},
				{//piece 4
					{false,true,false,true,false},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{true,true,false,true,false},
				},
				{//piece 5
					{false,true,false,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{true,false,true,false,false},
				},
				{//piece 6
					{false,true,false,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,false,true,true},
				}
				
			},
			{//red puzzle
				{//piece 1
					{false,false,false,true,true},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{false,true,false,true,true},
				},
				{//piece 2
					{false,true,false,true,false},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,true,false,false,false},
				},
				{//piece 3
					{false,true,true,false,true},
					{true,true,true,true,true},
					{false,true,true,false,false},
					{true,true,true,true,true},
					{true,false,false,true,true},
				},
				{//piece 4
					{false,false,true,false,false},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,false,true,false,false},
				},
				{//piece 5
					{false,false,true,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{true,false,true,false,false},
				},
				{//piece 6
					{false,true,true,false,false},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{true,true,false,true,true},
				}			
			},
			{//purple puzzle
				{//piece 1
					{true,true,false,true,false},
					{true,true,true,true,false},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{false,false,true,false,false},
				},
				{//piece 2
					{false,false,false,true,true},
					{true,true,true,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{false,true,false,true,false},
				},
				{//piece 3
					{false,true,false,false,false},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,false,true,false,false},
				},
				{//piece 4
					{true,true,false,true,true},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,true,true,true,false},
					{false,true,false,true,false},
				},
				{//piece 5
					{false,false,true,false,true},
					{false,true,true,true,true},
					{true,true,true,true,true},
					{true,true,true,true,false},
					{true,false,true,true,false},
				},
				{//piece 6
					{false,true,false,true,true},
					{false,true,true,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{true,true,false,true,false},
				}	
			},
			{//yellow puzzle
				{//piece 1
					{false,false,true,false,false},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,true,false,true,false},
				},
				{//piece 2
					{false,false,true,false,true},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{true,true,true,true,false},
					{false,true,false,true,false},
				},
				{//piece 3
					{false,false,true,false,true},
					{false,true,true,true,false},
					{false,false,false,false,true},
					{true,true,true,true,false},
					{true,false,true,false,false},
				},
				{//piece 4
					{true,false,true,false,true},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{true,false,true,false,false},
				},
				{//piece 5
					{false,false,true,false,false},
					{false,true,true,true,true},
					{true,true,true,true,false},
					{false,true,true,true,true},
					{true,true,false,true,false},
				},
				{//piece 6
					{false,true,false,true,false},
					{false,true,true,true,false},
					{true,true,true,true,true},
					{false,true,true,true,false},
					{false,true,false,true,true},
				}	
			},
			
		};
}
