package com.example.woddy.user.service;

import com.example.woddy.user.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import com.example.woddy.user.dto.CustomOAuth2User;
import com.example.woddy.user.dto.GoogleResponse;
import com.example.woddy.user.dto.OAuth2Response;
import com.example.woddy.user.dto.UserDTO;
import com.example.woddy.user.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.woddy.user.utils.webConfig;

import java.util.Map;


@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final webConfig webConfig = new webConfig();


    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String oauthId = oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();

        return getoAuth2User(oauthId, email, registrationId);
    }




    @Transactional
    public CustomOAuth2User findOrSaveMember(String id_token, String provider) throws JsonProcessingException {

        OAuth2Attribute oAuth2Attribute;
        switch (provider) {
            case "google":
                oAuth2Attribute = getGoogleData(id_token);
                break;
            default:
                throw new RuntimeException("제공하지 않는 인증기관입니다.");
        }


        String oauthId = oAuth2Attribute.getUserId();
        String email = oAuth2Attribute.getEmail();
        String registrationId = oAuth2Attribute.getProvider();

        return getoAuth2User(oauthId, email, registrationId);


    }

    private CustomOAuth2User getoAuth2User(String oauthId, String email, String registrationId) {


        UserEntity existData = userRepository.findByOauthId(oauthId);


        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setOauthId(oauthId);
            userEntity.setEmail(email);
            userEntity.setOauthProvider(registrationId);

            UserEntity user = userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(email);
            userDTO.setOauthId(oauthId);
            userDTO.setOauthProvider(registrationId);

            return new CustomOAuth2User(userDTO);
        } else {    //user가 존재할 경우, update 진행.
            existData.setEmail(email);

            UserEntity user = userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(email);
            userDTO.setOauthId(oauthId);
            userDTO.setOauthProvider(registrationId);

            return new CustomOAuth2User(userDTO);
        }
    }

    private OAuth2Attribute getGoogleData(String idToken) throws IOException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String googleApi = "https://oauth2.googleapis.com/tokeninfo";
        String targetUrl = UriComponentsBuilder.fromHttpUrl(googleApi)
                .queryParam("id_token", idToken)
                .build().toUriString();

        ResponseEntity<String> response = webConfig.restTemplate().exchange(targetUrl, HttpMethod.GET, entity, String.class);

        // Use Jackson to parse the JSON response
        JsonNode jsonBody = objectMapper.readTree(response.getBody());

        // Convert JsonNode to Map
        Map body = objectMapper.convertValue(jsonBody, Map.class);

        // Extracting necessary attributes from the response
        return OAuth2Attribute.of("google", "sub", body);
    }

}