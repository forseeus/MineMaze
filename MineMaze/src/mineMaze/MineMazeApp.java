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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.UnaryOperator;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
//NOTE:  gson-2.13.1.jar
//https://mvnrepository.com/artifact/com.google.code.gson/gson/2.11.0
//https://www.javadoc.io/doc/com.google.code.gson/gson/2.11.0/index-all.html
import com.google.gson.*;

//Main clase for the MineMaze application.
public class MineMazeApp extends Application{
	final double zoomFactor = 1.1;
    final boolean defaultRelativeCheck = true;
    final boolean defaultCarveCheck = true;
    final boolean defaultRoofCheck = true;
    final boolean defaultFillCheck = true;
    final boolean defaultFloorCoverCheck = false;
    final boolean defaultFloorCheck = true;
    final MinecraftBlockList defaultRoofMaterial = MinecraftBlockList.mossy_cobblestone;
    final MinecraftBlockList defaultFillMaterial = MinecraftBlockList.air;
    final MinecraftBlockList defaultWallMaterial = MinecraftBlockList.mossy_cobblestone;
    final MinecraftBlockList defaultFloorCoverMaterial = MinecraftBlockList.water;
    final MinecraftBlockList defaultFloorMaterial = MinecraftBlockList.mossy_cobblestone;
    final int defaultHeight = 2;
    final int defaultXOffset = 0;
    final int defaultYOffset = 0;
    final int defaultZOffset = 0;
    final int defaultRows = 32;
    final int defaultCols = 32;    
    final int maxHeight = 1000000;
    final int minHeight = -1000000;
    final int maxX = 1000000;
    final int minX = -1000000;
    final int maxY = 1000000;
    final int minY = -1000000;
    final int maxZ = 1000000;
    final int minZ = -1000000;
    final int maxRows = 128;    
    final int minRows = 1;
    final int maxCols = 128;
    final int minCols = 1;
    
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	Stage myStage;
	MazeView maze = new MazeView(600,600,defaultRows,defaultCols);
	
    KeyCodeCombination keyComboUndo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboRedo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboCut = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboPaste = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboClear = new KeyCodeCombination(KeyCode.DELETE);
    KeyCodeCombination keyComboNewMaze = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboOpenMaze = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboSaveMaze = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboEscape = new KeyCodeCombination(KeyCode.ESCAPE);
    KeyCodeCombination keyComboZoomIn = new KeyCodeCombination(KeyCode.EQUALS, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboZoomOut = new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination keyComboShowHelp = new KeyCodeCombination(KeyCode.F1);
    
	MenuItem fileNewMaze = new MenuItem("New Maze");
	MenuItem fileOpenMaze = new MenuItem("Open Maze");
	MenuItem fileSaveMaze = new MenuItem("Save Maze");
	MenuItem fileSaveMazeAs = new MenuItem("Save Maze As");
	MenuItem fileSaveOutput = new MenuItem("Save Output");
	MenuItem fileSaveOutputAs = new MenuItem("Save Output As");
	Menu FileMenu = new Menu("File", null, fileNewMaze,fileOpenMaze, fileSaveMaze, fileSaveMazeAs, fileSaveOutput, fileSaveOutputAs);	
	MenuItem editUndo = new MenuItem("Undo");
	MenuItem editRedo = new MenuItem("Redo");
	MenuItem editCut = new MenuItem("Cut");
	MenuItem editCopy = new MenuItem("Copy");
	MenuItem editPaste = new MenuItem("Paste");
	MenuItem editClear = new MenuItem("Delete");
	Menu EditMenu = new Menu("Edit",null,editUndo,editRedo, editCut, editCopy, editPaste, editClear);
	MenuItem viewZoomIn = new MenuItem("Zoom In");
	MenuItem viewZoomOut = new MenuItem("Zoom Out");
	Menu ViewMenu = new Menu("View", null, viewZoomIn, viewZoomOut);
	MenuItem helpAbout = new MenuItem("About");
	MenuItem helpShowHelp = new MenuItem("Show Help");
	Menu HelpMenu = new Menu("Help", null, helpAbout, helpShowHelp);
	MenuBar menuBar = new MenuBar(FileMenu, EditMenu, ViewMenu, HelpMenu);

	
	Button printButton = new Button("Print Commands");
	CheckBox relativeCheck = new CheckBox("Relative XYZ");
	CheckBox carveCheck = new CheckBox("Carve Mode");
	CheckBox fillCheck = new CheckBox("Add Fill");
	CheckBox florCheck = new CheckBox("Add Floor");
	CheckBox florCoverCheck = new CheckBox("Add Floor Cover");
	CheckBox roofCheck = new CheckBox("Add Roof");
	
	ComboBox<MinecraftBlockList> wallCombo = new ComboBox<MinecraftBlockList>();
	ComboBox<MinecraftBlockList> florCombo = new ComboBox<MinecraftBlockList>();
	ComboBox<MinecraftBlockList> roofCombo = new ComboBox<MinecraftBlockList>();
	ComboBox<MinecraftBlockList> fillCombo = new ComboBox<MinecraftBlockList>();
	ComboBox<MinecraftBlockList> florCoverCombo = new ComboBox<MinecraftBlockList>();

	//Filter matching text to a signed integer.
	private UnaryOperator<TextFormatter.Change> signedIntegerFilter = change -> {    
	    return change.getControlNewText().matches("\\-?\\d{0,8}") ? change : null;
	};

	//Filter matching text to an unsigned integer.
	private UnaryOperator<TextFormatter.Change> unsignedIntegerFilter = change -> {    
	    return change.getControlNewText().matches("\\d{0,8}") ? change : null;
	};
	
	TextField heightText = new TextField(Integer.toString(defaultHeight));
	TextField xText = new TextField(Integer.toString(defaultXOffset));
	TextField yText = new TextField(Integer.toString(defaultYOffset));
	TextField zText = new TextField(Integer.toString(defaultZOffset));
	TextField rowsText = new TextField(Integer.toString(defaultRows));
	TextField colsText = new TextField(Integer.toString(defaultCols));
	
	//Values of old text prior to setting undo points.
    String oldHeightText = "";
    String oldXText = "";
    String oldYText = "";
    String oldZText = "";
    String oldRowsText = "";
    String oldColsText = "";
	
	TextArea outText = new TextArea();
	
	Scene scene;
	
    @Override
    public void start(Stage primaryStage) {
    	isUndoEnabled = false;//Disable undo saving while building UI.
    	primaryStage.setTitle("MineMaze V1.0");
    	
    	editUndo.setOnAction(e->handleEditUndo(e));
    	editRedo.setOnAction(e->handleEditRedo(e));
    	editCut.setOnAction(e->handleEditCut(e));
    	editCopy.setOnAction(e->handleEditCopy(e));
    	editPaste.setOnAction(e->handleEditPaste(e));    	
    	editClear.setOnAction(e->handleEditClear(e));    	
    	fileNewMaze.setOnAction(e->handleFileNewMaze(e));
        fileOpenMaze.setOnAction(e->handleFileOpenMaze(e));
    	fileSaveMaze.setOnAction(e->handleFileSaveMaze(e));
    	fileSaveMazeAs.setOnAction(e->handleFileSaveMazeAs(e));
        fileSaveOutput.setOnAction(e->handleFileSaveOutput(e));
        fileSaveOutputAs.setOnAction(e->handleFileSaveOutputAs(e));
        viewZoomIn.setOnAction(e->handleZoomIn());
        viewZoomOut.setOnAction(e->handleZoomOut());
        helpAbout.setOnAction(e->handleHelpAbout());
        helpShowHelp.setOnAction(e->handleHelpShowHelp());        
    	
        editUndo.setAccelerator(keyComboUndo);
        editRedo.setAccelerator(keyComboRedo);
        editCut.setAccelerator(keyComboCut);
        editCopy.setAccelerator(keyComboCopy);
        editPaste.setAccelerator(keyComboPaste);
        editClear.setAccelerator(keyComboClear);
        fileNewMaze.setAccelerator(keyComboNewMaze);
        fileOpenMaze.setAccelerator(keyComboOpenMaze);
        fileSaveMaze.setAccelerator(keyComboSaveMaze);
        viewZoomIn.setAccelerator(keyComboZoomIn);
        viewZoomOut.setAccelerator(keyComboZoomOut);
        helpShowHelp.setAccelerator(keyComboShowHelp);        
        
    	printButton.setOnMouseClicked(e->printButtonClicked());
        
        maze.setOnMouseClicked(e->mazeOnMouseClicked(e));
        maze.setOnMousePressed(e->mazeOnMouseDragged(e));
        maze.setOnMouseDragged(e->mazeOnMouseDragged(e));
        maze.setOnMousePressed(e->mazeOnMouseDown(e));
        maze.setOnMouseReleased(e->mazeOnMouseReleased(e));
        maze.repaint();		
        
        //Add handlers that call saveUndoState when the UI element update.
        relativeCheck.setOnAction(e->saveUndoState());
        carveCheck.setOnAction(e->saveUndoState());
        fillCheck.setOnAction(e->saveUndoState());
        florCheck.setOnAction(e->saveUndoState());
        florCoverCheck.setOnAction(e->saveUndoState());
        roofCheck.setOnAction(e->saveUndoState());
        wallCombo.setOnAction(e->saveUndoState());
        florCombo.setOnAction(e->saveUndoState());
        roofCombo.setOnAction(e->saveUndoState());
        fillCombo.setOnAction(e->saveUndoState());
        florCoverCombo.setOnAction(e->saveUndoState());
        

        
        //Text boxes normally have undo/redo keyboard shortcuts that would
        //capture those events and prevent our applicaiton level undo/redo
        //logic from working.  Therefore we add handlers to redirect the 
        //undo/redo keyboard shortcuts to our own logic.
        heightText.addEventFilter(KeyEvent.KEY_PRESSED, e->consumeUndoRedo(e));
        xText.addEventFilter(KeyEvent.KEY_PRESSED, e->consumeUndoRedo(e));
        yText.addEventFilter(KeyEvent.KEY_PRESSED, e->consumeUndoRedo(e));
        zText.addEventFilter(KeyEvent.KEY_PRESSED, e->consumeUndoRedo(e));
        rowsText.addEventFilter(KeyEvent.KEY_PRESSED, e->consumeUndoRedo(e));
        colsText.addEventFilter(KeyEvent.KEY_PRESSED, e->consumeUndoRedo(e));
         
		heightText.setTextFormatter(new TextFormatter<String>(unsignedIntegerFilter));
		xText.setTextFormatter(new TextFormatter<String>(signedIntegerFilter));
		yText.setTextFormatter(new TextFormatter<String>(signedIntegerFilter));
		zText.setTextFormatter(new TextFormatter<String>(signedIntegerFilter));
		rowsText.setTextFormatter(new TextFormatter<String>(unsignedIntegerFilter));
		colsText.setTextFormatter(new TextFormatter<String>(unsignedIntegerFilter));
		
		heightText.setOnAction(e->validateHeightText());
		heightText.focusedProperty().addListener((obs,wasFocused,isFocused)->{			
			if(isFocused)oldHeightText = heightText.getText();
			if(!isFocused)validateHeightText();
			
		});		
		xText.setOnAction(e->validateXText());
		xText.focusedProperty().addListener((obs,wasFocused,isFocused)->{
			if(isFocused)oldXText = xText.getText();
			if(!isFocused)validateXText();
		});
		yText.setOnAction(e->validateYText());
		yText.focusedProperty().addListener((obs,wasFocused,isFocused)->{
			if(isFocused)oldYText = yText.getText();
			if(!isFocused)validateYText();
		});		
		zText.setOnAction(e->validateZText());
		zText.focusedProperty().addListener((obs,wasFocused,isFocused)->{
			if(isFocused)oldZText = zText.getText();
			if(!isFocused)validateZText();
		});		
		rowsText.setOnAction(e->validateRowsText());
		rowsText.focusedProperty().addListener((obs,wasFocused,isFocused)->{
			if(isFocused)oldRowsText = rowsText.getText();
			if(!isFocused)validateRowsText();
		});		
		colsText.setOnAction(e->validateColsText());
		colsText.focusedProperty().addListener((obs,wasFocused,isFocused)->{
			if(isFocused)oldColsText = colsText.getText();
			if(!isFocused)validateColsText();
		});
		
		//Populate combo boxes with minecraft block choices
        for(MinecraftBlockList x : MinecraftBlockList.values()) {
        	roofCombo.getItems().add(x);
        	fillCombo.getItems().add(x);
        	wallCombo.getItems().add(x);
        	florCoverCombo.getItems().add(x);
        	florCombo.getItems().add(x);
		}
		
        VBox controlBox = new VBox();
        controlBox.setMinWidth(150);
		controlBox.getChildren().add(printButton);
		controlBox.getChildren().add(relativeCheck);
		controlBox.getChildren().add(carveCheck);
		controlBox.getChildren().add(roofCheck);
		controlBox.getChildren().add(fillCheck);
		controlBox.getChildren().add(florCoverCheck);
		controlBox.getChildren().add(florCheck);
		controlBox.getChildren().add(new Label("Roof Blocks"));
		controlBox.getChildren().add(roofCombo);
		controlBox.getChildren().add(new Label("Fill Blocks"));
		controlBox.getChildren().add(fillCombo);
		controlBox.getChildren().add(new Label("Wall Blocks"));
		controlBox.getChildren().add(wallCombo);
		controlBox.getChildren().add(new Label("Floor Cover"));
		controlBox.getChildren().add(florCoverCombo);
		controlBox.getChildren().add(new Label("Floor Blocks"));
		controlBox.getChildren().add(florCombo);
		controlBox.getChildren().add(new Label("Height"));
		controlBox.getChildren().add(heightText);
		controlBox.getChildren().add(new Label("X Offset"));
		controlBox.getChildren().add(xText);
		controlBox.getChildren().add(new Label("Y Offset"));
		controlBox.getChildren().add(yText);
		controlBox.getChildren().add(new Label("Z Offset"));
		controlBox.getChildren().add(zText);
		controlBox.getChildren().add(new Label("Rows"));
		controlBox.getChildren().add(rowsText);
		controlBox.getChildren().add(new Label("Cols"));
		controlBox.getChildren().add(colsText);
		
		
		Tooltip printButtonToolTip = new Tooltip(
			"Click this button to generate the fill commands."
		);		
		Tooltip relativeCheckToolTip = new Tooltip(
			"When this box is checked the output commands will\n" +
		    "all be relative to the player position (using ~).\n" +
			"When not checked the output commands will be\n"+
		    "absolute coordinates (not relative to the player).");
		Tooltip carveCheckToolTip = new Tooltip(
			"When this box is checked the maze is built using \"Carve Mode\".\n"+
		    "In carve mode, the maze area is first filled with the wall\n"+
			"material, and then the spaces between the walls are \"carved out\"\n"+
		    "using the fill material.  This is in contrast to the normal mode\n"+
			"where the maze is first filled with the fill material and then\n"+
		    "the walls are added in one at a time.  Carve mode is useful for\n"+
			"buliding mazes underwater, so that the water won't rush in\n"+
		    "during construction."
			);
		Tooltip roofCheckToolTip = new Tooltip(
			"When checked, a roof is added to the maze.\n"+
		    "When unchecked, there is no roof."
			);
		Tooltip fillCheckToolTip = new Tooltip(
			"When checked, the space between and around the walls\n"+
		    "will be filled with the material specified by the.\n"+
			"\"Fill Blocks\" combo.  When unchecked, the area\n"+
		    "between the walls is left alone.\n"
			);
		Tooltip florCoverCheckToolTip = new Tooltip(
				"When checked, a layer of floor cover blocks (one block high)\n"+
				"is added one block above the maze floor.\n"+
			    "When unchecked, there is no floor cover."
			);
		Tooltip florCheckToolTip = new Tooltip(
			"When checked, a floor is added to the maze.\n"+
		    "When unchecked, there is no floor."
		);
		Tooltip roofComboToolTip = new Tooltip(
			"The material from which the maze roof will be made."
			);
		Tooltip wallComboToolTip = new Tooltip(
			"The material from which the maze walls will be made."
			);
		Tooltip fillComboToolTip = new Tooltip(
			"The material between and around the maze walls.\n"+
		    "This is usually air, but could be water or\n"+
		    "something the player could dig through."
			);
		Tooltip florCoverComboToolTip = new Tooltip(
			"The material from which the floor cover of the maze will be made.\n"+
		    "The floor cover material is one block above the floor material.\n"+
		    "This is the the same level as the bottom of the walls.  And it is\n"+
		    "Intended that the floor cover is something that the player walks\n"+
		    "on (like carpet), or walks in (like water).  For example, if the\n"+
		    "floor cover were made of water, then there would be a layer of\n"+
		    "water (one block tall) covering the whole floor of the maze."
			);
		Tooltip florComboToolTip = new Tooltip(
			"The material from which the floor of the maze will be made.\n"
			);
		Tooltip heightTextToolTip = new Tooltip(
			"The height (in blocks) between the floor and roof of the maze.\n"+
		    "This is the height of the space the player walks in.\n"+
		    "This is also the height of the walls.\n"+
		    "Only positive whole numbers (two or greater) are allowed.\n"+
		    "With a floor and roof added, the maze is HEIGHT+2 blocks tall.\n"
			);
		Tooltip xTextToolTip = new Tooltip(
			"The X coordinate (in the minecraft world) of the\n"+
			"upper left corner of the maze.\n"+
		    "In relative mode, this is relative to the player.\n"+
			"When not in relative mode, it's the aboslute coordiante.\n"+
		    "The X direction is shown horizontally in the onscreen grid.\n"+
			"Lower values are towards the left, of the screen.\n"+
		    "Higher values are towards the right of the screen.\n"+
			"The upper left corner of the grid is (X+0, Y, Z+0)\n"+
			"Only positive or negative whole numbers are allowed."
			);
		Tooltip yTextToolTip = new Tooltip(
			"The Y coordinate (in the minecraft world) of the maze at\n"+
			"the floor cover level.  With no cover this is the Y\n"+
			"coordinate the player would stand on.  The actual floor\n"+
			"is one block below (at Y-1)\n"+
		    "In relative mode, this is relative to the player.\n"+
			"When not in relative mode, it's the aboslute coordiante.\n"+
		    "Lower Y values are towards the ground, and higher values.\n"+
			"are towards the sky.\n"+
			"Only positive or negative whole numbers are allowed."	
			);
		Tooltip zTextToolTip = new Tooltip(
			"The Z coordinate (in the minecraft world) of the\n"+
			"upper left corner of the maze.\n"+
		    "In relative mode, this is relative to the player.\n"+
			"When not in relative mode, it's the aboslute coordiante.\n"+
		    "The Z direction is shown vertically in the onscreen grid.\n"+
			"Lower values are towards the top, of the screen.\n"+
		    "Higher values are towards the bottom of the screen.\n"+
			"The upper left corner of the grid is (X+0, Y, Z+0)\n"+
			"Only positive or negative whole numbers are allowed."	    
			);
		Tooltip rowsToolTip = new Tooltip(
			"The number of rows in the grid."	    
			);
		Tooltip colsToolTip = new Tooltip(
			"The number of columns in the grid."	    
			);
		Tooltip.install(printButton, printButtonToolTip);
		Tooltip.install(relativeCheck, relativeCheckToolTip); 
		Tooltip.install(carveCheck, carveCheckToolTip);
		Tooltip.install(florCoverCheck, florCoverCheckToolTip);
		Tooltip.install(florCheck, florCheckToolTip);
		Tooltip.install(roofCheck, roofCheckToolTip);
		Tooltip.install(fillCheck, fillCheckToolTip);
		Tooltip.install(roofCombo, roofComboToolTip);
		Tooltip.install(fillCombo, fillComboToolTip);
		Tooltip.install(wallCombo, wallComboToolTip);		
		Tooltip.install(florCoverCombo, florCoverComboToolTip);
		Tooltip.install(florCombo, florComboToolTip);
		Tooltip.install(heightText, heightTextToolTip);
		Tooltip.install(xText, xTextToolTip);
		Tooltip.install(yText, yTextToolTip);
		Tooltip.install(zText, zTextToolTip);
		Tooltip.install(rowsText, rowsToolTip);
		Tooltip.install(colsText, colsToolTip);
		//The tooltip text is long, so set the duration to infinite
		//so that people have a chance to read them.
		printButtonToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		relativeCheckToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		carveCheckToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		florCheckToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		roofCheckToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		fillCheckToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		roofComboToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		fillComboToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		wallComboToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		florCoverComboToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		florComboToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		heightTextToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		xTextToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		yTextToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));
		zTextToolTip.setShowDuration(new Duration(Integer.MAX_VALUE));		
		
		//Set layout properties on all children of control area.
		for(Node x : controlBox.getChildren()) {
			VBox.setMargin(x, new Insets(4,5,0,5));
		}

		//General scene layout
        ScrollPane topScrollPane = new ScrollPane();
        topScrollPane.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        topScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        topScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        topScrollPane.setContent(maze);
        //Zoom in/out when the user presses CTRL+scroll
        topScrollPane.setOnScroll(e->mazeOnScroll(e));
		
		SplitPane leftSplitPane = new SplitPane(topScrollPane,outText);
		leftSplitPane.setOrientation(Orientation.VERTICAL);
		leftSplitPane.setDividerPositions(0.75);		
		HBox root = new HBox(leftSplitPane, controlBox);
		HBox.setHgrow(leftSplitPane, Priority.ALWAYS);
		VBox.setVgrow(root, Priority.ALWAYS);
		VBox menuRoot = new VBox(menuBar, root);
		
		scene = new Scene(menuRoot, 900, 900, Color.WHITE);
        primaryStage.setScene(scene);        
        primaryStage.show();
        myStage = primaryStage;
        
		//Update all UI elements with default values
		setDefaults();
		undoState = null;
		isUndoEnabled = true;//Now that the UI is built, enable undo saving.
		saveUndoState();
    }
    
    //
    //[
    //{
    //  "LayerName" : string,
    //  "RelativeXYZ" : true | false,
    //  "CarveMode" = true | false,
    //  "AddRoof" = true | false,
    //  "AddFill" = true | false,
    //  "AddFloorCover" = true | false,
    //  "AddFloor" = true | false,
    //  "RoofBlocks" = string,
    //  "FillBlocks" = string,
    //  "WallBlocks" = string,
    //  "FloorCover" = string,
    //  "FloorBlocks" = string,
    //  "Height" = number,
    //  "XOffset" = number,
    //  "YOffset" = number,
    //  "ZOffset" = number,
    //  "Rows" = number,
    //  "Cols" = number,
    //  "Blocks" = number[][]
    //},
    //...possibly more layers here.
    //]
    //
    private File saveMazeFile = null;
    private File saveOutputFile = null;
    private File workingDirectory = null;
    public void handleFileOpenMaze(ActionEvent e) {
    	FileChooser x = new FileChooser();
    	x.setTitle("Open File");
    	x.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Maze Files", "*.maze"),
			new FileChooser.ExtensionFilter("Text Files", "*.txt"),
			new FileChooser.ExtensionFilter("Text Files", "*.json"),
			new FileChooser.ExtensionFilter("All Files", "*.*")
		);
    	if(workingDirectory != null)x.setInitialDirectory(workingDirectory);
    	File f = x.showOpenDialog(myStage);
    	if(f!=null) {
    		try {
    			FileReader fr = new FileReader(f);
    			try {
    				Gson gson = new Gson();
    				Maze m = gson.fromJson(fr, Maze.class);    				
    				syncUiToMaze(m);
    				maze.clearSelection();
    				saveMazeFile = f;
    				workingDirectory = f.getParentFile();
    				undoState = null;
    				saveUndoState();
    			}catch(JsonSyntaxException je){
    				//Notify user of bad file.
    				Alert alert = new Alert(
						AlertType.ERROR, 
						"There was a formatting error in the file.\n"+
						"Please check that file contents are valid.\n"
						+je.toString(), 
						ButtonType.OK
					);
    				alert.setTitle("Error opening file");
    				alert.showAndWait();
    			}catch(JsonIOException je) {
       				//Notify user of bad file.
    				Alert alert = new Alert(
						AlertType.ERROR, 
						"There was and IO exception when accessing the file.\n"+
						"Please check that the file exists and that you\n"+
						"have permission to access it.\n"+
						je.toString(), 
						ButtonType.OK
					);
    				alert.setTitle("Error opening file");
    				alert.showAndWait();
    			}    			
    		}catch(FileNotFoundException fe) {
   				//Notify user of bad file.
				Alert alert = new Alert(
					AlertType.ERROR, 
					"The selected file could not be found.\n"+
					"Please check that the file exists and\n"+
					"that you have permission to access it.\n"+
					fe.toString(), 					
					ButtonType.OK
				);
				alert.setTitle("Error opening file");
				alert.showAndWait();
    		}
    		
    	}
    }//End of handleFileOpenMaze
    

    //Sets all UI selections to default values.
    private void setDefaults() {
    	boolean reenable = isUndoEnabled;
    	isUndoEnabled = false;
    	relativeCheck.setSelected(defaultRelativeCheck);
    	carveCheck.setSelected(defaultCarveCheck);
    	roofCheck.setSelected(defaultRoofCheck);
    	fillCheck.setSelected(defaultFillCheck);
    	florCoverCheck.setSelected(defaultFloorCoverCheck);
    	florCheck.setSelected(defaultFloorCheck);
    	roofCombo.setValue(defaultRoofMaterial);
    	fillCombo.setValue(defaultFillMaterial);
    	wallCombo.setValue(defaultWallMaterial);
    	florCoverCombo.setValue(defaultFloorCoverMaterial);
    	florCombo.setValue(defaultFloorMaterial);
    	heightText.setText(Integer.toString(defaultHeight));
    	xText.setText(Integer.toString(defaultXOffset));
    	yText.setText(Integer.toString(defaultYOffset));
    	zText.setText(Integer.toString(defaultZOffset));
    	rowsText.setText(Integer.toString(defaultRows));
    	colsText.setText(Integer.toString(defaultCols));
    	maze.resize(defaultRows, defaultCols,0,0,0);
    	maze.setAllBlocks(0);
    	maze.clearSelection();
    	maze.repaint();
    	isUndoEnabled = reenable;
    }
    
    private void handleFileNewMaze(ActionEvent e) {
    	setDefaults();
    	saveMazeFile = null;
    	saveOutputFile = null;
    	outText.clear();
    	undoState = null;
    	saveUndoState();
    }    
    
    private void handleFileSaveMaze(ActionEvent e) {
    	if(saveMazeFile == null) {
    		handleFileSaveMazeAs(e);
    		return;
    	}
    	Maze m = new Maze();
    	syncMazeToUi(m);
    	//Generate the JSON text
    	Gson gson = new Gson();
    	String s = gson.toJson(m);
    	//Write out the file.
    	FileWriter f=null;
    	try {
    		f = new FileWriter(saveMazeFile, false);
    		f.write(s);
    		f.close();
    	}catch(IOException ex) {
			//Notify user of bad file.
			Alert alert = new Alert(
				AlertType.ERROR, 
				"The file could not be saved.\n"+
				"Please check that the target folder exists.\n"+
				"and that you have permission to write to it.\n"+
				ex.toString(), 					
				ButtonType.OK
			);
			alert.setTitle("Error Saving File");
			alert.showAndWait();
    	}
    	try {
    		if(f != null)f.close();
    	}catch(IOException ex) {
    	}
    }//End of handleFileSaveMaze    
    
    private void handleFileSaveMazeAs(ActionEvent e) {
    	FileChooser x = new FileChooser();
    	x.setTitle("Save File");
    	x.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Maze Files", "*.maze"),
			new FileChooser.ExtensionFilter("Text Files", "*.txt"),
			new FileChooser.ExtensionFilter("Text Files", "*.json"),
			new FileChooser.ExtensionFilter("All Files", "*.*")
		);
    	//Set default directory to the last place we saved to.
    	//If we haven't saved yet, then 
    	if(workingDirectory != null)x.setInitialDirectory(workingDirectory);
    	File f = x.showSaveDialog(myStage);
    	if(f != null) {
    		saveMazeFile = f;
    		workingDirectory = f.getParentFile();
    		handleFileSaveMaze(e);
    	}    	
    }//End of handleFileSaveMazeAs
    
    
    private void handleFileSaveOutput(ActionEvent e) {
    	if(saveOutputFile == null) {
    		handleFileSaveOutputAs(e);
    		return;
    	}
    	FileWriter f=null;    	
    	try {
        	//regenerate output before we save the file
        	printButtonClicked();
    		f = new FileWriter(saveOutputFile, false);
    		f.write(outText.getText());
    		f.close();
    	}catch(IOException ex) {
			//Notify user of bad file.
			Alert alert = new Alert(
				AlertType.ERROR, 
				"The file could not be saved.\n"+
				"Please check that the target folder exists.\n"+
				"and that you have permission to write to it.\n"+
				ex.toString(), 					
				ButtonType.OK
			);
			alert.setTitle("Error Saving File");
			alert.showAndWait();
    	}
    	try {
    		if(f != null)f.close();
    	}catch(IOException ex) {
    	}
    }//End of handleFileSaveOutput
    
    private void handleFileSaveOutputAs(ActionEvent e) {
    	FileChooser x = new FileChooser();
    	x.setTitle("Save Output File");
    	x.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Text Files", "*.txt"),
			new FileChooser.ExtensionFilter("All Files", "*.*")
		);
    	if(workingDirectory != null)x.setInitialDirectory(workingDirectory);
    	File f = x.showSaveDialog(myStage);
    	if(f != null) {
    		saveOutputFile = f;
    		workingDirectory = f.getParentFile();
    		handleFileSaveOutput(e);
    	}    	
    }//End of handleFileSaveOutputAs
    
    
    private UndoState undoState = null;
    private boolean isUndoEnabled = false;
    private void handleEditUndo(ActionEvent e) {
    	if(undoState == null)return;
    	if(undoState.prev == null)return;
    	undoState = undoState.prev;
    	//Grab previous state of undo enable
    	boolean reenable = isUndoEnabled;
    	//Disable undo saving while updating UI.    	
    	isUndoEnabled = false;
    	syncUiToMaze(undoState.maze);
    	//Revert undo enable state.
    	isUndoEnabled = reenable;
    }
    
    private void handleEditRedo(ActionEvent e) {
    	if(undoState == null)return;
    	if(undoState.next == null)return;
    	undoState = undoState.next;
    	//Grab previous state of undo enable
    	boolean reenable = isUndoEnabled;
    	//Disable undo saving while updating UI.    	
    	isUndoEnabled = false;
    	syncUiToMaze(undoState.maze);
    	//Revert undo enable state.
    	isUndoEnabled = reenable;
    }
    
    private void handleEditCut(ActionEvent e) {
    	//For a cut we just do a copy and then clear any selected cells.
    	handleEditCopy(e);
    	handleEditClear(e);//NOTE:  handleEditClear will will call redoSelection and maze.repaint for us.
    }

    private void handleEditCopy(ActionEvent e) {
    	Clipboard C = Clipboard.getSystemClipboard();
    	ClipboardContent content = new ClipboardContent();
    	ClipboardData D = new ClipboardData();
    	D.rows = maze.getRows();
    	D.cols = maze.getCols();
    	D.blocks = maze.getAllBlocks();
    	D.selection = maze.getSelection();
    	content.putString(D.toString());
    	C.setContent(content);    
    }
    
    
    private void handleEditPaste(ActionEvent e) {
    	Clipboard C = Clipboard.getSystemClipboard();
    	String S = C.getString();
    	ClipboardData D = ClipboardData.fromString(S);
    	if(D != null) {
    	    mazeMouseDownRow = 0;
    	    mazeMouseDownCol  = 0;
    		mazeSelectionDragging = true;
    		//Store current state of blocks before we started moving a selection.
    		mazeMouseDownUnselectedBlocks = maze.getAllBlocks();
    		mazeMouseDownSelectedBlocks = D.blocks;
    		mazeMouseDownSelection = D.selection;

			//maze.setBlocks(0, 0, mazeMouseDownUnselectedBlocks);
			//maze.clearMask(0,0,mazeMouseDownSelection);
			maze.mergeBlocksMasked(0,0,mazeMouseDownSelectedBlocks,mazeMouseDownSelection);			
			maze.clearSelection();
			maze.setSelection(0, 0, mazeMouseDownSelection);
			redoSelection();
    		maze.repaint();    		
    	}
    }
    
    private void handleEditClear(ActionEvent e) {
    	maze.clearMask(0, 0, maze.getSelection());
    	maze.clearSelection();
    	redoSelection();
    	saveUndoState();
    	maze.repaint();
    }
    
    private void saveUndoState() {
    	//If undo point saving is disabled 
    	//(for example during undo/redo) 
    	//then just exit.
    	if(isUndoEnabled == false)return;    	
    	//Add a new undo element to the end of the list.   	
    	UndoState x = new UndoState();
    	Maze m = new Maze();
    	syncMazeToUi(m);
    	x.maze = m;
    	if(undoState == null) {
    		undoState = x;
    	}else {
    		x.next = null;
    		x.prev = undoState;
    		undoState.next = x;
    		undoState = x;    		
    	}
    	System.out.println("Undo Point Saved");
    }

    //Handler used to redirect undo/redo events from TextFields which would
    //normally capture them and prevent our application level undo/redo from
    //working properly.
    private void consumeUndoRedo(KeyEvent e) {
    	if(keyComboUndo.match(e)) {
    		e.consume();
    		handleEditUndo(null);
    	}else if(keyComboRedo.match(e)) {
    		e.consume();
    		handleEditRedo(null);
    	}else if(keyComboEscape.match(e)) {
    		//If the user presses escape while editing a text field
    		//We will revert to the value of the field from just
    		//before they enterd the field.
    		e.consume();    		
    		Object T  = e.getSource();
    		if(T == heightText)heightText.setText(oldHeightText);
    		if(T == xText)xText.setText(oldXText);
    		if(T == yText)yText.setText(oldYText);
    		if(T == zText)zText.setText(oldYText);
    		if(T == colsText)colsText.setText(oldColsText);
    		if(T == rowsText)rowsText.setText(oldRowsText);
    	}
    }    
    void validateRange(TextField T, int min, int max, String def) {
    	try {
    		int x = Integer.parseInt(T.getText());
    		if(x < 1)T.setText(Integer.toString(1));
    		if(x > 128)T.setText(Integer.toString(128));
    	}catch(NumberFormatException e) {
    		T.setText(def);
    	}
    }
    void validateHeightText() {
    	validateRange(heightText,1,128,oldHeightText);
    	saveUndoState();
	}    	
    void validateXText() {
    	validateRange(xText,-1000000,1000000,oldXText);
    	saveUndoState();
	}
    void validateYText() {
    	validateRange(yText,-1000000,1000000,oldYText);
    	saveUndoState();
	}    
    void validateZText() {
    	validateRange(zText,-1000000,1000000,oldZText);
    	saveUndoState();
	}
    void validateRowsText() {
    	validateRange(rowsText,1,128,oldRowsText);
    	int rows = Integer.parseInt(rowsText.getText());
    	int cols = Integer.parseInt(colsText.getText());
    	maze.resize(rows,cols, 0,0,0);
    	saveUndoState();
	}
    void validateColsText() {
    	validateRange(colsText,1,128,oldColsText);
    	int rows = Integer.parseInt(rowsText.getText());
    	int cols = Integer.parseInt(colsText.getText());  	
    	maze.resize(rows,cols, 0,0,0);
    	saveUndoState();
	}
       
    //Drawing Command summary:    
    //CTRL + click primary button sets entire selection to single block.
    //CTRL + click secondary button sets entire selection to a single empty block (so clears all selection).
    //CTRL + SHIFT + click primary button adds a block to current selection.
    //CTRL + SHIFT + click secondary button removes a block from current selection.
    //CTRL + drag primary button sets entire selection to a rectangle of blocks
    //CTRL + drag secondary button sets entire selection to a cleared rectangle (so clears all selection).
    //CTRL + SHIFT + drag primary button adds a rectangle of blocks to selection
    //CTRL + SHIFT + drag secondary button clears a rectangle of blocks from selection
    //
    //click primary button sets a block.
    //click secondary button clears a block.
    //drag primary button sets blocks.
    //drag secondary button clears blocks.
    //shift + drag primary button sets a rectangle of blocks
    //shift + drag primary button clears a rectangle of blocks
    private int mazeMouseDownRow = 0;
    private int mazeMouseDownCol  = 0;
    private int mazeSelectionDeltaRows = 0;
    private int mazeSelectionDeltaCols = 0;    
    private boolean mazeSelectionDragging = false;
    private boolean[][] mazeMouseDownSelection = null;
    private int[][] mazeMouseDownUnselectedBlocks = null;
    private int[][] mazeMouseDownSelectedBlocks = null;
    void mazeOnMouseDown(MouseEvent event){
    	if(event.isControlDown() && !event.isShiftDown())maze.clearSelection();
    	mazeMouseDownRow = maze.getRowIndex(event.getY());
    	mazeMouseDownCol= maze.getColIndex(event.getX());
    	boolean clickedOnSeleciton = maze.getSelection(mazeMouseDownRow,mazeMouseDownCol);
    	//If the mouse down event occured on a selected cell and CTLR+SHIFT was 
    	//not pressed then we are dragging a selection.  Indicate that by
    	//setting the mazeSelectionDragging flag.  In all other cases we are
    	//either editing blocks or the selection, so clear the flag.
    	mazeSelectionDragging = clickedOnSeleciton && (!event.isControlDown() || event.isShiftDown());    	
    	
    	//If not moving or adding to a selection, then clear the selection.
    	if(!clickedOnSeleciton && (!event.isControlDown() || !event.isShiftDown())){
    		mazeSelectionDeltaRows = 0;
    		mazeSelectionDeltaCols = 0;
    		maze.clearSelection();
    		maze.repaint();
    		System.out.println("mazeOnMouseDown -> maze.clearSelection()");
    	}
    }

    void mazeOnMouseReleased(MouseEvent event){
    	//Store the position change on mouse up.
    	//If the user clicks again on the selection to drag it, we can use this
    	//delta to properly calculate the total delta.
    	int row = maze.getRowIndex(event.getY());
    	int col = maze.getColIndex(event.getX());
		int dRow = row - mazeMouseDownRow;
		int dCol = col - mazeMouseDownCol;
		if(mazeSelectionDragging) {
			mazeSelectionDeltaRows = mazeSelectionDeltaRows + dRow;
			mazeSelectionDeltaCols = mazeSelectionDeltaCols + dCol;
		}else {
			redoSelection();
		}
		saveUndoState();
    }
    
    public void redoSelection() {
		//Store current state of blocks before we started moving a selection.
    	int rows = maze.getRows();
    	int cols = maze.getCols();
		mazeMouseDownUnselectedBlocks = maze.getAllBlocks();
		mazeMouseDownSelectedBlocks = new int[rows][cols];
		mazeMouseDownSelection = maze.getSelection();    		
		//Cut out the selection from the blocks we are moving.
    	for(int r = 0;r<rows;r++) {
    		for(int c=0;c<cols;c++) {
    			if(mazeMouseDownSelection[r][c]) {
    				mazeMouseDownSelectedBlocks[r][c]=mazeMouseDownUnselectedBlocks[r][c];
    				mazeMouseDownUnselectedBlocks[r][c]=0;
    			}
    		}
    	}
		mazeSelectionDeltaRows = 0;
		mazeSelectionDeltaCols = 0;
		mazeSelectionDragging = false;
    }
    
    void mazeOnMouseClicked(MouseEvent event) {
    	int row = maze.getRowIndex(event.getY());
    	int col = maze.getColIndex(event.getX());
		
		if(event.getClickCount() == 1) {
			if(event.isControlDown()) {//changing selection
				//Hold shift to modify selection
				//Dont hold shift to replace selection.
				//When not modifying, the selection is first cleared in mazeOnMouseDown() 
				if(event.getButton() == MouseButton.PRIMARY) maze.setSelection(row, col, true);
				if(event.getButton() == MouseButton.SECONDARY) maze.setSelection(row, col, false);
			}else if(!mazeSelectionDragging){//changing blocks.
				if(event.getButton() == MouseButton.PRIMARY) maze.setBlock(row, col, 1);
				if(event.getButton() == MouseButton.SECONDARY) maze.setBlock(row, col, 0);				
			}
		}else if (event.getClickCount() == 2) {
			if(event.isControlDown()) {//modifying selection
    			if(event.getButton() == MouseButton.PRIMARY)maze.selectAll();
    			if(event.getButton() == MouseButton.SECONDARY)maze.clearSelection();
    			mazeSelectionDragging = false;
			}else {//Modifying blocks
    			if(event.getButton() == MouseButton.PRIMARY)maze.setAllBlocks(1);
    			if(event.getButton() == MouseButton.SECONDARY)maze.setAllBlocks(0);    				
			}			
		}
		maze.repaint();
	}   

    void mazeOnMouseDragged(MouseEvent event) {    	
    	int row = maze.getRowIndex(event.getY());
    	int col = maze.getColIndex(event.getX());		
		if(event.isControlDown()) {//modifying selection
			//Hold shift to modify selection.
			//Release shift to replace selection.
			if(!event.isShiftDown())maze.clearSelection();
			if(event.isPrimaryButtonDown()) {//adding to selection.
				maze.addSelectionRectangle(mazeMouseDownRow, mazeMouseDownCol, row, col, true);
			}else if(event.isSecondaryButtonDown()) {//removing from selection.
				maze.addSelectionRectangle(mazeMouseDownRow, mazeMouseDownCol, row, col, false);
			}
		}else if(mazeSelectionDragging) {
			//We implement drag selection as follows...
			//1)  Redraw the blocks as they were before we started dragging.
			//2)  Removed the blocks that were over the original selection.
			//3)  Add back in the blocks at the translated selection.
			//4)  Clear the selection
			//5)  Translate the selection to the new location.
			int dRow = row - mazeMouseDownRow + mazeSelectionDeltaRows;
			int dCol = col - mazeMouseDownCol + mazeSelectionDeltaCols;
			maze.setBlocks(0, 0, mazeMouseDownUnselectedBlocks);
			maze.mergeBlocksMasked(dRow,dCol,mazeMouseDownSelectedBlocks,mazeMouseDownSelection);			
			maze.clearSelection();
			maze.setSelection(dRow, dCol, mazeMouseDownSelection);		
		}else {//Modifying blocks
			if(event.isShiftDown()) {//if drawing rectangles
				maze.setBlocks(0, 0, mazeMouseDownUnselectedBlocks);
				if(event.isPrimaryButtonDown()) {
					maze.addBlockRectangle(mazeMouseDownRow, mazeMouseDownCol, row, col, 1);
				}else if(event.isSecondaryButtonDown()) {
					maze.addBlockRectangle(mazeMouseDownRow, mazeMouseDownCol, row, col, 0);
				}
			}else{//If drawing pixels
				if(event.isPrimaryButtonDown())maze.setBlock(row, col, 1);    			
				else if(event.isSecondaryButtonDown())maze.setBlock(row, col, 0);
			}
		}  		
		maze.repaint();
	}
    
    //Handle zooming of the content grid when the user presses CTRL+scroll.
    //The ScrollEvent gives you a positive DeltaY for scrolling "UP",
    //and a negative DeltaY for scrolling "DOWN".
    void mazeOnScroll(ScrollEvent event) {
		//Only scroll if alt is pressed.
		if(event.isControlDown() == true) {    			
			//TODO:  need to update scrolling so that your cursor stays over the
			//exact same square after the zoom occurs.
			if(event.getDeltaY() > 0) handleZoomIn();    				
			else if(event.getDeltaY() < 0) handleZoomOut();
			event.consume();
		}
	}
    
    void handleZoomIn() {
		maze.setHeight(maze.getHeight()*zoomFactor);
		maze.setWidth(maze.getWidth()*zoomFactor);
		maze.repaint();    	
    }
    
    void handleZoomOut() {
		maze.setHeight(maze.getHeight()/zoomFactor);
		maze.setWidth(maze.getWidth()/zoomFactor);
		maze.repaint();    
    }
    
    void handleHelpAbout() {
    	Dialog<String> dialog = new Dialog<>();
    	Label label = new Label(
			"Mine Maze V1.0.\n"+
			"By:  Andrew McBain\n"+
			"6-8-2025\n"
		);
    	dialog.getDialogPane().setContent(label);
    	dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    	dialog.setTitle("About Mine Maze");
    	dialog.showAndWait();
    }
    
    void handleHelpShowHelp() {
    	Dialog<String> dialog = new Dialog<>();
    	TextArea textArea = new TextArea(
    		"Mine Maze lets you draw the outline Minecraft structure graphically.\n"+
			"You then click the \"Print Commands\" button to generate a set of fill\n"+
			"commands that you an paste into Minecraft to mke the structure in game.\n"+
			"\n"+
			"Drawing Command summary:\n"+    
			"CTRL + click primary button sets entire selection to single block.\n"+
			"CTRL + click secondary button sets entire selection to a single empty block (so clears all selection).\n"+
			"CTRL + SHIFT + click primary button adds a block to current selection.\n"+
			"CTRL + SHIFT + click secondary button removes a block from current selection.\n"+
			"CTRL + drag primary button sets entire selection to a rectangle of blocks.\n"+
			"CTRL + drag secondary button sets entire selection to a cleared rectangle (so clears all selection).\n"+
			"CTRL + SHIFT + drag primary button adds a rectangle of blocks to selection.\n"+
			"CTRL + SHIFT + drag secondary button clears a rectangle of blocks from selection.\n"+
			"\n"+
			"click primary button sets a block.\n"+
			"click secondary button clears a block.\n"+
			"drag primary button sets blocks.\n"+
			"drag secondary button clears blocks.\n"+
			"shift + drag primary button sets a rectangle of blocks\n"+
			"shift + drag primary button clears a rectangle of blocks\n"+
			"\n"+
			"Please note that the Mine Maze project is not affiliated with Microsoft or Mojang in any way.\n"
		);
    	ScrollPane scrollPane = new ScrollPane(textArea);
    	textArea.setEditable(false);
    	scrollPane.setFitToWidth(true);
    	scrollPane.setFitToHeight(true);
    	dialog.setResizable(true);
    	scrollPane.setPrefSize(700, 500);
    	dialog.getDialogPane().setContent(scrollPane);
    	dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    	dialog.setTitle("Mine Maze Help Contents");
    	dialog.showAndWait();
    }
    
    

	public void printButtonClicked() {
		clearOutput();
		//Build a set of fill commands for the output
		int row = 0;
		int col = 0;
		int span;
		int rows = maze.getRows();
		int cols = maze.getCols();
		boolean fill = fillCheck.isSelected();
		boolean roof = roofCheck.isSelected();
		boolean covr = florCoverCheck.isSelected();
		boolean flor = florCheck.isSelected();
		boolean carve = carveCheck.isSelected();
		String tilde = relativeCheck.isSelected()?"~":"";    		
		String roofBlock = roofCombo.getValue().toString();
		String covrBlock = florCoverCombo.getValue().toString();
		String florBlock = florCombo.getValue().toString();
		String wallBlock = wallCombo.getValue().toString();
		String fillBlock = fillCombo.getValue().toString(); 
		int X = Integer.parseInt(xText.getText());//X coordinate of upper left portion of maze
		int Y = Integer.parseInt(yText.getText());//Y coordinate of upper left portion of maze (at floor level)
		int Z = Integer.parseInt(zText.getText());//Z coordinate of upper left portion of maze
		int H = Integer.parseInt(heightText.getText());
		boolean used[][] = new boolean[rows][cols];//array of blocks we already drew
		//Array of blocks in the structure.
		//We make it one block bigger than the actual maze in each direction, and leave the extra space
		//at the default "false" values.  By doing this, we don't need to includ extra logic to check
		//the array bounds in the span searching logic, since it will naturally terminate on the false edges.
		boolean blocks[][] = new boolean[rows+1][cols+1];
		for(row=0;row<rows;row++) {
			for(col=0;col<cols;col++) {
				//Copy the maze to the blocks array.  If carve mode, then flip the values.
				blocks[row][col] = (maze.getBlock(row,col) != 0) ^ carve;
			}
		}
		//When not in carve mode we fill the maze area with the fill material (usually air)
		//and then add the wall matterial in.  In carve mode, we fill the maze area with
		//the wall material, and then put the fill material (usually air) into the empty
		//spaces between the walls.
		//
		//In order to keep the same logic throughout the rest of the function, we achieve
		//carve mode by inverting the blocks[] table and swapping wallBlock and fillBlock.
		if(carve) {
			String T = wallBlock;
			wallBlock=fillBlock;
			fillBlock=T;
		}
		String X1, Y1, Z1, X2, Y2, Z2, command;
		//If checked, Initially fill maze volume with the fill material
		if(fill) {
			X1 = tilde + X;
			Y1 = tilde + Y;
			Z1 = tilde + Z;
			X2 = tilde + (X+ cols - 1);
			Y2 = tilde + (Y+ H - 1);
			Z2 = tilde + (Z+ rows - 1);
			command = "/fill "+X1+" "+Y1+" "+Z1+" "+X2+" "+Y2+" "+Z2+"  "+fillBlock;
			outputLine(command);
		}
		//If checked, fill the floor
		if(covr) {
			X1 = tilde + X;
			Y1 = tilde + (Y);
			Z1 = tilde + Z;
			X2 = tilde + (X + cols - 1);
			Y2 = tilde + (Y);
			Z2 = tilde + (Z + rows - 1);
			command = "/fill "+X1+" "+Y1+" "+Z1+" "+X2+" "+Y2+" "+Z2+"  "+covrBlock;
			outputLine(command);
		}
		//If checked, fill the floor
		if(flor) {
			X1 = tilde + X;
			Y1 = tilde + (Y - 1);
			Z1 = tilde + Z;
			X2 = tilde + (X + cols - 1);
			Y2 = tilde + (Y - 1);
			Z2 = tilde + (Z + rows - 1);
			command = "/fill "+X1+" "+Y1+" "+Z1+" "+X2+" "+Y2+" "+Z2+"  "+florBlock;
			outputLine(command);
		}
		//If checked, fill the roof
		if(roof) {
			X1 = tilde + X;
			Y1 = tilde + (Y + H);
			Z1 = tilde + Z;
			X2 = tilde + (X + cols - 1);
			Y2 = tilde + (Y + H);
			Z2 = tilde + (Z + rows - 1);
			command = "/fill "+X1+" "+Y1+" "+Z1+" "+X2+" "+Y2+" "+Z2+"  "+roofBlock;
			outputLine(command);
		}

		//If the wall height is > 0
		if(H>0) {
			//Fill all horizontal lines.    		
    		for(row = 0;row<rows;row++) {
    			for(col=0;col<cols;col=col+span){
    				span = 1;
    				//We must always start a span on a nonzero block.
    				if(blocks[row][col]) {    					
    					while(blocks[row][col+span])span++;
    					//If the span is only 1 we make the assumption that this block
    					//may be part of a vertical span.  Therefore we ignore it for now.
    					//it will get counted when we do the vertical search.
    					if(span>1) {
    						//mark blocks as used
    						for(int i=0;i<span;i++)used[row][col+i] = true;
    						//Output command
    						X1 = tilde + (X+col);
    						Y1 = tilde + Y;
    						Z1 = tilde + (Z+row);
    						X2 = tilde + (X+col+span-1);
    						Y2 = tilde + (Y + H-1);
    						Z2 = tilde + (Z+row);
    						command = "/fill "+X1+" "+Y1+" "+Z1+" "+X2+" "+Y2+" "+Z2+" "+wallBlock;
    						outputLine(command);
    					}
    				}
    			}//end for
    		}//end for
    		//Fill all vertical lines.
    		for(col=0;col<cols;col++) {
    			for(row=0;row<rows;row=row+span) {
    				span = 1;
    				//We must always start on an unused block that is also nonzero.
    				if(used[row][col]==false && blocks[row][col]) {
    					//Count how many blocks in a row there are going vertically.
    					//its possible that some blocks in this span were already used
    					//but its OK to count on through them because there could be unused
    					//blocks on the other side.  And by doing so we can reduct the total
    					//number of fill commands.
    					while(blocks[row+span][col])span++;
    					for(int i=0;i<span;i++)used[row+i][col] = true;//Not technically needed, but keep for now.
						//Output command
						X1 = tilde + (X+col);
						Y1 = tilde + Y;
						Z1 = tilde + (Z+row);
						X2 = tilde + (X+col);
						Y2 = tilde + (Y+H-1);
						Z2 = tilde + (Z+row+span-1);
						command = "/fill "+X1+" "+Y1+" "+Z1+" "+X2+" "+Y2+" "+Z2+" "+wallBlock;
						outputLine(command);
    				}
    			}//end for
    		}//end for	
		}//End if H>0    		
	}//end handle
    
    private void clearOutput() {
    	outText.setText("");
    }
    
    private void outputLine(String x) {
    	outText.appendText(x+"\n");
    }
    
    private void syncMazeToUi(Maze m) {
    	//Get the state of all the check boxes.
    	m.relativeXYZ = relativeCheck.isSelected();
    	m.carveMode = carveCheck.isSelected();
    	m.addRoof = roofCheck.isSelected();
    	m.addFill = fillCheck.isSelected();
    	m.addFloorCover = florCoverCheck.isSelected();
    	m.addFloor = florCheck.isSelected();
    	//Pull the material selections out of the combo boxes.
    	//The combo box entries should be valid, but just in case they are not, fill with defaults.
    	try {m.roofBlocks       = roofCombo.getValue().toString()     ;}catch(Exception ex) {m.roofBlocks = MinecraftBlockList.barrier.toString();}
    	try {m.fillBlocks       = fillCombo.getValue().toString()     ;}catch(Exception ex) {m.fillBlocks = MinecraftBlockList.air.toString();}
    	try {m.wallBlocks       = wallCombo.getValue().toString()     ;}catch(Exception ex) {m.wallBlocks = MinecraftBlockList.barrier.toString();}
    	try {m.floorCoverBlocks = florCoverCombo.getValue().toString();}catch(Exception ex) {m.floorCoverBlocks = MinecraftBlockList.water.toString();}
    	try {m.floorBlocks      = florCombo.getValue().toString()     ;}catch(Exception ex) {m.floorBlocks = MinecraftBlockList.barrier.toString();}
    	//pull numbers out of text fields.
    	//The numbers should be validated, but just in case of an error they will get defaults.
    	try {m.height  = Integer.parseInt(heightText.getText());}catch(NumberFormatException ex) {m.height  = 2;}
    	try {m.xOffset = Integer.parseInt(xText.getText());}catch(NumberFormatException ex) {m.xOffset = 0;}
    	try {m.yOffset = Integer.parseInt(yText.getText());}catch(NumberFormatException ex) {m.yOffset = 0;}
    	try {m.zOffset = Integer.parseInt(zText.getText());}catch(NumberFormatException ex) {m.zOffset = 0;}
    	//Fill in the grid
    	m.blocks = maze.getAllBlocks();
    	m.selection = maze.getSelection();
    	//Note that we could pull rows/cols from either the MazeView or rowsText/colsText
    	//but in this case we have chosen to use the maze object because it requires less
    	//parsing/exception handling.  We are assuming that the other validators ensured
    	//that these two data sources were equal at any point where syncMazeToUi() is called.
    	m.rows = maze.getRows();
    	m.cols = maze.getCols();
    	m.mazeSelectionDeltaRows = mazeSelectionDeltaRows;
    	m.mazeSelectionDeltaCols = mazeSelectionDeltaCols;
    	m.mazeSelectionDragging = mazeSelectionDragging;    	
        m.mazeMouseDownSelection = mazeMouseDownSelection;
        m.mazeMouseDownUnselectedBlocks = mazeMouseDownUnselectedBlocks;
        m.mazeMouseDownSelectedBlocks = mazeMouseDownSelectedBlocks; 	
    }
    
    private void syncUiToMaze(Maze m) {
    	if(m == null)return;
    	//Bounds check rows, cols, height
		if(m.height < minHeight)m.height = minHeight;
		if(m.height > maxHeight)m.height = maxHeight;
		if(m.rows < minRows)m.rows = minRows;
		if(m.rows > maxRows)m.rows = maxRows;
		if(m.cols < minCols)m.cols = minCols;		
		if(m.cols > maxCols)m.cols = maxCols;		
		
		//Make a good array of blocks.
		int[][] blocks = new int[m.rows][m.cols];		
		//Copy the blocks from the maze to the good array.
		//Note that the maze could have come from the undo/redo system.
		//But it also may have come from a JSON file.  In which case, it could
		//have null/missing elements that wee need to clean up.		
		if(m.blocks != null) {
			int maxRow = Math.min(m.rows, m.blocks.length);
			for(int row=0;row<maxRow;row++) {
				int maxCol = 0;
				if(m.blocks[row] != null)maxCol = Math.min(m.cols, m.blocks[row].length);
				for(int col=0;col<maxCol;col++) {
					blocks[row][col] = m.blocks[row][col];
				}
			}    					
		}
		m.blocks = blocks;

		//Make a good selection array.
		boolean[][] selection = new boolean[m.rows][m.cols];
		//Copy the selection from the maze to the good array.
		//Note that the maze could have come from the undo/redo system.
		//But it also may have come from a JSON file.  In which case, it could
		//have null/missing elements that wee need to clean up.
		if(m.selection != null) {
			int maxRow = Math.min(m.rows, m.selection.length);
			for(int row=0;row<maxRow;row++) {
				int maxCol = 0;
				if(m.selection[row] != null)maxCol = Math.min(m.cols, m.selection[row].length);
				for(int col=0;col<maxCol;col++) {
					selection[row][col] = m.selection[row][col];
				}
			}    					
		}
		m.selection = selection;
		
		MinecraftBlockList roofBlocks = MinecraftBlockList.safeValueOf(m.roofBlocks);
		if(roofBlocks == null)roofBlocks = MinecraftBlockList.barrier;

		MinecraftBlockList wallBlocks = MinecraftBlockList.safeValueOf(m.wallBlocks);
		if(wallBlocks == null)wallBlocks = MinecraftBlockList.barrier;
		
		MinecraftBlockList fillBlocks = MinecraftBlockList.safeValueOf(m.fillBlocks);
		if(fillBlocks == null)fillBlocks = MinecraftBlockList.air;
		
		MinecraftBlockList florCoverBlocks = MinecraftBlockList.safeValueOf(m.floorCoverBlocks);
		if(florCoverBlocks == null)florCoverBlocks = MinecraftBlockList.air;

		MinecraftBlockList florBlocks = MinecraftBlockList.safeValueOf(m.floorBlocks);
		if(florBlocks == null)florBlocks = MinecraftBlockList.barrier;
		
		roofCombo.setValue(roofBlocks);
		fillCombo.setValue(fillBlocks);
		wallCombo.setValue(wallBlocks);
		florCoverCombo.setValue(florCoverBlocks);
		florCombo.setValue(florBlocks);
		relativeCheck.setSelected(m.relativeXYZ);
		carveCheck.setSelected(m.carveMode);
		fillCheck.setSelected(m.addFill);
		florCheck.setSelected(m.addFloor);
		florCoverCheck.setSelected(m.addFloorCover);
		roofCheck.setSelected(m.addRoof);
		heightText.setText(String.valueOf(m.height));
		xText.setText(String.valueOf(m.xOffset));
		yText.setText(String.valueOf(m.yOffset));
		zText.setText(String.valueOf(m.zOffset));
		rowsText.setText(String.valueOf(m.rows));
		colsText.setText(String.valueOf(m.cols));
    	oldHeightText = heightText.getText();
    	oldXText = xText.getText();
    	oldYText = yText.getText();
    	oldZText = zText.getText();
    	oldRowsText = rowsText.getText();
    	oldColsText = rowsText.getText();
		maze.resize(m.rows, m.cols, 0, 0, 0);
		maze.setBlocks(0,0,m.blocks);
		maze.setSelection(0, 0, m.selection);
		maze.repaint();
		mazeSelectionDeltaRows = m.mazeSelectionDeltaRows;
		mazeSelectionDeltaCols = m.mazeSelectionDeltaCols;
		mazeSelectionDragging = m.mazeSelectionDragging;		
        mazeMouseDownSelection = m.mazeMouseDownSelection;
        mazeMouseDownUnselectedBlocks = m.mazeMouseDownUnselectedBlocks;
        mazeMouseDownSelectedBlocks = m.mazeMouseDownSelectedBlocks;		
    }
    
}
