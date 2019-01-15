/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

import static argumentdiagrams.StatementBox.window;
import java.util.ArrayList;
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
public class InstrBox {
    public static void display(){
        Stage window = new Stage();
        window.setWidth(790);
        window.setHeight(460);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Instructions");
        window.setMinWidth(250);
        ArrayList<Label> labels = new ArrayList();
        labels.add(new Label("Statements:"));
        labels.add(new Label("--Statements consist of a code (used to link it to nodes), and a statement"));
        labels.add(new Label("--codes are restricted to 4 characters, and neither codes nor statements can contain { } [ ] ="));
        labels.add(new Label("--statements can be clicked on to open the statement editor, which includes its delete button"));
        labels.add(new Label("--the statement list can be show or hidden using the menu options"));
        labels.add(new Label("Nodes:"));
        labels.add(new Label("--Base nodes can be created from the menu and cannot have co-premises."));
        labels.add(new Label("--Premises use the upper + on an existing node. Co-Premises use the side + of an existing node"));
        labels.add(new Label("--the minus button deletes that node and all of its premises, but not its co-premises or conclusions"));
        labels.add(new Label("--Clicking the code will open a window to edit it. Hovering over the code will display the linked sentence, if any"));
        labels.add(new Label("--Nodes can be moved by clicking on the circle and dragging it to the desired location"));
        labels.add(new Label("--Nodes cannot be moved outside the current bounds of the window."));
        labels.add(new Label("--If a larger window is made smaller, some nodes may be hidden to the right or bottom as a result"));
        VBox layout = new VBox(10);
        layout.getChildren().addAll(labels);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.TOP_LEFT);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
