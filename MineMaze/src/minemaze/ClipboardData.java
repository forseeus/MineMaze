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

//Class for handling cut/copy/paste to the clipboard.
public class ClipboardData {
	//All clipboard data should contain this GUID.
	final static private String ClipboardGUID = "1afe386c-c2d4-4794-9332-c89c8d053af2";
	public int rows;
	public int cols;
	//first dimension is rows, second dimension is cols
	public int[][] blocks;
	public boolean[][] selection;
	
	//Convert a ClipboardData object into a String.
	//The String can then be put onto the clipboard.
	@Override
	public String toString() {
		//We will store the clipboard data as JSON, but since its a fixed format
		//We won't go throught the trouble of instantiating a full JSON parser
		//like GSON.
		StringBuilder S = new StringBuilder(2*rows*cols+ClipboardGUID.length());
		S.append("{\r\n\"GUID\":\"");
		S.append(ClipboardGUID);
		S.append("\",\r\n\"rows\":");
		S.append(Integer.toString(rows));
		S.append(",\r\n\"cols\":");
		S.append(Integer.toString(cols));
		S.append(",\r\n\"blocks\":[");
		//Add block array
		if(blocks != null) {
			for(int row=0;row<rows;row++) {
				S.append("\r\n[");
				for(int col=0;col<cols;col++) {				
					S.append(Integer.toString(blocks[row][col]));
					if(col < cols - 1)S.append(",");
				}
				S.append("]");
				if(row < rows - 1)S.append(",");
			}
		}
		S.append("],\r\n\"selection\":[");
		//Add selection mask
		if(selection != null) {
			for(int row=0;row<rows;row++) {
				S.append("\r\n[");
				for(int col=0;col<cols;col++) {
					S.append(Integer.toString(selection[row][col]?1:0));
					if(col < cols - 1)S.append(",");
				}
				S.append("]");
				if(row < rows - 1)S.append(",");
			}
		}
		S.append("\r\n]}");
		return S.toString();
	}
	
	//Parse a String representation of a ClipboardData object (typically
	//generated via ClipboardData.toString()) and turn it into a real
	//ClipboardData object.
	public static ClipboardData fromString(String S) {
		if(S == null)return null;
		if(S.length() == 0)return null;
		int i=0;
		int j=0;
		int rows;
		int cols;
		int[][] blocks;
		boolean[][] selection;
		ClipboardData D = new ClipboardData();
		//Remove whitespace.
		StringBuilder B = new StringBuilder(S.length());
		for(int k=0;k<S.length();k++) {
			char c = S.charAt(k);
			if(!Character.isWhitespace(c))B.append(c);
		}
		S = B.toString();
		//Look for GUID and rows name
		String tok = "{\"GUID\":\"" + ClipboardGUID + "\",\"rows\":";
		j = tok.length();
		if(S.length() < j )return null;		
		if(!S.substring(i,j).equals(tok))return null;
		//rows value
		i = j;
		while(j < S.length() && S.charAt(j) != ',')j++;
		if(j >= S.length())return null;
		tok = S.substring(i,j);
		rows = Integer.parseInt(tok);
		//cols name
		i=++j;
		while(j < S.length() && S.charAt(j) != ':')j++;
		if(j >= S.length())return null;
		tok = S.substring(i,j);
		if(!tok.equals("\"cols\""))return null;		
		//cols value
		i=++j;
		while(j < S.length() && S.charAt(j) != ',')j++;
		if(j >= S.length())return null;
		tok = S.substring(i,j);
		cols = Integer.parseInt(tok);		
		//blocks name
		i=++j;
		while(j < S.length() && S.charAt(j) != ':')j++;
		if(j >= S.length())return null;
		tok = S.substring(i,j);
		if(!tok.equals("\"blocks\""))return null;
		
		//Make blocks array
		if(rows > 128 || cols > 128)return null;//don't allow larger than 128 x 128
		blocks = new int[rows][cols];
		selection = new boolean[rows][cols];
		
		//Outer Array Start
		j++;
		if(!(j < S.length() && S.charAt(j++) == '['))return null;
		//Inner array start
		for(int row=0;row<rows;row++) {
			//Inner array start
			if(!(j < S.length() && S.charAt(j++) == '['))return null;
			for(int col=0;col<cols;col++) {
				i=j;
				while(j < S.length()) {
					char c = S.charAt(j);
					if(c == ',') {//Next element
						if(col < cols-1)break;
						else return null;
					}else if(c == ']') {//Inner array end
						if(col == cols-1)break;
						else return null;
					}
					j++;
				}
				if(j >= S.length())return null;
				tok = S.substring(i,j++);
				blocks[row][col]=Integer.parseInt(tok);
			}
			//Check for commas on all but last element.
			if(j >= S.length())return null;
			else if(row == rows-1) {//Outer array end
				if(S.charAt(j++) != ']')return null;
			}else {//Comma between arrays
				if(S.charAt(j++) != ',')return null;
			}
		}
		//Check for comma
		if(!(j < S.length() && S.charAt(j++) == ','))return null;
		//selection name
		i=j;
		while(j < S.length() && S.charAt(j) != ':')j++;
		if(j >= S.length())return null;
		tok = S.substring(i,j);
		if(!tok.equals("\"selection\""))return null;
		
		//Outer Array Start
		j++;
		if(!(j < S.length() && S.charAt(j++) == '['))return null;
		//Inner array start
		for(int row=0;row<rows;row++) {
			//Inner array start
			if(!(j < S.length() && S.charAt(j++) == '['))return null;
			for(int col=0;col<cols;col++) {
				i=j;
				while(j < S.length()) {
					char c = S.charAt(j);
					if(c == ',') {//Next element
						if(col < cols-1)break;
						else return null;
					}else if(c == ']') {//Inner array end
						if(col == cols-1)break;
						else return null;
					}
					j++;
				}
				if(j >= S.length())return null;
				tok = S.substring(i,j++);
				//Note that we stored the selection array as 1/0 integers to save space.
				//Here we convert them back to boolean.
				selection[row][col]=tok.equals("1");
			}
			//Check for commas on all but last element.
			if(j >= S.length())return null;
			else if(row == rows-1) {//Outer array end
				if(S.charAt(j++) != ']')return null;
			}else {//Comma between arrays
				if(S.charAt(j++) != ',')return null;
			}
		}
		//Check for final end of object.
		if(!(j < S.length() && S.charAt(j++) == '}'))return null;
		D.rows = rows;
		D.cols = cols;
		D.blocks = blocks;
		D.selection = selection;
		return D;
	}
	
}
