/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentctnguyendinh;

import static hanto.common.HantoPieceType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.studentctnguyendinh.common.rule.*;
import hanto.studentctnguyendinh.alpha.AlphaHantoGame;
import hanto.studentctnguyendinh.beta.BetaHantoGame;
import hanto.studentctnguyendinh.gamma.GammaHantoGame;

/**
 * This is a singleton class that provides a factory to create an instance of any version
 * of a Hanto game.
 * 
 * @author gpollice
 * @version Feb 5, 2013
 */
public class HantoGameFactory
{
	private static final HantoGameFactory instance = new HantoGameFactory();
	
	/**
	 * Default private descriptor.
	 */
	private HantoGameFactory()
	{
		// Empty, but the private constructor is necessary for the singleton.
	}

	/**
	 * @return the instance
	 */
	public static HantoGameFactory getInstance()
	{
		return instance;
	}
	
	/**
	 * Create the specified Hanto game version with the Blue player moving
	 * first.
	 * @param gameId the version desired.
	 * @return the game instance
	 */
	public HantoGame makeHantoGame(HantoGameID gameId)
	{
		return makeHantoGame(gameId, HantoPlayerColor.BLUE);
	}
	
	/**
	 * Factory method that returns the appropriately configured Hanto game.
	 * @param gameId the version desired.
	 * @param movesFirst the player color that moves first
	 * @return the game instance
	 */
	public HantoGame makeHantoGame(HantoGameID gameId, HantoPlayerColor movesFirst) {
		HantoGame game = null;
		switch (gameId) {
			case ALPHA_HANTO:
				game = new AlphaHantoGame();
				break;
			case BETA_HANTO:
				game = new BetaHantoGame(movesFirst);
				break;
			case GAMMA_HANTO:
				game = makeGammaHantoGame(movesFirst); 
				break;
		}
		return game;
	}
	
	private HantoGame makeGammaHantoGame(HantoPlayerColor movesFirst) {
		HantoRule[] rules = {
				new HantoRuleGameOver(), 
				new HantoRuleFirstMoveAtOrigin(),
				new HantoRuleInputConsistency(),
				new HantoRuleMoveBeforeButterfly(),
				new HantoRuleOccupiedHex(),
				new HantoRuleNotAdjacent(),
				new HantoRulePiecesQuota(),
				new HantoRuleButterflyInFourMoves(),
				new HantoRuleAdjacentSameColor(),
				new HantoRuleContinuousMove()
		};
		
		HantoEndRule[] endRules = {
				new HantoEndRuleButterflyIsSurrounded(),
				new HantoEndRuleMaxNumberOfMoves()
		};
		HantoRuleValidator gammaRuleValidator = new HantoRuleValidatorImpl(
				new ArrayList<HantoRule>(Arrays.asList(rules)),
				new ArrayList<HantoEndRule>(Arrays.asList(endRules)));
		
		Map<HantoPieceType, Integer> gammaPiecesQuota = new HashMap<>();
		gammaPiecesQuota.put(BUTTERFLY, 1);
		gammaPiecesQuota.put(SPARROW, 5);
		
		return new GammaHantoGame(movesFirst, gammaRuleValidator, gammaPiecesQuota);
	}
}
