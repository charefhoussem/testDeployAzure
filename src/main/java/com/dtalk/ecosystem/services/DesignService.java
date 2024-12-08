package com.dtalk.ecosystem.services;

import com.dtalk.ecosystem.entities.Design;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface DesignService {
    public Design getDesignById(Long idDesign) ;
    public List<Design> retrieveAllDesgins();
    public List<Design> retrieveAllDesginsAcceptedAndPublished();

    public List<Design> retrieveAllDesginByUser(Long idUser);
    public Design createDesign(String name, double price,String description, MultipartFile imageFile, MultipartFile originFile,Long idDesigner,List<String> tagNames,List<String> fieldTitles) throws IOException, NoSuchAlgorithmException;
    public Boolean acceptDesign(Long idDesign);
    public Boolean disacceptDesign(Long idDesign);

    public Boolean publishDesign(Long idDesign);
    public Boolean unpublishDesign(Long idDesign);

    public Design modifyDesign(Long id,String name, double price, String description,List<String> tagNames,List<String> fieldTitles);

    public void deleteDesign(Long idDesign);
}
