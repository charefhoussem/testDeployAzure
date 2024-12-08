package com.dtalk.ecosystem.services.impl;
import com.dtalk.ecosystem.DTOs.request.reclamation.ReclamationRequest;
import com.dtalk.ecosystem.entities.Design;
import com.dtalk.ecosystem.entities.users.User;
import com.dtalk.ecosystem.repositories.DesignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReclamationServiceTest {
    @InjectMocks
    private ReclamationServiceImpl reclamationService;

    @Mock
    private ReclamationRepository reclamationRepository;

    @Mock
    private DesignRepository designRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddReclamation_Success() {
        Long idUser = 1L;
        Long idDesign = 1L;
        ReclamationRequest request = new ReclamationRequest();
        request.setDescription("Test description");

        User user = new User();
        user.setIdUser(idUser);

        Design design = new Design();
        design.setIdDesign(idDesign);

        when(userRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(designRepository.findById(idDesign)).thenReturn(Optional.of(design));

        Reclamation reclamation = Reclamation.builder()
                .description(request.getDescription())
                .etat("non traité")
                .date(LocalDateTime.now())
                .user(user)
                .design(design)
                .build();

        when(reclamationRepository.save(any(Reclamation.class))).thenReturn(reclamation);

        Reclamation result = reclamationService.addReclamation(request, idUser, idDesign);

        assertNotNull(result);
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals("non traité", result.getEtat());
        assertEquals(user, result.getUser());
        assertEquals(design, result.getDesign());

        verify(userRepository, times(1)).findById(idUser);
        verify(designRepository, times(1)).findById(idDesign);
        verify(reclamationRepository, times(1)).save(any(Reclamation.class));
    }
    @Test
    public void testGetReclamationById_Success() {
        Long idRec = 1L;
        Reclamation reclamation = new Reclamation();
        reclamation.setIdReclamation(idRec);

        when(reclamationRepository.findById(idRec)).thenReturn(Optional.of(reclamation));

        Reclamation result = reclamationService.getReclamationById(idRec);

        assertNotNull(result);
        assertEquals(idRec, result.getIdReclamation());

        verify(reclamationRepository, times(1)).findById(idRec);
    }



}
