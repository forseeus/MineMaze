/*
 * Project:  Mine Maze
 * Author:   Andrew McBain
 * Created:  2025-06-21
 *
 * License:
 * To the extent possible under law, the person who associated CC0
 * with MyProject has waived all copyright and related or neighboring
 * rights to MineMaze.  Libraries use by this code including gson 2.13.1,
 * javafx, and java SDK have their own licenses which may be more restrictive.
 *
 * You should have received a copy of the CC0 legalcode along with this work.
 * If not, see <https://creativecommons.org/publicdomain/zero/1.0/legalcode>.
 */
package minemaze;

//A class for storing a snapshot of the maze data
//This is used both for undo/redo and for file-save/file-open of .maze files.
public class Maze {
	public String layerName;
	public boolean relativeXYZ;
	public boolean carveMode;
	public boolean addRoof;
	public boolean addFill;
	public boolean addFloorCover;
	public boolean addFloor;
	public String roofBlocks;
	public String fillBlocks;
	public String wallBlocks;
	public String floorCoverBlocks;
	public String floorBlocks;
	public int height;
	public int xOffset;
	public int yOffset;
	public int zOffset;
	public int rows;
	public int cols;
	//first dimension is rows, second dimension is columns
	public int[][] blocks;
	public transient boolean[][] selection;
	public transient int mazeSelectionDeltaRows;
	public transient int mazeSelectionDeltaCols;
	public transient boolean mazeSelectionDragging;	
    public transient boolean[][] mazeMouseDownSelection;
    public transient int[][] mazeMouseDownUnselectedBlocks;
    public transient int[][] mazeMouseDownSelectedBlocks;
	
}
