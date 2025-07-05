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

module mineMaze {
	exports  minemaze;
	requires transitive javafx.controls;
	requires com.google.gson;
}
