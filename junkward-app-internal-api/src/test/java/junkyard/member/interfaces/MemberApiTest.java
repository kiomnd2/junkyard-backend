package junkyard.member.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.common.response.codes.Codes;
import junkyard.member.application.MemberFacade;
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
                .phoneNo("01000000001")
                .email("test@email.com")
                .method("kakao")
                .build();

        String testToken = "test-token";

        when(memberFacade.joinMember(any())).thenReturn(testToken);

        mockMvc.perform(post("/v1/api/member/join").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(memberDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andExpect(jsonPath("data.token").value(testToken))
                .andDo(MockMvcRestDocumentation.document("join",
                        requestFields(
                                fieldWithPath("id").description("사용자 체크 시, 리턴받은 고유 Id").type("Numeric"),
                                fieldWithPath("name").description("사용자 가입 시, 사용할 닉네임").type("String"),
                                fieldWithPath("phoneNo").description("사용자 가입 시, 사용할 핸드폰 번호").type("String"),
                                fieldWithPath("email").description("사용자 가입 시, 사용할 이메일").type("String"),
                                fieldWithPath("method").description("가입 매치 (kakao,toss ...)").type("String")
                        ),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지"),
                                fieldWithPath("data.token").description("JWT 토큰 값")
                        )
                        ))
        ;
    }


}
