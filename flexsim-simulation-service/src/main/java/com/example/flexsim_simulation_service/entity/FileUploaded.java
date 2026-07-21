package com.example.flexsim_simulation_service.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="uploadedfile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploaded {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long seq_id;


        @Column(name="sim_exp_id")
        private Integer simExpId;
        private String uploaded_file_name;
        private String uploaded_file_type;
        private LocalDate file_upload_date;


}
