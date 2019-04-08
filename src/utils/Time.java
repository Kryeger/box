package utils;

public class Time {

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

    public boolean equals(Time t){
        return (_time == t.getHours());
    }

    public void addHours(long hours){
        _time += hours;
    }

    public void addDays(long days){
        _time += (24 * days);
    }

    public void addMonths(long months){
        _time += (30 * 24 * months);
    }

    public void addYears(long years){
        _time += (12 * 30 * 24 * years);
    }

    public long getHours() {
        return _time;
    }

    public long getDays() {
        return _time / (24);
    }

    public long getMonths() {
        return _time / (24 * 30);
    }

    public long getYears() {
        return _time / (24 * 30 * 12);
    }

    public long getHour() {
        return (_time % getDays());
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
        return (String.valueOf(getDay()) + '/' + getMonth() + '/' + getYear());
    }


}
