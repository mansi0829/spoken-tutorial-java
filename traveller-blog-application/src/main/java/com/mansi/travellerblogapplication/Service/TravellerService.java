package com.mansi.travellerblogapplication.Service;

import java.util.List;
import java.util.Optional;
import com.mansi.travellerblogapplication.Entity.TravellerEntity;
import com.mansi.travellerblogapplication.Repository.TravellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TravellerService {

    @Autowired
    private TravellerRepository travellerRepository;

    public void saveImage(TravellerEntity imageGallery) {
        travellerRepository.save(imageGallery);
    }

    public List<TravellerEntity> getAllActiveImages() {
        return travellerRepository.findAll();
    }

    public Optional<TravellerEntity> getImageById(Long id) {
        return travellerRepository.findById(id);
    }
}