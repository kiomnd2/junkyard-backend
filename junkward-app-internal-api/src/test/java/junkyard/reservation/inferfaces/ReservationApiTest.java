package junkyard.reservation.inferfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.common.response.codes.Codes;
import junkyard.member.domain.MemberUser;
import junkyard.reservation.application.ReservationFacade;
import junkyard.security.JwtTokenProvider;
import junkyard.security.userdetails.MyUserDetails;
import junkyard.utils.IdempotencyCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReservationApi.class)
class ReservationApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationFacade reservationFacade;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private MyUserDetails myUserDetails;


    @BeforeEach
    public void setup() {
        MemberUser member = MemberUser.builder()
                .name("김")
                .email("test@email.com")
                .phoneNo("01000020002")
                .authId(12345L)
                .joinMethod(MemberUser.JoinMethod.KAKAO)
                .build();
        myUserDetails = new MyUserDetails(member);

    }

    @Test
    public void issueIdempotencyKey_shouldReturnSuccess() throws Exception {
        String idempotencyKey = "idempotency_key";


        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");

        try (MockedStatic<IdempotencyCreator> mockedStatic = mockStatic(IdempotencyCreator.class)) {

            mockedStatic.when(() ->IdempotencyCreator.create(anyString())).thenReturn(idempotencyKey);

            mockMvc.perform(post("/v1/api/reservation/checkout")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                            .with(authentication(atc))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                    .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                    .andExpect(jsonPath("data.idempotencyKey").value(idempotencyKey))
                    .andDo(MockMvcRestDocumentation.document("reservation-checkout",
                            requestHeaders(
                                    headerWithName("Authorization").description("Bearer token")
                            ),
                            responseFields(
                                    fieldWithPath("code").type("String").description("응답 결과 코드"),
                                    fieldWithPath("message").type("String").description("응답 메시지"),
                                    fieldWithPath("data.idempotencyKey").description("멱등성 키")
                            )
                    ))
            ;

        }
    }
}