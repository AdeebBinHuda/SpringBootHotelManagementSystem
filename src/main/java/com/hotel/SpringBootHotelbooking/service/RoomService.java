package com.hotel.SpringBootHotelbooking.service;

import com.hotel.SpringBootHotelbooking.entity.Hotel;
import com.hotel.SpringBootHotelbooking.entity.Room;
import com.hotel.SpringBootHotelbooking.repository.HotelRepository;
import com.hotel.SpringBootHotelbooking.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Value("src/main/resources/static/images")
    private String uploadDir;

    public List<Room> getAllRooms() {

        return roomRepository.findAll();
    }

    public void saveRoom(Room room, MultipartFile imageFile) throws IOException {
        if(imageFile != null && !imageFile.isEmpty()){

            String imageFileName =saveImage(imageFile, room);
            room.setImage(imageFileName);
        }
        roomRepository.save(room);
    }

    public Room getRoomById(int id){
        return roomRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(" Room with id " + id + " not found"));
    }

    public void deleteRoom(int id){
        if (!roomRepository.existsById(id)){
            throw new EntityNotFoundException("Room not found with id "+id);
        }

        roomRepository.deleteById(id);
    }


    public Room updateRoom(int id,Room updateRoom, MultipartFile  imageFile) throws IOException {

        Room existingRoom=roomRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Room with id " + id + " not found"));

        existingRoom.setName(updateRoom.getName());
        existingRoom.setImage(updateRoom.getImage());
        existingRoom.setPrice(updateRoom.getPrice());
        existingRoom.setArea(updateRoom.getArea());
        existingRoom.setAdultNo(updateRoom.getAdultNo());
        existingRoom.setChildNo(updateRoom.getChildNo());


        //update location
        Hotel hotel =hotelRepository.findById(updateRoom.getHotel().getId())
                .orElseThrow(()-> new EntityNotFoundException("updateRoom not found"+updateRoom.getHotel().getId()));
        // update into existionHotel
        existingRoom.setHotel(hotel);

        //update image
        if(imageFile != null && !imageFile.isEmpty()){
            String imageFileName = saveImage(imageFile, existingRoom);
            existingRoom.setImage(imageFileName);
        }

        return roomRepository.save(existingRoom);

    }

    public List<Room> findRoomByHotelName(String hotelName){
        return roomRepository.findRoomByHotelName(hotelName);
    }

    public List<Room> findRoomByHotelId(int hotelId){
        return roomRepository.findRoomByHotelId(hotelId);
    }


    private  String saveImage(MultipartFile file,Room room) throws IOException {
        Path uploadPath = Paths.get(uploadDir+"/Rooms");
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        // adeeb  =    adehdheitrkds
        String fileName = room.getName()+"_"+ UUID.randomUUID().toString();
        // name the file with proper style and store into filePath
        Path filePath= uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

}
