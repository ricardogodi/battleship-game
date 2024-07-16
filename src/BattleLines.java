import javafx.animation.PauseTransition;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.io.File;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import java.util.Random;
import javafx.util.Duration;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;

public class BattleLines extends Application {

	//MediaPlayer hitSound = new MediaPlayer(new Media(new File("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/hit.wav").toURI().toString()));
	//MediaPlayer hitSound = new MediaPlayer(new Media(new File("hit.wav").toURI().toString()));
	
	private TextField playerFeedback;
	private TextField adversary1Feedback;
	private TextField adversary2Feedback;

	private Label playerStatus;
	private Label adversary1Status;
	private Label adversary2Status;

	private BorderPane root;
	private VBox leftPanel;

	private int turnDuration = 1;
	private int winLosePauseDuration = 5;
	private PauseTransition defaultPause = new PauseTransition(Duration.seconds(0)); 
	private PauseTransition adversary1TurnPause = new PauseTransition(Duration.seconds(turnDuration)); 
	private PauseTransition adversary2TurnPause = new PauseTransition(Duration.seconds(turnDuration)); 
	private PauseTransition winLosePause = new PauseTransition(Duration.seconds(winLosePauseDuration));
	
	private Button playAgainBtn;
	
	private Button easyBtn;
	private Button mediumBtn;
	private Button hardBtn;
	
	/*
	 *  Boards:
	 *  
	 *  In playerArray[][], adversary1Array[][], and adversary2Array[][],
	 * 	 
	 *   (-1): Means a hit coordinate
	 *   0: Means an empty coordinate
	 *   1: Means there is a ship at this coordinate
	 */ 
	
	private GridPane playerBoard;
	private Button playerBoardArray[][] = new Button[BOARD_SIZE][BOARD_SIZE];  // For easier access to each button
	private int playerArray[][] = new int[BOARD_SIZE][BOARD_SIZE]; // Number matrix representation of the board

	private GridPane adversary1Board;
	private Button adversary1BoardArray[][] = new Button[BOARD_SIZE][BOARD_SIZE];	 // For easier access to each button
	private int adversary1Array[][] = new int[BOARD_SIZE][BOARD_SIZE];	// Number matrix representation of the board
	private EventHandler<ActionEvent> gameButtonHandler1;

	private GridPane adversary2Board;
	private Button adversary2BoardArray[][] = new Button[BOARD_SIZE][BOARD_SIZE];// For easier access to each button
	private int adversary2Array[][] = new int[BOARD_SIZE][BOARD_SIZE]; // Number matrix representation of the board
	private EventHandler<ActionEvent> gameButtonHandler2;

	private int playerHealth = 8;
	private int adversary1Health = 8;
	private int adversary2Health = 8;
	
	private boolean easyDifficulty = false;
	private boolean mediumDifficulty = false;
	private boolean hardDifficulty = false;

	private static final int BOARD_SIZE = 10;
	private static final int CELL_SIZE = 70;
	private static final int NUM_SHIPS = 8;
	private static final int SHIP_IMAGE_SIZE = 200;
	private static final int MISS_IMAGE_SIZE = 25;
	private static final int HIT_IMAGE_SIZE = 25;
	private static final int BOARD_MAX_HEIGTH = 400;
	private static final int BOARD_MAX_WIDTH = 500;
	private static final int BOARD_MIN_HEIGTH = 400;
	private static final int BOARD_MIN_WIDTH = 500;

	private Image[] shipImages = new Image[NUM_SHIPS];
	private ImageView[] shipImageViews = new ImageView[NUM_SHIPS];

	public static void main(String[] args) {
		launch(args);	
	}

	public void start(Stage primaryStage) throws Exception {

		String labelLayout = "-fx-font-size: 18;\n" +
				"-fx-font-family: 'Arial Black';\n" +
				"-fx-text-fill: #000000;";

		String currentTurnLayout = "-fx-border-color: DimGrey;\n" +
				"-fx-border-insets: 5;\n" +
				"-fx-border-width: 8;\n" + 
				"-fx-font-size: 16;\n" +
				"-fx-font-color: white;\n" + 
				"-fx-font-weight: 300;\n" + 
				"-fx-font-family: 'Arial Black';"+
				"-fx-background-color: #84D98E";

		String waitLayout = "-fx-border-color: DimGrey;\n" +
				"-fx-border-insets: 5;\n" +
				"-fx-border-width: 8;\n" + 
				"-fx-font-size: 16;\n" +
				"-fx-font-color: white;\n" + 
				"-fx-font-weight: 300;\n" + 
				"-fx-font-family: 'Arial Black';" +
				"-fx-background-color: LightSkyBlue ";


		String feedBackLayout = "-fx-border-color: DimGrey;\n" +
				"-fx-border-insets: 5;\n" +
				"-fx-border-width: 8;\n" + 
				"-fx-font-size: 16;\n" +
				"-fx-font-color: white;\n" + 
				"-fx-font-weight: 300;\n" + 
				"-fx-font-family: 'Arial Black';\n" +
				"-fx-background-color: LightSkyBlue ";

		String stdLayout = "-fx-font-size: 30;\n" +
				"-fx-font-weight: 300;\n" + 
				"-fx-font-family: 'Arial Black'";

		playAgainBtn = new Button("Play Again");
		playAgainBtn.setStyle(stdLayout);

		easyBtn = new Button("Easy");
		mediumBtn = new Button("Medium");
		hardBtn = new Button("Hard");
		
		easyBtn.setStyle(stdLayout);
		mediumBtn.setStyle(stdLayout);
		hardBtn.setStyle(stdLayout);
		
		easyBtn.setPrefWidth(200);
		mediumBtn.setPrefWidth(200);
		hardBtn.setPrefWidth(200);
		
		// Player's Feedback TextFields
		playerFeedback = new TextField();
		playerFeedback.setPrefSize(300, 100);
		playerFeedback.setStyle(feedBackLayout);
		playerFeedback.setText("Place your ships!");
		playerFeedback.setAlignment(Pos.CENTER);
		playerFeedback.setEditable(false);

		adversary1Feedback = new TextField();
		adversary1Feedback.setPrefSize(300, 100);
		adversary1Feedback.setStyle(feedBackLayout);
		adversary1Feedback.setAlignment(Pos.CENTER);
		adversary1Feedback.setEditable(false);

		adversary2Feedback = new TextField();
		adversary2Feedback.setPrefSize(300, 100);
		adversary2Feedback.setStyle(feedBackLayout);
		adversary2Feedback.setAlignment(Pos.CENTER);
		adversary2Feedback.setEditable(false);

		
		// The default paus 
		defaultPause.setOnFinished(e -> {
			
			playerFeedback.setStyle(waitLayout); // User just moved, now change to the wait layout

			if (adversary1Health != 0 ) {  // Adversary 1 is alive
				adversary1Feedback.setStyle(currentTurnLayout);    // Change to turn layout
				adversary1Feedback.clear();
				adversary1TurnPause.play(); 
			} else if (adversary2Health != 0) {  // Adversary 2 is ALIVE AND Adversary1 is DEAD
				adversary2Feedback.setStyle(currentTurnLayout);    // Change to turn layout
				adversary2Feedback.clear();
				adversary2TurnPause.play(); 
			}
		});

		adversary1TurnPause.setOnFinished(event -> {

			int randomTarget;

			while (true) {  // Generate a valid randomTarget
				
				randomTarget = biasedNum(); // Get 0 or 1 randomly
				
				if (randomTarget == 0 && playerHealth != 0) {  // User is still ALIVE
					playerHealth = fightBack(playerBoardArray, playerArray, playerHealth, 1, 0); // Attack the user
					break;
				} else if (randomTarget == 1 && adversary2Health != 0) { // Adversary 2 is still ALIVE
					adversary2Health = fightBack(adversary2BoardArray, adversary2Array, adversary2Health, 1, 2); // Attack Adversary 2
					break;
				}
			}

			adversary1Feedback.setStyle(waitLayout); 

			if (adversary2Health != 0) { // Adversary 2 is NOT DEAD
			
				adversary2Feedback.setStyle(currentTurnLayout); 
				adversary2Feedback.clear();
				adversary2TurnPause.play();  // Not it's adversary's 2 turn
				
			} else { // Adversary 2 is DEAD
				
				playerFeedback.setStyle(currentTurnLayout); // Now it's the user's turn
				playerFeedback.setText("YOUR TURN!");
			}
			
			if (playerHealth == 0 && adversary2Health == 0) {  // Check if Adversary 1 just WON
				adversary1Feedback.setText("ADVERSARY 1 WON! ");
				playerFeedback.setText("You lost!");
				adversary2Feedback.setStyle(waitLayout);
				playerFeedback.setStyle(waitLayout);
				winLosePause.play();  // WIN OR LOSE PAUSE
			}

			updateStatus();  // Update players' health status
		});

		adversary2TurnPause.setOnFinished(event -> {

			int randomTarget;

			while (true) {  // Generate a valid randomTarget

				randomTarget = biasedNum();

				if (randomTarget == 0 && playerHealth != 0) {   // User is still ALIVE
					playerHealth = fightBack(playerBoardArray, playerArray, playerHealth, 2, 0); // Attack the user
					break;

				} else if (randomTarget == 1 && adversary1Health != 0) {  // Adversary 1 is still ALIVE
					adversary1Health = fightBack(adversary1BoardArray, adversary1Array,adversary1Health, 2, 1);  // Attack Adversary 1
					break;
				}
			}
			
			adversary2Feedback.setStyle(waitLayout); 

			if (playerHealth == 0 && adversary1Health != 0) {   // Adv2 KILLED player
				playerFeedback.setText("You lost!");
				
				adversary1Feedback.setStyle(currentTurnLayout); 
				adversary1Feedback.clear();
				adversary1TurnPause.play();      
			}
			
			if (playerHealth != 0) {      // User is ALIVE, now it's his turn
				playerFeedback.setStyle(currentTurnLayout); 
				playerFeedback.setText("YOUR TURN!");
			}

			if (playerHealth == 0 && adversary1Health == 0) {		// Check if Adversary 1 just WON
				adversary2Feedback.setText("ADVERSARY 2 WON! ");
				playerFeedback.setText("You lost!");
				winLosePause.play();
			} 
			
			updateStatus();
		});

		gameButtonHandler1 = new EventHandler<ActionEvent>() {  // Event Handler for attacking Adversary 1

			public void handle(ActionEvent e) {	

				Button b = (Button) e.getSource();
				int row = GridPane.getRowIndex(b);
				int col = GridPane.getColumnIndex(b);

				if (adversary1Array[row][col] == -1) {   // Invalid row, col
					playerFeedback.setText("Coordinate already chosen");
				} else  { 		// Valid row, col

					if (adversary1Array[row][col] == 1) {  // HIT 
						adversary1Array[row][col] = -1;    // Mark this spot
						//hitSound.play();
						adversary1Health--;  

						// Place 'boom' image at row, col
						ImageView hit = new ImageView(new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/boom.png"));
						//ImageView hit = new ImageView(new Image("boom.png")); DOESN'T WORK
						hit.setPreserveRatio(true);
						hit.setFitWidth(HIT_IMAGE_SIZE);
						hit.setFitHeight(HIT_IMAGE_SIZE);
						b.setGraphic(hit);
						playerFeedback.setText("You hit a ship!");
						adversary1Feedback.setText("Adversary 1 lost a ship!");

						if (adversary1Health == 0 && adversary2Health == 0) {  // Check if user just WON
							adversary1Board.setDisable(true);
							playerFeedback.setText("YOU WIN!");
							adversary1Feedback.setText("Adversary 1 lost!");
							winLosePause.play();
						} else if (adversary1Health == 0) {   // Check if user just KILLED adversary 1
							adversary1Board.setDisable(true);
							playerFeedback.setText("You killed Adversary 1!");
							adversary1Feedback.setText("Adversary 1 lost!");

						}

					} else {  			// MISS
						adversary1Array[row][col] = -1;  // Mark spot
						
						// Place 'X' image at row, col
						ImageView miss = new ImageView(new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/x.png"));
						miss.setPreserveRatio(true);
						miss.setFitWidth(MISS_IMAGE_SIZE);
						miss.setFitHeight(MISS_IMAGE_SIZE);
						b.setGraphic(miss);
						playerFeedback.setText("You missed!");
					}
					
					defaultPause.play();  // default pause will call adversary 1 pause
				}

				updateStatus();   // update players' health
			} 
		};

		gameButtonHandler2 = new EventHandler<ActionEvent>() {		 // Event Handler for attacking Adversary 2

			public void handle(ActionEvent e) {		

				Button b = (Button) e.getSource();
				int row = GridPane.getRowIndex(b);
				int col = GridPane.getColumnIndex(b);

				if (adversary2Array[row][col] == -1) {			 // Invalid row, col
					playerFeedback.setText("Coordinate already chosen");
				} else  { 			// Valid row, col

					if (adversary2Array[row][col] == 1) {  // HIT 
						adversary2Array[row][col] = -1;		 // Mark this spot
						adversary2Health--;
						//hitSound.play();

						// Place 'boom' image at row, col
						ImageView hit = new ImageView(new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/boom.png"));
						hit.setPreserveRatio(true);
						hit.setFitWidth(HIT_IMAGE_SIZE);
						hit.setFitHeight(HIT_IMAGE_SIZE);
						b.setGraphic(hit);
						playerFeedback.setText("You hit a ship!");
						adversary2Feedback.setText("Adversary 2 lost a ship!");

						if (adversary1Health == 0 && adversary2Health == 0) {	 // Check if user just WON
							adversary2Board.setDisable(true);
							playerFeedback.setText("YOU WIN!");
							adversary2Feedback.setText("Adversary 2 lost!");
							winLosePause.play();
						} else if (adversary2Health == 0) {		 // Check if user just KILLED adversary 2
							adversary2Board.setDisable(true);
							playerFeedback.setText("You killed adversary 2");
							adversary2Feedback.setText("Adversary 2 lost!");
						}

					} else {  // MISS
						adversary2Array[row][col] = -1;		 // Mark spot
						
						// Place 'X' image at row, col
						ImageView miss = new ImageView(new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/x.png"));
						miss.setPreserveRatio(true);
						miss.setFitWidth(MISS_IMAGE_SIZE);
						miss.setFitHeight(MISS_IMAGE_SIZE);
						b.setGraphic(miss);
						playerFeedback.setText("You missed!");
					}
					defaultPause.play();		  // default pause will call adversary 1 pause
				}
				updateStatus();
			}
		};

		playerBoard = new GridPane();

		// Create button grid for player
		for(int col = 0; col < BOARD_SIZE; col++) {
			for(int row = 0; row < BOARD_SIZE; row++) {
				Button b = new Button(); 
				b.setStyle("-fx-background-color: #89cff0;");
				
				b.setOnDragDropped(new EventHandler<DragEvent>() {  // Set drag-dropped event on button to place the ships
					public void handle(DragEvent event) {
						
						if (event.getGestureSource() instanceof ImageView) {
							Dragboard db = event.getDragboard();
							boolean success = false;
							if (db.hasImage()) {
								// Get dropped image
								Image shipImage = db.getImage();
								// Place ship image on button
								ImageView imageView = new ImageView(shipImage);
								imageView.setPreserveRatio(true);
								imageView.setFitWidth(50);
								imageView.setFitHeight(50);
								
								Button targetButton = (Button) event.getTarget(); // Get reference to the button where the ship was dropped
								int row = GridPane.getRowIndex(targetButton);
								int col = GridPane.getColumnIndex(targetButton);
								playerArray[row][col] = 1;  // Mark in the matrix representation

								b.setGraphic(imageView);
								success = true;
							}
							event.setDropCompleted(success);
						}
						event.consume();
					}
				});

				b.setPrefSize(CELL_SIZE, CELL_SIZE);
				b.setMaxSize(CELL_SIZE, CELL_SIZE); 
				playerBoardArray[row][col] = b;  // This will make it easier to access each button
				playerBoard.add(b, col, row);   // Finally add the button to the grid
			}
		}

		playerBoard.setHgap(5);  // Make space between the buttons
		playerBoard.setVgap(5);
		playerBoard.setAlignment(Pos.CENTER);

		adversary1Board = new GridPane();
		adversary1Board.setDisable(true);

		// Create button grid for Adversary 1
		for(int col = 0; col < BOARD_SIZE; col++) {
			for(int row = 0; row < BOARD_SIZE; row++) {
				Button b = new Button();
				b.setStyle("-fx-background-color: #89cff0;");
				b.setPrefSize(CELL_SIZE, CELL_SIZE);
				b.setMaxSize(CELL_SIZE, CELL_SIZE); 
				b.setOnAction(gameButtonHandler1);
				adversary1BoardArray[row][col] = b;   // This will make it easier to access each button	
				adversary1Board.add(b, col, row);	 // Finally add the button to the grid
			}
		}

		adversary2Board = new GridPane();
		adversary2Board.setDisable(true);
		for(int col = 0; col < BOARD_SIZE; col++) {
			for(int row = 0; row < BOARD_SIZE; row++) {
				Button b = new Button();
				b.setStyle("-fx-background-color: #89cff0;");
				b.setPrefSize(CELL_SIZE, CELL_SIZE);
				b.setMaxSize(CELL_SIZE, CELL_SIZE); 
				b.setOnAction(gameButtonHandler2);
				adversary2BoardArray[row][col] = b;  // This will make it easier to access each button	
				adversary2Board.add(b, col, row);    // Finally add the button to the grid
			}
		}

		playerBoard.setMaxHeight(600);
		playerBoard.setMaxWidth(800);
		playerBoard.setMinHeight(300);
		playerBoard.setMinWidth(300);

		adversary1Board.setMaxHeight(BOARD_MAX_HEIGTH);
		adversary1Board.setMaxWidth(BOARD_MAX_WIDTH);
		adversary1Board.setMinHeight(BOARD_MIN_HEIGTH);
		adversary1Board.setMinWidth(BOARD_MIN_WIDTH);

		adversary2Board.setMaxHeight(BOARD_MAX_HEIGTH);
		adversary2Board.setMaxWidth(BOARD_MAX_WIDTH);
		adversary2Board.setMinHeight(BOARD_MIN_HEIGTH);
		adversary2Board.setMinWidth(BOARD_MIN_WIDTH);

		placeAdversaryShips(adversary1Array);  // Place the ships in the matrix representation
		placeAdversaryShips(adversary2Array);  // Place the ships in the matrix representation

		adversary1Board.setHgap(5);
		adversary1Board.setVgap(5);

		adversary2Board.setHgap(5);
		adversary2Board.setVgap(5);

		// create draggable ship images
		for (int i = 0; i < NUM_SHIPS; i++) {
			Image image = new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/" + i + ".png");
			shipImages[i] = image;
			ImageView imageView = new ImageView(image);
			imageView.setPreserveRatio(true);
			imageView.setFitWidth(SHIP_IMAGE_SIZE);
			imageView.setFitHeight(SHIP_IMAGE_SIZE);
			imageView.setUserData(i); // add ship index as user data
			imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
					ClipboardContent content = new ClipboardContent();
					content.putImage(image);
					content.put(DataFormat.PLAIN_TEXT, "ship" + imageView.getUserData());
					dragboard.setContent(content);
					event.consume();
				}
			});

			imageView.setOnDragDone(new EventHandler<DragEvent>() {
				public void handle(DragEvent event) {
					// remove the image from the left panel if it was dropped on the board
					if (event.getTransferMode() == TransferMode.MOVE) {
						((VBox) imageView.getParent()).getChildren().remove(imageView);
						
						// Check if the left panel is empty, meaning all the images were dragged and dropped.
						// THE GAME IS NOW READY TO START
						if (leftPanel.getChildren().isEmpty()) {
							
							adversary1Board.setDisable(false);
							adversary2Board.setDisable(false);
							playerFeedback.setStyle(currentTurnLayout); 
							playerFeedback.setText("YOU START!");
						}
					}
					event.consume();
				}
			});
			shipImageViews[i] = imageView;
		}

		// Place ships at the left panel
		leftPanel = new VBox();
		leftPanel.setSpacing(30);
		leftPanel.setMinSize(200, 400);
		leftPanel.setAlignment(Pos.CENTER);

		for (ImageView imageView : shipImageViews) {
			leftPanel.getChildren().add(imageView);
		}

		// Set drag over event
		playerBoard.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getGestureSource() instanceof ImageView) {
					event.acceptTransferModes(TransferMode.MOVE);
				}
				event.consume();
			}
		});

		
		// GUI OBJECTS:
		adversary1Status = new Label("Ships remaining: " + adversary1Health);
		adversary1Status.setStyle(labelLayout);
		
		VBox adversary1Info = new VBox(adversary1Feedback, adversary1Status);
		adversary1Info.setAlignment(Pos.CENTER);
		adversary1Info.setSpacing(20);

		HBox adversary1Box = new HBox(adversary1Info, adversary1Board);
		adversary1Box.setAlignment(Pos.CENTER);
		adversary1Box.setSpacing(30);
		
		adversary2Status = new Label("Ships remaining: " + adversary2Health);
		adversary2Status.setStyle(labelLayout);
		
		VBox adversary2Info = new VBox(adversary2Feedback, adversary2Status);
		adversary2Info.setAlignment(Pos.CENTER);
		adversary2Info.setSpacing(20);

		HBox adversary2Box = new HBox(adversary2Board, adversary2Info);
		adversary2Box.setAlignment(Pos.CENTER);
		adversary2Box.setSpacing(30);

		HBox.setMargin(adversary1Board, new Insets(20,0,10,0));
		HBox.setMargin(adversary2Board, new Insets(20,0,10,0));

		HBox adversaries = new HBox(adversary1Box, adversary2Box);
		adversaries.setAlignment(Pos.CENTER);
		adversaries.setSpacing(30);

		playerStatus = new Label("Ships remaining: " + playerHealth);
		playerStatus.setStyle(labelLayout);
		
		VBox playerInfo = new VBox(playerFeedback, playerStatus);
		playerInfo.setAlignment(Pos.CENTER);
		playerInfo.setSpacing(20);

		HBox playerBox = new HBox(playerBoard, playerInfo);
		playerBox.setAlignment(Pos.CENTER);
		playerBox.setSpacing(30);

		root = new BorderPane();
		root.setTop(adversaries);
		root.setLeft(leftPanel);
		root.setCenter(playerBox);

		BorderPane.setAlignment(adversaries, Pos.CENTER);
		BorderPane.setMargin(leftPanel, new Insets(0,0,0,125));

		Screen screen = Screen.getPrimary();
		double screenWidth = screen.getBounds().getWidth();
		double screenHeight = screen.getBounds().getHeight();
		Scene gameScene = new Scene(root, screenWidth,screenHeight);

		playAgainBtn.setOnAction(event -> {  // RESET BOARDS AND PLAYERS' INFORMATION
			playerFeedback.setText("Place your ships!");
			adversary1Feedback.clear();
			adversary2Feedback.clear();
			
			playerFeedback.setStyle(waitLayout);
			adversary1Feedback.setStyle(waitLayout);
			adversary2Feedback.setStyle(waitLayout);
			
			playerArray = clearBoard(playerBoardArray);
			adversary1Array = clearBoard(adversary1BoardArray);
			adversary2Array = clearBoard(adversary2BoardArray);

			playerBoard.setDisable(false);
			adversary1Board.setDisable(true);
			adversary2Board.setDisable(true);

			placeAdversaryShips(adversary1Array);
			placeAdversaryShips(adversary2Array);

			// Remove existing image views from leftPanel
			leftPanel.getChildren().removeAll(shipImageViews);

			for (ImageView imageView : shipImageViews) {
				leftPanel.getChildren().add(imageView);    // Place ships on the leftPanel again
			}

			playerHealth = 8;
			adversary1Health = 8;
			adversary2Health = 8;

			updateStatus();

			primaryStage.setScene(gameScene);
			primaryStage.show();
		});
		
		BorderPane startingPane = new BorderPane();	
		
		Label select = new Label("Select a Difficulty:");
		Label title = new Label("Battle Lines");
		
		
		select.setStyle("-fx-font-size: 37;\n" +
				"-fx-font-family: 'Arial Black';\n" +
				"-fx-text-fill: #000000;");
		
		title.setStyle("-fx-font-family: 'Arial Black';\n" +
	               "-fx-font-size: 100px;\n" +
	               "-fx-font-weight: bold;\n" +
	               "-fx-text-fill: #FFFFFF;\n" +
	               "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);\n" +
	               "-fx-padding: 10px;");
		
		
		VBox difficultyBox = new VBox(select, easyBtn, mediumBtn, hardBtn);
		startingPane.setCenter(difficultyBox);
		startingPane.setTop(title);
		
		BorderPane.setAlignment(title, Pos.BOTTOM_CENTER);
		BorderPane.setMargin(title, new Insets(100,0,0,0));
		Image coolShip1 = new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/coolShip1.png");
		ImageView coolShip1IV = new ImageView(coolShip1);
		
		Image coolShip2 = new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/coolShip2.png");
		ImageView coolShip2IV = new ImageView(coolShip2);
		startingPane.setLeft(coolShip1IV);
		startingPane.setRight(coolShip2IV);
		
		BorderPane.setAlignment(coolShip1IV, Pos.CENTER);
		BorderPane.setAlignment(coolShip2IV, Pos.CENTER);
		coolShip1IV.setFitWidth(500); // set width to 200 pixels
		coolShip1IV.setFitHeight(500); // set height to 200 pixels
		
		coolShip2IV.setFitWidth(500); // set width to 200 pixels
		coolShip2IV.setFitHeight(500); // set height to 200 pixels
		
		coolShip1IV.setPreserveRatio(true);
		coolShip2IV.setPreserveRatio(true);
		
		BorderPane.setMargin(coolShip1IV, new Insets(0,0,0,200));
		BorderPane.setMargin(coolShip2IV, new Insets(0,200,0,0));
		
		
		difficultyBox.setAlignment(Pos.CENTER);
		difficultyBox.setSpacing(30);
		
		Scene startingScene =  new Scene(startingPane, screenWidth,screenHeight);
		
		BorderPane vbox = new BorderPane(playAgainBtn);
		Scene playAgainScene = new Scene(vbox, screenWidth,screenHeight);

		winLosePause.setOnFinished(event -> {
			primaryStage.setScene(playAgainScene);
			primaryStage.show();
		});
		
		easyBtn.setOnAction(event -> {  
			
			easyDifficulty = true;	
			primaryStage.setScene(gameScene);
			primaryStage.show();
		});
		
		mediumBtn.setOnAction(event -> {  
			
			mediumDifficulty = true;
			primaryStage.setScene(gameScene);
			primaryStage.show();
		});
		
		hardBtn.setOnAction(event -> {  
			
			hardDifficulty = true;
			primaryStage.setScene(gameScene);
			primaryStage.show();
		});

		Image backgroundImage = new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/ocean.png");
		BackgroundImage background= new BackgroundImage(backgroundImage,
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
		
		root.setBackground(new Background(background));
		vbox.setBackground(new Background(background));
		startingPane.setBackground(new Background(background));

		primaryStage.setScene(startingScene);
		//primaryStage.setScene(playAgainScene);
		primaryStage.setTitle("Battle Lines");
		primaryStage.show();
	}

	// Method to clear the board arrays and button graphics
	private int [][] clearBoard(Button[][] board) {

		int  boardArray[][] = new int[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col].setGraphic(null);
				board[row][col].setDisable(false);
			}
		}
		return boardArray;
	}

	void placeAdversaryShips (int array[][]) {  // Place ships in the matrix representation of the board
		Random rand = new Random();
		int randRow;
		int randCol;
		int counter = 0;
		while(counter < NUM_SHIPS) {  // Loop until we generate NUM_SHIPS valid coordinates
			
			randRow = rand.nextInt(BOARD_SIZE);
			randCol = rand.nextInt(BOARD_SIZE);	

			if (array[randRow][randCol] != 1) {
				array[randRow][randCol] = 1;
				counter++;
			}
		}
	}

	void printBoardState(int board[][] ) {  // For debugging purposes
		System.out.println("Printing State");

		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	// 0 means USER,
	// 1 means ADVERSARY 1
	// 2 means ADVERSARY 2
	private int fightBack(Button buttonBoard[][], int board[][], int health, int attacker, int target) {

		Random rand = new Random();

		int randRow;
		int randCol;

		do {
			randRow = rand.nextInt(BOARD_SIZE);
			randCol = rand.nextInt(BOARD_SIZE);				
		} while (board[randRow][randCol] == -1); // GENERATE A VALID COORDINATE

		if (board[randRow][randCol] == 1) { // Hit
			board[randRow][randCol] = -1;  // Mark spot
			//hitSound.play();
			
			health--;
			ImageView hit = new ImageView(new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/boom.png"));
			hit.setPreserveRatio(true);

			if (target == 0) {   // The images for the user will be larger
				hit.setFitWidth(40); 
				hit.setFitHeight(40);
			} else {   // adversary
				hit.setFitWidth(HIT_IMAGE_SIZE);   
				hit.setFitHeight(HIT_IMAGE_SIZE);
			}

			Button playerButton = buttonBoard[randRow][randCol];
			playerButton.setGraphic(hit);

			if (attacker == 1) {   // Attacker is Adversary 1

				if (target == 0) {  // Attack the player

					playerFeedback.setText("You lost a ship! :(");
					adversary1Feedback.setText("TAKE THAT!!!");

					if (health == 0) {

						playerFeedback.setText("You Lost!");
						playerBoard.setDisable(true);
					} 

				} else if (target == 2) {   // Attack adversary 2
					adversary1Feedback.setText("TAKE THAT!!!");
					adversary2Feedback.setText("Adversary 2 lost a ship! ");

					if (health == 0) {
						//playerFeedback.setText("Adversary 2 Lost!");
						adversary2Feedback.setText("Adversary 2 lost! ");
						adversary2Board.setDisable(true);
					} 
				}
			} else if (attacker == 2) {  // Attacker is Adversary 2

				if (target == 0) {      // Attack the player

					playerFeedback.setText("You lost a ship! :(");
					adversary2Feedback.setText("TAKE THAT!!!");

					if (health == 0) {
						playerFeedback.setText("You Lost!");
						playerBoard.setDisable(true);
					} 	

				} else if (target == 1) {      // Attack the adversary 1
					adversary2Feedback.setText("TAKE THAT!!!");
					adversary1Feedback.setText("Adversary 1 lost a ship! ");

					if (health == 0) {
						adversary1Feedback.setText("Adversary 1 lost! ");
						adversary1Board.setDisable(true);
					} 
				}
			}
		} else { 		// MISS
			board[randRow][randCol] = -1;
			ImageView miss = new ImageView(new Image("file:/Users/ricardogonzalez/eclipse-workspace/BattleLinesJavaFX/src/x.png"));
			miss.setPreserveRatio(true);

			if (target == 0) {
				miss.setFitWidth(40);
				miss.setFitHeight(40);
			} else {
				miss.setFitWidth(MISS_IMAGE_SIZE);
				miss.setFitHeight(MISS_IMAGE_SIZE);
			}

			Button playerButton = buttonBoard[randRow][randCol];
			playerButton.setGraphic(miss);

			if (attacker == 1) {
				adversary1Feedback.setText("Adversary 1 missed! ");

			} else if (attacker == 2) {
				adversary2Feedback.setText("Adversary 2 missed! ");
			}	
		}
		return health;
	}

	private int biasedNum() {   // Get a biased number.
		Random rand = new Random();
		
		int randomTarget = rand.nextInt(10);  // Generate number between 0-9 INCLUSIVE
		
		if (easyDifficulty) {  				// 20%
			int arr[] = {1,1,1,0,1,1,1,1,0,1};
			return arr[randomTarget];
		} else if (mediumDifficulty) {      // 50%
			int arr[] = {1,1,0,0,1,0,1,1,0,0};
			return arr[randomTarget];
		} else if (hardDifficulty) {        // 80%
			int arr[] = {1,0,0,0,0,0,0,1,0,0};
			return arr[randomTarget];
		}
		
		return 0;
	}
	
	private void updateStatus() {
		playerStatus.setText("Ships Remaining: " + playerHealth);
		adversary1Status.setText("Ships Remaining: " + adversary1Health);
		adversary2Status.setText("Ships Remaining: " + adversary2Health);
	}

	private void printState() {  // For debugging purposes
		System.out.println("Player Health: " + playerHealth);
		System.out.println("Adversary 1 Health: " + adversary1Health);
		System.out.println("Adversary 2 Health: " + adversary2Health);

	}
}