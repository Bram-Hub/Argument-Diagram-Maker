# Argument Diagram Maker
## Authors
2018:
Marissa Kirk

## About
Notes about the current version of the program:

-Is known to work on windows, linux (once javafx is installed) and mac

- save files have the extension .admf

To Any Potential Future Programmers:
-This was written using the default version of Java at the time, and the only known additional library is JavaFX for the GUI
-My focus while making this was on completion of a usable program, not necessarily on well documented code. Sorry for any inconvenience this causes.
	- there are lots of references to variables within other classes without get/set functions 
	- there are very few comments
	- some of the functions, like that for moving the nodes, are a mess.
		- to be honest I don't know why they work, I just know that they do. 



The GUI Layout

Scene(Borderpane) consists of[
	Top Panel: MenuBar contains many [Menu contains many[MenuItem]]
	LeftPane: VBox contains many [Button]
	Center Pane: StackPane contains [SubScene(Group) contains many [BubbleGroup] ] ***
	
	BubbleGroup extends HBox contains many [Bubble, Label{the + signs}]
	
	Bubble extends borderpane consists of[
		- top = premise button
		- left = co-premise button
		- right = delete button
		- center = stackpane contains [circle, button]
		] 
	] 
	
	*** Subscene is necessary to make the bubbles movable, and the stackpane makes said subscene auto-resizable with the window
