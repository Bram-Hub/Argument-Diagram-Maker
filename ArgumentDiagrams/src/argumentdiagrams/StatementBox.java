/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

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
public class StatementBox {
    static boolean answer;
    static Stage window;
    static VBox errors;
    static BorderPane b;
    public static boolean display(String code, String message, Map<String,String> stats){
         window= new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Statement");
        window.setWidth(400);
        window.setHeight(260);
        window.setResizable(false);
        
        Label codeLabel = new Label("Code:");
        TextField codeText = new TextField(code);
        codeText.setPromptText(code);
        
        Label statLabel = new Label("Statement:");
        TextField statText = new TextField(message);
        statText.setPromptText(message);
        
        Button ok = new Button("Done");
        ok.setOnAction(e -> {
            answer= true;
            close(codeText, statText, stats, code);
                });
        
        window.setOnCloseRequest(e-> answer= false);
        
        Button delete = new Button("Delete");
        delete.setOnAction(e-> {
            answer = true;
            remove(code, message, stats);
        });
        
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10,10,10,10));
        layout.setVgap(10);
        layout.setHgap(10);
        GridPane.setConstraints(codeLabel, 0, 0);
        GridPane.setConstraints(codeText, 1, 0 );
        GridPane.setConstraints(statLabel, 0, 1);
        GridPane.setConstraints(statText, 1, 1 );
        GridPane.setConstraints(ok, 1,2);
        GridPane.setConstraints(delete, 0,2);
        
        errors = new VBox();
        errors.setPadding(new Insets(10,10,10,10));
        
        layout.getChildren().addAll(codeLabel, codeText, statLabel, statText, ok, delete);
        
        b = new BorderPane();
        b.setCenter(layout);
        b.setBottom(errors);
        
        Scene scene = new Scene(b);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }
    
    private static void close(TextField code, TextField message, Map<String,String> stats, String orig){
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
        if(!code.getText().equals(orig) && stats.containsKey(code.getText())){
            Label codeRepeat = new Label("code already in use");
            errors.getChildren().add(codeRepeat);
            window.show();
        }
        if(message.getText().contains(":")| message.getText().contains("{")| message.getText().contains("}")| message.getText().contains("[")| message.getText().contains("]")| message.getText().contains("=")){
            Label messAlphaNum = new Label("statement cannot contain {} [] =");
            errors.getChildren().add(messAlphaNum);
            window.show();
        }
        if(errors.getChildren().isEmpty()){
            stats.remove(orig);
            stats.put(code.getText(), message.getText());
            window.close();
        }
        
    }
    
    private static void remove(String code, String Message, Map<String,String> stats){
        boolean answer = ConfirmBox.display("Delete Statement Confirmation", "Are you sure you want to delete statement " + code + " ?");
        if (answer){
            stats.remove(code);
            window.close();
        }
    }

}
