package junkyard.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.JunkyardPaymentConfig;
import junkyard.car.domain.CarInfo;
import junkyard.member.domain.MemberUser;
import junkyard.payment.application.CheckoutFacade;
import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.checkout.CheckoutResult;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.interfaces.controller.api.CheckoutApi;
import junkyard.payment.interfaces.controller.api.CheckoutDto;
import junkyard.reservation.domain.Reservation;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.ReservationService;
import junkyard.reservation.domain.estimate.EstimateInfo;
import junkyard.response.CommonResponse;
import junkyard.security.userdetails.user.MyUserDetails;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private CheckoutFacade checkoutFacade;


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

    static ReservationInfo mockReservationInfo(String clientName, Double amount, String reservationId) {
        CarInfo carInfo = new CarInfo("Honda", "Civic", "XYZ987");
        EstimateInfo estimateInfo = new EstimateInfo(amount, LocalDateTime.now(), "Initial estimate", true);
        List<EstimateInfo> estimateInfos = List.of(estimateInfo);

        return new ReservationInfo(
                clientName,
                "987-654-3210",
                reservationId,
                "user789",
                carInfo,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                Reservation.State.COMPLETED,
                "No specific contents",
                null,
                estimateInfos,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @WithMockUser
    @Test
    public void paymentCheckout_shouldReturnSuccess() throws Exception{
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");

        String reservationId = "1";
        String janeDoe = "jane Doe";
        double amount = 200.0;
        CheckoutDto.CheckoutRequest checkoutRequest = CheckoutDto.CheckoutRequest.builder()
                .reservationId(reservationId)
                .buyerId(1L)
                .build();

        CheckoutResult result = CheckoutResult.builder()
                .orderId(reservationId)
                .amount(BigDecimal.valueOf(amount))
                .orderName(janeDoe)
                .build();

//        when(reservationService.inquireInfo(eq(myUserDetails.memberUser().getAuthId()), any())).thenReturn(reservationInfo);
        when(checkoutFacade.checkout(anyLong(), any())).thenReturn(result);

        MvcResult mvcResult = mockMvc.perform(post("/v1/api/payment/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(checkoutRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .with(authentication(atc))
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("code").value("NORMAL"))
                .andExpect(jsonPath("message").value("정상처리되었습니다."))
                .andExpect(jsonPath("data.orderId").isNotEmpty())
                .andExpect(jsonPath("data.orderName").value(janeDoe))
                .andExpect(jsonPath("data.amount").value(amount))
                .andReturn();
    }

}
