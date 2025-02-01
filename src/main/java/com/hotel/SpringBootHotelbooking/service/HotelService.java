package com.hotel.SpringBootHotelbooking.service;

import com.hotel.SpringBootHotelbooking.entity.Hotel;
import com.hotel.SpringBootHotelbooking.entity.Location;
import com.hotel.SpringBootHotelbooking.repository.HotelRepository;
import com.hotel.SpringBootHotelbooking.repository.LocationRepository;
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
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private LocationService locationService;

    @Value("src/main/resources/static/images")
    private String uploadDir;

    @Autowired
    private LocationRepository locationRepository;

    // create list which list hotel data
    public List<Hotel> getAllHotels() {

        return hotelRepository.findAll();
    }

    // save hotel
    public void saveHotel(Hotel h, MultipartFile imageFile) throws IOException {
        if(imageFile != null && !imageFile.isEmpty()){
            String imageFileName =saveImage(imageFile, h);
            h.setImage(imageFileName);
        }
        Location location = locationRepository.findById(h.getLocation().getId())
                        .orElseThrow(()-> new EntityNotFoundException("Location not found"+h.getLocation().getId()));

        h.setLocation(location);
        // save h
        hotelRepository.save(h);
    }

    // find by ID
    public Hotel findHotelById(int id) {
        return hotelRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("hotel not found"));
    }



    public Hotel updateHotel(int id, Hotel updateHotel, MultipartFile imageFile) throws IOException {

        Hotel existingHotel = hotelRepository.findById(id)
                              .orElseThrow(()-> new EntityNotFoundException("Hotel not found"));

        existingHotel.setName(updateHotel.getName());
        existingHotel.setAddress(updateHotel.getAddress());
        existingHotel.setRating(updateHotel.getRating());
        existingHotel.setMaxPrice(updateHotel.getMaxPrice());
        existingHotel.setMinPrice(updateHotel.getMinPrice());


        //update location
        Location location = locationRepository.findById(updateHotel.getLocation().getId())
                .orElseThrow(()-> new EntityNotFoundException("updateHotel not found"+updateHotel.getLocation().getId()));
        // update into existionHotel
        existingHotel.setLocation(location);

        //update image
        if(imageFile != null && !imageFile.isEmpty()){
            String imageFileName = saveImage(imageFile, existingHotel);
            existingHotel.setImage(imageFileName);
        }

         return hotelRepository.save(existingHotel);
    }

    public List<Hotel> findHotelByLocationName(String locationName) {
        return hotelRepository.findHotelByLocationName(locationName);
    }


    //delete hotel
    public void deleteHotel(int id){
        if (!hotelRepository.existsById(id)){
            throw new EntityNotFoundException("Hotel not found with id "+id);
        }

        hotelRepository.deleteById(id);
    }

    private  String saveImage(MultipartFile file, Hotel h) throws IOException {
        Path uploadPath = Paths.get(uploadDir+"/Hotels");
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        // hotel  =    adehdheitrkds
        String fileName = h.getName()+"_"+ UUID.randomUUID().toString();
        // name the file with proper style and store into filePath
        Path filePath= uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }

}
