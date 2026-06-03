package org.Application.client;

import lombok.extern.slf4j.Slf4j;
import org.Application.client.dto.JobResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class JobClient {

    @Autowired
    private RestTemplate restTemplate;

    public JobResponse getJob(String jobId) {
        String url = "http://job-service/internal/jobs/" + jobId;
        try {
            return restTemplate.getForObject(url, JobResponse.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Job not found in Job-Service: jobId={}", jobId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Công việc không tồn tại");
            }
            log.error("HTTP error calling Job Service: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể xác thực thông tin công việc từ Job Service");
        } catch (Exception e) {
            log.error("Error calling Job Service to fetch job: jobId={}", jobId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể kết nối đến Job Service");
        }
    }
}
