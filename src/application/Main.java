package application;
	

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

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


/* TODO LIST 
 * pb de placement des labels
 	
 	-mockups Balsamiq à finir
 	-compte rendue en pdf
 	-push sur le gitlab de l'iut 
 	-video de presentation
 	
 	
 	
 	-pb dans String miseEnForme() : avec le texte pkmn, il marche plus
 	-pb dans le onChange quand on supprime task
 	-suppression de subtask
 	-recharger la Serialization
 	-Remplir la popup aide
 	-horloge / animation à configurer, il faut sauvegarder les temps
 	-probleme de numerotation automatique des sous taches
 	-voir si on peut utiliser le system tray
 
 
 
 */

public class Main extends Application {
	
	
	
	private void movePivot(Node node, double x, double y){
        node.getTransforms().add(new Translate(-x,-y));
        node.setTranslateX(x); node.setTranslateY(y);
    }
	
	private static final int NB_CHAR_MAX_TEXT_AREA = 20;
	private Task selectedTask = new Task();
	private Chrono chrono = new Chrono(selectedTask);
	private Timer chronometre = new Timer();
	private Line minuteTick = new Line(); 
	private boolean sublistChange = false; 		//permet de ne pas compter le depart de sublist vers list mais juste quand on clique sur sublist pas quand on change de liste
	private List<Task> taskList = new ArrayList<Task>();
	
	
	public void start(Stage primaryStage) {
		try {
			
			System.out.println("taskList : "+taskList.toString());		
			
			
		
			BorderPane root = new BorderPane();
			HBox cadreListes = new HBox();
			Pane horlogerie = new Pane();
			Pane description = new Pane();
			AnchorPane newTaskPane = new AnchorPane();
			
			
			Popup taskCreationWindow = new Popup();taskCreationWindow.setX(800); taskCreationWindow.setY(400);
			Stage popUpStage = new Stage();
			Label popUpTitle = new Label("New Task");popUpTitle.setFont(Font.font("System", 32));popUpTitle.setLayoutX(7);popUpTitle.setLayoutY(8);
			Label popUpTaskName = new Label("Nom de la Task");popUpTaskName.setLayoutX(14);popUpTaskName.setLayoutY(79);popUpTaskName.setPrefSize(101, 16);
			Label popUpDate = new Label("Date : JJ : MM : AAAA");popUpDate.setLayoutX(14);popUpDate.setLayoutY(148);popUpDate.setPrefSize(161, 16);
			Label popUpDescription = new Label("Description");popUpDescription.setLayoutX(14);popUpDescription.setLayoutY(226);
			TextField nomTaskInput = new TextField();nomTaskInput.setPrefSize(190, 26);nomTaskInput.setLayoutX(5);nomTaskInput.setLayoutY(105);
			Spinner<Integer> jours = new Spinner<Integer>(01,31,LocalDate.now().getDayOfMonth());jours.setPrefSize(61, 26);jours.setLayoutX(10);jours.setLayoutY(181);jours.setEditable(true);
			Spinner<Integer> mois = new Spinner<Integer>(01,12,LocalDate.now().getMonthValue());mois.setPrefSize(56, 26);mois.setLayoutX(90);mois.setLayoutY(181);mois.setEditable(true);
			Spinner<Integer> annees = new Spinner<Integer>(2020,2100,LocalDate.now().getDayOfYear());annees.setPrefSize(108, 26);annees.setLayoutX(168);annees.setLayoutY(181);annees.setEditable(true);
			
			Popup editDescriptionWindow = new Popup();editDescriptionWindow.setX(800); editDescriptionWindow.setY(400);
			Stage popUpDescriptionStage = new Stage();
			AnchorPane editDescriptionPane = new AnchorPane();
			TextArea descriptionInput = new TextArea();descriptionInput.setPrefSize(289, 109);descriptionInput.setLayoutX(14);descriptionInput.setLayoutY(251);
			SplitMenuButton parentsList = new SplitMenuButton();
			Label descriptionText = new Label("kind of test"); descriptionText.alignmentProperty().set(Pos.TOP_LEFT);
			Label warningNbCharacteres = new Label("200 charactères max.");warningNbCharacteres.setTextFill(Paint.valueOf("#a84040"));warningNbCharacteres.setLayoutX(14);warningNbCharacteres.setLayoutY(365);
			Label popUpTitleDescription = new Label(selectedTask.getName());popUpTitleDescription.setFont(Font.font("System", 32));popUpTitleDescription.setLayoutX(7);popUpTitleDescription.setLayoutY(8);

			
			
			Task essais = new Task("task 1",LocalDate.now(),"test des taches",Etats.enCours);
			Task essais2 = new Task("task main test",LocalDate.now(),"test des taches",Etats.enCours);
			Subtask sousTache1 = new Subtask(LocalDate.now());
			
			TimerNumeric timerNumeric = new TimerNumeric(chrono.getMinutes()+"");
			Node timer = timerNumeric.getTimer();
			timer.setLayoutX(0);timer.setLayoutY(288);
			
			taskList.add(essais);
			taskList.add(essais2);
			essais.addSubtask(sousTache1);
			
			List<String> taskListNames = new ArrayList<String>();
			for(Task t : taskList) {
				taskListNames.add(t.getName());
			}
			List<String> subtaskListNames = new ArrayList<String>();
			
			ObservableList<String> listElement = FXCollections.observableArrayList(taskListNames);
			ListView<String> list = new ListView<String>(listElement);
			ListView<String> subList = new ListView<String>();
			
			System.out.println("taskList : "+taskList.toString());
			
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
						System.out.println("taskList : "+taskList.toString());
						
					}catch (IOException i){
						 i.printStackTrace();
					}
					
				}
			});
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			parentsList.setText("chose the parent Task"); parentsList.setLayoutX(250);parentsList.setLayoutY(105);
			
			
			
			EventHandler<ActionEvent> chronoEvent = new EventHandler<ActionEvent>() {
				

				@Override
				public void handle(ActionEvent arg0) {

					if(chrono.isOn()) {
						chrono.setOff(); //va eteindre le chrono et enregistrer le temps de fonctionnement.
						rotationMinutes.stop();
						rotationHours.stop();
						
						
						
						System.out.println(selectedTask.getDureeEnMinutes()+" min");
						//si il ya un timer ON, on enregistre le temps et on le met off
						//TODO sauvegarder le temps int nbMinutes de la TASK
					}
					
					else if(!chrono.isOn()) {
						//on prends la task selectionnée
						//on met son timer
						selectedTask.setEnCours(true);
						chrono = new Chrono(selectedTask);							// on crée un nv chrono pour eviter d'avoir un pb quand on schedule
						chrono.addTimer(timerNumeric);
						chrono.setOn(); 								// on allume le nouveau chrono
						chronometre.schedule(chrono, 0,1000/*36000*/);	//on programme la TimerTask chrono, avec un delais de 0 millisecondes et pour une durée de 1000 milisecondes  
						rotationMinutes.play();
						rotationHours.play();
						
					}}
			};

			
			
			
			
			
			Button popUpValider = new Button("valider");popUpValider.setLayoutX(333);popUpValider.setLayoutY(360);popUpValider.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					
					if(newTaskPane.getChildren().contains(parentsList)) {
						for(Task t : taskList) {
							if(t.getName() == parentsList.getText()) {
								t.addSubtask(new Subtask(nomTaskInput.getText(),LocalDate.of(annees.getValue(), mois.getValue(), jours.getValue()),miseEnForme(descriptionInput.getText())));  
							}
						}
					}else {
						taskList.add(new Task(nomTaskInput.getText(),LocalDate.of(annees.getValue(), mois.getValue(), jours.getValue()),miseEnForme(descriptionInput.getText())));
						list.getItems().add(nomTaskInput.getText());
						taskListNames.add(nomTaskInput.getText());
						addToParentsList(taskList, parentsList);
						System.out.println("taskList : "+taskList.toString());
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
					selectedTask.setDescription(miseEnForme(descriptionInput.getText()));
					editDescriptionWindow.hide();
				}
			});
			Button popUpAnnulerDescription = new Button("annuler");popUpAnnulerDescription.setLayoutX(417);popUpAnnulerDescription.setLayoutY(360);popUpAnnulerDescription.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){  
				public void handle(ActionEvent arg0) {
					editDescriptionWindow.hide();
				}});
			Button startStop = new Button("Start/Stop");startStop.setLayoutX(22);startStop.setLayoutY(400);startStop.setPrefSize(160, 40);startStop.addEventHandler(ActionEvent.ACTION, chronoEvent);
			
			
			newTaskPane.getChildren().addAll(popUpTaskName,popUpTitle,nomTaskInput,popUpDate,jours,mois,annees,popUpDescription,descriptionInput,popUpValider,popUpAnnuler,warningNbCharacteres);
			newTaskPane.setPrefSize(500, 400); 
			
			editDescriptionPane.getChildren().addAll(popUpTitleDescription,popUpDescription,descriptionInput,popUpValiderDescription,popUpAnnulerDescription,warningNbCharacteres);
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
			newTask.addEventHandler(ActionEvent.ACTION, new PopUpGenerator(primaryStage, taskCreationWindow,newTaskPane,parentsList,false));
			newSubtask.addEventHandler(ActionEvent.ACTION, new PopUpGenerator(primaryStage, taskCreationWindow,newTaskPane,parentsList,true));
			
			
			Menu edition = new Menu("Edition");
			MenuItem deleteTask = new MenuItem("supprimer Task");
			MenuItem deleteSubTask = new MenuItem("supprimer subTask");
			MenuItem editDescription = new MenuItem("editer description ");
			editDescription.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					editDescriptionWindow.show(primaryStage);
				}
				
			});
			/*
			MenuItem generate = new MenuItem("generer compte rendu");
			generate.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

				public void handle(ActionEvent arg0) {
					PdfGenerator gen = new PdfGenerator(selectedTask.getName());
					gen.getPdf();
				}
				
			});
			*/
			
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
						//retirer de la liste, listview listview des noms
						final int emplacementElementSupprimer = taskListNames.indexOf(selectedTask.getName());
						taskList.remove(emplacementElementSupprimer); //vu que c'est le bordel l.427, une erreur est causé ici
						listElement.remove(emplacementElementSupprimer);	
						taskListNames.remove(emplacementElementSupprimer);//la taille de taskListNames passe de 1 à 2 en bas alors qu'il n'y a que 1 element dedans
						
						System.out.println("taskList : "+taskList.toString());
						System.out.println(taskListNames.size());
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
					alert.setContentText("- Créer une Task (file > new Task)"+'\n'+"- Supprimer une Task (selectionner la task puis : edition > supprimer task)"+'\n'+"- Demmarer ou stopper un chrono (boutton inférieur droit ou edition > start/stop chrono)");

					alert.showAndWait();
					
				}

			});
				
			Menu exit = new Menu("Exit");
			
			MenuItem close = new MenuItem("Close");
			exit.getItems().addAll(close);
			close.addEventHandler(ActionEvent.ACTION , new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent arg0) {
					try {
						FileOutputStream fileOut = new FileOutputStream("saves/"+"sauvegarde");
						System.out.println("saved !");
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(taskList);
						out.close();
						fileOut.close();
						
						System.out.println("taskList : "+taskList.toString());
					}catch (IOException i){
						 i.printStackTrace();
					}
					
					System.exit(0);
				}
				
			});
			
			
			addToParentsList(taskList, parentsList);
	
		
//marche pas, You'll need to refactor your code so that you create a new TimerTask, rather than re-using one.
					

			
			
			
			
			Rectangle rectangle = new Rectangle();
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

			
			
			
			
			Circle cercle = new Circle();						//cercle de centre 200
			cercle.setCenterX(/*200.0f*/0);
			cercle.setCenterY(/*200.0f*/0);
			cercle.setLayoutX(100);
			cercle.setLayoutY(162);
			cercle.setRadius(80.0f);
			cercle.setFill(Paint.valueOf("#107290"));;
			cercle.setStrokeType(StrokeType.OUTSIDE);
			cercle.setStrokeWidth(3);
			cercle.setStroke(Paint.valueOf("black"));
			
									//nouvelle ligne qui part du centre du cercle(200)
			minuteTick.setStartX(/*200.0*/-60);
			minuteTick.setStartY(/*200.0*/-22);
			minuteTick.setEndX(/*200.0*/-60);
			minuteTick.setEndY(/*100.0*/-110);
			minuteTick.setLayoutX(160);
			minuteTick.setLayoutY(195);
			minuteTick.setStrokeWidth(5);
			minuteTick.setStroke(Paint.valueOf("#11a14d"));
			minuteTick.setStrokeLineCap(StrokeLineCap.ROUND);



			Line hourTack = new Line(); 						//nouvelle ligne qui part du centre du cercle(200)
			hourTack.setStartX(/*200.0*/-60);
			hourTack.setStartY(/*200.0*/-22);
			hourTack.setEndX(/*200.0*/-60);
			hourTack.setEndY(/*100.0*/-110);
			hourTack.setLayoutX(160);
			hourTack.setLayoutY(195);
			hourTack.setStrokeWidth(5);
			hourTack.setStroke(Paint.valueOf("#9e1b95"));
			hourTack.setStrokeLineCap(StrokeLineCap.ROUND);
		
			//la fonction de rotation
			movePivot(minuteTick, 0, /*50*/33);					//set du nouveau point de rotation
			rotationMinutes.setToAngle(360);					//angle de rotation
			rotationMinutes.setNode(minuteTick);
			rotationMinutes.setDuration(Duration.millis(60000));//le temps d'attente en millisecondes
			
			movePivot(hourTack, 0, /*50*/33);					//set du nouveau point de rotation
			rotationHours.setToAngle(360);						//angle de rotation
			rotationHours.setNode(hourTack);
			rotationHours.setDuration(Duration.millis(3600000));//le temps d'attente en millisecondes
			
			
			System.out.println(chrono.getMinutes() + " min");
			
			//ToolBar toolBar = new ToolBar(file,edition,help,exit);
			//toolBar.setBackground(new Background(new BackgroundFill(Color.rgb(16, 114, 144), CornerRadii.EMPTY, Insets.EMPTY)));
			
			
			
			
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
					System.out.println("T : "+c.getList().toString().substring(1,c.getList().toString().length()-1));
					System.out.println("tasklListName size : "+taskListNames.size());
					System.out.println("taskList : "+taskList.toString());
					//System.out.println(taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))));

					/*le pb de suppression est ici*/ObservableList<String> subListElement = FXCollections.observableArrayList(taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))).getSubtaskNames());
					subList.setItems(subListElement);
					descriptionText.setText(taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))).getDescription());
					selectedTask = taskList.get(taskListNames.indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))); 
				}
				
			});
			
			
			subList.setPrefSize(200, 200);
			subList.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {

				@Override
				public void onChanged(Change<? extends String> c) {
					if(!sublistChange) {
						sublistChange = true;
					//System.out.println(c.getList().toString().substring(1,c.getList().toString().length()-1));
					descriptionText.setText(selectedTask.getSubtaskNames().get(selectedTask.getSubtaskNames().indexOf(c.getList().toString().substring(1,c.getList().toString().length()-1))).toString());
				
					}else {
						sublistChange = false;
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
				//tmp = texteAtransformer.substring(i, texteAtransformer.length());
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

/*
 Un Pokémon a pour faiblesse un type dont la puissance des attaques augmente face au type du Pokémon défenseur. Ainsi, l'attaque adverse a une capacité qui est multipliée par 2 (par 1.2 dans Pokémon GO).

Une attaque efficace sur le Pokémon défenseur est ainsi indiquée dans les jeux vidéo par la phrase « C'est super efficace ! » après l'attaque.

Dans le cas des Pokémon à deux types, si l'attaque est d'un type efficace sur les deux types du Pokémon défenseur, la capacité est multipliée logiquement par 4. 
*/



