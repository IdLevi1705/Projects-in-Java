package chess;

import java.util.ArrayList;
import java.util.Scanner;

import board.Board;
import board.Spot;
import pieces.*;

public class Chess {
	
	public static void main(String[] args) {
		Board b = new Board();
		Spot[][] board = b.getBoard();
		Player[] player = new Player[2];
		player[0] = new Player(false, board[7][4]); //0 is white turn
		player[1] = new Player(true, board[0][4]); //1 is black turn
		
		Scanner scanner = new Scanner(System.in);
		int turn = 0;
		String cur = "";
		b.printBoard();
		
		String p1 = "", p2 = "", cmd = "";
		while (true) {
			player[turn % 2].printStartTurn();
			resetEnPassant(board, player[turn % 2]);
			
			cur = scanner.nextLine();
			if (cur.equalsIgnoreCase("resign")) {
				System.out.println();
				player[(turn + 1) % 2].printWinningStatement();
				return;
			}
			
			if (cur.equalsIgnoreCase("draw") && cmd.equalsIgnoreCase("draw?")) {
				System.out.println();
				System.out.print("draw");
				return;
			}
			
			int x1, y1;
			int x2, y2;
			String[] curArr = cur.split(" ");
			if (curArr.length < 1) {
				badInput();
				continue;
			}
			p1 = curArr[0];
			p2 = curArr.length > 1 ? curArr[1] : "";
			cmd = curArr.length > 2 ? curArr[2] : "";
			if (p1.length() != 2 || p2.length() != 2){
				badInput();
				continue;
			}
			y1 = colToInt(p1.charAt(0));
			x1 = rowToInt(p1.charAt(1));
			y2 = colToInt(p2.charAt(0));
			x2 = rowToInt(p2.charAt(1));

			if (outOfBounds(x1, y1, x2, y2)) {
				badInput();
				continue;
			}
			
			if (!board[x1][y1].hasPiece()) {
				badInput();
				continue;
			}
			
			if (player[turn % 2].isBlack() != board[x1][y1].getPiece().isBlack()) {
				//cant move black piece on white turn, and vice versa
				badInput();
				continue;
			}
			
			ArrayList<Spot> moves = board[x1][y1].getPiece().moves(board, board[x1][y1]);
			
			if (moves.contains(new Spot(null, x2, y2))) {
				King k1 =  (King) (player[turn % 2].getKingSpot().getPiece());
				King k2 =  (King) (player[(turn + 1) % 2].getKingSpot().getPiece());
				
				//move piece to new spot
				Piece temp = board[x2][y2].getPiece();	
				b.movePiece(board[x1][y1], board[x2][y2]);
				if (board[x1][y1].equals(player[turn%2].getKingSpot()))
					player[turn%2].setKingSpot(board[x2][y2]);
				
				if (k1.isChecked(b, player[(turn) % 2].getKingSpot())) {
					//moving this piece, caused the king to be in check, invalid move
					//move piece back and retry a new move
					b.movePiece(board[x2][y2], board[x1][y1]);
					board[x2][y2].setPiece(temp);
					board[x1][y1].getPiece().decMoves();
					board[x1][y1].getPiece().decMoves();
					if (player[turn%2].getKingSpot().equals(board[x2][y2]))
						player[turn%2].setKingSpot(board[x1][y1]);
					badInput();
					continue;
				}
				
				//set status of can die by en passant if moving pawn for first time
				setEnPassant(board[x2][y2]);
				pawnPromotion(b, board[x2][y2], cmd);
		
				//if king was moved, update position
				if (board[x1][y1].equals(player[turn % 2].getKingSpot())) {
					player[turn % 2].setKingSpot(board[x2][y2]);
				}
				
				System.out.println();
				b.printBoard();
			
				if (!k2.isChecked(b, player[(turn + 1) % 2].getKingSpot())) {
					//do nothing
				} else if (k2.isCheckedMate(b, player[(turn + 1) % 2].getKingSpot())) {
					System.out.println("Checkmate");
					System.out.println();
					player[turn % 2].printWinningStatement();
					return;
				} else {
					System.out.println("Check");
					System.out.println();
				}

			} else {
				//if we got here, that means the user might be trying to attempt to do a move
				//that requires special consideration i.e. en passant or castling
				
				//check if we are trying an en passant
				if (enPassant(b, board[x1][y1], board[x2][y2])) {
					//en passant move occured
					System.out.println();
					b.printBoard();
				} else if (castling(b, board[x1][y1], board[x2][y2], player[turn % 2])) {
					System.out.println();
					b.printBoard();
				} else {
					badInput();
					continue;
				}
				

			}
			turn++;
		}
	}

	private static boolean castling(Board b, Spot start, Spot end, Player player) {
		if (start.hasPiece() && !(start.getPiece() instanceof King))
			return false;
		
		Spot[][] board = b.getBoard();
		int r1 = start.getR();
		int c1 = start.getC();
		int r2 = end.getR();
		int c2 = end.getC();
		
		King k = (King) start.getPiece();
		
		if (k.isChecked(b, player.getKingSpot()))
			return false; //cant do castling if under check

		//this has to be the king's first move
		if (k.hasMoved() || !player.getKingSpot().equals(start))
			return false;
		
		
		//find enemies
		ArrayList<Spot> enemies = new ArrayList<Spot>();

		for (int r = 0; r <= 7; r++) {
			for (int c = 0; c <= 7; c++) {
				if (!board[r][c].hasPiece())
					continue;
				if (player.isBlack() != board[r][c].getPiece().isBlack()) {
					enemies.add(new Spot(board[r][c].getPiece(), r, c));
				}
			}
		}
		
		if (k.isBlack()) {
			//left rook
			if (r2 == 0 && c2 == 2) {
				if ((board[0][0].getPiece() instanceof Rook)) {
					Rook rook = (Rook) board[0][0].getPiece();
					if (rook.hasMoved())
						return false;
					if (board[0][1].hasPiece() || board[0][2].hasPiece() || board[0][3].hasPiece())
						return false;
					
					//make sure every no spot the king moves through is attacked
					for (Spot enemy : enemies) {
						ArrayList<Spot> enemy_moves = enemy.getPiece().moves(board, enemy);
						if (enemy_moves.contains(new Spot(null, 0, 2)) || enemy_moves.contains(new Spot(null, 0, 3))) {
							return false;
						}
					}
					
					//do the castling
					b.movePiece(start, end);
					b.movePiece(board[0][0], board[0][3]);
					player.setKingSpot(end);
					return true;
				}
			} else if (r2 == 0 && c2 == 6) { //castling w/ right rook
				if ((board[0][7].getPiece() instanceof Rook)) {
					Rook rook = (Rook) board[0][7].getPiece();
					if (rook.hasMoved())
						return false;
					if (board[0][5].hasPiece() || board[0][6].hasPiece())
						return false;
					
					//make sure every no spot the king moves through is attacked
					for (Spot enemy : enemies) {
						ArrayList<Spot> enemy_moves = enemy.getPiece().moves(board, enemy);
						if (enemy_moves.contains(new Spot(null, 0, 5)) || enemy_moves.contains(new Spot(null, 0, 6))) {
							return false;
						}
					}
					
					//do the castling
					b.movePiece(start, end);
					b.movePiece(board[0][7], board[0][5]);
					player.setKingSpot(end);
					return true;
				}
			}
		} else {
			//check if king is trying to move 2 left or 2 right
			//castling with left rook
			if (r2 == 7 && c2 == 2) {
				if ((board[7][0].getPiece() instanceof Rook)) {
					Rook rook = (Rook) board[7][0].getPiece();
					if (rook.hasMoved())
						return false;
					if (board[7][1].hasPiece() || board[7][2].hasPiece() || board[7][3].hasPiece())
						return false;
					
					//make sure every no spot the king moves through is attacked
					for (Spot enemy : enemies) {
						ArrayList<Spot> enemy_moves = enemy.getPiece().moves(board, enemy);
						if (enemy_moves.contains(new Spot(null, 7, 2)) || enemy_moves.contains(new Spot(null, 7, 3))) {
							return false;
						}
					}
					
					//do the castling
					b.movePiece(start, end);
					b.movePiece(board[7][0], board[7][3]);
					player.setKingSpot(end);
					return true;
				}
			} else if (r2 == 7 && c2 == 6) { //castling w/ right rook
				if ((board[7][7].getPiece() instanceof Rook)) {
					Rook rook = (Rook) board[7][7].getPiece();
					if (rook.hasMoved())
						return false;
					if (board[7][5].hasPiece() || board[7][6].hasPiece())
						return false;
					//make sure every no spot the king moves through is attacked
					for (Spot enemy : enemies) {
						ArrayList<Spot> enemy_moves = enemy.getPiece().moves(board, enemy);
						if (enemy_moves.contains(new Spot(null, 7, 5)) || enemy_moves.contains(new Spot(null, 7, 6))) {
							return false;
						}
					}
					
					//do the castling
					b.movePiece(start, end);
					b.movePiece(board[7][7], board[7][5]);
					player.setKingSpot(end);
					return true;
				}
			}
		}
	
		return false;
	}

	private static boolean enPassant(Board b, Spot start, Spot end) {
		Spot[][] board = b.getBoard();
		Piece p1 = start.getPiece();
		Piece p2 = end.getPiece();
		int r1 = start.getR();
		int c1 = start.getC();
		int r2 = end.getR();
		int c2 = end.getC();
		
		if (p2 != null)
			return false; //en passant final position does not have a piece there
		
		if (p1.isBlack()) {
			//check left side
			if (c1 >= 1 && r1 == 4 && (board[r1][c1-1].getPiece() instanceof Pawn)
					&& board[r1][c1-1].getPiece().isWhite()) {
				Pawn pawnToKill = (Pawn) board[r1][c1-1].getPiece();
				if (pawnToKill.canDieFromEnPassant()) {
					if (r2 == r1 + 1 && c2 == c1 - 1) {
						//do the en passant
						b.movePiece(board[r1][c1], board[r2][c2]); //move pawn diagonally
						board[r1][c1-1].setPiece(null); //kill opposing pawn to the left
						return true;
					}
				}
			}
			
			//check right side
			if (c1 <= 6 && r1 == 4 && (board[r1][c1+1].getPiece() instanceof Pawn)
					&& board[r1][c1+1].getPiece().isWhite()) {
				Pawn pawnToKill = (Pawn) board[r1][c1+1].getPiece();
				if (pawnToKill.canDieFromEnPassant()) {
					if (r2 == r1 + 1 && c2 == c1 + 1) {
						//do the en passant
						b.movePiece(board[r1][c1], board[r2][c2]); //move pawn diagonally
						board[r1][c1+1].setPiece(null); //kill opposing pawn to the right
						return true;
					}
				}
			}
		} else {
			//p1 is white piece
			//black and white pieces must be on 3rd row (in 2d array)
			//the black piece to be killed will be on the right or left of p1
			
			//check left side
			if (c1 >= 1 && r1 == 3 && (board[r1][c1-1].getPiece() instanceof Pawn)
					&& board[r1][c1-1].getPiece().isBlack()) {
				Pawn pawnToKill = (Pawn) board[r1][c1-1].getPiece();
				if (pawnToKill.canDieFromEnPassant()) {
					if (r2 == r1 - 1 && c2 == c1 - 1) {
						//do the en passant
						b.movePiece(board[r1][c1], board[r2][c2]); //move pawn diagonally
						board[r1][c1-1].setPiece(null); //kill opposing pawn to the left
						return true;
					}
				}
			}
			
			//check right side
			if (c1 <= 6 && r1 == 3 && (board[r1][c1+1].getPiece() instanceof Pawn)
					&& board[r1][c1+1].getPiece().isBlack()) {
				Pawn pawnToKill = (Pawn) board[r1][c1+1].getPiece();
				if (pawnToKill.canDieFromEnPassant()) {
					if (r2 == r1 - 1 && c2 == c1 + 1) {
						//do the en passant
						b.movePiece(board[r1][c1], board[r2][c2]); //move pawn diagonally
						board[r1][c1+1].setPiece(null); //kill opposing pawn to the right
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private static void resetEnPassant(Spot[][] board, Player player) {
		//reset whether a pawn can be killed by en passant by opposing player
		for (int r = 0; r <= 7; r++) {
			for (int c = 0; c <= 7; c++) {
				Piece p = board[r][c].getPiece();
				if (p != null && (p instanceof Pawn) && (p.isBlack() == player.isBlack())) {
					((Pawn) p).setCanDieFromEnPassant(false);
				}
			}
		}
	}

	private static void setEnPassant(Spot spot) {
		if (!(spot.getPiece() instanceof Pawn)) {
			return;
		}
		
		Pawn pawn = (Pawn) spot.getPiece();
		if (pawn.isBlack()) {
			//check if we only moved once and we did it by moving down twice
			if (pawn.getTimesMoves() == 1 && spot.getR() == 3) {
				pawn.setCanDieFromEnPassant(true);
			}
		} else {
			//check if we only moved once and we did it by moving up twice
			if (pawn.getTimesMoves() == 1 && spot.getR() == 4) {
				pawn.setCanDieFromEnPassant(true);
			}
		}

		
	}

	private static void pawnPromotion(Board b, Spot spot, String promote) {
		if (!(spot.getPiece() instanceof Pawn)) {
			return;
		}
		
		int r = spot.getR();
		int c = spot.getC();
		Spot[][] board = b.getBoard();
		
		Pawn pawn = (Pawn) spot.getPiece();
		if (pawn.isBlack()) {
			if (r != 7)
				return;
			//if the row is 0, then change the pawn to new piece
			switch (promote) {
				case "R":
					board[r][c] = new Spot(new Rook(true), r, c);
					break;
				case "N":
					board[r][c] = new Spot(new Knight(true), r, c);
					break;
				case "B":
					board[r][c] = new Spot(new Bishop(true), r, c);
					break;
				default:
					board[r][c] = new Spot(new Queen(true), r, c);
			}
		} else {
			if (r != 0)
				return;
			//if the row is 0, then change the pawn to new piece
			switch (promote) {
				case "R":
					board[r][c] = new Spot(new Rook(false), r, c);
					break;
				case "N":
					board[r][c] = new Spot(new Knight(false), r, c);
					break;
				case "B":
					board[r][c] = new Spot(new Bishop(false), r, c);
					break;
				default:
					board[r][c] = new Spot(new Queen(false), r, c);
			}
		}
		
	}

	public static void badInput() {
		System.out.println("\nIllegal move, try again\n");
	}
	
	//returns -1 on bad input
	public static int colToInt(char c) {
		if (c < 'a' || c > 'h')
			return -1;
		return (int) (c - 'a');
	}
	
	public static int rowToInt(char r) {
		return 8 - Character.digit(r, 10);
	}
	
	public static boolean outOfBounds(int x1, int y1, int x2, int y2) {
		if (x1 < 0 || x1 > 7 || x2 < 0 || x2 > 7 || y1 < 0 || y1 > 7 || y2 < 0 || y2 > 7) {
			return true;
		}
		return false;
	}
}