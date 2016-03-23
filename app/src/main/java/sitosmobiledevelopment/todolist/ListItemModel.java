package sitosmobiledevelopment.todolist;

/**
 * Created by Darran on 3/19/2016.
 */
public class ListItemModel {
    public String text;
    public boolean completed;


    public ListItemModel() {}//empty constructor needed firebase...


    public ListItemModel(String text, boolean completed) {
        this.completed = completed;
        this.text = text;
    }

    public ListItemModel(String text) {
        this.text = text;
        completed = false;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }




}
