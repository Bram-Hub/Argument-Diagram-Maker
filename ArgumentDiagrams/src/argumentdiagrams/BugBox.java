/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

import static argumentdiagrams.StatementBox.window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author kirkm
 */
public class BugBox {
    public static void display(){
        Stage window = new Stage();
        window.setWidth(500);
        window.setHeight(150);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Known Bugs and Fixes");
        window.setMinWidth(250);
        Label label1 = new Label();
        label1.setText("Arrow and/or Underline not showing correctly: ");
        Label label2 = new Label("          Move any directly connected node to update");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1,label2);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.TOP_LEFT);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
