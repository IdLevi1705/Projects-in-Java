package pieces;

import java.util.ArrayList;

import board.Board;
import board.Spot;
import chess.Player;

/**
 * 
 * @author shlomi
 * Class that represents a King chess piece
 */
public class King extends Piece {
	
	/**
	 * Constructor for King
	 * @param black, whether the piece is black or not. True for black, false for white
	 */
	public King(boolean black) {
		super(black);
		String piece_name = (black) ? "bK" : "wK";
		setPieceName(piece_name);
	}

	@Override
	public ArrayList<Spot> moves(Spot[][] board, Spot start) {
		ArrayList<Spot> possible_moves = new ArrayList<Spot>();
		int r = start.getR();
		int c = start.getC();
		
		//NW
		if (r >= 1 && c >= 1 && (!board[r-1][c-1].hasPiece() || isBlack() != board[r-1][c-1].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r-1, c-1));
		
		//N
		if (r >= 1 && (!board[r-1][c].hasPiece() || isBlack() != board[r-1][c].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r-1, c));
		
		//NE
		if (r >= 1 && c <= 6 && (!board[r-1][c+1].hasPiece() || isBlack() != board[r-1][c+1].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r-1, c+1));
		
		//W
		if (c >= 1 && (!board[r][c-1].hasPiece() || isBlack() != board[r][c-1].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r, c-1));
		
		//E
		if (c <= 6 && (!board[r][c+1].hasPiece() || isBlack() != board[r][c+1].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r, c+1));
		
		//SW
		if (r <= 6 && c >= 1 && (!board[r+1][c-1].hasPiece() || isBlack() != board[r+1][c-1].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r+1, c-1));
		
		//S
		if (r <= 6 && (!board[r+1][c].hasPiece() || isBlack() != board[r+1][c].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r+1, c));
		
		//SE
		if (r <= 6 && c <= 6 && (!board[r+1][c+1].hasPiece() || isBlack() != board[r+1][c+1].getPiece().isBlack()))
			possible_moves.add(new Spot(null, r+1, c+1));
		
		
		return possible_moves;
	}	

	
	/**
	 * This method is for testing if a king is checked
	 * @param b, the board object the chess game is played on
	 * @param king, the Spot (position) of the king
	 * @return, returns true if checked, false if not
	 */
	public boolean isChecked(Board b, Spot king) {
		Spot target_king = king;
		Spot[][] board = b.getBoard();
		ArrayList<Spot> enemies = new ArrayList<Spot>();
		
		//get ally and enemy positions
		for (int r = 0; r <= 7; r++) {
			for (int c = 0; c <= 7; c++) {
				if (!board[r][c].hasPiece())
					continue;
				if (isBlack() != board[r][c].getPiece().isBlack()) {
					enemies.add(new Spot(board[r][c].getPiece(), r, c));
				}
			}
		}
		
		int numThreats = 0;
		for (Spot enemy : enemies) {
			if (enemy.getPiece().moves(board, enemy).contains(target_king)) {
				numThreats++;
			}
		}
		
		return numThreats > 0;
	}
	
	/**
	 * This method is for testing whether a king is in check mate
	 * @param b, the board object the chess game is played on
	 * @param king, the Spot (position) of the king
	 * @return, returns true if in check mate, false if not
	 */
	public boolean isCheckedMate(Board b, Spot king) {
		Spot target_king = king;
		Spot[][] board = b.getBoard();
		ArrayList<Spot> allies = new ArrayList<Spot>();
		ArrayList<Spot> enemies = new ArrayList<Spot>();
		
		//get ally and enemy positions
		for (int r = 0; r <= 7; r++) {
			for (int c = 0; c <= 7; c++) {
				if (!board[r][c].hasPiece())
					continue;
				if (isBlack() != board[r][c].getPiece().isBlack()) {
					enemies.add(new Spot(board[r][c].getPiece(), r, c));
				} else {
					if (board[r][c] != target_king) //dont include king as ally
						allies.add(new Spot(board[r][c].getPiece(), r, c));
				}
			}
		}

		//1. check if the king can move somewhere
		ArrayList<Spot> king_moves = target_king.getPiece().moves(board, target_king);

		for (Spot potential_king_move : king_moves) {
			boolean hit = false;
			
			/* move king to potential spot (so that the enemies can see if 
			 * they can reach the king at this potential new spot */
			Piece temp = board[potential_king_move.getR()][potential_king_move.getC()].getPiece();
			b.movePiece(target_king, potential_king_move);
			enemies.remove(potential_king_move);
			
			for (Spot enemy : enemies) {
				if (enemy.getPiece().moves(board, enemy).contains(potential_king_move)) {
					hit = true;
					break;
				}
			}
			
			//move back the piece to original board
			b.movePiece(potential_king_move, target_king);
			potential_king_move.setPiece(temp);
			target_king.getPiece().decMoves();
			target_king.getPiece().decMoves();
			//only add back to enemy list if there was an enemy there
			if (temp != null) enemies.add(potential_king_move);
			
			if (hit == false)
				//the king has a valid place he can move without getting hit by any enemy
				return false; 
		}
		
		//2. check if there are 0, or more than 1 threat
		int numThreats = 0;
		for (Spot enemy : enemies) {
			if (enemy.getPiece().moves(board, enemy).contains(target_king)) {
				numThreats++;
			}
		}
		if (numThreats == 0)
			return false; //not check mated
		if (numThreats >= 2)
			return true; //more than 1 enemy can kill king, check mate
		
		//3. if there is only 1 threat, can an ally kill it?
		for (Spot enemy : enemies) {
			if (enemy.getPiece().moves(board, enemy).contains(target_king)) {
				boolean threat = true;
				for (Spot ally : allies) {
					if (ally.getPiece().moves(board, ally).contains(enemy)) {
						threat = false;
						break;
					}
				}
				if (threat == false)
					//an ally could have killed the threat piece
					return false;
			}
		}
		
		//4. if there is only 1 threat, can an ally block the path to the king?
		for (Spot enemy : enemies) {
			if (enemy.getPiece().moves(board, enemy).contains(target_king)) {
				if (!(enemy.getPiece() instanceof Knight)) {
					int delta_r = target_king.getR() - enemy.getR();
					int delta_c = target_king.getC() - enemy.getC();
					int r1 = enemy.getR();
					int c1 = enemy.getC();
					ArrayList<Spot> path = new ArrayList<Spot>();
					
					do {
						if (delta_r != 0)
							delta_r = (delta_r > 0) ? delta_r - 1 : delta_r + 1;
						
						if (delta_c != 0)
							delta_c = (delta_c > 0) ? delta_c - 1 : delta_c + 1;
						
						if (delta_r == 0 && delta_c == 0)
							break;
						
						path.add(new Spot(null, r1 + delta_r, c1 + delta_c));
					} while(delta_r != 0 || delta_c != 0);
					
					for (Spot ally : allies) {
						ArrayList<Spot> moves = ally.getPiece().moves(board, ally);
						for (Spot p : path) {
							if (moves.contains(p)) {
								return false; //not checkmate, we can block the path
							}
						}
					}
					
				} else {
					//cant block a knights path, check mate
					return true;
				}
			}
		}
		
		//if we couldnt pass any of these, we are checkmated
		return true;
	}

}
