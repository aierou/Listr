package com.victor.project.listr;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Seth on 3/29/2017.
 * This is a bad solution, but it works.
 * Database reference doesn't really need to be stored, but I don't want to change it.
 */

public abstract class Globals {
    public static DatabaseReference database;
    public static String username;
    public static TrackGPS gps;

    //Remove reference from each user
    public static void deleteList(final String id){
        database.child("Lists").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.child("members").getChildren()){
                    database.child("Users").child(s.getValue(String.class)).child("lists").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot.getValue(String.class).equals(id)){
                                dataSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //Remove the list once we have made all the requests
                database.child("Lists").child(id).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
