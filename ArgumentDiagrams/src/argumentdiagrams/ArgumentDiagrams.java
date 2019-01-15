/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argumentdiagrams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author kirkm
 */
public class ArgumentDiagrams extends Application {
    static Boolean saved;
    private static Stage window;
    static BorderPane layout;
    static VBox statList;
    static Group nodePlane;
    public static Map<String, String> statements;
    public static List<BubbleGroup> nodes;
    
    static double orgSceneX, orgSceneY;
    static double orgTranslateX, orgTranslateY;
    
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Argument Diagram Maker");
        saved = true;
        statements = new TreeMap();
        nodes = new ArrayList();
        
        //Start Menu Setup
        Menu file = new Menu("File");
        MenuItem f_nw = new MenuItem("New");
        f_nw.setOnAction(e-> newFile());
        file.getItems().add(f_nw);
        MenuItem f_ld = new MenuItem("Load");
        f_ld.setOnAction(e-> loadFile());
        file.getItems().add(f_ld);
        MenuItem f_sv = new MenuItem("Save");
        f_sv.setOnAction(e-> saveFile());
        file.getItems().add(f_sv);
        
        Menu node = new Menu("Node");
        MenuItem n_nw = new MenuItem("New Base Node");
        n_nw.setOnAction(e-> addNode());
        node.getItems().add(n_nw);
        MenuItem n_cl = new MenuItem("Clear All");
        node.getItems().add(n_cl);
        n_cl.setOnAction(e-> clearNode());
        
        Menu stat = new Menu("Statement");
        MenuItem s_nw = new MenuItem("New Statement");
        stat.getItems().add(s_nw);
        s_nw.setOnAction( e -> addStat());
        MenuItem s_hd = new MenuItem("Hide List");
        s_hd.setOnAction(e-> statList.getChildren().clear());
        stat.getItems().add(s_hd);
        MenuItem s_sw = new MenuItem("Show List");
        s_sw.setOnAction(e-> updateStat());
        stat.getItems().add(s_sw);
        MenuItem s_cl = new MenuItem("Clear All");
        s_cl.setOnAction(e->{
            statList.getChildren().clear();
            statements.clear();
        });
        stat.getItems().add(s_cl);
        
        
        Menu help = new Menu("Help");
        MenuItem instr = new MenuItem("Instructions");
        instr.setOnAction(e-> InstrBox.display());
        help.getItems().add(instr);
        MenuItem bugs = new MenuItem("Known Bugs/Fixes");
        bugs.setOnAction(e-> BugBox.display());
        help.getItems().add(bugs);
        
        
        MenuBar menu = new MenuBar();
        menu.getMenus().addAll(file, node, stat, help);
        //END menu setup
        
        
        //Start Statement Pane Setup
        statList = new VBox();
        statList.setAlignment(Pos.TOP_RIGHT);
        statList.setPadding(new Insets(10,10,10,10));
        statList.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        //End Statement Pane Setup
        
        //Start node pane setup
        nodePlane = new Group();
        SubScene nodeScene = new SubScene(nodePlane, 700,600);
        nodeScene.setManaged(false);
        
        StackPane inter = new StackPane();
        inter.getChildren().add(nodeScene);
        nodeScene.heightProperty().bind(inter.heightProperty());
        nodeScene.widthProperty().bind(inter.widthProperty());
        
        
        layout = new BorderPane();
        layout.setTop(menu);
        layout.setRight(statList);
        layout.setCenter(inter);
        
        
        
        Scene scn = new Scene(layout, 800, 600);
        window.setScene(scn);
        window.setOnCloseRequest(e-> {
           e.consume();
           closeProgram();
        });
        window.show();
    }
    
    
    private void newFile(){
        boolean answer;
        if(!saved){
             answer= ConfirmBox.display("Save?", "Would you like to save the current file?");
            if (answer){
                saveFile();
            }
        }
        saved = true;
        nodePlane.getChildren().clear();
        statList.getChildren().clear();
        nodes.clear();
        statements.clear();
    }
    private void loadFile(){
        boolean answer;
        if(!saved){
             answer= ConfirmBox.display("Save?", "Would you like to save the current file?");
            if (answer){
                saveFile();
            }
        }
        nodePlane.getChildren().clear();
        statList.getChildren().clear();
        nodes.clear();
        statements.clear();
        
        FileChooser load = new FileChooser();
        load.setTitle("Load File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Argument Diagram Maker File (*.admf)", "*.admf");
        load.getExtensionFilters().add(extFilter);
        File file =load.showOpenDialog(window);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null){
                    parseLine(st);
                }
        }catch(FileNotFoundException e){
            System.err.println("Could not open file- file not found");
        }catch(IOException e){
            System.err.println(e);
        }
        for(int i=0; i < nodes.size(); ++i){
            nodes.get(i).update();
            nodes.get(i).updateLine();
            nodes.get(i).updateArrow();
        }
        updateNodes();
        updateHover();
        saved = true;
    }
    
    private void parseLine(String line){
        if(line.charAt(0)=='{' && line.charAt(line.length()-1)== '}'){
            //Statement List
            String code= line.substring(1,line.indexOf("="));
            String text;
            int comma = line.indexOf(",");
            int eql = line.indexOf("=");
            while(code!= null){
                if(comma > 0){
                  text = line.substring(eql+1, comma);  
                }else{
                    text = line.substring(eql+1, line.length()-1);
                }
                statements.put(code, text);
                eql = line.indexOf("=", eql+1 );
                if(comma >0 && eql > 0){
                   code = line.substring(comma+2, eql); 
                   comma = line.indexOf(",", comma+1);
                }else{
                    code = null;
                }
                
            }
            updateStat();
        }else if(line.charAt(0)=='B'){
            //Bubble
            int cln = line.indexOf(":", line.indexOf("B:")+1);
            int gIndex = Integer.valueOf(line.substring(cln+1, line.indexOf(":", cln+1)));
            cln =line.indexOf(":", cln+1);
            int bIndex = Integer.valueOf(line.substring(cln+1, line.indexOf(":", cln+1)));
            cln =line.indexOf(":", cln+1);
            String code = line.substring(cln+1, line.length());
            Bubble b =new Bubble(code, nodes.get(gIndex));
            nodes.get(gIndex).nodes.add(b);
        }else if(line.contains("G")){
            //BubbleGroup
            int cln = line.indexOf(":", line.indexOf("G:")+2);
            double x = Double.valueOf(line.substring(line.indexOf("G:")+2,cln ));
            double y = Double.valueOf(line.substring(cln+1, line.indexOf(":", cln+1)));
            cln =line.indexOf(":", cln+1);
            Bubble b= null;
            if(line.indexOf(":", cln+1)>=0){
                int gIndex = Integer.valueOf(line.substring(cln+1, line.indexOf(":", cln+1)));
                cln =line.indexOf(":", cln+1);
                int bIndex = Integer.valueOf(line.substring(cln+1));
                b = nodes.get(gIndex).nodes.get(bIndex);
            
            }
            BubbleGroup g = new BubbleGroup(x,y,b);
            g.setOnMousePressed(MovePress);
            g.setOnMouseDragged(MoveDrag);
            nodes.add(g);
        }else if(!(line.charAt(0)==']')){
            System.err.println("Incorrectly Formatted Line!");
        }
    }
    
    public static void saveFile(){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Argument Diagram Maker File (*.admf)", "*.admf");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(window);

        if(file != null){
            String content = statements.toString()+"\n" + nodes.toString();
            try{
               FileWriter fwrite = new FileWriter(file); 
               fwrite.write(content);
               fwrite.close();
                saved = true;
            }catch(IOException e){
                System.err.println("File failed to save");
            }
        }
       
    }
    
    private void addNode(){
        BubbleGroup b = new BubbleGroup();
        if(b.setup()){
                b.setOnMousePressed(MovePress);
                b.setOnMouseDragged(MoveDrag);
                nodes.add(b);
                nodePlane.getChildren().add(b);
                saved = false;
        }
    }
    public static void addPremise(Bubble par){
        BubbleGroup b = new BubbleGroup(par);
        if(b.setup()){
                b.setOnMousePressed(MovePress);
                b.setOnMouseDragged(MoveDrag);
                nodes.add(b);
                nodePlane.getChildren().add(b); 
                if(b.parrow != null){
                    
                    nodePlane.getChildren().add(b.updateArrow());
                }
                saved = false;
        }
    }
    
    public static void clearDependentNodes(Bubble b){
        for(int i =0; i < nodes.size(); ++i){
            if(nodes.get(i).parent== b ){
                BubbleGroup bg = nodes.get(i);
                nodes.remove(i);
                for(int j = 0; j < bg.nodes.size(); ++j){
                    clearDependentNodes(bg.nodes.get(j));
                }
                
            }
        }
    }
    
    public static void updateNodes(){
        nodePlane.getChildren().clear();
        for(int i=0; i< nodes.size(); ++i){
            nodePlane.getChildren().add(nodes.get(i));
            if(nodes.get(i).parrow != null){
                nodePlane.getChildren().add(nodes.get(i).parrow);
            }
            if(nodes.get(i).nodes.size()>1){
                nodePlane.getChildren().add(nodes.get(i).updateLine());
            }
        }
    }
    
    private void updateHover(){
        for(int i = 0; i< nodes.size(); ++i){
            for(int j = 0; j<nodes.get(i).nodes.size(); ++j){
                nodes.get(i).nodes.get(j).hoverText();
            }
        }
    }
    private void clearNode(){
        if(!statements.isEmpty()){
            saved = false;
        }
        nodes.clear();
        nodePlane.getChildren().clear();
    }
    
    
    private void addStat(){
        StatementBox.display("", "", statements);
        saved = false;
        updateStat();
    }
    
    private void editStat(String k, String v){
        boolean changed = StatementBox.display(k, v, statements);
        if(changed){
            updateStat();
            saved = false;
            updateHover();
        }
    }
    
    
    private void updateStat(){
        statList.getChildren().clear();
        for (Map.Entry<String,String> entry : statements.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            Button b = new Button(key+" : "+value);
            b.setOnAction(e-> editStat(key, value));
            statList.getChildren().add(b);
        }
        updateHover();
        layout.setRight(statList);
        window.show();
       
    }
    
    private void closeProgram(){
        boolean answer;
        answer= ExitBox.display("Exit", "Would you like to save the current file before exiting?");
        if (answer){
            window.close();
        }
        
        
    }
    
    
     static EventHandler<MouseEvent> MovePress = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            saved = false;
            orgSceneX = t.getScreenX();
            orgSceneY = t.getScreenY();
            orgTranslateX = ((HBox)(t.getSource())).getTranslateX();
            orgTranslateY = ((HBox)(t.getSource())).getTranslateY();
        }
    };
     
    static EventHandler<MouseEvent> MoveDrag = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            saved = false;
            double offsetX = t.getScreenX() - orgSceneX;
            double offsetY = t.getScreenY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
            if(newTranslateX > 0 & (newTranslateX+((HBox)(t.getSource())).getWidth()) < (((HBox)(t.getSource())).getScene().getWidth()- statList.getWidth())& newTranslateY >0 & (newTranslateY+((HBox)(t.getSource())).getHeight()) < ((HBox)(t.getSource())).getScene().getHeight()){
                ((HBox)(t.getSource())).setTranslateX(newTranslateX);
                ((HBox)(t.getSource())).setTranslateY(newTranslateY);
                if(((BubbleGroup)(t.getSource())).parrow != null){
                    nodePlane.getChildren().remove(((BubbleGroup)(t.getSource())).parrow);
                   ((BubbleGroup)(t.getSource())).updateArrow();
                   nodePlane.getChildren().add(((BubbleGroup)(t.getSource())).parrow);
                }
                nodePlane.getChildren().remove(((BubbleGroup)(t.getSource())).underline);
                if(((BubbleGroup)(t.getSource())).nodes.size()>1){
                    nodePlane.getChildren().add(((BubbleGroup)(t.getSource())).updateLine());
                }
                for(int i=0 ;i < nodes.size(); ++i){
                    if(((BubbleGroup)(t.getSource())).nodes.contains(nodes.get(i).parent )){
                        nodePlane.getChildren().remove(nodes.get(i).parrow);
                        nodes.get(i).updateArrow();
                        nodePlane.getChildren().add(nodes.get(i).parrow);
                    }
                }
            }
        }
    };
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
