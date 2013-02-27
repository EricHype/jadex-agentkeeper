package jadex.agentkeeper.worldmodel.structure;

import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.agentkeeper.worldmodel.enums.WalkType;


public abstract class SolidInfo extends TileInfo
{

	protected MapType[]	neighbours	= new MapType[]{mapType};

	protected boolean					isBreakable	= false;


	public SolidInfo(MapType mapType)
	{
		super(mapType);
	}


	public MapType[] getNeighbors()
	{
		return neighbours;
	}

	/**
	 * @return the isBreakable
	 */
	public boolean isBreakable()
	{
		return isBreakable;
	}

	/**
	 * @param isBreakable the isBreakable to set
	 */
	public void setBreakable(boolean isBreakable)
	{
		this.isBreakable = isBreakable;
	}

}