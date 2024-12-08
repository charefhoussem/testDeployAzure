package com.dtalk.ecosystem.services.impl;

import com.dtalk.ecosystem.entities.Design;
import com.dtalk.ecosystem.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class DesignServiceTest {

    @Mock
    private DesignRepository designRepository;

    @Mock


    @InjectMocks
    private DesignServiceImpl designService;



    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);


    }

    @Test
    public void testGetDesignById() {
        Design design = new Design();
        design.setIdDesign(1L);
        when(designRepository.findById(1L)).thenReturn(Optional.of(design));

        Design result = designService.getDesignById(1L);
       assertEquals(1L,result.getIdDesign());
    }

    @Test
    public void testRetrieveAllDesigns() {
        List<Design> designs = List.of(new Design(), new Design());
        when(designRepository.findAll()).thenReturn(designs);

        List<Design> result = designService.retrieveAllDesgins();
        assertEquals(designs.size(), result.size());
    }



}
