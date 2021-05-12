package edu.sjsu.android.daytrac;

import java.util.Date;

public class TaskHistory {
    private int successes;
    private int failures;
    private Date lastDateListed;
    private String name;

    public TaskHistory(String name, Date date, boolean success){
        this.name = name;
        lastDateListed = date;
        if(success){
            successes = 1;
            failures = 0;
        }else{
            successes = 0;
            failures = 1;
        }
    }

    public String getName() {
        return name;
    }

    public Date getLastDateListed() {
        return lastDateListed;
    }

    public void setLastDateListed(Date lastDateListed) {
        this.lastDateListed = lastDateListed;
    }


    public int getSuccesses() {
        return successes;
    }

    public void setSuccesses(int successes) {
        this.successes = successes;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }
}
