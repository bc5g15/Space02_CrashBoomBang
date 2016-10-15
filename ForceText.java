/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crashboombang;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Point2D;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author Benedict
 */
public class ForceText extends Text {
    final int W = 600;
    final int H = 450;
    
    
    public double width;
    public double height;
    
    public double velX = 5;
    public double velY = 5;
    public final double slowdown = 0.01;
    final double maxSpeed = 30;
    
    
    //Just use the text constructor
    public ForceText(double posX, double posY, String text)
    {
        super(posX, posY, text);
        this.fontProperty().setValue(Font.font("Verdana", 30));
        //Don't ask me why I have to do this.
        //It just behaves weirdly if I don't
        this.xProperty().set(0);
        this.yProperty().set(0);
        this.setLayoutX(0);
        this.setLayoutY(0);
        
        width = this.getLayoutBounds().getWidth();
        height = this.getLayoutBounds().getHeight();
        
                
    }
    
    public double mySpeed()
    {
        return Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
    }
    
    public void setVelocity(double x, double y)
    {
        velX = x;
        velY = y;
    }
    
    public void ApplyRotation(double value)
    {
        //Maybe?
    }
    
    public void ApplyForce(ForceText target)
    {
        //This whole function is so horrible I can't even believe it
        //But amazingly it does kinda work.
        Point2D myPos = this.localToParent(0, 0);
        Point2D theirPos = target.localToParent(0, 0);
        double xForce = myPos.getX() - theirPos.getX();
        double yForce = myPos.getY() - theirPos.getY();
        
        double hypotenuse = Math.sqrt(Math.pow(xForce, 2) + Math.pow(yForce, 2));
        
        
        
        //System.out.println(xForce);
        //System.out.println(yForce);
        //System.out.println(hypotenuse);
        
        target.velX -= (this.mySpeed()  * (xForce/hypotenuse))/2;
        target.velY -= (this.mySpeed() * (yForce/hypotenuse))/2;
        this.velX += (target.mySpeed() *(xForce/hypotenuse))/2;
        this.velY += (target.mySpeed() * (yForce/hypotenuse))/2;
        
        //Very Gimmicky. Update the colour of the colling object
        Random rnd = new Random();
        int r = rnd.nextInt(256);
        int g = rnd.nextInt(256);
        int b = rnd.nextInt(256);
        
        this.setFill(Color.rgb(r, g, b));
        
        target.updatePosition();
        this.updatePosition();
    }
    
    public void recieveForce(ForceText source)
    {
        double xPow = this.getPosX() - source.getPosX();
        double yPow = this.getPosY() - source.getPosY();
        
        double big = Math.sqrt(Math.pow(xPow, 2) + Math.pow(yPow, 2));
        this.velX = maxSpeed*(xPow/big);
        this.velY = maxSpeed*(yPow/big);
        this.updatePosition();
    }
    
    public void setPosition(double x, double y)
    {
        this.setTranslateX(x - (W/2));
        this.setTranslateY(y - (H/2));
    }
    
    public double getPosX()
    {
        //return translate + layout
        return this.translateXProperty().getValue() + this.layoutXProperty().getValue() + (width/2);
    }
    public double getPosY()
    {
        return this.translateYProperty().getValue() + this.layoutYProperty().getValue() - (height/2);
    }
    
    public void updatePosition()
    {
        //I'm cheating by having the class know about the playing field
        this.setTranslateX(this.translateXProperty().getValue() + velX);
        this.setTranslateY(this.translateYProperty().getValue() + velY);
       
        
        //Now check against the boundaries of the playing field
        if(this.getPosX()> W 
                || this.getPosX() <0)
        {
            //velX = -velX;
            if(getPosX() > W)
            {
                this.setPosition(W-width/2, this.getPosY());
                velX = -Math.abs(velX *0.9);
            }
            else if(getPosX() <0)
            {
                this.setPosition(width/2, this.getPosY());
                velX = Math.abs(velX *0.9);
            }
        }


        if(this.getPosY()>H
                || this.getPosY() < 0)
        {
            //velY = -velY;
            //this.setTranslateY(H+this.height);
            if(getPosY() > H)
            {
                this.setPosition(this.getPosX(), H);
                //Readjust speed to prevent permanant bouncing
                velY = -Math.abs(velY * 0.9);
            }
            else if(getPosY()<0)
            {
                this.setPosition(this.getPosX(), this.height/2);
                velY = Math.abs(velY * 0.9);
                //System.out.println(velY);
            }
                    

        }
        
        //Apply natural slowdown
       velX = velX-Math.copySign(slowdown * velX/mySpeed(), velX);
       velY = velY-Math.copySign(slowdown * velY/mySpeed(), velY);
       
       //Limit the maximal velocity
       double max = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY,2));
       if(max> maxSpeed)
       {
           velX *= (velX/max);
           velY *= (velY/max);
       }
      
    }
}
