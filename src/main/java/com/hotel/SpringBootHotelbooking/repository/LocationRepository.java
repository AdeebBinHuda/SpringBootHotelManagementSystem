package com.hotel.SpringBootHotelbooking.repository;

import com.hotel.SpringBootHotelbooking.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {


}
