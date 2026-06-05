package org.Application.client;

import lombok.extern.slf4j.Slf4j;
import org.Application.client.dto.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class ProfileClient {

    @Autowired
    private RestTemplate restTemplate;

    public ProfileResponse getProfileByUserId(String userId) {
        String url = "http://profile-service/api/v1/profiles/public/user/" + userId;
        try {
            return restTemplate.getForObject(url, ProfileResponse.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Profile not found in Profile-Service: userId={}", userId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Người phỏng vấn không tồn tại");
            }
            log.error("HTTP error calling Profile Service: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể xác thực thông tin người phỏng vấn");
        } catch (Exception e) {
            log.error("Error calling Profile Service to fetch profile: userId={}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể kết nối đến Profile Service");
        }
    }
}
