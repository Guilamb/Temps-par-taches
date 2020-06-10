package application;
	

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;



/* Pour creer des task de test rapidement pour effectuer des tests
 
Task essais = new Task("task 1",LocalDate.now(),"test des taches",Etats.enCours);
Task essais2 = new Task("task main test",LocalDate.now(),"test des taches",Etats.enCours);
Subtask sousTache1 = new Subtask(LocalDate.now());

taskList.add(essais);
taskList.add(essais2);
essais.addSubtask(sousTache1);

*/
public class Main extends Application {
	
	
	
	private void movePivot(Node node, double x, double y){
        node.getTransforms().add(new Translate(-x,-y));
        node.setTranslateX(x); node.setTranslateY(y);
    }
	
	private static final int NB_CHAR_MAX_TEXT_AREA = 20;
	private Task selectedTask = new Task();
	private Subtask selectedSubtask = new Subtask();
	private Chrono chrono = new Chrono(selectedTask);
	private Timer chronometre = new Timer();
	private Line minuteTick = new Line(); 
	private Line hourTack = new Line(); 						//nouvelle ligne qui part du centre du cercle(200)
	private Circle cercle = new Circle();						//cercle de centre 200
	private Rectangle rectangle = new Rectangle();

	
	private boolean sublistChange = false; 						//permet de ne pas compter le depart de sublist vers list mais juste quand on clique sur sublist pas quand on change de liste
	private boolean listChange = false;
	private List<Task> taskList = new ArrayList<Task>();
	private ObservableList<String> subListElement;

	
	public void start(Stage primaryStage) {
		try {
			
			try {
				FileInputStream fileIn = new FileInputStream("saves/"+"sauvegarde");
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         taskList =  (List<Task>) in.readObject();
		         in.close();
		         fileIn.close();
				
			}catch (IOException e) {
				System.out.println("no save found");
			}
			
			
		
			BorderPane root = new BorderPane();
			HBox cadreListes = new HBox();
			Pane horlogerie = new Pane();
			Pane description = new Pane();
			AnchorPane newTaskPane = new AnchorPane();
			
			
			Popup taskCreationWindow = new Popup();taskCreationWindow.setX(800); taskCreationWindow.setY(400);
			Stage popUpStage = new Stage();
			Label popUpTaskName = new Label("Nom de la Task");popUpTaskName.setLayoutX(14);popUpTaskName.setLayoutY(79);popUpTaskName.setPrefSize(101, 16);
			Label popUpDate = new Label("Date : JJ : MM : AAAA");popUpDate.setLayoutX(14);popUpDate.setLayoutY(148);popUpDate.setPrefSize(161, 16);
			Label popUpDescription = new Label("Description");popUpDescription.setLayoutX(14);popUpDescription.setLayoutY(226);
			TextField nomTaskInput = new TextField();nomTaskInput.setPrefSize(190, 26);nomTaskInput.setLayoutX(5);nomTaskInput.setLayoutY(105);
			Label popUpTitle = new Label();popUpTitle.setFont(Font.font("System", 32));popUpTitle.setLayoutX(7);popUpTitle.setLayoutY(8);

			
			TextArea descriptionInput = new TextArea();descriptionInput.setPrefSize(289, 109);descriptionInput.setLayoutX(14);descriptionInput.setLayoutY(251);  
			Label warningNbCharacteres = new Label("200 charactères max.");warningNbCharacteres.setTextFill(Paint.valueOf("#a84040"));warningNbCharacteres.setLayoutX(14);warningNbCharacteres.setLayoutY(365);

			Spinner<Integer> jours = new Spinner<Integer>(01,31,LocalDate.now().getDayOfMonth());jours.setPrefSize(61, 26);jours.setLayoutX(10);jours.setLayoutY(181);jours.setEditable(true);
			Spinner<Integer> mois = new Spinner<Integer>(01,12,LocalDate.now().getMonthValue());mois.setPrefSize(56, 26);mois.setLayoutX(90);mois.setLayoutY(181);mois.setEditable(true);
			Spinner<Integer> annees = new Spinner<Integer>(2020,2100,LocalDate.now().getDayOfYear());annees.setPrefSize(108, 26);annees.setLayoutX(168);annees.setLayoutY(181);annees.setEditable(true);
			
			Popup editDescriptionWindow = new Popup();editDescriptionWindow.setX(800); editDescriptionWindow.setY(400);
			Stage popUpDescriptionStage = new Stage();
			AnchorPane editDescriptionPane = new AnchorPane();
			TextArea editDescriptionInput = new TextArea();editDescriptionInput.setPrefSize(289, 109);editDescriptionInput.setLayoutX(14);editDescriptionInput.setLayoutY(251); 
			Label editionPopUpDescription = new Label("Description");editionPopUpDescription.setLayoutX(14);editionPopUpDescription.setLayoutY(226);
			SplitMenuButton parentsList = new SplitMenuButton();
			Label descriptionText = new Label("kind of test"); descriptionText.alignmentProperty().set(Pos.TOP_LEFT);
			Label editionWarningNbCharacteres = new Label("200 charactères max.");editionWarningNbCharacteres.setTextFill(Paint.valueOf("#a84040"));editionWarningNbCharacteres.setLayoutX(14);editionWarningNbCharacteres.setLayoutY(365);
			Label popUpTitleDescription = new Label(/*selectedTask.getName()*/);popUpTitleDescription.setFont(Font.font("System", 32));popUpTitleDescription.setLayoutX(7);popUpTitleDescription.setLayoutY(8);
			
			
			
		
			
			TimerNumeric timerNumeric = new TimerNumeric(chrono.getMinutes()+"");
			Node timer = timerNumeric.getTimer();
			timer.setLayoutX(0);timer.setLayoutY(288);
			
			Button startStop = new Button("Start/Stop");startStop.setLayoutX(22);startStop.setLayoutY(400);startStop.setPrefSize(160, 40);

			
			List<String> taskListNames = new ArrayList<String>();
			for(Task t : taskList) {
				taskListNames.add(t.getName());
			}
			List<String> subtaskListNames = new ArrayList<String>();
			for(Subtask s : selectedTask.getSubtask()) {
				subtaskListNames.add(s.getName());
			}
			
			ObservableList<String> listElement = FXCollections.observableArrayList(taskListNames);
			
			
			ListView<String> list = new ListView<String>(listElement);
			ListView<String> subList = new ListView<String>();
			
			
			
			RotateTransition  rotationMinutes = new RotateTransition();
			RotateTransition  rotationHours = new RotateTransition();

			
			
			
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					try {
						FileOutputStream fileOut = new FileOutputStream("saves/"+"sauvegarde");
						System.out.println("saved !");
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(taskList);
						out.close();
						fileOut.close();
						/* on eteint les timer */
						for(Task t : taskList) {
							if(t.isEnCours()) {
								Alert clockAlert = new Alert(AlertType.WARNING);
								clockAlert.setTitle("Attention");
								clockAlert.setHeaderText(null);
								clockAlert.setContentText("Les chrono non eteints ont été enregistrés "+'\n'+"("+t.getName()+" était allumé)");
								clockAlert.showAndWait();
								
							}
							for(Subtask s : t.getSubtask()) {
								s.setEnCours(false);
							}
							chrono.setOff();
							t.setEnCours(false);
							t.setDureeEnMinutes(chrono.getMinutes());
						}
						System.exit(0); 	//le programme continue de tourner en arrière plan quand on clique sur la croix

						
						
						
						
					}catch (IOException i){
						 i.printStackTrace();
							System.exit(0); 	//le programme continue de tourner en arrière plan quand on clique sur la croix

					}
					
				}
			});
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			parentsList.setText("chose the parent Task"); parentsList.setLayoutX(250);parentsList.setLayoutY(105);
			
			
			
			EventHandler<ActionEvent> chronoEvent = new EventHandler<ActionEvent>() {
				

				@Override
				public void handle(ActionEvent arg0) {
					if(!selectedTask.getName().equals("tâche N°0") ) {
					if(chrono.isOn()) {
						chrono.setOff(); //va eteindre le chrono et enregistrer le temps de fonctionnement.
						rotationMinutes.stop();
						rotationHours.stop();
						System.out.println("eteint"); // sert à se reperer dans la console
							if(selectedSubtask.getName() != null && selectedSubtask.isEnCours()) { //si une sous tache est selectionne on eteint son chrono et on rajoute son chrono
								
								//selectedSubtask.setDureeEnMinutes(chrono.getMinutes());
								selectedSubtask.setEnCours(false);
								
								selectedTask.setDureeEnMinutes(chrono.getMinutes()+1);
								selectedTask.setEnCours(false);
							}else {
								
								selectedTask.setDureeEnMinutes(chrono.getMinutes());
								taskList.get(taskList.indexOf(selectedTask)).setDureeEnMinutes(chrono.getMinutes());
								//mettre la meme chose pour la tasklist
							}
						
						
						
						
						System.out.println(selectedTask.getDureeEnMinutes()+" min");
						
					}
					else if(!chrono.isOn()) {
						//on prends la task selectionnée
						//on met son timer
							
						
							System.out.println("demarer");
							if(selectedSubtask != null && selectedTask.getSubtask().contains(selectedSubtask)) { //si une sous tache est selectionnee et qu'elle appartient à la tache actuelle(eviter de demarer une sous tache d'une autre tache encore selected), on met le chrono on
								selectedSubtask.setEnCours(true);
								chrono = new Chrono(selectedSubtask);	// on crée un nv chrono pour eviter d'avoir un pb quand on schedule

							}else {
								selectedTask.setEnCours(true);
								chrono = new Chrono(selectedTask);		// on crée un nv chrono pour eviter d'avoir un pb quand on schedule

							}
							
							chrono.addTimer(timerNumeric);
							chrono.setOn(); 								// on allume le nouveau chrono
							chronometre.schedule(chrono, 0,1000/*36000*/);	//on programme la TimerTask chrono, avec un delais de 0 millisecondes et pour une durée de 1000 milisecondes   
							rotationMinutes.play();
							rotationHours.play();
							
							
						}
							
						}else{
							Alert informationAlert = new Alert(AlertType.WARNING);
							informationAlert.setTitle("Veuillez selectionner une tâche");
							informationAlert.setContentText("Merci de selectionner une tâche avant"+'\n' +"de demarer un chrono");
							informationAlert.showAndWait();
						}

						
					}
			};

			
			
			
			
			
			Button popUpValider = new Button("valider");popUpValider.setLayoutX(333);popUpValider.setLayoutY(360);popUpValider.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					
					if(newTaskPane.getChildren().contains(parentsList)) {
						for(Task t : taskList) {
							if(t.getName() == parentsList.getText()) {
								Subtask tmp = new Subtask(nomTaskInput.getText(),LocalDate.of(annees.getValue(), mois.getValue(), jours.getValue()),miseEnForme(descriptionInput.getText()));
								
								t.addSubtask(tmp);
								tmp.setName(nomTaskInput.getText());  
								tmp.setDateDebut(LocalDate.of(annees.getValue(), mois.getValue(), jours.getValue()));  
								tmp.setDescription(miseEnForme(descriptionInput.getText()));
								
								subtaskListNames.clear();
								for(Subtask s : selectedTask.getSubtask()) {
									subtaskListNames.add(s.getName());
								}
								subListElement = FXCollections.observableArrayList(subtaskListNames);
								subList.setItems(subListElement);
								System.out.println("nom :"+ t.getSubtaskNames());
								
						
							}
						}
					}else {
						taskList.add(new Task(nomTaskInput.getText(),LocalDate.of(annees.getValue(), mois.getValue(), jours.getValue()),miseEnForme(descriptionInput.getText())));
						list.getItems().add(nomTaskInput.getText());
						taskListNames.add(nomTaskInput.getText());
						addToParentsList(taskList, parentsList);

					}
					System.out.println();
					
					newTaskPane.getChildren().remove(parentsList);
					taskCreationWindow.hide();
				}
			});
			Button popUpAnnuler = new Button("annuler");popUpAnnuler.setLayoutX(417);popUpAnnuler.setLayoutY(360);popUpAnnuler.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){  
				public void handle(ActionEvent arg0) {
					newTaskPane.getChildren().remove(parentsList);
					taskCreationWindow.hide();
				}});
			
			
			Button popUpValiderDescription = new Button("valider");popUpValiderDescription.setPrefSize(64, 26);popUpValiderDescription.setLayoutX(333);popUpValiderDescription.setLayoutY(360);popUpValiderDescription.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					if(selectedTask.getSubtask().contains(selectedSubtask) && sublistChange) { 			
						selectedSubtask.setDescription(miseEnForme(editDescriptionInput.getText())); 	
						descriptionText.setText(selectedSubtask.getDescription());
					}else {
						selectedTask.setDescription(miseEnForme(editDescriptionInput.getText()));	
						descriptionText.setText(selectedTask.getDescription());
					}
					editDescriptionWindow.hide();
				}
			});
			Button popUpAnnulerDescription = new Button("annuler");popUpAnnulerDescription.setLayoutX(417);popUpAnnulerDescription.setLayoutY(360);popUpAnnulerDescription.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){  
				public void handle(ActionEvent arg0) {
					editDescriptionWindow.hide();
				}});
			
			//on ajoute l'event au boutton 
			startStop.addEventHandler(ActionEvent.ACTION, chronoEvent);
			
			
			
			newTaskPane.getChildren().addAll(popUpTaskName,nomTaskInput,popUpDate,jours,mois,annees,popUpDescription,descriptionInput,popUpValider,popUpAnnuler,warningNbCharacteres);
			newTaskPane.setPrefSize(500, 400); 
			
			editDescriptionPane.getChildren().addAll(popUpTitleDescription,editionPopUpDescription,editDescriptionInput,popUpValiderDescription,popUpAnnulerDescription,editionWarningNbCharacteres);
			editDescriptionPane.setPrefSize(500, 400);
			
			Scene popUpScene = new Scene(newTaskPane,500,400);
			popUpStage.setScene(popUpScene);
			
			Scene popUpDescriptionScene = new Scene(editDescriptionPane,500,400);
			popUpDescriptionStage.setScene(popUpDescriptionScene);
			
			
			MenuBar menuDeBase = new MenuBar();
			menuDeBase.setStyle("-fx-background-color :  107290; -fx-text-fill: white;");
			menuDeBase.setBackground(new Background(new BackgroundFill(Color.rgb(16, 114, 144), CornerRadii.EMPTY, Insets.EMPTY)));
			menuDeBase.setStyle("-fx-text-fill: white;");
			
			Scene scene = new Scene(root,800,500);
			
			
			Menu file = new Menu("File");
			MenuItem newTask = new MenuItem("nouvelle Task");
			MenuItem newSubtask = new MenuItem("nouvelle subTask");
			file.getItems().addAll(newTask,newSubtask);
			newTask.addEventHandler(ActionEvent.ACTION, new PopUpGenerator(primaryStage, taskCreationWindow,newTaskPane,parentsList,popUpTitle,false));
			newSubtask.addEventHandler(ActionEvent.ACTION, new PopUpGenerator(primaryStage, taskCreationWindow,newTaskPane,parentsList,popUpTitle,true));
			
			
			Menu edition = new Menu("Edition");
			MenuItem deleteTask = new MenuItem("supprimer Task");
			MenuItem deleteSubTask = new MenuItem("supprimer subTask");
			MenuItem editDescription = new MenuItem("editer description ");
			
			editDescription.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if(selectedTask.getSubtask().contains(selectedSubtask) && sublistChange) { 			//si on a selectionné une subTask(sublistchange permet de savoir si on a selectionné vraiment une subtask) et pas juste une task
						popUpTitleDescription.setText(selectedSubtask.getName());						//on va changer la description de la subtask et non de la task
						
					}else {
						popUpTitleDescription.setText(selectedTask.getName()); 						

					}
					
					editDescriptionWindow.show(primaryStage);
				}
				
			});
			
			MenuItem ChronoOnOff = new MenuItem("Start/Stop chrono");
			edition.getItems().addAll(ChronoOnOff,deleteTask,deleteSubTask,editDescription/*,generate*/);
			
			ChronoOnOff.addEventHandler(ActionEvent.ACTION, chronoEvent);
			
			deleteTask.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
					deleteAlert.setTitle("Supprimer Task ?");
					deleteAlert.setContentText("Êtes vous sûr de vouloir supprimer la task"+'\n'+selectedTask.getName()+" ?");
					Optional<ButtonType> result = deleteAlert.showAndWait();
					if (result.get() == ButtonType.OK) {
						
						try {
							final int emplacementElementSupprimer = taskListNames.indexOf(selectedTask.getName());
							taskList.remove(emplacementElementSupprimer); 
							listElement.remove(emplacementElementSupprimer);	
							taskListNames.remove(emplacementElementSupprimer);
							System.out.println(taskListNames.size());
						}catch (IndexOutOfBoundsException i) {
							Alert deleteError = new Alert(AlertType.ERROR);
							deleteError.setTitle("Il doit y avoir au moins 1 task");
							deleteError.setContentText("Pour supprimer cette Task,"+'\n'+"merci de bien vouloir en créer une nouvelle avant");
							Optional<ButtonType> resultat = deleteError.showAndWait();
						}
						
						}else {
							System.out.println(taskListNames);
					}
						
				}
				
			});
			
			
			deleteSubTask.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					Alert subatskDeleteAlert = new Alert(AlertType.CONFIRMATION);
					subatskDeleteAlert.setTitle("Supprimer subtask ?");
					subatskDeleteAlert.setContentText("Êtes vous sûr de vouloir supprimer la subtask"+'\n'+selectedTask.getName()+" ?");
					Optional<ButtonType> result = subatskDeleteAlert.showAndWait();
					if (result.get() == ButtonType.OK) {
						
						
							
							final int emplacementElementSupprimer = selectedTask.getSubtaskNames().indexOf(selectedSubtask.getName());
							System.out.println(subListElement);
							selectedTask.getSubtask().remove(emplacementElementSupprimer);	
							subListElement.remove(emplacementElementSupprimer);
							
							
							subListElement = FXCollections.observableArrayList(taskList.get(taskListNames.indexOf(selectedTask.getName())).getSubtaskNames());
							subList.setItems(subListElement);
							descriptionText.setText(selectedSubtask.getDescription());
							popUpTitleDescription.setText(selectedTask.getName());//le nom de la task est enregistré ici, mais le label est deja crée  
							
							subtaskListNames.clear();
							for(Subtask s : selectedTask.getSubtask()) {
								subtaskListNames.add(s.getName());
							}
							subListElement = FXCollections.observableArrayList(subtaskListNames);
							
						
						}else {
							System.out.println(taskListNames);
					}
						
				}
		
			});
			
			
			Menu help = new Menu("Help");
			MenuItem showHelp = new MenuItem("afficher l'aide");
			help.getItems().add(showHelp);
			showHelp.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Help dialog");
					alert.setHeaderText(null);
					alert.setContentText("Go read the text file Readme.txt for more information");

					alert.showAndWait();
					
				}

			});
				
			Menu exit = new Menu("Exit");
			
			MenuItem close = new MenuItem("Close");
			exit.getItems().addAll(close);
			close.addEventHandler(ActionEvent.ACTION , new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					 label : try {
						FileOutputStream fileOut = new FileOutputStream("saves/"+"sauvegarde");
						System.out.println("saved !");
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(taskList);
						out.close();
						fileOut.close();
						
						/* on eteint les timer */ 				//mais on enregistre pas les temps
						for(Task t : taskList) {
							if(t.isEnCours()) {
								Alert clockAlert = new Alert(AlertType.WARNING);
								clockAlert.setTitle("Attention");
								clockAlert.setHeaderText(null);
								clockAlert.setContentText("Les chrono non eteints ne seront pas comptés "+'\n'+"("+t.getName()+" est allumé)");
								clockAlert.showAndWait();
								break label;
							}
							for(Subtask s : t.getSubtask()) {
								s.setEnCours(false);
							}
							t.setEnCours(false);
						}
						System.exit(0);
						
						
					}catch (IOException i){
						 i.printStackTrace();
						 System.exit(0);
					}
					
				}
				
			});
			
			
			addToParentsList(taskList, parentsList);
	
							

			
			
			
			
			rectangle.setHeight(200);
			rectangle.setWidth(200);
			rectangle.setLayoutY(60);
			rectangle.setStrokeWidth(5);
			rectangle.setStroke(Paint.valueOf("black"));
			rectangle.setFill(Paint.valueOf("#107290"));
			rectangle.setStrokeLineCap(StrokeLineCap.ROUND);
			rectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
			rectangle.setStrokeMiterLimit(20);
			rectangle.setStrokeType(StrokeType.INSIDE);
			rectangle.setArcWidth(20);
			rectangle.setArcHeight(20);

			
			
			
			
			cercle.setCenterX(/*200.0f*/0);
			cercle.setCenterY(/*200.0f*/0);
			cercle.setLayoutX(100);
			cercle.setLayoutY(162);
			cercle.setRadius(80.0f);
			cercle.setFill(Paint.valueOf("#107290"));;
			cercle.setStrokeType(StrokeType.OUTSIDE);
			cercle.setStrokeWidth(3);
			cercle.setStroke(Paint.valueOf("black"));
			
									
			minuteTick.setStartX(/*200.0*/-60);
			minuteTick.setStartY(/*200.0*/-22);
			minuteTick.setEndX(/*200.0*/-60);
			minuteTick.setEndY(/*100.0*/-110);
			minuteTick.setLayoutX(160);
			minuteTick.setLayoutY(195);
			minuteTick.setStrokeWidth(5);
			minuteTick.setStroke(Paint.valueOf("#11a14d"));
			minuteTick.setStrokeLineCap(StrokeLineCap.ROUND);



			hourTack.setStartX(/*200.0*/-60);
			hourTack.setStartY(/*200.0*/-22);
			hourTack.setEndX(/*200.0*/-60);
			hourTack.setEndY(/*100.0*/-110);
			hourTack.setLayoutX(160);
			hourTack.setLayoutY(195);
			hourTack.setStrokeWidth(5);
			hourTack.setStroke(Paint.valueOf("#9e1b95"));
			hourTack.setStrokeLineCap(StrokeLineCap.ROUND);
		
			/*la fonction de rotation*/
			movePivot(minuteTick, 0, /*50*/33);					//set du nouveau point de rotation
			rotationMinutes.setToAngle(360);					//angle de rotation
			rotationMinutes.setNode(minuteTick);
			rotationMinutes.setDuration(Duration.millis(60000));//le temps d'attente en millisecondes
			
			
			movePivot(hourTack, 0, /*50*/33);					//set du nouveau point de rotation
			rotationHours.setToAngle(360);						//angle de rotation
			rotationHours.setNode(hourTack);
			rotationHours.setDuration(Duration.millis(3600000));//le temps d'attente en millisecondes
			
			
			System.out.println(chrono.getMinutes() + " min");
			
			
			
			horlogerie.setPrefSize(222, 354);
			horlogerie.getChildren().addAll(rectangle,cercle,minuteTick,hourTack,timer,startStop);
			
			description.setPrefSize(200, 200);
			description.getChildren().add(descriptionText);
			
			descriptionText.setPrefWidth(170);
			descriptionText.setPrefHeight(500);
			
			list.setPrefSize(200, 200);
			list.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {

				@Override
				public void onChanged(Change<? extends String> c) {
					listChange = true;
					sublistChange = false;
					if(listChange) { //si la liste change, la sous liste ne change pas
						
						
						
						subListElement = FXCollections.observableArrayList(taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))).getSubtaskNames());
						subList.setItems(subListElement);
						descriptionText.setText(taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))).getDescription());
						selectedTask = taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))); 
						popUpTitleDescription.setText(selectedTask.getName());//le nom de la task est enregistré ici, mais le label est deja crée  
						
						subtaskListNames.clear();
						for(Subtask s : selectedTask.getSubtask()) {
							subtaskListNames.add(s.getName());
						}
						subListElement = FXCollections.observableArrayList(subtaskListNames);
						
						if(!selectedTask.isEnCours()) {
							timerNumeric.setText(String.valueOf(selectedTask.getDureeEnMinutes()));
							System.out.println(selectedTask.getName());
							System.out.println(String.valueOf(selectedTask.getDureeEnMinutes()));
							System.out.println(chrono.getTimer());
							
	
						}
						
						
					}
				}
				
			});
			
			
			subList.setPrefSize(200, 200);
			subList.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {

				@Override
				public void onChanged(Change<? extends String> c) {
					try {
						listChange = false;
						sublistChange = true;
						if(sublistChange) {
							
							
						String cFormate = c.getList().toString().substring(1,c.getList().toString().length()-1);
						selectedSubtask = selectedTask.getSubtask().get(selectedTask.getSubtaskNames().indexOf(cFormate));
						
						
						descriptionText.setText(selectedSubtask.getDescription());
						
						
						if(!selectedSubtask.isEnCours()) {
							timerNumeric.setText(String.valueOf(selectedSubtask.getDureeEnMinutes()));
							System.out.println(String.valueOf(selectedSubtask.getDureeEnMinutes()));
							System.out.println(chrono.getTimer());
						}
						
						}
						
						
					}catch(IndexOutOfBoundsException e) { //supprimer une subTask cause une indexOutOfBound exception du au raccourcicement du nombre de subtask
						System.out.println("subtask deleted");
					}
					
					
					
				}
				
			});
			
			taskCreationWindow.getContent().addAll(newTaskPane);
			editDescriptionWindow.getContent().addAll(editDescriptionPane);

			
			cadreListes.getChildren().addAll(list,subList,description);
			cadreListes.setAlignment(Pos.CENTER_LEFT);
			cadreListes.resize(460, 423);
			cadreListes.setPrefSize(200, 100);
			cadreListes.setStyle("-fx-border-color: black;"+"-fx-border-width: 3;");
			
			
			menuDeBase.getMenus().addAll(file, edition, help, exit);
			
			
			root.setTop(/*toolBar*/menuDeBase);
			root.setCenter(cadreListes);
			root.setMargin(cadreListes, new Insets(10,50,10,10));
			root.setMargin(horlogerie, new Insets(0,30,0,0));
			root.setRight(horlogerie);
			
			
			
			scene.setRoot(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		

		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	public static void addToParentsList(List<Task> taskList,SplitMenuButton parentsList) {
		parentsList.getItems().clear();
		for (int i = 0; i < taskList.size(); i++) {
			
			MenuItem tmp = new MenuItem(taskList.get(i).getName());	
			tmp.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					parentsList.setText(tmp.getText());	//pour chaque Task de la liste on met un event : quand on clique dessus ça affiche le nom de la task en cours
					
				}});
			
			parentsList.getItems().add(tmp);			//on ajoute au splitMenuButton les elements de la  liste des tasks
		}
	}
	
	
	
	public static String miseEnForme(String texteAtransformer) {
		String texteFinal = "";
		String tmp = "";
		int taille = texteAtransformer.length();
		int lastValueOfI = 0; // premiere ligne
		
		for (int i = 0; i < taille && taille > 20; i++) { //si la string est <20 characteres ne pas entrer dans la boucle soulagera un peu l'ordinateur avec toutes mes operations inutiles il a deja assez à faire
			if(i%NB_CHAR_MAX_TEXT_AREA == 0 && i != 0) {
				tmp = texteAtransformer.substring(lastValueOfI, i);
				if(i==200) {
					i = taille;
					break;
				}
				if(i == 20){
					texteFinal += tmp;
				}
				else {
					texteFinal += '\n' + tmp;
					
				}
				lastValueOfI = i;
			}
		}
		
		texteFinal += '\n' + texteAtransformer.substring(lastValueOfI, taille);		//permet de rajouter la fin du texte, car la fin passe pas forcement le if i%20
		System.out.println(texteFinal);
		return texteFinal;
	}
	
	
	
	
	public static void main(String[] args) {
		launch(args);
		Task essais = new Task("task main test",LocalDate.now(),"test des taches",Etats.enCours);
		Subtask sousTache1 = new Subtask(LocalDate.now());
		Subtask sousTache2 = new Subtask("task 2",LocalDate.now(),"essais sous tache 2");
		Subtask sousTache3 = new Subtask("third task",LocalDate.now(),"essais sous tache 3", Etats.Fini);
		
		essais.addSubtask(sousTache1);
		essais.addSubtask(sousTache2);
		essais.addSubtask(sousTache3);
		System.out.println(essais.toString());

	}
}





