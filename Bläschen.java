package task6;

/**
 * Created by user on 31.03.16.
 */
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Bläschen extends Application {
 
    private List<MovingEllipse> ovals = new ArrayList<MovingEllipse>();
    private Group group = new Group();  //root node for the play window
    private final Random random = new Random();
    private final Button bt = new Button("Reset");
    private int points = 0;
    Button score = new Button("" + points);
    private int num = 15;
    

    public static void main(String[] args) {
        Application.launch(args);  
    }

    @Override
    public void start(Stage primaryStage) {
        final BorderPane borderPane; 
        final Scene scene;
        final Pane pane;
        primaryStage.setTitle("Ellipse & AnimationTimer Example");
        
        for (int i = 0; i < num; i++)    {
            Color color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
            double randomNumber = random.nextInt(500 - 50) + 50;
            double x = randomNumber;
            double randomNumber2 = random.nextInt(500 - 50) + 50;
            double y = randomNumber2;
            generate(color, x, y, 35.0, true);
        }
        //create a pane for a group with all moving objects
        pane = new Pane(group); 
        pane.setPrefSize(500,500);
        pane.setStyle("-fx-background-color: white;");
        //create a restart button
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	ovals.clear();              //clear List with references
                group.getChildren().clear();//clear all moving objects  
                for (int i = 0; i < num; i++)    {
                    Color color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble());
                    double randomNumber = random.nextInt(500 - 50) + 50;
                    double x = randomNumber;
                    double randomNumber2 = random.nextInt(500 - 50) + 50;
                    double y = randomNumber2;
                    generate(color, x, y, 35.0, true);
                }
            }
        });
        //create the main window lauout
        borderPane = new BorderPane();
        borderPane.setTop(bt);
        borderPane.setCenter(pane);
        scene = new Scene(borderPane, 800, 800, Color.BLACK);
        primaryStage.setScene(scene);
        //set pane autoresisable
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                pane.setPrefWidth(scene.getWidth());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                pane.setPrefHeight(scene.getHeight());
            }
        });
        primaryStage.show();

        new AnimationTimer() { //animate all circles
            @Override
            public void handle(long now) {
                for(MovingEllipse e:ovals){
                    //bottom
                    if((e.getEllipse().getCenterY() + e.getEllipse().getRadiusX()) > pane.getHeight())    {
                        e.setStepY(-e.getStepY()); 
                    }
                    //top
                    if((e.getEllipse().getCenterY() -e.getEllipse().getRadiusY()) < 0)  {
                        e.setStepY(-e.getStepY());
                    }
                    //right
                    if((e.getEllipse().getCenterX() + e.getEllipse().getRadiusX()) > pane.getWidth())   {
                        e.setStepX(-e.getStepX());
                    }
                    //left
                    if((e.getEllipse().getCenterX() - e.getEllipse().getRadiusX()) < 0) {
                        e.setStepX(-e.getStepX());
                    }
                    e.getEllipse().setCenterX(e.getEllipse().getCenterX()+e.getStepX());
                    e.getEllipse().setCenterY(e.getEllipse().getCenterY()+e.getStepY());
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }
    
    private void generate(Color c, Double x, Double y, Double radius, boolean clickable){
                        Ellipse localCircle = new Ellipse(x, y, radius, radius);
                        localCircle.setStrokeWidth(3);
                        localCircle.setStroke(Color.BLACK);
                        localCircle.setFill(c);
                        if(clickable){  //add event handler
                            localCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) { //create more moving circles
                                    localCircle.setVisible(false);
                                    points++;
                                    if(points == num)    {
                                        System.out.println("You won");
                                        final Stage stage = new Stage();
                                        Group rootGroup = new Group();
                                        Scene scene = new Scene(rootGroup, 500, 200, Color.WHITESMOKE);
                                        stage.setScene(scene);
                                        stage.setTitle("Congratulations");
                                        stage.centerOnScreen();
                                        stage.show();
                                        Text text = new Text(20,80, "YOU WON!!!");
                                        text.setFill(Color.RED);
                                        text.setEffect(new Lighting());
                                        text.setFont(Font.font(Font.getDefault().getFamily(), 50));
                                        rootGroup.getChildren().add(text);
                                    }
                                }
                            });
                        }
                        ovals.add(new MovingEllipse(localCircle, random.nextDouble(), random.nextDouble()));
                        
                        group.getChildren().add(localCircle); //add object to the group
        
    }

    private class MovingEllipse{
        private double stepX; //
        private double stepY;
        private Ellipse c; //reference on a circle
        double randomNumber = random.nextInt(6 - 0) + 0;
        double x = randomNumber;
        double randomNumber2 = random.nextInt(6 - 0) + 0;
        double y = randomNumber2;
        MovingEllipse(Ellipse c, double dx, double dy){
            this.c = c;
            stepX = dx + x;
            stepY = dy + y;
        }
        public double getStepX() {
            return stepX;
        }
        public void setStepX(double stepX) {
            this.stepX = stepX;
        }
        public double getStepY() {
            return stepY;
        }
        public void setStepY(double stepY) {
            this.stepY = stepY;
        }
        public Ellipse getEllipse() {
            return c;
        }
        public void setEllipse(Ellipse c) {
            this.c = c;
        }
        
        double getDistance(Ellipse e){
            double x = c.getCenterX() - e. getCenterX();
            double y = c.getCenterY() - e. getCenterY();
            return Math.sqrt(x * x + y * y);
        }
        double getDistance(double coordX, double coordY){
            double x = c.getCenterX() - coordX;
            double y = c.getCenterY() - coordY;
            return Math.sqrt(x * x + y * y);
        }
    }
}