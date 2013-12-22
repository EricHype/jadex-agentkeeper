package jadex.agentkeeper.worldmodel.structure.building;


import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.structure.BuildingInfo;


public class TreasuryInfo extends BuildingInfo
{
	
	public static final int MAX_AMOUNT = 3000;
	private int amount = 0;

	public TreasuryInfo(MapType mapType)
	{
		super(mapType);
		this.hitpoints = 30;
		// TODO: Amount may greater than MaxAmount ? o.O
//		this.amount = Math.round(((float)Math.random())*4000);
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
	public void addAmount(int amount)
	{
		this.amount+=amount;
	}
	
	public void removeAmount(int amount)
	{
		if(this.amount>=amount){
			this.amount-=amount;
		}
	}

}
