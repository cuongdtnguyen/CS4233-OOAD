/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package hanto.studentctnguyendinh.gamma;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.SPARROW;

import java.util.HashMap;
import java.util.Map;

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentctnguyendinh.common.HantoGameBase;
import hanto.studentctnguyendinh.common.HantoGameState;
import hanto.studentctnguyendinh.common.piece.HantoMovementRule;
import hanto.studentctnguyendinh.common.piece.HantoPieceImpl;
import hanto.studentctnguyendinh.common.piece.MVWalking;
import hanto.studentctnguyendinh.common.rule.HantoRule;
import hanto.studentctnguyendinh.common.rule.HantoRuleAdjacentSameColor;
import hanto.studentctnguyendinh.common.rule.HantoRuleButterflyInFourMoves;
import hanto.studentctnguyendinh.common.rule.HantoRuleMoveBeforeButterfly;
import hanto.studentctnguyendinh.common.rule.HantoRuleNotAdjacent;
import hanto.studentctnguyendinh.common.rule.HantoRuleOccupiedHex;
import hanto.studentctnguyendinh.common.rule.HantoRuleValidatorImpl;

/**
 * A concrete implementation of the Gamma version of the Hanto game.
 * 
 * @author Cuong Nguyen
 * @version April 7, 2016
 */
public class GammaHantoGame extends HantoGameBase {

	/**
	 * Construct a GammaHantoGame instance with the player who moves first being
	 * specified.
	 * 
	 * @param movesFirst
	 *            color of the player who moves first
	 */
	public GammaHantoGame(HantoPlayerColor movesFirst) {
		maxNumberOfMove = 40;

		HantoRule[] rules = { new HantoRuleMoveBeforeButterfly(), new HantoRuleOccupiedHex(),
				new HantoRuleNotAdjacent(), new HantoRuleButterflyInFourMoves(), new HantoRuleAdjacentSameColor() };

		ruleValidator = new HantoRuleValidatorImpl(rules);

		Map<HantoPieceType, Integer> gammaPiecesQuota = new HashMap<>();
		gammaPiecesQuota.put(BUTTERFLY, 1);
		gammaPiecesQuota.put(SPARROW, 5);

		gameState = new HantoGameState(movesFirst, gammaPiecesQuota);
	}

	@Override
	public HantoPiece makeHantoPiece(HantoPlayerColor color, HantoPieceType pieceType) {
		HantoMovementRule validator;
		switch (pieceType) {
		case BUTTERFLY:
		case SPARROW:
			validator = new MVWalking(1);
			break;
		default:
			validator = null;
		}

		return new HantoPieceImpl(color, pieceType, validator);
	}
}
