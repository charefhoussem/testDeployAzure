package com.dtalk.ecosystem.services.impl;

import com.dtalk.ecosystem.entities.Design;
import com.dtalk.ecosystem.exceptions.ResourceNotFoundException;
import com.dtalk.ecosystem.repositories.*;
import com.dtalk.ecosystem.services.DesignService;
import com.dtalk.ecosystem.utils.UniqueIdentifierUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@AllArgsConstructor
public class DesignServiceImpl implements DesignService {
    private final DesignRepository designRepository;
    private final BlobService myBlobService;


    @Override
    public Design getDesignById(Long idDesign) {
        return null;
    }

    @Override
    public List<Design> retrieveAllDesgins() {
        return designRepository.findAll();
    }

    @Override
    public List<Design> retrieveAllDesginsAcceptedAndPublished() {
      return designRepository.findDesignsByIsPublishedIsTrueAndIsAcceptedIsTrue();
     }

    @Override
    public List<Design> retrieveAllDesginByUser(Long idUser) {
        return List.of();
    }


    @Override
    public Design createDesign(String name, double price, String description, MultipartFile imageFile, MultipartFile originFile, Long idDesigner, List<String> tagNames,List<String> fieldTitles) throws IOException, NoSuchAlgorithmException {
        Design design = new Design();
        design.setDescription(description);
        design.setName(name);
        design.setIsAccepted(false);
        design.setIsPublished(false);

        String regenrateImageName =UniqueIdentifierUtil.generateSecureIdentifier(imageFile.getOriginalFilename());
        myBlobService.storeFile(regenrateImageName,imageFile.getInputStream(), imageFile.getSize());
        design.setImagePath(regenrateImageName);

        regenrateImageName =UniqueIdentifierUtil.generateSecureIdentifier(originFile.getOriginalFilename());
        myBlobService.storeFile(regenrateImageName,originFile.getInputStream(), originFile.getSize());
        design.setOriginFilePath(regenrateImageName);



        return designRepository.save(design);
    }

    @Override
    public Boolean acceptDesign(Long idDesign) {
        return null;
    }

    @Override
    public Boolean disacceptDesign(Long idDesign) {
        return null;
    }

    @Override
    public Boolean publishDesign(Long idDesign) {
        return null;
    }

    @Override
    public Boolean unpublishDesign(Long idDesign) {
        return null;
    }

    @Override
    public Design modifyDesign(Long id, String name, double price, String description, List<String> tagNames, List<String> fieldTitles) {
        return null;
    }


    @Override
    public void deleteDesign(Long idDesign) {
        Design design = designRepository.findById(idDesign).orElseThrow(()->new ResourceNotFoundException("Design not found with id: " + idDesign));
            designRepository.deleteById(idDesign);

    }





}

