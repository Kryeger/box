package utils;

public class Time implements Comparable<Time> {

    private long _time;

    public Time() {
        _time = 0;
    }

    public Time(long time) {
        _time = time;
    }

    public Time(long day, long month, long year){
        addYears(year);
        addMonths(month);
        addDays(day);
    }

    public Time(long day, long month, long year, long hour, long minute, long second){
        addYears(year);
        addMonths(month);
        addDays(day);
        addHours(hour);
        addMinutes(minute);
        addSeconds(second);
    }

    public Time(Time time){
        _time = time._time;
    }

    public boolean equals(Time t){
        return (_time == t.getHours());
    }

    public void addSeconds(long seconds){
        _time += seconds;
    }

    public void addMinutes(long minutes){
        _time += (minutes * 60);
    }

    public void addHours(long hours){
        _time += (hours * 60 * 60);
    }

    public void addDays(long days){
        _time += (days * 24 * 60 * 60);
    }

    public void addMonths(long months){
        _time += (months * 30 * 24 * 60 * 60);
    }

    public void addYears(long years){
        _time += (years * 12 * 30 * 24 * 60 * 60);
    }

    public long getSeconds(){
        return _time;
    }

    public long getMinutes() {
        return _time / 60;
    }

    public long getHours() {
        return _time / (60 * 60);
    }

    public long getDays() {
        return _time / (24 * 60 * 60);
    }

    public long getMonths() {
        return _time / (24 * 30 * 60 * 60);
    }

    public long getYears() {
        return _time / (12 * 30 * 24 * 60 * 60);
    }

    public long getSecond() {
        return (getSeconds() % 60);
    }

    public long getMinute() {
        return (getMinutes() % 60);
    }

    public long getHour() {
        return (getHours() % 24);
    }

    public long getDay() {
        return (getDays() % 30) + 1;
    }

    public long getMonth() {
        return (getMonths() % 12) + 1;
    }

    public long getYear() {
        return (getYears() + 1);
    }

    public String getDate() {
        return (String.valueOf(getDay()) + " / " + getMonth() + " / " + getYear());
    }

    public String getTime() {
        return (String.valueOf(getHour() + ":" + getMinute()));
    }

    public String getFullTime() {
        return (String.valueOf(getHour() + ":" + getMinute() + ":" + getSecond()));
    }

    @Override
    public String toString() {
        return "Time{" + getDate() + " " + getFullTime() + "}\n";
    }

    @Override
    public int compareTo(Time time) {
        if(_time > time._time) return 1;
        if(_time == time._time) return 0;
        return -1;
    }
}
