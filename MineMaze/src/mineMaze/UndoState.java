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

//A simple linked list class for undo/redo.
//Undo/redo in MineMaze is implemented by taking snapshots of the application
//state at various points and saving them into an UndoState object.
public class UndoState {
	public Maze maze;	
	public UndoState prev;
	public UndoState next;
}
