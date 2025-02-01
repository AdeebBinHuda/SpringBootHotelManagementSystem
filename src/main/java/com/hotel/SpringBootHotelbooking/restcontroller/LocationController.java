package com.hotel.SpringBootHotelbooking.restcontroller;

import com.hotel.SpringBootHotelbooking.entity.Location;
import com.hotel.SpringBootHotelbooking.service.LocationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;


    @GetMapping("/")
    public ResponseEntity<List<Location>> getAllLocations() {

        List<Location> locationsList = locationService.getAllLocations();

        return ResponseEntity.ok(locationsList);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveLocation(
            @RequestPart Location l,
            @RequestParam (value = "image", required= true) MultipartFile file
            ) throws IOException {

        //  LocationService class saveLocation being called
        locationService.saveLocation(l, file);

        return new ResponseEntity<>("Student Saved", HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {

        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok("Location with id " + id + "has been Deleted");

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable int id,
                                                   @RequestPart Location l,
                                                   @RequestParam (value = "image", required= true) MultipartFile file) throws IOException {
        Location updateLocation = locationService.updateLocation(id, l, file);

        return ResponseEntity.ok(updateLocation);
    }

   /* @GetMapping("/{email}")
    public ResponseEntity<Location> findStudentById(@PathVariable String email){
        Location updateLocation = locationService.findLocationByEmail(email);

        return ResponseEntity.ok(updateStudent);
    */
}

