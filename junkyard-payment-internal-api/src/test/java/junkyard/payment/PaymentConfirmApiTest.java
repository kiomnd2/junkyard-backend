package junkyard.payment;


import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.member.domain.MemberUser;
import junkyard.payment.domain.confirm.PaymentValidator;
import junkyard.payment.interfaces.controller.api.TossPaymentApi;
import junkyard.security.userdetails.user.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TossPaymentApi.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PaymentConfirmApiTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    PaymentValidator paymentValidator;

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
    void shouldBeMarkedSuccessIfPaymentConfirmationSuccessPSP() throws Exception {
        String orderId;



        when(paymentValidator.isValid(any(), any())).thenReturn(true);

        mockMvc.perform(post("/v1/payment/toss/confirm"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
