package com.map;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Floor {
    ArrayList<Room> rooms = new ArrayList<>();
    int floorNumber = 0;
    public Room currentRoom;

    public Floor(int floorNumber){
        this.floorNumber = floorNumber;
    }

    public void genRooms(int numRooms){
        for(int i = 0; i < numRooms; i++){
            Room tempRoom = new Room(i, 13, 13);
            tempRoom.genContents();
            rooms.add(tempRoom);
        }
        currentRoom = rooms.get(0);
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

    public void render(Graphics2D g) throws IOException {
        currentRoom.render(g);
    }
}
