package com.winbid.backend.service;

import com.winbid.backend.model.Image;
import com.winbid.backend.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> list(){
        return imageRepository.findByOrderById();
    }
    public Optional<Image> getOne(int id){
        return imageRepository.findById(id);
    }
    public void save(Image image){
        imageRepository.save(image);
    }
    public void delete(int id){
        imageRepository.deleteById(id);
    }
}










