package net.fe.builderStage;

import java.util.function.IntUnaryOperator;

/**
 * Represents a maximum amount of funds and exp avaliable to a team
 */
public final class TeamBuilderResources {
	/** Max money usable to buy weapons */
	public final int funds;
	/** Max exp usable to buy levels */
	public final int exp;
	
	public TeamBuilderResources(int funds, int exp) {
		this.funds = funds;
		this.exp = exp;
	}
	
	/**
	 * Create and return a new TeamBuilderResources with the new amount of funds and the same exp
	 */
	public TeamBuilderResources copyWithNewFunds(int newFunds) {
		return new TeamBuilderResources(newFunds, this.exp);
	}
	
	public TeamBuilderResources copyWithNewExp(int newExp) {
		return new TeamBuilderResources(this.funds, newExp);
	}
	
	/**
	 * Create and return a new TeamBuilderResources with the new amount of funds and the same exp
	 */
	public TeamBuilderResources copyWithNewFunds(IntUnaryOperator newFunds) {
		return new TeamBuilderResources(newFunds.applyAsInt(this.funds), this.exp);
	}
	
	public TeamBuilderResources copyWithNewExp(IntUnaryOperator newExp) {
		return new TeamBuilderResources(this.funds, newExp.applyAsInt(this.exp));
	}
}
