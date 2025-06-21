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
package mineMaze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

//A class that displays a grid of squares and selection data.
//The commands for interacting with the grid are defined at the application
//level using mouse events, keyboard shortcuts, and menu commands.
public class MazeView extends Canvas{
	private Object lockObj = new Object();
	private Color selectColor = new Color(0,0,0,0.3);
	private Paint backPaint = Color.ALICEBLUE;
	private Paint blockPaint = Color.BROWN;
	private Paint gridStroke = Color.BLACK;
	private double gridWidth = 2;
	private int rows=10;//number of rows
	private int cols=10;//number of columns
	//the contents of the maze
	private int[][] blocks = new int[rows][cols];
	private boolean[][] selection = new boolean[rows][cols];
	
	 public MazeView() {		 
	 }
	 public MazeView(double height, double width) {
		 this.setWidth(width);
		 this.setHeight(height);		 
	 }
	 public MazeView(double height, double width, int rows, int cols) {
		 this.setWidth(width);
		 this.setHeight(height);
		 this.rows =  rows;
		 this.cols = cols;
		 blocks = new int[rows][cols];
		 selection = new boolean[rows][cols];
	 }
	 
	 //gets the column index for a block given an x coordinate on the screen.
	 public int getColIndex(double x) {
		 synchronized(lockObj) {
			 if(cols == 0)return -1;
			 double w = getWidth();
			 double dx = w / cols;
			 int i = (int)(x / dx);
			 return i;
		 }
	 }

	 //gets the index for a block in the x dimension given
	 //an x coordinate on the screen.
	 public int getRowIndex(double y) {
		 synchronized(lockObj) {
			 if(blocks == null)return -1;
			 if(rows == 0)return -1;
			 double h = getHeight();
			 double dy = h / rows;
			 int i = (int)(y / dy);
			 return i;	
		 }
	 }
	 
	 public void setBlock(int row, int col, int val) {
		 synchronized(lockObj) {
			if(col >= 0 && row >= 0 && 
			   col < cols && row < rows
		    ) {
				blocks[row][col] = val; 
			}			
		 }
	 }
	 
	 //Sets the selection in a rectanble defined by two corners to value.
	 public void addBlockRectangle(int row1, int col1, int row2, int col2, int value) {
		 synchronized(lockObj) {
			 //make sure row1 <= row2, and if not swap them.
			 if(row2<row1) {
				 int temp = row1;
				 row1 = row2;
				 row2 = temp;
			 }
			 //make sure col1 <= col2, and if not swap them.
			 if(col2 < col1) {
				 int temp = col1;
				 col1 = col2;
				 col2 = temp;
			 }
			 if(row1 < 0)row1 = 0;
			 if(row1 > rows-1)row1 = rows-1;
			 if(row2 < 0)row2 = 0;
			 if(row2 > rows-1)row2 = rows-1;
			 if(col1 < 0)col1 = 0;
			 if(col1 > cols-1)col1 = cols-1;
			 if(col2 < 0)col2 = 0;
			 if(col2 > cols-1)col2 = cols-1;
			 for(int row = row1;row <= row2;row++) {
				 for(int col = col1;col <= col2;col++) {
					 blocks[row][col] = value;					 
				 }//next col		 
			 }//next row			 
		 }//end synchronized
	 }
	 
	 //Sets some blocks from an array, and offsets.
	 public void setBlocks(int rowOff, int colOff, int[][] newBlocks){
		 synchronized(lockObj) {			 
			if(newBlocks == null)return;
			int maxRow = Math.min(rowOff+newBlocks.length-1, rows-1);
			int minRow = Math.max(rowOff, 0);
			for(int row = minRow;row<=maxRow;row++) {
				if(newBlocks[row] != null) {
					int maxCol = Math.min(colOff+newBlocks[row].length-1, cols-1);
					int minCol = Math.max(0, colOff);
					for(int col = minCol;col<=maxCol;col++) {
						blocks[row][col] = newBlocks[row-rowOff][col-colOff];						
					}//next col
				}//end if
			}//next row			 
		 }//end lock
	 }//end setSelection
	 
	 //Gets the value of a block
	 public int getBlock(int row, int col) {
		 synchronized(lockObj) {
			if(col >= 0 && row >= 0 && 
			   col < cols && row < rows
		    ) {
				return blocks[row][col]; 
			}else {
				return -1;
			}
		 }
	 }
	 
	 //Gets a copy of all the blocks.
	 public int[][] getAllBlocks(){
		synchronized(lockObj) {
			int[][] x = new int[rows][cols];
			for(int row=0;row<rows;row++) {
				x[row] = blocks[row].clone();
			}
			return x;
		}		
	 }
	 
	 public int getRows() {
		synchronized(lockObj) {
			return rows;
		}
	 }
	 
	 public int getCols() {
		synchronized(lockObj) {
			return cols;
		}
	 }
	 
	 //Clears all blocks.
	 public void setAllBlocks(int val) {
		 synchronized(lockObj){
			 if(blocks != null) {				
				for(int row=0;row<rows;row++) {
					for(int col=0;col<cols;col++) {
						blocks[row][col] = val;
					}//next row			
				}//next col
			 }
		 }//end synchronized
	 }//end setAllBlocks
	 
	 //resizes the current mazeView in terms of rows/cols
	 //The size in pixels is adjusted to keep the same scaling on each cell
	 public void resize(int newRows, int newCols, int rowOffset, int colOffset, int fillVal) {
		 synchronized(lockObj) {
			 double rowScale = getHeight()/rows;
			 double colScale = getWidth()/cols;
			 if(newCols<1)newCols=1;
			 if(newRows<1)newRows=1;
			 int[][] newBlocks = new int[newRows][newCols];
			 boolean[][] newSelection = new boolean[newRows][newCols];
			 //First fill newBlocks with fillVal
			 for(int row=0;row<newRows;row++) {
				 for(int col=0;col<newCols;col++) {				
					newBlocks[row][col] = fillVal;
					newSelection[row][col] = false;
				}
			 }
			 int maxCol = Math.min(cols-1,newCols-colOffset-1);
			 int maxRow = Math.min(rows-1,newRows-rowOffset-1);
			 int minCol = Math.max(0, -colOffset);
			 int minRow = Math.max(0, -rowOffset);
			 for(int row = minRow;row <= maxRow;row++) {
				 for(int col = minCol;col <= maxCol;col++) {				
					newBlocks[row+rowOffset][col+colOffset]=blocks[row][col];
					newSelection[row+rowOffset][col+colOffset]=selection[row][col];
				}
			 }
			 cols = newCols;
			 rows = newRows;
			 blocks = newBlocks;
			 selection = newSelection;
			 setHeight(rows*rowScale);
			 setWidth(cols*colScale);
			 repaint();
		 }
	 }	 
	 
	 //Sets the selected state of a block.
	 public void setSelection(int row, int col, boolean val){
		 synchronized(lockObj) {
			if(col >= 0 && row >= 0 && 
			   col < cols && row < rows
		    ) {
				selection[row][col] = val; 
			}			
		 }	 
	 }

	 //modifies the selected state from an array, and offsets.
	 public void setSelection(int rowOff, int colOff, boolean[][] newSelection){
		 synchronized(lockObj) {
			if(newSelection == null)return;
			int maxRow = Math.min(rowOff+newSelection.length-1, rows-1);
			int minRow = Math.max(rowOff,0);
			for(int row = minRow;row<=maxRow;row++) {
				if(newSelection[row] != null) {
					int maxCol = Math.min(colOff+newSelection[row].length-1, cols-1);
					int minCol = Math.max(colOff,0);
					for(int col = minCol;col<=maxCol;col++) {						
						selection[row][col] = newSelection[row-rowOff][col-colOff];
					}//next col
				}//end if
			}//next row			 
		 }//end lock
	 }//end setSelection

	 //Conditionally sets blocks based on a source array of blocks, and a boolean mask.
	 public void mergeBlocksMasked(int rowOff, int colOff, int[][] srcBlocks, boolean[][] srcMask){
		 synchronized(lockObj) {
			if(srcMask == null)return;
			if(srcBlocks == null)return;
			int maxRow = Math.min(srcBlocks.length-1, Math.min(rowOff+srcMask.length-1, rows-1));
			int minRow = Math.max(rowOff,0);
			for(int row = minRow;row<=maxRow;row++) {
				if(srcMask[row] != null) {
					int maxCol = Math.min(srcBlocks[row].length-1,Math.min(colOff+srcMask[row].length-1, cols-1));
					int minCol = Math.max(colOff,0);
					for(int col = minCol;col<=maxCol;col++) {
						if(srcMask[row-rowOff][col-colOff]) {
							blocks[row][col] = srcBlocks[row-rowOff][col-colOff];
						}
					}//next col
				}//end if
			}//next row			 
		 }//end lock
	 }//end setSelection
	 
	 //Clears all the blocks specified by a boolean mask and a row, col offset.
	 public void clearMask(int rowOff, int colOff, boolean[][] srcMask){
		 synchronized(lockObj) {
			if(srcMask == null)return;
			int maxRow = Math.min(rowOff+srcMask.length-1, rows-1);
			int minRow = Math.max(rowOff,0);
			for(int row = minRow;row<=maxRow;row++) {
				if(srcMask[row] != null) {
					int maxCol = Math.min(colOff+srcMask[row].length-1, cols-1);
					int minCol = Math.max(colOff,0);
					for(int col = minCol;col<=maxCol;col++) {
						if(srcMask[row-rowOff][col-colOff])blocks[row][col] = 0;
					}//next col
				}//end if
			}//next row			 
		 }//end lock
	 }//end setSelection
	 
	 /*
	 //Sets a selection rectangle to the specified value.
	 public void setSelectionRectangle(int row1, int col1, int row2, int col2, boolean value) {
		 synchronized(lockObj) {
			 //make sure row1 <= row2, and if not swap them.
			 if(row2<row1) {
				 int temp = row1;
				 row1 = row2;
				 row2 = temp;
			 }
			 //make sure col1 <= col2, and if not swap them.
			 if(col2 < col1) {
				 int temp = col1;
				 col1 = col2;
				 col2 = temp;
			 }
			 for(int row = 0;row <= rows;row++) {
				 for(int col = 0;col <= cols;col++) {
					 if(row>=row1 && row<=row2 && col>=col1 && col<=col2)selection[row][col] = value;				 
				 }//next col		 
			 }//next row			 
		 }//end synchronized
	 }*/

	 //Sets the selection in a rectanble defined by two corners to value.
	 public void addSelectionRectangle(int row1, int col1, int row2, int col2, boolean value) {
		 synchronized(lockObj) {
			 //make sure row1 <= row2, and if not swap them.
			 if(row2<row1) {
				 int temp = row1;
				 row1 = row2;
				 row2 = temp;
			 }
			 //make sure col1 <= col2, and if not swap them.
			 if(col2 < col1) {
				 int temp = col1;
				 col1 = col2;
				 col2 = temp;
			 }
			 if(row1 < 0)row1 = 0;
			 if(row1 > rows-1)row1 = rows-1;
			 if(row2 < 0)row2 = 0;
			 if(row2 > rows-1)row2 = rows-1;
			 if(col1 < 0)col1 = 0;
			 if(col1 > cols-1)col1 = cols-1;
			 if(col2 < 0)col2 = 0;
			 if(col2 > cols-1)col2 = cols-1;
			 for(int row = row1;row <= row2;row++) {
				 for(int col = col1;col <= col2;col++) {
					 selection[row][col] = value;					 
				 }//next col		 
			 }//next row			 
		 }//end synchronized
	 }
	 
	 //Gets the selection state of a block.
	 public boolean getSelection(int row, int col) {
		 synchronized(lockObj) {
			if(col >= 0 && row >= 0 && 
			   col < cols && row < rows
		    ) {
				return selection[row][col]; 
			}else {
				return false;
			}
		 }
	 }
	 
	 //Gets a copy of all the block selection data.
	 public boolean[][] getSelection(){
		synchronized(lockObj) {
			boolean[][] x = new boolean[rows][cols];
			for(int row=0;row<rows;row++) {
				x[row] = selection[row].clone();
			}
			return x;
		}		
	 }
	 
	 //Gets a copy of all the block selection data.
	 public void selectAll(){
		synchronized(lockObj) {
			for(int row=0;row<rows;row++) {
				for(int col=0;col<cols;col++)
					selection[row][col]=true;
			}
		}	
	 }

	 //Gets a copy of all the block selection data.
	 public void clearSelection(){
		synchronized(lockObj) {
			for(int row=0;row<rows;row++) {
				for(int col=0;col<cols;col++)
					selection[row][col]=false;
			}
		}
	 }
	 
	 //https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/GraphicsContext.html
	public void repaint() {
		synchronized(lockObj){			
			GraphicsContext gc = this.getGraphicsContext2D();
			double h = getHeight();
			double w = getWidth();
			//fill in the background
			if(backPaint != null) {
				gc.setFill(backPaint);
				gc.fillRect(0, 0, w, h);
			}
			if(rows == 0 || cols == 0)return;
			//fill in the foreground
			//calcualte the size of the blocks in pixels
			double dx = w / cols;
			double dy = h / rows;
			//fill in the blocks			
			if(blockPaint != null) {
				for(int row=0;row<rows;row++) {
					for(int col=0;col<cols;col++){					
						if(blocks[row][col] != 0) {
							gc.setFill(blockPaint);
							gc.fillRect(col*dx, row*dy, dx, dy);					
						}//end if block exists.
						if(selection[row][col]) {
							gc.setFill(selectColor);
							gc.fillRect(col*dx, row*dy, dx, dy);	
						}//end if selected	
					}
				}
			}
			//draw the grid
			gc.setStroke(gridStroke);
			gc.setLineWidth(gridWidth);
			if(gridStroke != null) {
				//vertical lines
				for(int row = 1;row<rows;row++) {
					gc.strokeLine(0, row*dy, w, row*dy);
				}
				//horizontal lines
				for(int col = 1;col<cols;col++) {
					gc.strokeLine(col*dx,0,col*dx,h);
				}
			}		
		}//end synchronized
	}//end repaint()
	
	
	
}
