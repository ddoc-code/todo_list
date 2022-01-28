import java.io.Serializable;
import java.time.LocalDateTime;

//implemented serializable marker interface to enable saving/loading
public class Todo implements Serializable {

    //variables
    private String text;
    private LocalDateTime due;
    private Category cat;
    private Importance importance;
    private Status completion;

    //constructor
    Todo(String textP, LocalDateTime dueP, Category catP, Importance importanceP, Status completionP) {
        this.text = textP;
        this.due = dueP;
        this.cat = catP;
        this.importance = importanceP;
        this.completion = completionP;
    }

    //text getter and setter
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    //due getter and setter
    public LocalDateTime getDue() {
        return due;
    }
    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    //cat getter and setter
    public Category getCat() {
        return cat;
    }
    public void setCat(Category cat) {
        this.cat = cat;
    }

    //importance getter and setter
    public Importance getImportance() {
        return importance;
    }
    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    //completion getter and setter
    public Status getCompletion() {
        return completion;
    }
    public void setCompletion(Status completion) {
        this.completion = completion;
    }

    //overridden toString() method
    @Override
    public String toString() {
//        return cat.getColour() +
//                "Todo{text='" + this.text +
//                "', due=" + this.due +
//                ", importance=" + this.importance +
//                ", completion=" + this.completion +
//                "}\033[0m";

        return "Todo{text='" + this.text +
                "', due=" + this.due +
                ", importance=" + this.importance +
                ", completion=" + this.completion +
                "}";
    }
}
