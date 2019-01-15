/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author kirkm
 */
public class BubbleBox {
    static boolean answer;
    static Stage window;
    static VBox errors;
    static BorderPane b;
    public static boolean display( Bubble bub, List<Bubble> nodes){
         window= new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Bubble");
        window.setWidth(350);
        window.setHeight(200);
        window.setResizable(false);
        
        Label codeLabel = new Label("Code:");
        TextField codeText = new TextField(bub.code);
        codeText.setPromptText(bub.code);
        
        
        Button ok = new Button("Done");
        ok.setOnAction(e -> {
            answer= true;
            close(codeText, bub, nodes);
                });
        
        window.setOnCloseRequest(e-> answer= false);
        
       
        
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(10);
        layout.setHgap(10);
        GridPane.setConstraints(codeLabel, 0, 0);
        GridPane.setConstraints(codeText, 1, 0 );
        GridPane.setConstraints(ok, 1,1);
        
        errors = new VBox();
        errors.setPadding(new Insets(10,10,10,10));
        
        layout.getChildren().addAll(codeLabel, codeText, ok);
        
        b = new BorderPane();
        b.setCenter(layout);
        b.setBottom(errors);
        
        Scene scene = new Scene(b);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }
    
    private static void close(TextField code, Bubble bub, List<Bubble> nodes){
        errors.getChildren().clear();
        if(code.getText().length() > 4){
            Label codeLength = new Label("code must be 4 characters or less");
            errors.getChildren().add(codeLength);
            window.show();
        }
        if(code.getText().contains(":")| code.getText().contains("{")| code.getText().contains("}")| code.getText().contains("[")| code.getText().contains("]")| code.getText().contains("=")){
            Label codeAlphaNum = new Label("code cannot contain {} [] =");
            errors.getChildren().add(codeAlphaNum);
            window.show();
        }
        if(errors.getChildren().isEmpty()){
            nodes.remove(bub);
            bub.setCode(code.getText());
            nodes.add(bub);
            window.close();
        }
        
    }
    
}
