/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


/**
 *
 * @author kirkm
 */
class BubbleGroup extends HBox{ 
    
    
    ArrayList<Bubble> nodes;
    Bubble parent;
    Arrow parrow;
    Path underline;
    
    BubbleGroup(){
        nodes = new ArrayList<>();
        parent = null;
        this.setManaged(false);
    }
    
    BubbleGroup(Bubble b){
        nodes = new ArrayList<>();
        parent = b;
        this.setTranslateX(b.parent.getTranslateX() - 40);
        this.setTranslateY(b.parent.getTranslateY() - 150);
        this.setManaged(false);
    }
    
    BubbleGroup(double x, double y,Bubble b ){
        nodes = new ArrayList<>();
        parent = b;
        this.setTranslateX(x);
        this.setTranslateY(y);
        parrow = null;
        this.setManaged(false);
    }
    
    public boolean setup(){
        parrow = null;
        Bubble b = new Bubble(this);
        boolean answer = BubbleBox.display(b, nodes);
        
        if(answer){
            this.getChildren().add(b);
            if(parent != null){
                updateArrow();
            }
            return true;
        }else{
            return false;
        }
        
    }
    
    public void update(){
        this.getChildren().clear();
        if(nodes.isEmpty()){
            ArgumentDiagrams.nodes.remove(this);
            ArgumentDiagrams.updateNodes();
        }
        if(nodes.size()==1){
            this.getChildren().add(nodes.get(0));
        }else{
            for(int i =0; i< nodes.size(); ++i){
                this.getChildren().add(nodes.get(i));
                if(i < nodes.size()-1){
                    Label l = new Label("+");
                    l.setFont(Font.font("Veranda", FontWeight.EXTRA_BOLD, 40));
                    this.setAlignment(Pos.BOTTOM_CENTER);
                    this.getChildren().add(l);
                }
            }
        }
        ArgumentDiagrams.updateNodes();
    }
    
    public Path updateLine(){
        this.autosize();
        underline = new Path();
            underline.setFill(Color.BLACK);
            underline.setStrokeWidth(4);
        
        //Line
            underline.getElements().add(new MoveTo(this.getTranslateX(), this.getTranslateY()+this.getHeight()));
            underline.getElements().add(new LineTo(this.getTranslateX()+this.getWidth(), this.getTranslateY()+this.getHeight()));
        return underline;
    }
    
    public Arrow updateArrow(){
        this.autosize();
        if(parent != null){
            parrow = new Arrow(this.getTranslateX()+this.getWidth()/2, 
                                this.getTranslateY()+this.getHeight(), 
                                parent.getLayoutX() + parent.parent.getTranslateX()+parent.getWidth()/2, 
                                parent.getLayoutY()+parent.parent.getTranslateY());
            
        }
        return parrow;
    }
    
    @Override
   public String toString(){
       String s = "G:"+this.getTranslateX()+":"+this.getTranslateY()+":";
       if(parent == null){
           s=s+"-1"+"\n";
       }else{
          s= s+ ArgumentDiagrams.nodes.indexOf(parent.parent) + ":" + parent.parent.nodes.indexOf(parent)+"\n"; 
       }
       for(int i=0; i < nodes.size(); ++i){
           s = s + nodes.get(i).toString();
       }
       
        return s;
    }
    
}
