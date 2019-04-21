package com.map;

import java.util.ArrayList;

public class Floor {
    ArrayList<Room> rooms = new ArrayList<>();
    int floorNumber = 0;
    public Room currentRoom;

    public Floor(ArrayList<Room> rooms, int floorNumber){
        this.rooms = rooms;
        this.floorNumber = floorNumber;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
}
