/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package hanto.studentctnguyendinh.common;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.HashMap;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * HantoGameState is an interface for providing state of a HantoGame during
 * playing.
 * 
 * @author Cuong Nguyen
 * @version April 6, 2016
 */
public class HantoGameState {

	private HantoBoard board = new HantoBoard();

	private HantoPlayerColor currentPlayer;

	private int moveCount = 0;
	private boolean gameOver = false;

	private HantoPlayerState bluePlayerState;
	private HantoPlayerState redPlayerState;
	private HantoPlayerState currentPlayerState;

	/**
	 * Create a game state with give first-move player and quota of the pieces.
	 * 
	 * @param movesFirst
	 *            color of the player who moves first.
	 * @param piecesQuota
	 *            quota for the pieces.
	 */
	public HantoGameState(HantoPlayerColor movesFirst, Map<HantoPieceType, Integer> piecesQuota) {
		currentPlayer = movesFirst;
		bluePlayerState = new HantoPlayerState(new HashMap<HantoPieceType, Integer>(piecesQuota));
		redPlayerState = new HantoPlayerState(new HashMap<HantoPieceType, Integer>(piecesQuota));
		currentPlayerState = movesFirst == BLUE ? bluePlayerState : redPlayerState;
	}

	/**
	 * Increase the number of moves.
	 */
	void advanceMove() {
		moveCount++;
		currentPlayer = currentPlayer == BLUE ? RED : BLUE;
		currentPlayerState = currentPlayer == BLUE ? bluePlayerState : redPlayerState;
	}

	/**
	 * Turn on the game over flag.
	 */
	void flagGameOver() {
		gameOver = true;
	}

	/**
	 * Put a piece at a specified location on the board.
	 * 
	 * @param coord
	 *            coordinate of the location of the new piece.
	 * @param piece
	 *            a new piece to be put on the board.
	 */
	void putPieceAt(HantoCoordinate coord, HantoPiece piece) {
		HantoCoordinateImpl innerCoord = new HantoCoordinateImpl(coord);
		HantoPieceType pieceType = piece.getType();

		board.putPieceAt(innerCoord, piece);

		int rem = currentPlayerState.getNumberOfRemainingPieces(pieceType);
		currentPlayerState.setNumberOfRemainingPieces(pieceType, rem - 1);
		if (piece.getType() == BUTTERFLY) {
			currentPlayerState.setButterflyCoordinate(innerCoord);
		}
	}

	/**
	 * Move a piece between two coordinates on the board.
	 * 
	 * @param from
	 *            coordinate of the hex containing the piece.
	 * @param to
	 *            destination of the piece.
	 */
	void movePiece(HantoCoordinate from, HantoCoordinate to) {
		if (getPieceAt(from).getType() == BUTTERFLY) {
			currentPlayerState.setButterflyCoordinate(to);
		}
		board.movePiece(from, to);
	}

	public String getPrintableBoard() {
		return board.getPrintableBoard();
	}

	/**
	 * Get a Hanto piece at a given coordinate.
	 * 
	 * @param coord
	 *            coordinate that needs retrieving a piece.
	 * @return a Hanto piece at the given coordinate. Null if there is none.
	 */
	public HantoPiece getPieceAt(HantoCoordinate coord) {
		return board.getPieceAt(coord);
	}

	/**
	 * @return number of played moves.
	 */
	public int getNumberOfPlayedMoves() {
		return moveCount;
	}

	/**
	 * @return a boolean indicating if game is over.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * @return get color of current player.
	 */
	public HantoPlayerColor getCurrentPlayer() {
		return currentPlayer;
	}
	

	/**
	 * @param player
	 *            of a player.
	 * @return object representing state of a player with given color. Null if
	 *         it is not placed yet.
	 */
	public HantoPlayerState getPlayerState(HantoPlayerColor player) {
		return player == BLUE ? bluePlayerState : redPlayerState;
	}

	/**
	 * Check whether connectivity of the pieces on the board is valid.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean validateBoard() {
		return board.isContinuous();
	}

	/**
	 * @return a copy of the Hanto board contained in this game state.
	 */
	public HantoBoard cloneBoard() {
		return new HantoBoard(board);
	}

	/**
	 * HantoPlayerState is an interface for providing state of a player.
	 * 
	 * @author Cuong Nguyen
	 * @version April 6, 2016
	 *
	 */
	public class HantoPlayerState {

		private HantoCoordinate butterflyCoord = null;
		private Map<HantoPieceType, Integer> remaining = new HashMap<>();

				
		/**
		 * Create a new player state with a given color and piece quota.
		 *
		 * @param pieceQuota
		 *            quota of the pieces for each player.
		 */
		HantoPlayerState(Map<HantoPieceType, Integer> pieceQuota) {
			remaining = pieceQuota;
		}

		/**
		 * @return coordinate of the butterfly of this player.
		 */
		public HantoCoordinate getButterflyCoordinate() {
			return butterflyCoord;
		}

		private void setButterflyCoordinate(HantoCoordinate coord) {
			butterflyCoord = coord;
		}
		
		/**
		 * @param pieceType
		 *            type of piece in question.
		 * @return number of remaining pieces of the given type.
		 */
		public int getNumberOfRemainingPieces(HantoPieceType pieceType) {
			Integer rem = remaining.get(pieceType);
			return rem == null ? 0 : rem.intValue();
		}

		/**
		 * Set the number of remaining pieces by a new value.
		 * 
		 * @param pieceType
		 *            type of the piece need to be set.
		 * @param newVal
		 *            new value for the number of the remaining piece.
		 */
		private void setNumberOfRemainingPieces(HantoPieceType pieceType, int newVal) {
			remaining.put(pieceType, newVal);
		}
		
		/**
		 * @return total number of remaining pieces.
		 */
		public int getTotalOfRemainingPieces() {
			int total = 0;
			for (Integer i : remaining.values()) {
				total += i;
			}
			return total;
		}
		
	}
}
