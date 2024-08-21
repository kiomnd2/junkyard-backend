package junkyard.member.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.common.response.codes.Codes;
import junkyard.member.application.MemberFacade;
import junkyard.member.application.TokenInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MemberApi.class)
public class MemberApiTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MemberFacade memberFacade;

    @WithMockUser
    @Test
    void registerMemberJoinTest() throws Exception {
        MemberDto.RequestJoin memberDto = MemberDto.RequestJoin.builder()
                .id(123L)
                .name("kim")
                .profileUrl("http://test.url")
                .phoneNo("01000000001")
                .email("test@email.com")
                .method("kakao")
                .build();

        String testToken = "test-token";
        String refreshToken = "refresh-token";
        TokenInfo tokenInf = TokenInfo.builder()
                .accessToken(testToken)
                .refreshToken(refreshToken)
                .build();
        when(memberFacade.joinMember(any())).thenReturn(tokenInf);

        mockMvc.perform(post("/v1/api/member/join").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(memberDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andExpect(jsonPath("data.token.accessToken").value(testToken))
                .andExpect(jsonPath("data.token.refreshToken").value(refreshToken))
                .andDo(MockMvcRestDocumentation.document("join",
                        requestFields(
                                fieldWithPath("id").description("사용자 체크 시, 리턴받은 고유 Id").type("Numeric"),
                                fieldWithPath("name").description("사용자 가입 시, 사용할 닉네임").type("String"),
                                fieldWithPath("profileUrl").description("카카오 사용자 프로필 URL").type("String"),
                                fieldWithPath("phoneNo").description("사용자 가입 시, 사용할 핸드폰 번호").type("String"),
                                fieldWithPath("email").description("사용자 가입 시, 사용할 이메일").type("String"),
                                fieldWithPath("method").description("가입 매치 (kakao,toss ...)").type("String")
                        ),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지"),
                                fieldWithPath("data.token.accessToken").description("JWT access 토큰 값"),
                                fieldWithPath("data.token.refreshToken").description("JWT refresh 토큰 값")
                        )
                        ))
        ;
    }

    @WithMockUser
    @Test
    void checkRefreshTokenTest() throws Exception {
        String refreshToken = "refresh-token";

        TokenInfo tokenInfo = TokenInfo.builder()
                .accessToken("accessToken")
                .refreshToken(refreshToken)
                .build();

        when(memberFacade.refresh(refreshToken)).thenReturn(tokenInfo);

        mockMvc.perform(post("/v1/api/member/refresh-token").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("refreshToken", refreshToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andExpect(jsonPath("data.token.accessToken").value(tokenInfo.accessToken()))
                .andExpect(jsonPath("data.token.refreshToken").value(tokenInfo.refreshToken()))
                .andDo(MockMvcRestDocumentation.document("refresh-token",
                        requestHeaders(
                                headerWithName("refreshToken").description("카카오 Oauth 에서 반환반은 accessToken")
                        ),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지"),
                                fieldWithPath("data.token.accessToken").description("JWT access 토큰 값"),
                                fieldWithPath("data.token.refreshToken").description("JWT refresh 토큰 값")
                        ))
                );
    }

}
