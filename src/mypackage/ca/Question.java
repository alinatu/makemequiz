package mypackage.ca;

// represents a question and an answer
public class Question {
    // question string
    private final String question;
    // answer string
    private final String answer;
    
    // constructor creates an object from q and a strings
    public Question(String quesVar, String ansVar){
        if (quesVar.trim().isEmpty() || ansVar.trim().isEmpty()) {
            throw new IllegalArgumentException("text cannot be blank");   
        } 
        question = quesVar;
        answer = ansVar;
    }
    
    // getter to get q
    public String getQuestion() {
        return question;
    }
    
    // getter to get ans
    public String getAnswer() {
        return answer;
    }
    
   
}
