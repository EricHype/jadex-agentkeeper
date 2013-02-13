package jadex.agentkeeper.worldmodel;

import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.worldmodel.enums.NeighbourType;
import jadex.agentkeeper.worldmodel.enums.WalkType;
import jadex.extension.envsupport.environment.SpaceObject;

public abstract class TileInfo {
	public static String TYPE_NAME = "Tile";
	private static int quantity;
	
	protected int hitpoints;
	protected int owner = 0;
	protected String neighbourhood;
	protected WalkType walkType;
	protected NeighbourType neighbourType;

	public TileInfo() {
		quantity++;
		this.hitpoints = 10;
		this.owner = 0;
		this.neighbourhood = "00000000";
		this.walkType = WalkType.PASSABLE;
		this.neighbourType = NeighbourType.COMPLEX;
	}

	/**
	 * @return the quantity
	 */
	public static int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public static void setQuantity(int quantity) {
		TileInfo.quantity = quantity;
	}

	/**
	 * @return the hitpoints
	 */
	public int getHitpoints() {
		return hitpoints;
	}

	/**
	 * @param hitpoints
	 *            the hitpoints to set
	 */
	public void setHitpoints(int hitpoints) {
		this.hitpoints = hitpoints;
	}

	/**
	 * @return the owner
	 */
	public int getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(int owner) {
		this.owner = owner;
	}

	/**
	 * @return the neighbourhood
	 */
	public String getNeighbourhood() {
		return neighbourhood;
	}

	/**
	 * @param neighbourhood
	 *            the neighbourhood to set
	 */
	public void setNeighbourhood(String neighbourhood) {
		this.neighbourhood = neighbourhood;
	}

	/**
	 * @return the walkType
	 */
	public WalkType getWalkType() {
		return walkType;
	}

	/**
	 * @param walkType
	 *            the walkType to set
	 */
	public void setWalkType(WalkType walkType) {
		this.walkType = walkType;
	}

	public static final <T> T getTileInfo(SpaceObject obj, Class<T> type) {
		return (T) obj.getProperty(ISObjStrings.PROPERTY_TILEINFO);
	}

	public abstract String[] getNeighbors();
}
