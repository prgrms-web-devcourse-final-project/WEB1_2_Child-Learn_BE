package com.prgrms.ijuju.domain.member.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.entity.Role;
import com.prgrms.ijuju.domain.member.oAuth2.CustomOAuth2User;
import com.prgrms.ijuju.domain.member.oAuth2.GoogleResponse;
import com.prgrms.ijuju.domain.member.oAuth2.KakaoResponse;
import com.prgrms.ijuju.domain.member.oAuth2.OAuth2Response;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser 실행");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User.getAttributes().toString());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            log.info("google");
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            log.info("kakao");
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        Optional<Member> opMember = memberRepository.findByEmail(oAuth2Response.getEmail());
        if (opMember.isEmpty()) {
            Member member = Member.builder()
                    .email(oAuth2Response.getEmail())
                    .username(oAuth2Response.getUserName() + new Random().nextInt(1000))
                    .role(Role.USER)
                    .pw(oAuth2Response.getProviderId() + " " + oAuth2Response.getProvider())
                    .build();

            memberRepository.save(member);
        } else {
            opMember.get().changeEmail(oAuth2Response.getEmail());

            memberRepository.save(opMember.get());
        }

        Member member = memberRepository.findByEmail(oAuth2Response.getEmail()).get();

        return new CustomOAuth2User(oAuth2Response, String.valueOf(member.getRole()),member.getId());
    }

}
