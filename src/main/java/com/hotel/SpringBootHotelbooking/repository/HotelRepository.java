package com.hotel.SpringBootHotelbooking.repository;

import com.hotel.SpringBootHotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {


    @Query("select h from Hotel h where h.location.name=:locationName")
    List<Hotel> findHotelByLocationName(@Param("locationName") String locationName);


}
