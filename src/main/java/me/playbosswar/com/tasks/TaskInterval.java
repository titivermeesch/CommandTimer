package me.playbosswar.com.tasks;

public class TaskInterval {
    private int days;
    private int hours;
    private int minutes;
    private int seconds;

    public TaskInterval(int days, int hours, int minutes, int seconds) {
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
    }

    public void incrementDays() {
        days += 1;
    }

    public void decrementDays() {
        if (days == 0) {
            return;
        }

        days -= 1;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void incrementHours() {
        this.hours += 1;
    }

    public void decrementHours() {
        if (hours == 0) {
            return;
        }

        hours -= 1;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void incrementMinutes() {
        this.minutes += 1;
    }

    public void decrementMinutes() {
        if (minutes == 0) {
            return;
        }

        minutes -= 1;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void incrementSeconds() {
        this.seconds += 1;
    }

    public void decrementSeconds() {
        if (seconds == 0) {
            return;
        }

        seconds -= 1;
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
