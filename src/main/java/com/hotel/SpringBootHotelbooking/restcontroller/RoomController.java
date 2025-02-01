package com.hotel.SpringBootHotelbooking.restcontroller;

import com.hotel.SpringBootHotelbooking.entity.Room;
import com.hotel.SpringBootHotelbooking.service.HotelService;
import com.hotel.SpringBootHotelbooking.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @GetMapping("/")
    public ResponseEntity <List<Room> >getAllRooms(){
          List<Room> rooms = roomService.getAllRooms();
          return ResponseEntity.ok(rooms);
    }

    // Save room
    @PostMapping("/save")
    public ResponseEntity <String> saveRoom(
            @RequestPart Room room,
            @RequestParam(value = "image") MultipartFile image) throws IOException {


        roomService.saveRoom(room, image);
        return  new ResponseEntity<>("room saved successfully", HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable int id) {
        try{
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Room> updateRoom(
                       @PathVariable int id,
                       @RequestPart Room room,
                       @RequestPart ( value ="image") MultipartFile image

    ) throws IOException {
        Room updateRoom=  roomService.updateRoom(id, room , image);
        return ResponseEntity.ok(updateRoom);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable int id) {

        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok("Room with id " + id + "has been Deleted");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    //findRoomByHotelName

    @GetMapping("/r/findRoomByHotelName")
    public ResponseEntity<List<Room>> findRoomByHotelName(@RequestParam("hotelName") String hotelName) {

        List<Room> rooms = roomService.findRoomByHotelName(hotelName);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/r/findRoomByHotelId")
    public ResponseEntity<List<Room>> findRoomByHotelId(@RequestParam("hotelId") int hotelId) {

        List<Room> rooms = roomService.findRoomByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }
}
