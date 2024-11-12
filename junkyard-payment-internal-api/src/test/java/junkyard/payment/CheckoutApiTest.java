package junkyard.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.member.domain.MemberUser;
import junkyard.payment.interfaces.controller.api.CheckoutApi;
import junkyard.security.userdetails.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CheckoutApi.class)
class CheckoutApiTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

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
    @WithMockUser
    @Test
    public void paymentCheckout_shouldReturnSuccess() throws Exception{
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");



        mockMvc.perform(post("/v1/api/payment/checkout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .with(authentication(atc))
                .with(csrf())
        ).andExpect(status().isOk());


    }

}
