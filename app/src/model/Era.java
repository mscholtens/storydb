package model;

/**
 * Created by Marijn on 23.12.17.
 */
public class Era {

    String name;
    int startYear;
    int endYear;

    public Era(String name, int startYear, int endYear) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }
}
