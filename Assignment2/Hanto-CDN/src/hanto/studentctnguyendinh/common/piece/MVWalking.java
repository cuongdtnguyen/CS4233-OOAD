/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package hanto.studentctnguyendinh.common.piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import hanto.common.HantoCoordinate;
import hanto.common.HantoPiece;
import hanto.studentctnguyendinh.common.HantoBoard;
import hanto.studentctnguyendinh.common.HantoCoordinateImpl;
import hanto.studentctnguyendinh.common.HantoGameState;

/**
 * This rule ensures that the piece only walks one hex.
 * 
 * @author Cuong Nguyen
 * @version April 7, 2016
 */
public class MVWalking implements HantoMovementRule {

	private int maxSteps = Integer.MAX_VALUE;

	/**
	 * Default constructor.
	 */
	public MVWalking() {
	}

	/**
	 * Constructor that allows specifying maximum number of steps in a walk.
	 * @param maxSteps maximum number of steps for walking.
	 */
	public MVWalking(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	@Override
	public String validate(HantoGameState gameState, HantoCoordinate from, HantoCoordinate to) {
		ArrayList<HantoCoordinate> reachable = getReachableCoordinates(gameState, from);
		if (!reachable.contains(new HantoCoordinateImpl(to))) {
			return "Invalid walk";
		}
		return null;
	}

	/**
	 * Get a set of coordinates that can be reached from a given coordinate by walking.
	 * @param gameState current state of the game.
	 * @param from coordinate to be checked.
	 * @return a set of reachable coordinate using walking.
	 */
	public ArrayList<HantoCoordinate> getReachableCoordinates(HantoGameState gameState, HantoCoordinate from) {
		HantoCoordinateImpl fromCoord = new HantoCoordinateImpl(from);
		ArrayList<HantoCoordinate> reachable = new ArrayList<>();
		LinkedList<HantoCoordinateImpl> queue = new LinkedList<>();
		Map<HantoCoordinateImpl, Boolean> checked = new HashMap<>();

		HantoBoard board = prepareScratchBoard(gameState, fromCoord);

		checked.put(fromCoord, true);
		queue.push(fromCoord);
		while (!queue.isEmpty()) {
			HantoCoordinateImpl cur = queue.pop();
			int distance = board.getDataAt(cur);
			if (distance > 0) {
				reachable.add(cur);
			}
			if (distance < maxSteps) {
				HantoCoordinateImpl[] adj = cur.getAdjacentCoordsSet();
				for (HantoCoordinateImpl coord : adj) {
					if (checked.get(coord) == null 
							&& goodToPlace(board, board.getNumberOfPartition(), cur, coord)) {

						board.setDataAt(coord, distance + 1);
						checked.put(coord, true);
						queue.push(coord);
					}
				}
			}
		}
		return reachable;
	}

	private HantoBoard prepareScratchBoard(HantoGameState gameState, HantoCoordinateImpl from) {
		HantoBoard board = gameState.cloneBoard();
		board.removePieceAt(from);
		board.setDataAt(from, new Integer(0));
		return board;
	}

	private boolean goodToPlace(HantoBoard board, int partitions, HantoCoordinateImpl from, HantoCoordinateImpl to) {
		return isNotBlocked(board, from, to) && isAdjacentToOthers(board, to) && isNotOccupied(board, to)
				&& isContinuous(board, partitions, to);
	}

	private boolean isAdjacentToOthers(HantoBoard board, HantoCoordinateImpl coord) {
		HantoCoordinateImpl[] adj = coord.getAdjacentCoordsSet();
		for (HantoCoordinateImpl c : adj) {
			if (board.getPieceAt(c) != null) {
				return true;
			}
		}
		return false;
	}

	private boolean isNotOccupied(HantoBoard board, HantoCoordinateImpl coord) {
		return board.getPieceAt(coord) == null;
	}

	private boolean isContinuous(HantoBoard board, int partitions, HantoCoordinateImpl coord) {
		HantoCoordinateImpl[] adj = coord.getAdjacentCoordsSet();
		boolean isContinuous = true;
		if (partitions > 1) {
			isContinuous = false;
			int parts = 0;
			for (HantoCoordinateImpl c : adj) {
				int p = board.getPartitionAt(c);
				if (p != 0) {
					if (parts == 0) {
						parts = p;
					} else {
						if (parts != p) {
							isContinuous = true;
							break;
						}
					}
				}
			}
		}
		if (!isContinuous) {
			return false;
		}

		return true;
	}

	private boolean isNotBlocked(HantoBoard board, HantoCoordinate from, HantoCoordinate to) {
		int normX = to.getX() - from.getX();
		int normY = to.getY() - from.getY();
		HantoCoordinateImpl coadjCoord1 = new HantoCoordinateImpl(from.getX() + normX + normY, from.getY() - normX);
		HantoCoordinateImpl coadjCoord2 = new HantoCoordinateImpl(from.getX() - normY, from.getY() + normX + normY);

		HantoPiece coadjPiece1 = board.getPieceAt(coadjCoord1);
		HantoPiece coadjPiece2 = board.getPieceAt(coadjCoord2);

		return coadjPiece1 == null || coadjPiece2 == null;
	}
}
