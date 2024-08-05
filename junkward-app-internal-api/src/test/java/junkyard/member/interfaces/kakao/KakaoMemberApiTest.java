package junkyard.member.interfaces.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.member.application.MemberFacade;
import junkyard.member.domain.CheckUserResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(KakaoMemberApi.class)
class KakaoMemberApiTest {


    @Autowired
    protected MockMvc mockMvc;


    @MockBean
    private MemberFacade memberFacade;

    @WithMockUser
    @Test
    void authCheck_withValidAccessToken_shouldReturnSuccess() throws Exception {
        String accessToken = "accessToken";

        CheckUserResult result = CheckUserResult.builder()
                .nickname("김띠용")
                .isJoined(false)
                .token("token")
                .authId(12345L)
                .build();

        when(memberFacade.checkMember(accessToken, "kakao")).thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/member/kakao/auth-check")
                .header("kakaoAccessToken", accessToken)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.kakaoId").value(result.authId()))
                .andExpect(jsonPath("data.nickname").value(result.nickname()))
                .andExpect(jsonPath("data.joined").value(result.isJoined()))
                .andExpect(jsonPath("data.token").value(result.token()))
                .andDo(print())
                .andDo(document("authCheck",
                        requestHeaders(
                                headerWithName("kakaoAccessToken").description("카카오 Oauth 에서 반환반은 accessToken")
                        ),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지"),
                                fieldWithPath("data.kakaoId").description("카카오에서 사용자 정보 조회시 반환받은 사용자 고유 ID 값"),
                                fieldWithPath("data.token").optional().description("사용자 가입 조회 성공 시 생성된 JWT 토큰"),
                                fieldWithPath("data.joined").description("사용자 가입 여부"),
                                fieldWithPath("data.nickname").description("카카오에서 사용자 정보 조회시 반환반은 사용지 닉네입")
                        ))
                );
    }

}