package robertobiondo.utilities;

import java.util.Calendar;

/**
 *
 * @author Bob
 */
public class Date implements Comparable<Date> {

    public enum Month {
        JANUARY(1, 31), FEBRUARY(2, 28), MARCH(3, 31), APRIL(4, 30), MAY(5, 31), JUNE(6, 30), JULY(7, 31), AUGUST(8, 31), SEPTEMBER(9, 30),
        OCTOBER(10, 31), NOVEMBER(11, 30), DECEMBER(12, 31);
        private int index;
        private int days;

        private Month(int index, int days) {
            this.index = index;
            this.days = days;
        }

        public int getIndex() {
            return this.index;
        }

        public static Month monthFromIndex(int index) throws IllegalDateException {
            if (index > 12 || index < 1) {
                throw new IllegalDateException("A month's index must be bounded from 1 to 12!");
            }
            return Month.values()[index - 1];
        }

        public int getMonthDays() {
            return this.days;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    private int day = 1;
    private Month month = Month.JANUARY;
    private int year = 1970;

    public Date() {
    }

    public Date(int day, Month month, int year) throws IllegalDateException {
        this.setYear(year);
        this.setDay(day);
        this.setMonth(month);
    }

    public Date(int day, int month, int year) throws IllegalDateException {
        this(day, Month.monthFromIndex(month), year);
    }

    public Date(String date) throws IllegalDateException {
        try {
            String[] dateSplitted = date.split("/");
            if (dateSplitted.length == 3) {
                this.setYear(Integer.parseInt(dateSplitted[2]));
                this.setMonth(Month.monthFromIndex(Integer.parseInt(dateSplitted[1])));
                this.setDay(Integer.parseInt(dateSplitted[0]));

            } else {
                throw new IllegalDateException();
            }
        } catch (NumberFormatException exc) {
            throw new IllegalDateException();
        }

    }

    public Date(int dayOfTheYear, int year) throws IllegalDateException {
        this(1, 1, year);
        this.setDayAndMonth(dayOfTheYear);
    }

    public Date(int day, String month, int year) throws IllegalDateException {
        this(day, 1, year);
        this.setMonth(Month.valueOf(month.toUpperCase()));
    }

    public void setDay(int year) throws IllegalDateException {
        if (this.checkDayAndMonth(year, this.month)) {
            this.day = year;
        } else {
            throw new IllegalDateException("Invalid day!");
        }
    }

    public void setMonth(Month month) throws IllegalDateException {
        if (this.checkDayAndMonth(this.day, month)) {
            this.month = month;
        } else {
            throw new IllegalDateException("Invalid month!");
        }
    }

    public void setYear(int year) throws IllegalDateException {
        if (year >= 0 && year <= 3000) {
            this.year = year;
        } else {
            throw new IllegalDateException("Invalid year!");
        }
    }

    private void setDayAndMonth(int dayOfTheYear) throws IllegalDateException {
        if (dayOfTheYear > this.daysOfTheYear() || dayOfTheYear < 0) {
            throw new IllegalDateException("Invalid day of the year!");
        }
        boolean leap = this.isLeapYear();
        int dayCount = 0;
        int monthToSet = 1;
        while (dayCount + Month.monthFromIndex(monthToSet).getMonthDays() < dayOfTheYear) {
            dayCount += Month.monthFromIndex(monthToSet).getMonthDays();
            if (monthToSet == 2 && leap) {
                dayCount++;
            }
            monthToSet++;
        }
        if (dayOfTheYear - dayCount == 0) {
            monthToSet--;
            dayOfTheYear += 29;
        }
        this.setMonth(Month.monthFromIndex(monthToSet));
        this.setDay(dayOfTheYear - dayCount);
    }

    public int getDay() {
        return this.day;
    }

    public Month getMonth() {
        return this.month;
    }

    public int getYear(String format) {
        if (format.equals("AAAA")) {
            return this.year;
        }
        if (format.equals("AA")) {
            StringBuilder a = new StringBuilder(this.year);
            return (a.charAt(2) * 10 + a.charAt(3));
        } else {
            return 0;
        }
    }

    public int getYear() {
        return this.getYear("AAAA");
    }

    public String toString(String format) {
        String s;
        if (format.equals("GGG AAAA")) {
            return (String.format("%03d", this.getDayOfTheYear()) + " "
                    + String.format("%04d", this.getYear()));
        }
        if (format.equals("GG/MM/AA")) {
            return (String.format("%02d", this.getDay())
                    + "/" + String.format("%02d", this.getMonth().index) + "/"
                    + String.format("%04d", this.getYear()));
        }
        if (format.equals("GG MMMM AAAA")) {
            return (this.getDay() + " " + this.getMonth() + " " + this.getYear());
        } else {
            return "";
        }
    }

    public String toString() {
        return this.toString("GG/MM/AAAA");
    }

    public void stampa(String format) {
        System.out.println(this.toString(format));
    }

    public void print() {
        this.stampa("GG/MM/AA");
    }

    private int getDayOfTheYear() {
        int dayOfTheYear = this.getDay();
        boolean leap = this.isLeapYear();
        try {
            for (int m = 1; m < this.getMonth().getIndex(); m++) {
                dayOfTheYear += Month.monthFromIndex(m).getMonthDays();
                if (m == 2 && leap) {
                    dayOfTheYear++;
                }
            }
        } catch (IllegalDateException exc) {

        }
        return dayOfTheYear;
    }

    private boolean checkDayAndMonth(int day, Month month) {
        boolean leap = this.isLeapYear();
        if (leap && month == Month.FEBRUARY) {
            return day <= 29;
        }
        return (day <= month.getMonthDays());
    }

    public int daysPassed(Date d2) {
        return ((d2.getYear() - this.getYear()) * 365) + (d2.getDayOfTheYear() - this.getDayOfTheYear());
    }

    public int yearsPassed(Date d2) {
        return this.daysPassed(d2) / 365;
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date d = null;
        try {
            d = new Date(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        } catch (IllegalDateException exc) {
        }
        return d;
    }

    public Date addDays(int days) {
        int yearsToAdd = days / 365;
        int dayToSet = this.getDayOfTheYear() + days - (yearsToAdd * 365);
        if (dayToSet > 365) {
            dayToSet -= 365;
            yearsToAdd++;
        }
        Date d = null;
        try {
            d = new Date(dayToSet, this.getYear() + yearsToAdd);
        } catch (IllegalDateException exc) {
        }
        return d;
    }

    public Date addYears(int years) {
        Date d = null;
        try {
            d = new Date(this.getDay(), this.getMonth(), this.getYear() + years);
        } catch (IllegalDateException exc) {
        }
        return d;
    }

    public int daysFromYearZero() {
        return this.getYear() * 365 + this.getDayOfTheYear();
    }

    @Override
    public Date clone() {
        Date clone = null;
        try {
            clone = new Date(this.getDay(), this.getMonth(), this.getYear());
        } catch (IllegalDateException exc) {
        }
        return clone;
    }

    @Override
    public int compareTo(Date date) {
        if (date != null) {
            return this.daysFromYearZero() - date.daysFromYearZero();
        } else {
            throw new NullPointerException();
        }
    }

    public static boolean isLeapYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative!");
        }
        if (year < 8) {
            return false;
        }
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean isLeapYear() {
        return isLeapYear(this.getYear());
    }

    public int daysOfTheYear() {
        if (this.isLeapYear()) {
            return 366;
        } else {
            return 365;
        }
    }

    public static void main(String args[]) {
        Date date = new Date();
        try {
            date = new Date(60, 8);
        } catch (IllegalDateException ex) {
        }
        date.print();
        System.out.println(date.isLeapYear());
    }
}
