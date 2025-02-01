package com.hotel.SpringBootHotelbooking.service;

import com.hotel.SpringBootHotelbooking.entity.Location;
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
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Value("src/main/resources/static/images")
    private String uploadDir;


    // create list which list student data
    public List<Location> getAllLocations() {

        return locationRepository.findAll();
    }

    public void saveLocation(Location s, MultipartFile imageFile) throws IOException {
        if(imageFile != null && !imageFile.isEmpty()){
            String imageFileName =saveImage(imageFile, s);
            s.setImage(imageFileName);
        }
        locationRepository.save(s);

    }

    public Location getLocationById(int id){
        return locationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(" Location with id " + id + " not found"));
    }

    public void deleteLocation(int id){
        if (!locationRepository.existsById(id)){
            throw new EntityNotFoundException("Location not found with id "+id);
        }

        locationRepository.deleteById(id);
    }

    public Location updateLocation(int id,Location updateLocation, MultipartFile  imageFile) throws IOException {

        Location existingLocation=locationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(" Location with id " + id + " not found"));

        if(updateLocation.getName()!=null){

            existingLocation.setName(updateLocation.getName());
        }

        if(imageFile != null && !imageFile.isEmpty()){
            String imageFileName = saveImage(imageFile, existingLocation);
            existingLocation.setImage(imageFileName);
        }

        return  locationRepository.save(existingLocation);
    }


    // if getById is used then this can be workable;  overloading being used
    public void updateLocation(Location updateLocation){

        locationRepository.save(updateLocation);
    }




    private  String saveImage(MultipartFile file, Location l) throws IOException {
        Path uploadPath = Paths.get(uploadDir+"/Locations");
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        // adeeb  =    adehdheitrkds
        String fileName = l.getName()+"_"+ UUID.randomUUID().toString();
        // name the file with proper style and store into filePath
        Path filePath= uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }


}
