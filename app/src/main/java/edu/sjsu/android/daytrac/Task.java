package edu.sjsu.android.daytrac;

import java.util.Date;

public class Task {
    private String name;
    private Date dateAdded;
    private boolean bCompleted;

    public Task(String name, Date date){
        this.name = name;
        dateAdded = date;
        bCompleted = false;
    }

    public void setbCompleted(boolean bCompleted) {

        this.bCompleted = bCompleted;
    }

    public String getName() {
        return name;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public boolean isbCompleted() {
        return bCompleted;
    }
}
