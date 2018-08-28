package mypackage.ca;


import javafx.collections.FXCollections;

import java.util.Random;


import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// driver class javafx program
public class QuizGenerator extends Application {
        private Stage stage;
        // window size
        private final int appWidth = 520;
        private final int appHeight = 320;
        private final int columnWidth = 150;
        // list of question user inputed
        private QuestionList queslist_instance = QuestionList.getInstance();
        // user visual text fields, buttons, texts, gridpane and drop down menu
        private TextField question;
        private TextField answer;
        private Button add;
        private Button startQuiz;
        private Text rndQuestion;
        private GridPane grid2;
        private MenuButton answerMenu;
        private Text correct;
        private Text wrong;
        private Button next;
        // count wrong and right answers
        private int countRight = 0;
        private int countWrong = 0;
        
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        // retrieve grid panes for scene1 and scene2
        GridPane grid1 = gridOneBuilder();
        grid2 = gridTwoBuilder();
        // add the grids to scenes and link the style sheet
        Scene scene1 = new Scene(grid1, appWidth, appHeight);
        scene1.getStylesheets().add("mypackage/ca/Style.css");
        Scene scene2 = new Scene(grid2, appWidth, appHeight);
        scene2.getStylesheets().add("mypackage/ca/Style.css");
        
        // event listener for the start quiz button
        startQuiz.setOnAction(e ->  {
            // if no answer and question were entered alert
            if (QuestionList.getInstance().getQuesList().isEmpty()) {
                Alert err = new Alert(AlertType.ERROR);
                err.setHeaderText("No input");
                err.setContentText("Please enter at least one question and answer.");
                err.showAndWait();
            }
            // else switch to scene2 
            else  {
                primaryStage.setScene(scene2);
                generateQuestion(e);

            }
        });
        
        primaryStage.setTitle("Pop Quiz!");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }
    // build initial grid generating answers with textfields buttons and title
    private GridPane gridOneBuilder() {
        // create grid pane and apply layout properties
        GridPane grid1 = new GridPane();
        grid1.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        grid1.setAlignment(Pos.TOP_CENTER);
        grid1.setVgap(8);
        grid1.setPadding(new Insets(10));
        
        // add title text fields, buttons for entering answers and questions into grid
        Text title1 = new Text("Make Me Quiz!");
        title1.getStyleClass().add("title");
        title1.setStyle("-fx-font-size: 32pt;");
        grid1.add(title1, 0, 0);
        
        question = new TextField();
        answer = new TextField();
        Label q = new Label("Enter Question:");
        Label a = new Label("Enter Answer:");
        grid1.add(q, 0, 1);
        grid1.add(question, 1, 1);
        grid1.add(a, 0, 2);
        grid1.add(answer, 1, 2);
        
        add = new Button("Add");
        grid1.add(add, 1, 3);
        
        // event listener for adding questions
        add.setOnAction(this::addQuestion);
        
        startQuiz = new Button("Click to start quiz!");
        startQuiz.getStyleClass().add("startQuiz");
        startQuiz.setPrefSize(300, 50);
        startQuiz.setVisible(false);
        grid1.add(startQuiz, 0, 4, 2, 1);
        
        return grid1;
    }
    
    //build second grid for quiz play game
    private GridPane gridTwoBuilder () {
     // create grid pane and apply layout properties
        grid2 = new GridPane();
        grid2.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        grid2.setAlignment(Pos.TOP_CENTER);
        grid2.setVgap(8);
        grid2.setPadding(new Insets(10));
        
        // add title text fields, buttons and drop menu for quiz
        Text title2 = new Text("Test Yourself!");
        title2.getStyleClass().add("title");
        title2.setStyle("-fx-font-size: 32pt;");
        grid2.add(title2, 0, 0);
        
        Label q2 = new Label("Question: ");
        grid2.add(q2, 0, 1);
        
        answerMenu = new MenuButton("Pick an answer:");
        answerMenu.getStyleClass().add("answerMenu");
        answerMenu.setPrefSize(260, 30);
        grid2.add(answerMenu, 0, 3, 3 ,1);
        
        correct = new Text("Correct!");
        correct.getStyleClass().add("correct");
        correct.setStyle("-fx-font-size: 30pt;");
        
        wrong = new Text("Wrong");
        wrong.getStyleClass().add("wrong");
        wrong.setStyle("-fx-font-size: 30pt;");
        correct.setVisible(false);
        wrong.setVisible(false);
        grid2.add(correct, 1, 5);
        grid2.add(wrong, 1, 5);
        
        next = new Button("Next ->");
        next.getStyleClass().add("next");
        next.setVisible(false);
        
        // event listener for next question button
        next.setOnAction(this::switchQuestion);
        grid2.add(next, 1, 6);
        
        grid2.setMaxWidth(500);
        
        return grid2;
    }
    //function to add answers to the question list
    private void addQuestion(ActionEvent event) { 
        // add input into the question list
        try {
            // create a question object from user input
            Question temp = new Question(question.getText(), answer.getText());
            // add question into question list
            QuestionList.getInstance().addQuestion(temp); 
            // clear all fields
            question.clear();
            answer.clear();
            startQuiz.setVisible(true);
        }
        // if input was blank or empty, alert
        catch (IllegalArgumentException x){
            Alert err = new Alert(AlertType.ERROR);
            err.setHeaderText("Input not valid");
            err.setContentText("The input field cannot be blank.");
            err.showAndWait();
        }
    }
    
    // function to generate a random question from the question list
    private void generateQuestion(ActionEvent event) {
        // retrieve the index randomly chosen
        int pos = queslist_instance.generateQuestionPos();
        // if index is -1 the quiz is terminated
        if (pos == -1) {
            gameEnd();
            return;
        }
        // display the question at the random index
        try {
            // generate a ransom question into random question text object
            rndQuestion = new Text(queslist_instance.generateQuestion(pos));
            rndQuestion.setVisible(true);
            rndQuestion.getStyleClass().add("rndQuestion");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // generate answers that apply to the question
        generateAnswer(pos);
        // set size and add the text to the grid
        rndQuestion.setWrappingWidth(200);
        grid2.add(rndQuestion, 1, 1, 1, 2);
    }
    
    //function to generate answers according to the random question
    private void generateAnswer(int pos) {
        // set all labels visibility to false 
        correct.setVisible(false);
        wrong.setVisible(false);
        next.setVisible(false);
        answerMenu.getItems().clear();

        // amount of total Q&A
        int size = queslist_instance.getSize();
        
        // the correct answer for the randomly chosen question
        String correctAns = queslist_instance.generateAnswer(pos);
        
        /* if amount of total questions is over 3. therefore there should be 2 more
          randomly chosen answers added to the menu */
        if (size > 3) {
            // add the correct answer to the menu item object
            MenuItem correctAnsItem = new MenuItem(correctAns);
            // add menu item object into drop down menu
            answerMenu.getItems().add(correctAnsItem);
            // even listener for chosing this correct answer
            correctAnsItem.setOnAction(e -> {
                rightAns();
                next.setVisible(true);
            });
            /* create random object to generate 2 false random answers by splitting the 
               list to 2 parts and picking one answer from each half */
            Random rnd = new Random();
            int index_one = rnd.nextInt(size/2);
            int index_two = rnd.nextInt(size/2) + (size/2);
            // if the answer generates is already the correct one, generate again
            while (index_one == pos) {
                index_one = rnd.nextInt(size/2);
            }
            while (index_two == pos) {
                index_two = rnd.nextInt(size/2) + (size/2);
            }
            // add to wrong random answers to the menu
            String ans_one = queslist_instance.generateAnswer(index_one);
            String ans_two = queslist_instance.generateAnswer(index_two);
            // add the wrong answers to the menu item object
            MenuItem item_one = new MenuItem(ans_one);
            MenuItem item_two = new MenuItem(ans_two);
            // add menu item object into drop down menu
            answerMenu.getItems().add(item_one);
            answerMenu.getItems().add(item_two);
            // even listeners for choosing an answer
            item_one.setOnAction(e -> applyEventMenuOption(ans_one));
            item_two.setOnAction(e -> applyEventMenuOption(ans_two));
            
            //if total amount of Q&A is 3 or less, add all of them to the menu
        } else {
            for (int i = 0; i < size; i ++) {
                /* generate the answer string, create menu item object from string
                   and add to the drop down menu */
                String temp = queslist_instance.generateAnswer(i);
                MenuItem item_temp = new MenuItem(temp);
                answerMenu.getItems().add(item_temp);
                item_temp.setOnAction(e -> applyEventMenuOption(temp));
            }
        }
        // shuffle the menu
        FXCollections.shuffle(answerMenu.getItems());
        
    }
    
    //function to check if the answer chosen from drop down menu is correct or wrong
    private void applyEventMenuOption (String value) {
        // boolean objcect match is true when string matches the correct answer
        Boolean match = queslist_instance.checkIfMatch(rndQuestion.getText(), value);
        // if true 
        if (match) {
            rightAns();
       // if wrong
        } else {
            wrongAns();
        }
        next.setVisible(true);
    }
        
    // function to generate a new random question
    private void switchQuestion (ActionEvent event) {
        rndQuestion.setVisible(false);
        generateQuestion(event);
    }
    
    // function for right answer, increments right answer counter and shows the correct label    
    private void rightAns() {
        countRight++;
        correct.setVisible(true);
        wrong.setVisible(false);
    }
    // function for wrong answer, increments wrong answer counter and shows the wrong label
    private void wrongAns() {
        countWrong++;
        wrong.setVisible(true);
        correct.setVisible(false);
    }
    
    // function to create the final scene grid pane, title, text and export button
    private void gameEnd() {
        // create grip pane object and apply layout properties
        GridPane grid3 = new GridPane();
        grid3.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        grid3.setAlignment(Pos.TOP_LEFT);
        grid3.setVgap(18);
        grid3.setPadding(new Insets(10));
        
        // add title, text and export butto to the gtid
        Text title3 = new Text("Quiz is over");
        title3.getStyleClass().add("title");
        title3.setStyle("-fx-font-size: 32pt;");
        grid3.add(title3, 1, 0);
        
        Text rightAns = new Text("Number of right answers:     " + countRight);
        Text wrongAns = new Text("Number of wrong answers:  " + countWrong);
        rightAns.getStyleClass().add("summary");
        wrongAns.getStyleClass().add("summary");
        grid3.add(rightAns, 0, 1);
        grid3.add(wrongAns, 0, 2);
        
        Button export = new Button("Export your Q+A");
        export.getStyleClass().add("export");
        // event listener for export button 
        export.setOnAction(e -> {
            // create a file chooser object for opening a dialog
            FileChooser fileChooser = new FileChooser();
            
            //set extension filter
            FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
             
            //show save file dialog
            File file = fileChooser.showSaveDialog(stage);
             
            // if file is creates use the toTxt function of the quiestion list object to generate the txt
            if(file != null){
                try {
                 queslist_instance.toTxt(file);
                } catch (Exception x) {

                }
            }
        });
        grid3.add(export, 0, 4, 6, 1);
        // add grid pane into scene no. 3 and link css stylesheet
        Scene scene3 = new Scene(grid3, appWidth, appHeight);
        scene3.getStylesheets().add("mypackage/ca/Style.css");
        
        // display scene3
        stage.setScene(scene3);
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
