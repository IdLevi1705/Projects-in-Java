package pieces;

import java.util.ArrayList;

import board.Spot;

/**
 * 
 * Class that represents a Bishop chess piece
 */
public class Bishop extends Piece {
	
	/**
	 * Constructor method for Bishop
	 * @param black, true if piece black, false if piece white
	 */
	public Bishop(boolean black) {
		super(black);
		String piece_name = (black) ? "bB" : "wB";
		setPieceName(piece_name);
	}

	@Override
	public ArrayList<Spot> moves(Spot[][] board, Spot start) {
		ArrayList<Spot> possible_moves = new ArrayList<Spot>();
		int r = start.getR();
		int c = start.getC();
		
		//NW
		int _r = r - 1;
		int _c = c - 1;
		while (_r >= 0 && _c >= 0) {
			if (!board[_r][_c].hasPiece()) {
				possible_moves.add(new Spot(null, _r, _c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[_r][_c].getPiece().isBlack())
					possible_moves.add(new Spot(null, _r, _c));
				//you can't go through a piece, so stop searching for spots
				break;
			}
			_r--;
			_c--;
		}
		
		//NE
		_r = r - 1;
		_c = c + 1;
		while (_r >= 0 && _c <= 7) {
			if (!board[_r][_c].hasPiece()) {
				possible_moves.add(new Spot(null, _r, _c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[_r][_c].getPiece().isBlack())
					possible_moves.add(new Spot(null, _r, _c));
				//you can't go through a piece, so stop searching for spots
				break;
			}
			_r--;
			_c++;
		}
		
		//SW
		_r = r + 1;
		_c = c - 1;
		while (_r <= 7 && _c >= 0) {
			if (!board[_r][_c].hasPiece()) {
				possible_moves.add(new Spot(null, _r, _c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[_r][_c].getPiece().isBlack())
					possible_moves.add(new Spot(null, _r, _c));
				//you can't go through a piece, so stop searching for spots
				break;
			}
			_r++;
			_c--;
		}
		
		//SE
		_r = r + 1;
		_c = c + 1;
		while (_r <= 7 && _c <= 7) {
			if (!board[_r][_c].hasPiece()) {
				possible_moves.add(new Spot(null, _r, _c));
			} else {
				//there is a piece there, only add if colors are opposite (i.e. you can kill the piece)
				if (isBlack() != board[_r][_c].getPiece().isBlack())
					possible_moves.add(new Spot(null, _r, _c));
				//you can't go through a piece, so stop searching for spots
				break;
			}
			_r++;
			_c++;
		}
		
		return possible_moves;
	}
}
