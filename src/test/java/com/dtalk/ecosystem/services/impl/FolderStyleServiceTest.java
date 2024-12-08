package com.dtalk.ecosystem.services.impl;
import com.dtalk.ecosystem.DTOs.request.folderStyle.AddFolderStyleRequest;
import com.dtalk.ecosystem.DTOs.request.folderStyle.ModifyFolderStyleRequest;

import com.dtalk.ecosystem.entities.users.FashionDesigner;
import com.dtalk.ecosystem.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;
public class FolderStyleServiceTest {


    @Mock
    private FashionDesignerRepository fashionDesignerRepository;

    @Mock
    private FolderStyleRepository folderStyleRepository;

    @Mock
    private FieldFolderStyleRepository fieldFolderStyleRepository;

    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private FolderStyleServiceImpl folderStyleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFolderStyleById() {
        Long idFolderStyle = 1L;
        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(idFolderStyle);

        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.of(folderStyle));

        FolderStyle result = folderStyleService.getFolderStyleById(idFolderStyle);

        assertEquals(idFolderStyle, result.getIdFolder());
    }

    @Test
    void testGetFolderStyleById_NotFound() {
        Long idFolderStyle = 1L;
        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            folderStyleService.getFolderStyleById(idFolderStyle);
        });
    }

    @Test
    void testRetrieveAllFolderStyles() {
        List<FolderStyle> folderStyles = new ArrayList<>();
        folderStyles.add(new FolderStyle());
        when(folderStyleRepository.findAll()).thenReturn(folderStyles);

        List<FolderStyle> result = folderStyleService.retrieveAllFolderStyles();

        assertEquals(folderStyles.size(), result.size());
    }

    @Test
    void testRetrieveAllFolderStyleAcceptedAndPublished() {
        List<FolderStyle> folderStyles = new ArrayList<>();
        folderStyles.add(new FolderStyle());
        when(folderStyleRepository.findFolderStylesByIsPublishedIsTrueAndIsAcceptedIsTrue()).thenReturn(folderStyles);

        List<FolderStyle> result = folderStyleService.retrieveAllFolderStyleAcceptedAndPublished();

        assertEquals(folderStyles.size(), result.size());
    }

    @Test
    void testRetrieveAllFolderStyleByUser() {
        Long idUser = 1L;
        FashionDesigner user = new FashionDesigner();
        user.setIdUser(idUser);

        List<FolderStyle> folderStyles = new ArrayList<>();
        folderStyles.add(new FolderStyle());

        when(fashionDesignerRepository.findById(idUser)).thenReturn(Optional.of(user));
        when(folderStyleRepository.findFolderStylesByFashionDesignerEquals(user)).thenReturn(folderStyles);

        List<FolderStyle> result = folderStyleService.retrieveAllFolderStyleByUser(idUser);

        assertEquals(folderStyles.size(), result.size());
    }

    @Test
    void testCreateFolderStyle() throws IOException {
        AddFolderStyleRequest request = new AddFolderStyleRequest();
        request.setName("Test Folder Style");
        request.setDescription("This is a test folder style");
        request.setType("Test Type");
        request.setPrice(100.0);
        MultipartFile originFile = mock(MultipartFile.class);
        request.setOriginFile(originFile);
        List<String> fields = new ArrayList<>();
        fields.add("Test Field");
        request.setFields(fields);

        Long idFashionDesigner = 1L;
        FashionDesigner user = new FashionDesigner();
        user.setIdUser(idFashionDesigner);

        when(fashionDesignerRepository.findById(idFashionDesigner)).thenReturn(Optional.of(user));
        when(fileStorageService.saveFile(originFile)).thenReturn("originFilePath");
        when(fieldFolderStyleRepository.findByTitle("Test Field")).thenReturn(Optional.empty());

        FolderStyle savedFolderStyle = new FolderStyle();
        savedFolderStyle.setIdFolder(1L);
        savedFolderStyle.setName(request.getName());
        savedFolderStyle.setDescription(request.getDescription());
        savedFolderStyle.setType(request.getType());
        savedFolderStyle.setPrice(request.getPrice());
        savedFolderStyle.setIsAccepted(false);
        savedFolderStyle.setIsPublished(false);
        savedFolderStyle.setOriginFile("originFilePath");

        when(folderStyleRepository.save(any(FolderStyle.class))).thenReturn(savedFolderStyle);

        FolderStyle createdFolderStyle = folderStyleService.createFolderStyle(request, idFashionDesigner);

        assertEquals(request.getName(), createdFolderStyle.getName());
        assertEquals(request.getDescription(), createdFolderStyle.getDescription());
        assertEquals(request.getType(), createdFolderStyle.getType());
        assertEquals(request.getPrice(), createdFolderStyle.getPrice());
        assertEquals("originFilePath", createdFolderStyle.getOriginFile());
        assertEquals(false, createdFolderStyle.getIsAccepted());
        assertEquals(false, createdFolderStyle.getIsPublished());
        verify(folderStyleRepository, times(1)).save(any(FolderStyle.class));
    }

    @Test
    void testAcceptFolderStyle() {
        FashionDesigner  fashionDesigner = new FashionDesigner();
        fashionDesigner.setEmail("fashion-designer@example.com");

        Long idFolderStyle = 1L;
        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(idFolderStyle);
        folderStyle.setIsAccepted(false);
        folderStyle.setFashionDesigner(fashionDesigner);

        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.of(folderStyle));

        Boolean result = folderStyleService.acceptFolderStyle(idFolderStyle);

        assertEquals(true, result);
        assertEquals(true, folderStyle.getIsAccepted());
        verify(folderStyleRepository, times(1)).save(folderStyle);

        verify(emailService, times(1)).notification("fashion-designer@example.com", true, "NotificationFolderStyle", "Notification Dossier de style");

    }

    @Test
    void testDisacceptFolderStyle() {
         FashionDesigner  fashionDesigner = new FashionDesigner();
          fashionDesigner.setEmail("fashion-designer@example.com");

        Long idFolderStyle = 1L;
        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(idFolderStyle);
        folderStyle.setIsAccepted(true);
        folderStyle.setFashionDesigner(fashionDesigner);
        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.of(folderStyle));

        Boolean result = folderStyleService.disacceptFolderStyle(idFolderStyle);

        assertEquals(true, result);
        assertEquals(false, folderStyle.getIsAccepted());
        verify(folderStyleRepository, times(1)).save(folderStyle);
        verify(emailService, times(1)).notification("fashion-designer@example.com", false, "NotificationFolderStyle", "Notification Dossier de style");

    }

    @Test
    void testPublishFolderStyle() {
        Long idFolderStyle = 1L;
        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(idFolderStyle);
        folderStyle.setIsPublished(false);

        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.of(folderStyle));

        Boolean result = folderStyleService.publishFolderStyle(idFolderStyle);

        assertEquals(true, result);
        assertEquals(true, folderStyle.getIsPublished());
        verify(folderStyleRepository, times(1)).save(folderStyle);
    }

    @Test
    void testUnpublishFolderStyle() {
        Long idFolderStyle = 1L;
        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(idFolderStyle);
        folderStyle.setIsPublished(true);

        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.of(folderStyle));

        Boolean result = folderStyleService.unpublishFolderStyle(idFolderStyle);

        assertEquals(true, result);
        assertEquals(false, folderStyle.getIsPublished());
        verify(folderStyleRepository, times(1)).save(folderStyle);
    }

    @Test
    void testModifyFolderStyle() {
        ModifyFolderStyleRequest request = new ModifyFolderStyleRequest();
        request.setId(1L);
        request.setName("Modified Folder Style");
        request.setDescription("This is a modified folder style");
        request.setType("Modified Type");
        request.setPrice(150.0);
        List<String> fields = new ArrayList<>();
        fields.add("Modified Field");
        request.setFields(fields);

        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(request.getId());

        when(folderStyleRepository.findById(request.getId())).thenReturn(Optional.of(folderStyle));
        when(fieldFolderStyleRepository.findByTitle("Modified Field")).thenReturn(Optional.empty());

        FolderStyle modifiedFolderStyle = new FolderStyle();
        modifiedFolderStyle.setIdFolder(request.getId());
        modifiedFolderStyle.setName(request.getName());
        modifiedFolderStyle.setDescription(request.getDescription());
        modifiedFolderStyle.setType(request.getType());
        modifiedFolderStyle.setPrice(request.getPrice());

        when(folderStyleRepository.save(any(FolderStyle.class))).thenReturn(modifiedFolderStyle);

        FolderStyle result = folderStyleService.modifyFolderStyle(request);

        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getType(), result.getType());
        assertEquals(request.getPrice(), result.getPrice());
        verify(folderStyleRepository, times(1)).save(any(FolderStyle.class));
    }

    @Test
    void testDeleteFolderStyle() {
        Long idFolderStyle = 1L;
        FolderStyle folderStyle = new FolderStyle();
        folderStyle.setIdFolder(idFolderStyle);

        when(folderStyleRepository.findById(idFolderStyle)).thenReturn(Optional.of(folderStyle));

        folderStyleService.deleteFolderStyle(idFolderStyle);

        verify(folderStyleRepository, times(1)).deleteById(idFolderStyle);
    }

}
