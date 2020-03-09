package pieces;

import java.util.ArrayList;

import board.Spot;

/**
 * 
 * @author shlomi
 * Class that represents a Rook chess piece
 */
public class Rook extends Piece {
	
	/**
	 * Constructor method for Rook
	 * @param black, true if piece is black, false if piece is white
	 */
	public Rook(boolean black) {
		super(black);
		String piece_name = (black) ? "bR" : "wR";
		setPieceName(piece_name);
	}

	@Override
	public ArrayList<Spot> moves(Spot[][] board, Spot start) {
		ArrayList<Spot> possible_moves = new ArrayList<Spot>();
		int r = start.getR();
		int c = start.getC();
		
		//up
		int _r = r-1;
		while (_r >= 0) {
			if (!board[_r][c].hasPiece()) {
				possible_moves.add(new Spot(null, _r, c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[_r][c].getPiece().isBlack())
					possible_moves.add(new Spot(null, _r, c));
				//you can't go through a piece, so stop searching for spots up
				break;
			}
			_r--;
		}
		
		//down
		_r = r + 1;
		while (_r <= 7) {
			if (!board[_r][c].hasPiece()) {
				possible_moves.add(new Spot(null, _r, c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[_r][c].getPiece().isBlack())
					possible_moves.add(new Spot(null, _r, c));
				//you can't go through a piece, so stop searching for spots down
				break;
			}
			_r++;
		}
		
		//left
		int _c = c - 1;
		while (_c >= 0) {
			if (!board[r][_c].hasPiece()) {
				possible_moves.add(new Spot(null, r, _c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[r][_c].getPiece().isBlack())
					possible_moves.add(new Spot(null, r, _c));
				//you can't go through a piece, so stop searching for spots down
				break;
			}
			_c--;
		}
		
		//right
		_c = c + 1;
		while (_c <= 7) {
			if (!board[r][_c].hasPiece()) {
				possible_moves.add(new Spot(null, r, _c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[r][_c].getPiece().isBlack())
					possible_moves.add(new Spot(null, r, _c));
				//you can't go through a piece, so stop searching for spots down
				break;
			}
			_c++;
		}
		
		return possible_moves;
	}
}

