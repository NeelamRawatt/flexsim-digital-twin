package com.example.flexsim_simulation_service.DTO;


import lombok.Data;

import java.time.LocalDate;

@Data
public class FileUploadedDTO {

    private Integer sim_exp_id;
    private String uploaded_file_name;
    private String uploaded_file_type;
    private LocalDate file_upload_date;


}
