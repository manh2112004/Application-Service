package org.Application.command.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationRequest {
    @NotBlank(message = "Mã công việc không được để trống")
    private String jobId;

    @NotBlank(message = "Mã công ty không được để trống")
    private String companyId;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    private String currentJobTitle;
    private String coverLetter;
    private String linkedinUrl;
    private String portfolioUrl;
    private String resumeFileUrl;
}
