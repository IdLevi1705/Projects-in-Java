package board;

import pieces.Piece;

/**
 * A class that represents an individual spot in an 8x8 chess grid
 */
public class Spot {
	/**
	 * A piece, if any, that are on this spot
	 */
	private Piece piece;
	/**
	 * The symbol of the piece that is on this spot
	 */
	private String spot_symbol;
	/**
	 * The row of this spot
	 */
	private int r;
	/**
	 * The column of this spot
	 */
	private int c;
	
	/**
	 * Constructor method for spot
	 * @param piece, the piece for this spot. Enter null for no piece.
	 * @param r, the row for this spot
	 * @param c, the column for this spot
	 */
	public Spot(Piece piece, int r, int c) {
		this.piece = piece;
		this.r = r;
		this.c = c; 
		this.spot_symbol = setSymbol();
	}
	
	/**
	 * Sets the symbol of the spot. If no piece is present, the symbol will be "  " or "##"
	 * depending on position of the row and column.
	 * @return
	 */
	private String setSymbol() {
		if (piece != null)
			return piece.getPieceName();
		
		if (r % 2 == 0 && c % 2 == 1 || r % 2 == 1 && c % 2 == 0) {
			return "##";
		}
		
		return "  ";
	}
	
	/**
	 * This method is used to determine if a piece exists at this spot
	 * @return true if there is a piece, else false
	 */
	public boolean hasPiece() {
		return this.piece != null;
	}
	
	/**
	 * This method returns a String representation of a spot
	 * @return String representation of a spot
	 */
	public String toString() {
		if (hasPiece())
			return piece.toString() + " (" + getR() + "," + getC() + ")";
		return "(" + getR() + "," + getC() + ")";
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Spot))
			return false;
		Spot cur = (Spot) o;
		return cur.r == this.r && cur.c== this.c;
	}
	
	//getters
	/**
	 * This method returns the piece on this spot
	 * @return null if no piece present, else a piece
	 */
	public Piece getPiece() {
		return this.piece;
	}	
	/**
	 * This method returns the row of the current spot
	 * @return row of spot
	 */
	public int getR() {
		return this.r;
	}
	/**
	 * This method returns the column of the current spot
	 * @return column of spot
	 */
	public int getC() {
		return this.c;
	}
	/**
	 * This method returns the symbol of the current spot
	 * @return symbol of spot
	 */
	public String getSpotSymbol() {
		return this.spot_symbol;
	}
	
	//setters
	/**
	 * This method sets the piece on this spot
	 * @param piece, the piece to be placed on this spot
	 */
	public void setPiece(Piece piece) {
		this.piece = piece;
		this.spot_symbol = setSymbol();
	}	
	/**
	 * This method sets the row of this spot
	 * @param r, the new row of the spot
	 */
	public void setR(char r) {
		this.r = r;
	}
	/**
	 * This method sets the column of this spot
	 * @param c, the new column of the spot
	 */
	public void setY(int c) {
		this.c = c;
	}
}
