package org.Application.client;

import lombok.extern.slf4j.Slf4j;
import org.Application.client.dto.CompanyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class CompanyClient {

    @Autowired
    private RestTemplate restTemplate;

    public CompanyResponse getCompany(String companyId) {
        String url = "http://company-service/internal/companies/" + companyId;
        try {
            return restTemplate.getForObject(url, CompanyResponse.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Company not found in Company-Service: companyId={}", companyId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công ty không tồn tại");
            }
            log.error("HTTP error calling Company Service: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể xác thực thông tin công ty từ Company Service");
        } catch (Exception e) {
            log.error("Error calling Company Service to fetch company: companyId={}", companyId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể kết nối đến Company Service");
        }
    }
}
