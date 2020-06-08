package application;

import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class TimerNumeric {
	private StackPane root = new StackPane();
	private Rectangle rect = new Rectangle();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");	//creation d'un format pour l'heure
	private String text;
	
	public TimerNumeric(String text) {
		this.text = text;
	}
	
	
	
	
	private void main() {
		
		rect.setWidth(200);//x
		rect.setHeight(100);//y
		rect.setFill(null);
		rect.setStroke(Paint.valueOf("#107290"));
		rect.setStrokeType(StrokeType.INSIDE);
		rect.setStrokeLineJoin(StrokeLineJoin.ROUND);
		rect.setStrokeLineCap(StrokeLineCap.ROUND);
		rect.setStrokeMiterLimit(20);
		rect.setArcWidth(20);
		rect.setArcHeight(20);
		rect.setX(100);
		rect.setY(100);
		rect.setStrokeWidth(3);
		
		Label label = new Label(text + " min"/*LocalTime.now().format(formatter).toString()*/);
		label.setFont(javafx.scene.text.Font.font("KozMinPro-Heavy", 40));
		
		
		//le timer avec le event handler qui se declanche tout les secondes pour capturer l'heure
		Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

		       @Override
		       public void handle(ActionEvent event) {
		           label.setText(/*LocalTime.now()*/text + " min" /*.format(formatter).toString()*/);
		       }}));
		
		timer.setCycleCount(Timeline.INDEFINITE);//timer qui ne s'arrete jamais
		timer.play();
		
		
		
		
		root.getChildren().addAll(rect,label);
	}
	
	public Node getTimer() {
		main();
		return root;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
