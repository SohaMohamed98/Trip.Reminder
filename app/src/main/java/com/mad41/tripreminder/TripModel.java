package com.mad41.tripreminder;

public class TripModel {
   private String date;
   private String time;
   private String place;
   private String start;
   private String end;


   public TripModel(String date, String time, String place, String start, String end)
   {
       this.date=date;
       this.time=time;
       this.place=place;
       this.start=start;
       this.end=end;
   }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
