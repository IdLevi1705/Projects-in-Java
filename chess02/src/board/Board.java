package board;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

/**
 * A Class that represents a Chess Board using a 2D matrix of Spots
 */
public class Board {
	/**
	 * A 2D matrix of spots
	 */
	private Spot[][] spots;
	
	/**
	 * Constructor method for board, creates and setups the chess pieces on an 8x8 board
	 */
	public Board() {		
		//init board pieces
		spots = new Spot[8][8];
		initBoard();
	}
	
	
	private void testBoard() {
		//black king row
		spots[0][0] = new Spot(new Rook(true), 0, 0);
		spots[0][1] = new Spot(new Pawn(true), 0, 1);
		spots[0][2] = new Spot(new Pawn(true), 0, 2);
		spots[0][3] = new Spot(new Pawn(true), 0, 3);
		spots[0][4] = new Spot(new King(true), 0, 4);
		spots[0][5] = new Spot(new Knight(true), 0, 5);
		spots[0][6] = new Spot(new Knight(true), 0, 6);
		spots[0][7] = new Spot(new Rook(true), 0, 7);
		
		//black pawn row
		for (int c = 0; c <= 7; c++) {
			spots[1][c] = new Spot(new Pawn(true), 1, c);
		}
		
		spots[1][4] = new Spot(null, 1, 4);
		
		//empty slots
		for (int r = 2; r <= 5; r++) {
			for (int c = 0; c <= 7; c++) {
				spots[r][c] = new Spot(null, r, c);
			}
		}
		
		//white pawn row
		for (int c = 0; c <= 7; c++) {
			spots[6][c] = new Spot(new Pawn(false), 6, c);
		}
		
		//white king row
		spots[7][0] = new Spot(new Rook(false), 7, 0);
		spots[7][1] = new Spot(new Knight(false), 7, 1);
		spots[7][2] = new Spot(new Bishop(false), 7, 2);
		spots[7][3] = new Spot(null, 7, 3);
		spots[7][4] = new Spot(new King(false), 7, 4);
		spots[7][5] = new Spot(new Bishop(false), 7, 5);
		spots[7][6] = new Spot(new Knight(false), 7, 6);
		spots[7][7] = new Spot(new Rook(false), 7, 7);
		
		spots[4][6] = new Spot(new Queen(false), 4, 6);
	}
	
	/**
	 * This method initializes the board with the default chess setup and proper
	 * positions for each piece
	 */
	private void initBoard() {
		//black king row
		spots[0][0] = new Spot(new Rook(true), 0, 0);
		spots[0][1] = new Spot(new Knight(true), 0, 1);
		spots[0][2] = new Spot(new Bishop(true), 0, 2);
		spots[0][3] = new Spot(new Queen(true), 0, 3);
		spots[0][4] = new Spot(new King(true), 0, 4);
		spots[0][5] = new Spot(new Bishop(true), 0, 5);
		spots[0][6] = new Spot(new Knight(true), 0, 6);
		spots[0][7] = new Spot(new Rook(true), 0, 7);
		
		//black pawn row
		for (int c = 0; c <= 7; c++) {
			spots[1][c] = new Spot(new Pawn(true), 1, c);
		}
		
		//empty slots
		for (int r = 2; r <= 5; r++) {
			for (int c = 0; c <= 7; c++) {
				spots[r][c] = new Spot(null, r, c);
			}
		}
		
		//white pawn row
		for (int c = 0; c <= 7; c++) {
			spots[6][c] = new Spot(new Pawn(false), 6, c);
		}
		
		//white king row
		spots[7][0] = new Spot(new Rook(false), 7, 0);
		spots[7][1] = new Spot(new Knight(false), 7, 1);
		spots[7][2] = new Spot(new Bishop(false), 7, 2);
		spots[7][3] = new Spot(new Queen(false), 7, 3);
		spots[7][4] = new Spot(new King(false), 7, 4);
		spots[7][5] = new Spot(new Bishop(false), 7, 5);
		spots[7][6] = new Spot(new Knight(false), 7, 6);
		spots[7][7] = new Spot(new Rook(false), 7, 7);
	}
	
	/**
	 * This method prints out the contents of the entire chess board
	 */
	public void printBoard() {
		for (int r = 0; r <= 7; r++) {
			for (int c = 0; c <= 7; c++) {
				System.out.print(spots[r][c].getSpotSymbol() + " ");
			}
			System.out.println(8 - r);
		}
		System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println();
	}
	
	/**
	 * This method moves a piece from start to end position
	 * @param start is where the piece starts
	 * @param end is where the piece should be moved to
	 */
	public void movePiece(Spot start, Spot end) {
		start.getPiece().incMoves();
		end.setPiece(start.getPiece());
		start.setPiece(null);
	}
	
	/**
	 * This method is used to get the 2D matrix of Spots 
	 * @return the 2D matrix spots, the representation of the chess board
	 */
	public Spot[][] getBoard() {
		return spots;
	}
}
