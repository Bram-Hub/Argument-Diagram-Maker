/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;



import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 *
 * @author kirkm
 */
public class ExitBox {
    
    static boolean answer;
    
    public static boolean display(String title, String message){
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yes = new Button("Save and Quit");
        Button may = new Button("Just Quit");
        Button no = new Button("Cancel");
        
        yes.setOnAction(e -> {
            answer = true;
            ArgumentDiagrams.saveFile();
            window.close();
        });
        may.setOnAction(e -> {
            answer = true;
            window.close();
        });
        
        no.setOnAction(e -> {
            answer = false;
            window.close();
        });
        
        VBox layout = new VBox(10);
        HBox btnLayout = new HBox(10);
        btnLayout.setAlignment(Pos.CENTER);
        btnLayout.getChildren().addAll(yes, may, no);
        layout.getChildren().addAll(label, btnLayout);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }
}
