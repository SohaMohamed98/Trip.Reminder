package com.mad41.tripreminder.trip_ui;

public class TripModel {
    private int id;
    private String name;
    private String startLoacation;
    private String endLoacation;
    private String time;
    private String date;
    private int status;
    private boolean isRepeated;
    private boolean isRound;

    public TripModel(String name, String startLoacation, String endLoacation,
                     String time, String date, int status, boolean isRepeated, boolean isRound) {
        this.name = name;
        this.startLoacation = startLoacation;
        this.endLoacation = endLoacation;
        this.time = time;
        this.date = date;
        this.status = status;
        this.isRepeated = isRepeated;
        this.isRound = isRound;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartLoacation() {
        return startLoacation;
    }

    public String getEndLoacation() {
        return endLoacation;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public boolean isRound() {
        return isRound;
    }

}
