package pieces;

import java.util.ArrayList;

import board.Spot;

/**
 * 
 * @author shlomi
 * Abstract class that represents a Piece on the chess board
 *
 */
public abstract class Piece {
	/**
	 * String representation of the piece on the board (i.e. bK for black king)
	 */
	private String piece_name;
	/**
	 * Boolean for determining if a piece is black or white
	 */
	private boolean black;
	/**
	 * Int that counts how many times this piece has been moved
	 */
	private int moves;
	
	/**
	 * Constructor method initializes a piece with its appropriate color and sets moves to 0
	 * @param black for color of piece
	 * 
	 */
	public Piece(boolean black) {
		this.black = black;
		this.moves = 0;
	}
	
	/**
	 * This method returns the piece name of a piece
	 * @return String representation of piece name
	 */
	public String toString() {
		return getPieceName();
	}
	
	//getters
	/**
	 * This method returns this piece object
	 * @return Piece object
	 */
	public Piece getPiece() {
		return this;
	}	
	/**
	 * This method gets the Piece Name
	 * @return Piece Name
	 */
	public String getPieceName() {
		return this.piece_name;
	}	
	/**
	 * This method returns if the piece is black or not
	 * @return true if black else false
	 */
	public boolean isBlack() {
		return this.black;
	}
	/**
	 * This method returns the number of times a piece has moved
	 * @return number of moves
	 */
	public int getTimesMoves() {
		return this.moves;
	}
	/**
	 * This method returns if the piece has made atleast 1 move
	 * @return if piece moved atleast once
	 */
	public boolean hasMoved() {
		return this.moves != 0;
	}
	/**
	 * This method returns if the piece is white or not
	 * @return true if white, else false
	 */
	public boolean isWhite() {
		return !this.black;
	}

	
	//setters
	/**
	 * This method sets the piece name
	 * @param piece_name
	 */
	public void setPieceName(String piece_name) {
		this.piece_name = piece_name;
	}
	/**
	 * This method sets if the piece is black or white
	 * @param black (true for black, false for white)
	 */
	public void setBlack(boolean black) {
		this.black = black;
	}
	/**
	 * This method will increase the number of moves by 1
	 */
	public void incMoves() {
		this.moves++;
	}
	
	/**
	 * This method will decrease the number of moves by 1
	 */
	public void decMoves() {
		this.moves--;
	}
	
	//abstract methods
	/**
	 * Abstract method to be implemented by subclasses. This method should return a list
	 * of all possible spots a piece can move to, given a specific arrangement of the board
	 * @param board is a 2D matrix of Spots (the current game state)
	 * @param start is the current position Spot where the piece is
	 * @return an ArrayList of Spots that indicate where the piece can move to
	 */
	public abstract ArrayList<Spot> moves(Spot[][] board, Spot start);
}
