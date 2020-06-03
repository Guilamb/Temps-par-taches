package application;

import java.time.LocalDate;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopUpGenerator implements EventHandler<ActionEvent>{
	private Stage primaryStage;
	private Popup popup;
	private boolean isSubtask;
	private AnchorPane newTaskPane;
	private SplitMenuButton parentsList;
	
	public PopUpGenerator(Stage primaryStage,Popup popup,AnchorPane newTaskPane,SplitMenuButton parentsList,boolean isSubtask) {
		this.primaryStage = primaryStage;
		this.popup = popup;
		this.isSubtask = isSubtask;
		this.newTaskPane = newTaskPane;
		this.parentsList = parentsList;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		System.out.println("ok");
		 if(isSubtask) {
			 newTaskPane.getChildren().add(parentsList);
		 }
		
		popup.show(primaryStage);
		
	}

}
