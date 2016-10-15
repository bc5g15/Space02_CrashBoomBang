/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crashboombang;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.scene.text.Text;
import java.util.Random;
/**
 *
 * @author Benedict
 */
public class CrashBoomBang extends Application {
    //Width and height
    static final int W = 600;
    static final int H = 450;
    
    ForceText crash = new ForceText(100, 50, "CRASH");
    ForceText boom = new ForceText(100, 50, "BOOM");
    ForceText bang = new ForceText(150, 50, "BANG");
    
    ForceText colliders[] = {crash, boom, bang};
    
    public void checkCollisions()
    {
        for(ForceText a : colliders)
        {
            for(ForceText b: colliders)
            {
                if(a!=b)
                {
                    //Apply collision
                    if(a.getBoundsInParent().intersects(b.getBoundsInParent()))
                    {
                        a.ApplyForce(b);
                    }
                }
            }
        }
    }
    
    public void EmitForce(ForceText source)
    {
        for(ForceText item : colliders)
        {
            if(item != source)
            {
                item.recieveForce(source);
            }
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        //Create a canvas that defines the size of the page
        final Canvas canvas = new Canvas (W, H);
        Button btnCr = new Button();
        Button btnBo = new Button();
        Button btnBa = new Button();
        btnCr.setText("Crash");
        btnCr.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
                EmitForce(crash);
            }
        });
        
        btnBo.setText("Boom");
        btnBo.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                EmitForce(boom);
            }
        });
        
        btnBa.setText("Bang");
        btnBa.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                EmitForce(bang);
            }
        });
        
        //Use all of this to create the infinite loop.
        DoubleProperty x = new SimpleDoubleProperty();
        DoubleProperty y = new SimpleDoubleProperty();
        
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                    new KeyValue(x, 0),
                    new KeyValue(y, 0)),
                new KeyFrame(Duration.seconds(3),
                    new KeyValue(x, 0),
                    new KeyValue(y,0))
        );
        
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        
        
        
        AnimationTimer timer = new AnimationTimer(){
            @Override
            public void handle(long now){
                //Looking good. This rotate property is a good start
                //textTest.setRotate(textTest.rotateProperty().intValue() + 10);
                crash.updatePosition();
                boom.updatePosition();
                bang.updatePosition();
                checkCollisions();
                //System.out.println(crash.velX);
                
            }
        };
        
        
        StackPane root = new StackPane();
        //root.getChildren().add(btn);
        crash.setPosition(100, 150);
        boom.setPosition(300, 350);
        bang.setPosition(200, 200);
        
        //Horrible manual button position setting
        btnCr.setTranslateX(-(W/2) + 50);
        btnCr.setTranslateY(-(H/2) + 50);
        btnBo.setTranslateX(-(W/2) + 150);
        btnBo.setTranslateY(-(H/2) + 50);
        btnBa.setTranslateX(-(W/2) + 250);
        btnBa.setTranslateY(-(H/2) + 50);
        
  
        
        btnCr.setLayoutY(0);
        root.getChildren().add(btnCr);
        root.getChildren().add(btnBo);
        root.getChildren().add(btnBa);
        
        //root.getChildren().add(canvas);
        root.getChildren().add(crash);
        root.getChildren().add(boom);
        root.getChildren().add(bang);
        
        
        crash.setPosition(100, 150);
        boom.setPosition(300, 350);
        bang.setPosition(200, 200);
        
        Random rnd = new Random();
        crash.setVelocity(rnd.nextDouble() * 10, rnd.nextDouble()*10);
        boom.setVelocity(rnd.nextDouble() * 10, rnd.nextDouble()*10);
        bang.setVelocity(rnd.nextDouble()*10, rnd.nextDouble()*10);
        
        
        
        
        //My collision detector
        System.out.println(crash.getBoundsInParent().intersects(boom.getBoundsInParent()));

        
        //Define the size of the whole thing
        Scene scene = new Scene(root, W, H);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        timer.start();
        timeline.play();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
