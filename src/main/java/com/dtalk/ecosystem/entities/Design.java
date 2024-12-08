package com.dtalk.ecosystem.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="designs")

public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDesign;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotBlank(message = "Image path is mandatory")
    private String imagePath;

    @NotBlank(message = "Origin file path is mandatory")
    private String originFilePath;

    @NotNull(message = "Publish status is mandatory")
    private Boolean isPublished;

    @NotNull(message = "Acceptance status is mandatory")
    private Boolean isAccepted;



    @Transient
    private MultipartFile imageFile;
    @Transient
    private MultipartFile originFile;



}
