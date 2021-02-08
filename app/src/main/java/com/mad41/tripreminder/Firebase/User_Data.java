package com.mad41.tripreminder.Firebase;

import java.util.ArrayList;

public class User_Data {
    private String TripName;
    private String Start;
    private String End;
    private String Time;
    private String Date;
    private String Status;
    private String Notes ;
    private String IsRepeated;

    public String getIsRepeated() {
        return IsRepeated;
    }

    public String getIsRound() {
        return IsRound;
    }

    private String IsRound;

    public User_Data(){
        //Notes = new ArrayList<>();

    }
    public String getTripName() {
        return TripName;
    }

    public String getStart() {
        return Start;
    }

    public String getEnd() {
        return End;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public String getStatus() {
        return Status;
    }

    public String getNotes() { return Notes; }

   /* public void setNotes(ArrayList<String> notes) {
//        System.out.println( "the notes size"+notes.size());
       for(int i = 0 ; i < notes.size() ; i++)
        {
           Notes.add(i,notes.get(i));

        }
    }*/
}
