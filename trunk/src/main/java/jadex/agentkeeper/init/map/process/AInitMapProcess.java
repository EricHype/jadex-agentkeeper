package jadex.agentkeeper.init.map.process;

import jadex.agentkeeper.game.state.buildings.SimpleBuildingMapState;
import jadex.agentkeeper.game.state.creatures.SimpleCreatureState;
import jadex.agentkeeper.game.state.map.SimpleMapState;
import jadex.agentkeeper.game.state.missions.Auftragsverwalter;
import jadex.agentkeeper.game.state.player.SimplePlayerState;
import jadex.agentkeeper.game.userinput.UserEingabenManager;
import jadex.agentkeeper.util.ISObjStrings;
import jadex.agentkeeper.util.ISpaceStrings;
import jadex.agentkeeper.worldmodel.enums.MapType;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.SimplePropertyObject;
import jadex.extension.envsupport.environment.IEnvironmentSpace;
import jadex.extension.envsupport.environment.ISpaceProcess;
import jadex.extension.envsupport.environment.SpaceObject;
import jadex.extension.envsupport.environment.space2d.Grid2D;
import jadex.extension.envsupport.math.IVector2;
import jadex.extension.envsupport.math.Vector2Double;
import jadex.extension.envsupport.math.Vector2Int;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class AInitMapProcess extends SimplePropertyObject implements ISpaceProcess, IMap, ISObjStrings
{

	public static final String					GEBAEUDELISTE	= "gebaeudeliste";

	public static Map<String, String>			imagenames;

	public static Map<String, String>			loadMapMapping;

	public static int							monsteressverbrauch;

	public static Vector2Double					portalort;

	public SimpleCreatureState					creatureState;

	public SimpleBuildingMapState				buildingState;
	
	public SimpleMapState						mapTypeState;

	public SimplePlayerState					playerState;

	public UserEingabenManager					uem;

	public static final Map<String, MapType>	TILE_MAP		= new HashMap<String, MapType>();
	static
	{


		TILE_MAP.put("1F", MapType.HATCHERY);
		TILE_MAP.put("1G", MapType.DUNGEONHEART);
		TILE_MAP.put("1C", MapType.TREASURY);
		TILE_MAP.put("1F", MapType.HATCHERY);
		TILE_MAP.put("1D", MapType.LAIR);
		TILE_MAP.put("1E", MapType.PORTAL);
		TILE_MAP.put("1I", MapType.TRAININGROOM);
		TILE_MAP.put("1L", MapType.LIBRARY);
		TILE_MAP.put("1X", MapType.TORTURE);

		TILE_MAP.put("Ob", MapType.IMPENETRABLE_ROCK);
		TILE_MAP.put("Oc", MapType.ROCK);
		TILE_MAP.put("1B", MapType.REINFORCED_WALL);
		TILE_MAP.put("Og", MapType.GOLD);
		TILE_MAP.put("Oh", MapType.GEMS);
		TILE_MAP.put("Od", MapType.DIRT_PATH);
		TILE_MAP.put("1A", MapType.CLAIMED_PATH);
		TILE_MAP.put("Oe", MapType.WATER);
		TILE_MAP.put("Of", MapType.LAVA);
		TILE_MAP.put("Oh", MapType.HEROTILE);
		

		portalort = new Vector2Double(12, 19);
		monsteressverbrauch = 4;

		imagenames = new HashMap<String, String>();

		imagenames.put("Ob", IMPENETRABLE_ROCK);
		imagenames.put("Oc", ROCK);
		imagenames.put("1B", REINFORCED_WALL);
		imagenames.put("Og", GOLD);
		imagenames.put("Oh", GEMS);
		imagenames.put("Od", DIRT_PATH);
		imagenames.put("1A", CLAIMED_PATH);
		imagenames.put("Oe", WATER);
		imagenames.put("Of", LAVA);
		imagenames.put("Oh", HEROTILE);

		imagenames.put("1G", DUNGEONHEART);
		imagenames.put("1Z", DUNGEONHEARTCENTER);
		imagenames.put("1C", TREASURY);
		imagenames.put("1F", HATCHERY);
		imagenames.put("ZF", HATCHERYCENTER);
		imagenames.put("1D", LAIR);
		imagenames.put("1E", PORTAL);
		imagenames.put("1I", TRAININGROOM);
		imagenames.put("1L", LIBRARY);
		imagenames.put("1X", TORTURE);

		NEIGHBOR_RELATIONS.put(ROCK, ROCK_NEIGHBORS);
		NEIGHBOR_RELATIONS.put(GOLD, GOLD_NEIGHBORS);
		NEIGHBOR_RELATIONS.put(REINFORCED_WALL, REINFORCED_WALL_NEIGHBORS);
		NEIGHBOR_RELATIONS.put(IMPENETRABLE_ROCK, IMPENETRABLE_ROCK_NEIGHBORS);
		NEIGHBOR_RELATIONS.put(WATER, WATER_NEIGHBORS);
		NEIGHBOR_RELATIONS.put(LAVA, LAVA_NEIGHBORS);
		NEIGHBOR_RELATIONS.put(LAIR, BUILDING_TYPES);
		NEIGHBOR_RELATIONS.put(TRAININGROOM, BUILDING_TYPES);
		NEIGHBOR_RELATIONS.put(LIBRARY, BUILDING_TYPES);
		NEIGHBOR_RELATIONS.put(TORTURE, BUILDING_TYPES);
		NEIGHBOR_RELATIONS.put(HATCHERY, BUILDING_TYPES);
		NEIGHBOR_RELATIONS.put(TREASURY, BUILDING_TYPES);

		for(int i = 0; i < FIELD_TYPES.length; i++)
		{
			FIELD_SET.add(FIELD_TYPES[i]);
		}

		for(int i = 0; i < BUILDING_TYPES.length; i++)
		{
			BUILDING_SET.add(BUILDING_TYPES[i]);
		}

		for(int i = 0; i < BREAKABLE_FIELD.length; i++)
		{
			BREAKABLE_FIELD_TYPES.add(BREAKABLE_FIELD[i]);
		}

		for(int i = 0; i < MOVE_TYPES.length; i++)
		{
			MOVEABLES.add(MOVE_TYPES[i]);
		}

		CENTER_TYPES.put(HATCHERY, HATCHERYCENTER);
		CENTER_TYPES.put(PORTAL, PORTALCENTER);
		CENTER_TYPES.put(TRAININGROOM, TRAININGROOMCENTER);
		CENTER_TYPES.put(LIBRARY, LIBRARYCENTER);
		CENTER_TYPES.put(PORTAL, PORTALCENTER);

	}

	// -------- attributes --------

	/** The last tick. */
	protected double							lasttick;

	protected void loadAndSetupMissions(Grid2D grid)
	{
		// grid.getBorderMode();
		// Initialize the field.
		try
		{
			Auftragsverwalter auftragsverwalter = new Auftragsverwalter(grid);

			grid.setProperty("auftraege", auftragsverwalter);

			uem = new UserEingabenManager(grid);

			grid.setProperty("uem", uem);

			this.creatureState = new SimpleCreatureState();
			this.buildingState = new SimpleBuildingMapState();
			this.mapTypeState = new SimpleMapState();
			this.playerState = new SimplePlayerState(1);
			grid.setProperty(ISpaceStrings.CREATURE_STATE, this.creatureState);
			grid.setProperty(ISpaceStrings.BUILDING_STATE, this.buildingState);
			grid.setProperty(ISpaceStrings.MAPTYPE_STATE, this.mapTypeState);
			grid.setProperty(ISpaceStrings.PLAYER_STATE, this.playerState);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method will be executed by the object before the process is removed
	 * from the execution queue.
	 * 
	 * @param clock The clock.
	 * @param space The space this process is running in.
	 */
	public void shutdown(IEnvironmentSpace space)
	{
		System.out.println("- - - Init Map Process done - - -");
	}

	/**
	 * Executes the environment process
	 * 
	 * @param clock The clock.
	 * @param space The space this process is running in.
	 */
	public void execute(IClockService clock, IEnvironmentSpace space)
	{
		System.out.println("- - - Init Map Process starting - - -");
	}

	public static Vector2Int convertToIntPos(IVector2 pos)
	{
		int xrund = (int)Math.round(pos.getXAsDouble());
		int yrund = (int)Math.round(pos.getYAsDouble());
		Vector2Int temp = new Vector2Int(xrund, yrund);
		return temp;
	}

	public static Set<SpaceObject> getNeighborBlocksInRange(IVector2 ziel, int range, Grid2D grid, String types[])
	{
		if(types != null)
		{
			return grid.getNearGridObjects(ziel, range, types);
		}
		return null;

	}

	public static SpaceObject getSolidTypeAtPos(IVector2 pos, Grid2D gridext)
	{
		SpaceObject ret = null;
		ret = getFieldTypeAtPos(pos, gridext);
		if(ret == null)
		{
			ret = getBuildingTypeAtPos(pos, gridext);
		}
		return ret;
	}

	public static SpaceObject getFieldTypeAtPos(IVector2 pos, Grid2D gridext)
	{
		for(int i = 0; i < FIELD_TYPES.length; i++)
		{
			Collection sobjs = gridext.getSpaceObjectsByGridPosition(pos, FIELD_TYPES[i]);
			if(sobjs != null)
			{
				return (SpaceObject)sobjs.iterator().next();
			}

		}
		return null;
	}

	public static SpaceObject getBuildingTypeAtPos(IVector2 pos, Grid2D gridext)
	{
		for(int i = 0; i < BUILDING_TYPES.length; i++)
		{
			Collection sobjs = gridext.getSpaceObjectsByGridPosition(pos, BUILDING_TYPES[i]);
			if(sobjs != null)
			{
				return (SpaceObject)sobjs.iterator().next();
			}

		}
		return null;
	}

	public static boolean isMoveable(IVector2 pos, Grid2D gridext)
	{
		for(int i = 0; i < MOVE_TYPES.length; i++)
		{
			Collection sobjs = gridext.getSpaceObjectsByGridPosition(pos, MOVE_TYPES[i]);
			if(sobjs != null)
			{
				return true;
			}

		}
		return false;
	}

}
