package com.dtalk.ecosystem.controllers;

import com.dtalk.ecosystem.entities.Design;
import com.dtalk.ecosystem.response.ResponseHandler;
import com.dtalk.ecosystem.services.DesignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/design")
@AllArgsConstructor
public class DesignController {
    private final DesignService designService;
    @GetMapping("/")
    public List<Design> getAllDesigns() {
        return designService.retrieveAllDesgins();
    }


    @GetMapping("/{id}")
    public Design getDesignById(@PathVariable Long id) {
        Design design = designService.getDesignById(id);
        return design;

    }

    @GetMapping("/accepted-published")
    public List<Design> aaceptedAndPublishedDesign(){
        List<Design> designs = designService.retrieveAllDesginsAcceptedAndPublished();

        return designs;

    }

    @GetMapping("/designer/{idDesigner}")

    public List<Design> getAllDesignByUser(@PathVariable("idDesigner") Long id){
        List<Design> designs = designService.retrieveAllDesginByUser(id);
        return designs;

    }

    @PostMapping("/add/{idDesigner}")
    public ResponseEntity<Object> createDesign(
            @RequestParam("description") String description,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("originFile") MultipartFile originFile,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("tagNames") List<String> tags,
            @RequestParam("fields") List<String> fields,

            @PathVariable Long idDesigner

            ) {

        try {
            Design savedDesign = designService.createDesign(name,price,description,imageFile,originFile,idDesigner,tags,fields);
            return ResponseHandler.responseBuilder("design added  successfully", HttpStatus.OK,null);
        } catch (IOException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }catch (NoSuchAlgorithmException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);

        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<Design> updateDesign(
            @PathVariable Long id,
            @RequestParam("description") String description,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("tagNames") List<String> tags,
            @RequestParam("fieldTitles") List<String> fieldTitles

            ) {


            Design updatedDesign = designService.modifyDesign(id,name,price,description,tags,fieldTitles);
            return ResponseEntity.ok(updatedDesign);

    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<Object> acceptDesign(@PathVariable("id") Long id){
        Boolean accept = designService.acceptDesign(id);
        if (accept){
         return    ResponseHandler.responseBuilder("design accepted",HttpStatus.OK,null);
        }else{
         return    ResponseHandler.responseBuilder("design not found",HttpStatus.NOT_FOUND,null);
        }
    }

    @PutMapping("/disaccept/{id}")
    public ResponseEntity<Object> disacceptDesign(@PathVariable("id") Long id){
        Boolean disccept = designService.disacceptDesign(id);
        if (disccept){
            return    ResponseHandler.responseBuilder("design disccepted",HttpStatus.OK,null);
        }else{
            return    ResponseHandler.responseBuilder("design not found",HttpStatus.NOT_FOUND,null);
        }
    }

    @PutMapping("/publish/{idDesign}")
    public ResponseEntity<Object>  publichDesign(@PathVariable("idDesign") Long id){
        Boolean ispublished = designService.publishDesign(id);
        if (ispublished){
            return    ResponseHandler.responseBuilder("design published",HttpStatus.OK,null);

        }else{
            return    ResponseHandler.responseBuilder("design not found",HttpStatus.NOT_FOUND,null);

        }
    }

    @PutMapping("/unpublish/{idDesign}")
    public ResponseEntity<Object>  unpublichDesign(@PathVariable("idDesign") Long id){
        Boolean isunpublished = designService.unpublishDesign(id);
        if (isunpublished){
            return    ResponseHandler.responseBuilder("design unpublished",HttpStatus.OK,null);

        }else{
            return    ResponseHandler.responseBuilder("design not found",HttpStatus.NOT_FOUND,null);

        }
    }


    @DeleteMapping("/delete/{idDesign}")
    public ResponseEntity<Object> deleteDesign(@PathVariable("idDesign") Long id){
        designService.deleteDesign(id);
        return ResponseHandler.responseBuilder("design deleted",HttpStatus.OK,null);


    }

}
