package mypackage.ca;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


/* represents a question-list list that contains question objects
 * implementing Singleton class with getInstance() method
 */
public class QuestionList{
    
    // the list that contains question object
    private final ArrayList<Question> quesList = new ArrayList<Question>();
    // static question list object to use in program
    private static QuestionList queslist_instance = null;
    // hash set of integers to keep track of unique indexes
    private HashSet<Integer> rnd_order_set = new HashSet<Integer>();
    // generated questions iteration counter
    private int count = 0;
    
    private QuestionList() {
    }
    
    // function that recieves a question objects and adds it to the list
    public void addQuestion(Question newQues) {
        quesList.add(newQues);
    }
    
    // getter of list of questions
    public ArrayList<Question> getQuesList() {
        return quesList;
    }
    
    // writes all the q and a that are in the list into the incoming file
    public void toTxt(File file) throws FileNotFoundException, UnsupportedEncodingException {
        // create a printwriter object and an iterator
        PrintWriter writer = new PrintWriter(file);
        Iterator<Question> quesIterator = quesList.iterator();
        // iterate through the list and write the q and a into the file
        while (quesIterator.hasNext()) {
            Question temp = quesIterator.next();
            writer.println("Q: " + temp.getQuestion());
            writer.println("A: " + temp.getAnswer() + "\n");
        }
        writer.close();
       
    }
    
    /* function that generates a random q by randomly generate an index number, 
     * check if already been used and count how many times it generated q in the program.
     * stops when all indexes were used.
     */
    public int generateQuestionPos() {
        // create a random object and generate an integer in the range of 0 to list size
        Random rnd = new Random();
        int pos = rnd.nextInt(quesList.size());
        /* if unique hash set already contains the index but the count did not exceed list size
        generate new integer*/
        while (rnd_order_set.contains(pos) && count < quesList.size()) {
            pos = rnd.nextInt(quesList.size());
        }
        // when number of interation equals list size, return 01 to terminate the game
        if (count == quesList.size()) {
            return -1;
        }
        // add the index to the unique hash set and incerement iteration counter
        rnd_order_set.add(pos);
        count ++;
        return pos;
    }
    
    // returns the question in the pos index
    public String generateQuestion(int pos) {
        return quesList.get(pos).getQuestion();
    }
    
    // returns the answer is pos index
    public String generateAnswer(int pos) {
        return quesList.get(pos).getAnswer();
    }
    
    // returns list size
    public int getSize() {
        return quesList.size();
    }
    
    // check if the quesVal matches the ansVal is the question list list
    public boolean checkIfMatch(String quesVal, String ansVal) {
        Iterator<Question> quesIterator = quesList.iterator();
        while (quesIterator.hasNext()) {
            Question temp = quesIterator.next();
            if (quesVal.equals(temp.getQuestion())) {
                    if (ansVal.equals(temp.getAnswer())) {
                        return true;
                    } else {
                        return false;
                    }
            }
        }
        return false;
    }
    
    //static method to create instance of the class
    public static QuestionList getInstance() {
        if (queslist_instance == null) {
            queslist_instance  = new QuestionList();
        }
        return queslist_instance;
    }
    
    // returns the list in string
    public String toString() {
        return quesList.toString();
    }
    
    
    
}
