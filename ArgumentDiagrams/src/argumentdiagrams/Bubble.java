/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author kirkm
 */
public class Bubble extends BorderPane{
    String code;
    BubbleGroup parent;
    Button codeButton;
    
    public Bubble(){
        code = "NULL";
        parent= null;
        setup();
    }
    
    public Bubble(BubbleGroup b){
        code = "NULL";
        parent = b;
        setup();
    }
    
    public Bubble(String c, BubbleGroup b){
        code = c;
        parent = b;
        setup();
    }
    
    public void setCode(String c){
        code = c;
        setup();
    }
    
    private void setup(){
        this.setManaged(true);
        Button premiseButton = new Button("+");
        premiseButton.setOnAction(e->premiseButton());
        BorderPane.setAlignment(premiseButton, Pos.CENTER);
        this.setTop(premiseButton);

        if(parent.parent != null){
           Button copremiseButton = new Button("+");
           copremiseButton.setOnAction(e->copremiseButton()); 
           this.setRight(copremiseButton);
        }
        
        
        codeButton = new Button(code);
        codeButton.setOnAction(e->codeButton());
        
        Button deleteButton = new Button("-");
        deleteButton.setOnAction(e->deleteButton());
        this.setLeft(deleteButton);
        
        
        
        
        StackPane pane = new StackPane();
        
        Circle circle = new Circle(36.0f, Color.ANTIQUEWHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        pane.getChildren().add(circle);
        pane.getChildren().add(codeButton);
        
        this.setCenter(pane);
        this.autosize();
        hoverText();
    }
    
    private void premiseButton(){
        ArgumentDiagrams.addPremise(this);
    }
    
    private void copremiseButton(){
        Bubble b = new Bubble(parent);
        boolean answer = BubbleBox.display(b, parent.nodes);
        parent.update();
    }
    
    private void codeButton(){
        boolean answer = BubbleBox.display(this, parent.nodes);
        hoverText();
    }
    
    public void hoverText(){
         Tooltip t= new Tooltip(ArgumentDiagrams.statements.get(code));
         codeButton.setTooltip(t);
    }
    
    private void deleteButton(){
        boolean answer = ConfirmBox.display("Delete Node Confirmation", "Are you sure you want to delete this node and all of its premises?");
        if (answer){
            parent.nodes.remove(this);
            ArgumentDiagrams.clearDependentNodes(this);
            parent.update(); 
        }
        
    }
    
    @Override
    public String toString(){
        return "B:"+ArgumentDiagrams.nodes.lastIndexOf(parent)+":"+parent.nodes.lastIndexOf(this)+":"+code+"\n";
    }
    
}
