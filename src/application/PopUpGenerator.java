package application;

import java.time.LocalDate;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopUpGenerator implements EventHandler<ActionEvent>{
	private Stage primaryStage;
	private Popup popup;
	private boolean isSubtask;
	private AnchorPane newTaskPane;
	private SplitMenuButton parentsList;
	private Label popUpTitle;
	public PopUpGenerator(Stage primaryStage,Popup popup,AnchorPane newTaskPane,SplitMenuButton parentsList,Label popUpTitle,boolean isSubtask) {
		this.primaryStage = primaryStage;
		this.popup = popup;
		this.isSubtask = isSubtask;
		this.newTaskPane = newTaskPane;
		this.parentsList = parentsList;
		this.popUpTitle = popUpTitle;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		
		System.out.println("ok");
		 newTaskPane.getChildren().removeAll(parentsList,popUpTitle);
		 
		 if(isSubtask) {
			 popUpTitle.setText("new Subtask");
			 newTaskPane.getChildren().addAll(parentsList,popUpTitle);
			 
		 }else {
			 popUpTitle.setText("new Task");
			 newTaskPane.getChildren().addAll(popUpTitle);
		 }
			 
		 

		
		popup.show(primaryStage);
		
		
	}

}
