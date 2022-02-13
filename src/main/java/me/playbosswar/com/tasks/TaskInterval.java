package me.playbosswar.com.tasks;

public class TaskInterval {
    private transient Task task;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;

    public TaskInterval(Task task, int days, int hours, int minutes, int seconds) {
        this.task = task;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
        task.storeInstance();
    }

    public void incrementDays() {
        days += 1;
        task.storeInstance();
    }

    public void decrementDays() {
        if (days == 0) {
            return;
        }

        days -= 1;
        task.storeInstance();
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
        task.storeInstance();
    }

    public void incrementHours() {
        this.hours += 1;
        task.storeInstance();
    }

    public void decrementHours() {
        if (hours == 0) {
            return;
        }

        hours -= 1;
        task.storeInstance();
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
        task.storeInstance();
    }

    public void incrementMinutes() {
        this.minutes += 1;
        task.storeInstance();
    }

    public void decrementMinutes() {
        if (minutes == 0) {
            return;
        }

        minutes -= 1;
        task.storeInstance();
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
        task.storeInstance();
    }

    public void incrementSeconds() {
        this.seconds += 1;
        task.storeInstance();
    }

    public void decrementSeconds() {
        if (seconds == 0) {
            return;
        }

        seconds -= 1;
        task.storeInstance();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int toSeconds() {
        int days = this.days * 86400;
        int hours = this.hours * 3600;
        int minutes = this.minutes * 60;

        return days + hours + minutes + seconds;
    }

    public String toString() {
        String s = "";

        if (days > 0) {
            s += days + " days ";
        }

        if (hours > 0) {
            s += hours + " hours ";
        }

        if (minutes > 0) {
            s += minutes + " minutes ";
        }

        if (seconds > 0) {
            s += seconds + " seconds ";
        }

        return s;
    }
}
