package junkyard.reservation.inferfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import junkyard.car.domain.CarInfo;
import junkyard.common.response.codes.Codes;
import junkyard.member.domain.MemberUser;
import junkyard.reservation.application.ReservationFacade;
import junkyard.reservation.domain.Reservation;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.estimate.EstimateInfo;
import junkyard.security.JwtTokenProvider;
import junkyard.security.userdetails.user.MyUserDetails;
import junkyard.utils.IdempotencyCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

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

            mockedStatic.when(IdempotencyCreator::create).thenReturn(idempotencyKey);

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
                            preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                            preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력

                            requestHeaders( // 요청 헤더 문서화
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                            ),
                            responseFields(
                                    fieldWithPath("code").type("String").description("응답 결과 코드"),
                                    fieldWithPath("message").type("String").description("응답 메시지"),
                                    fieldWithPath("data.idempotencyKey").description("멱등성 키")
                            )
                    ));
        }
    }

    @WithMockUser
    @Test
    public void reservation_shouldRetrunSuccess() throws Exception {
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");
        String idempotencyKey = "idempotency_key";
        String contents = "contents";
        String clientName ="김띠용";

        ReservationDto.RequestCars cars  = ReservationDto.RequestCars.builder()
                .make("hyundai")
                .licensePlate("12345123")
                .model("avante")
                .build();

        ReservationDto.RequestReservation reservation = ReservationDto.RequestReservation.builder()
                .idempotencyKey(idempotencyKey)
                .contents(contents)
                .clientName(clientName)
                .car(cars)
                .build();

        mockMvc.perform(post("/v1/api/reservation")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(reservation))
                .with(authentication(atc))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andDo(MockMvcRestDocumentation.document("reservation",
                        preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                        preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력

                        requestHeaders( // 요청 헤더 문서화
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("idempotencyKey").type("String").description("예약 키"),
                                fieldWithPath("contents").type("String").description("예약 내용"),
                                fieldWithPath("phoneNo").type("String").description("핸드폰 번호"),
                                fieldWithPath("clientName").type("String").description("예약자 명"),
                                fieldWithPath("car.make").type("String").description("차량 제조사"),
                                fieldWithPath("car.model").type("String").description("차량 모델"),
                                fieldWithPath("car.licensePlate").type("String").description("차량 번호")),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지")
                        )
                ));
    }

    @WithMockUser
    @Test
    public void reservationUpdateContents_shouldReturnSuccess() throws Exception {
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");
        String idempotencyKey = "idempotency_key";
        String contents = "예약내용변경";

        ReservationDto.RequestUpdateReservation request = ReservationDto.RequestUpdateReservation.builder()
                .contents(contents)
                .idempotencyKey(idempotencyKey)
                .build();

        mockMvc.perform(put("/v1/api/reservation")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(atc))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andDo(MockMvcRestDocumentation.document("reservation-update",
                        preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                        preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력
                        requestHeaders( // 요청 헤더 문서화
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("idempotencyKey").type("String").description("예약 키"),
                                fieldWithPath("contents").type("String").description("변경 내용")),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지")
                        )
                ));
    }

    @WithMockUser
    @Test
    public void reservationCancel_shouldReturnSuccess() throws Exception {
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");
        String idempotencyKey = "idempotency_key";
        String cancelReason = "취소 사유";

        ReservationDto.RequestCancelReservation requestCancelReservation = ReservationDto.RequestCancelReservation.builder()
                .idempotencyKey(idempotencyKey)
                .cancelReason(cancelReason)
                .build();

        mockMvc.perform(post("/v1/api/reservation/cancel")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(requestCancelReservation))
                        .with(authentication(atc))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andDo(MockMvcRestDocumentation.document("reservation-cancel",
                        preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                        preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력

                        requestHeaders( // 요청 헤더 문서화
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("idempotencyKey").type("String").description("예약 키"),
                                fieldWithPath("cancelReason").type("String").description("취소 사유")),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지")
                        )
                ));
    }

    @WithMockUser
    @Test
    public void inquireReservationInfo_shouldReturnSuccess() throws Exception {
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");
        String username = myUserDetails.getUsername();
        String clientName = "김띠용";
        String reservationId = "idempotency-key";

        ReservationInfo reservationInfo = ReservationInfo.builder()
                .reservationId(reservationId)
                .userId("userId")
                .phoneNo("01000010001")
                .clientName(clientName)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(1))
                .status(Reservation.State.PENDING)
                .carInfo(CarInfo.builder()
                        .make("hyundai")
                        .model("model")
                        .licensePlate("12345")
                        .build())
                .estimateInfos(List.of(EstimateInfo.builder()
                        .amount(1000.0)
                        .isFinal(true)
                        .description("이래저래 가격이럼")
                        .issuedDate(LocalDateTime.now())
                        .build()))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        when(reservationFacade.inquireInfo(Long.parseLong(username), reservationId)).thenReturn(reservationInfo);

        mockMvc.perform(get("/v1/api/reservation/{reservationId}", reservationId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(authentication(atc))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andExpect(jsonPath("data.reservationId").value(reservationId))
                .andExpect(jsonPath("data.userId").value(reservationInfo.userId()))
                .andExpect(jsonPath("data.clientName").value(reservationInfo.clientName()))
                .andExpect(jsonPath("data.phoneNo").value(reservationInfo.phoneNo()))
                .andExpect(jsonPath("data.contents").value(reservationInfo.contents()))
                .andExpect(jsonPath("data.startTime").value(reservationInfo.startTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data.endTime").value(reservationInfo.endTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data.status").value("PENDING"))
                .andExpect(jsonPath("data.updatedAt").value(reservationInfo.updatedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data.createdAt").value(reservationInfo.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data.car.make").value(reservationInfo.carInfo().make()))
                .andExpect(jsonPath("data.car.model").value(reservationInfo.carInfo().model()))
                .andExpect(jsonPath("data.car.licensePlate").value(reservationInfo.carInfo().licensePlate()))
                .andExpect(jsonPath("data.estimate[0].amount").value(reservationInfo.estimateInfos().get(0).amount()))
                .andExpect(jsonPath("data.estimate[0].description").value(reservationInfo.estimateInfos().get(0).description()))
                .andExpect(jsonPath("data.estimate[0].issuedDate").value(reservationInfo.estimateInfos().get(0).issuedDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data.estimate[0].isFinal").value(reservationInfo.estimateInfos().get(0).isFinal()))
                .andDo(MockMvcRestDocumentation.document("inquire-reservation-info", // 생성될 문서의 이름
                        preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                        preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력
                        requestHeaders( // 요청 헤더 문서화
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                        ),
                        responseFields( // 응답 필드 문서화
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지"),
                                fieldWithPath("data.reservationId").type("String").description("예약키"),
                                fieldWithPath("data.userId").type("String").description("사용자 ID"),
                                fieldWithPath("data.clientName").type("String").description("사용자 명"),
                                fieldWithPath("data.phoneNo").type("String").description("핸드폰번호"),
                                fieldWithPath("data.contents").type("String").description("예약내용"),
                                fieldWithPath("data.startTime").type("String").description("예약 시작 시간"),
                                fieldWithPath("data.endTime").type("String").description("예약 종료 시간"),
                                fieldWithPath("data.status").type("String").description("예약 상태"),
                                fieldWithPath("data.updatedAt").type("String").description("최종수정시간"),
                                fieldWithPath("data.createdAt").type("String").description("최종생성시간"),
                                fieldWithPath("data.car.make").type("String").description("차량 제조사"),
                                fieldWithPath("data.car.model").type("String").description("차량 모델"),
                                fieldWithPath("data.car.licensePlate").type("String").description("차량 번호판"),
                                fieldWithPath("data.estimate[0].amount").type("String").description("견적 금액"),
                                fieldWithPath("data.estimate[0].description").type("String").description("견적 설명"),
                                fieldWithPath("data.estimate[0].issuedDate").type("String").description("견적 발행일"),
                                fieldWithPath("data.estimate[0].isFinal").type("String").description("최종 견적 여부")
                        )
                ));
        ;

    }

    @WithMockUser
    @Test
    public void inquireReservationList_shouldReturnSuccess() throws Exception {
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");
        String username = myUserDetails.getUsername();
        String clientName = "김띠용";
        String reservationId = "idempotency-key";

        ReservationInfo reservationInfo = ReservationInfo.builder()
                .reservationId(reservationId)
                .userId("userId")
                .phoneNo("01000010001")
                .clientName(clientName)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusDays(1))
                .status(Reservation.State.PENDING)
                .carInfo(CarInfo.builder()
                        .make("hyundai")
                        .model("model")
                        .licensePlate("12345")
                        .build())
                .estimateInfos(List.of(EstimateInfo.builder()
                        .amount(1000.0)
                        .isFinal(true)
                        .description("이래저래 가격이럼")
                        .issuedDate(LocalDateTime.now())
                        .build()))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        when(reservationFacade.inquireList(Long.parseLong(username))).thenReturn(List.of(reservationInfo));

        mockMvc.perform(get("/v1/api/reservation")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(authentication(atc))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andExpect(jsonPath("data[0].reservationId").value(reservationId))
                .andExpect(jsonPath("data[0].userId").value(reservationInfo.userId()))
                .andExpect(jsonPath("data[0].clientName").value(reservationInfo.clientName()))
                .andExpect(jsonPath("data[0].phoneNo").value(reservationInfo.phoneNo()))
                .andExpect(jsonPath("data[0].contents").value(reservationInfo.contents()))
                .andExpect(jsonPath("data[0].startTime").value(reservationInfo.startTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data[0].endTime").value(reservationInfo.endTime().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data[0].status").value("PENDING"))
                .andExpect(jsonPath("data[0].updatedAt").value(reservationInfo.updatedAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data[0].createdAt").value(reservationInfo.createdAt().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data[0].car.make").value(reservationInfo.carInfo().make()))
                .andExpect(jsonPath("data[0].car.model").value(reservationInfo.carInfo().model()))
                .andExpect(jsonPath("data[0].car.licensePlate").value(reservationInfo.carInfo().licensePlate()))
                .andExpect(jsonPath("data[0].estimate[0].amount").value(reservationInfo.estimateInfos().get(0).amount()))
                .andExpect(jsonPath("data[0].estimate[0].description").value(reservationInfo.estimateInfos().get(0).description()))
                .andExpect(jsonPath("data[0].estimate[0].issuedDate").value(reservationInfo.estimateInfos().get(0).issuedDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("data[0].estimate[0].isFinal").value(reservationInfo.estimateInfos().get(0).isFinal()))
                .andDo(MockMvcRestDocumentation.document("inquire-reservation-list", // 생성될 문서의 이름
                        preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                        preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력
                        requestHeaders( // 요청 헤더 문서화
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                        ),
                        responseFields( // 응답 필드 문서화
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지"),
                                fieldWithPath("data[0].reservationId").type("String").description("예약키"),
                                fieldWithPath("data[0].userId").type("String").description("사용자 ID"),
                                fieldWithPath("data[0].clientName").type("String").description("사용자 명"),
                                fieldWithPath("data[0].phoneNo").type("String").description("핸드폰번호"),
                                fieldWithPath("data[0].contents").type("String").description("예약내용"),
                                fieldWithPath("data[0].startTime").type("String").description("예약 시작 시간"),
                                fieldWithPath("data[0].endTime").type("String").description("예약 종료 시간"),
                                fieldWithPath("data[0].status").type("String").description("예약 상태"),
                                fieldWithPath("data[0].updatedAt").type("String").description("최종수정시간"),
                                fieldWithPath("data[0].createdAt").type("String").description("최종생성시간"),
                                fieldWithPath("data[0].car.make").type("String").description("차량 제조사"),
                                fieldWithPath("data[0].car.model").type("String").description("차량 모델"),
                                fieldWithPath("data[0].car.licensePlate").type("String").description("차량 번호판"),
                                fieldWithPath("data[0].estimate[0].amount").type("String").description("견적 금액"),
                                fieldWithPath("data[0].estimate[0].description").type("String").description("견적 설명"),
                                fieldWithPath("data[0].estimate[0].issuedDate").type("String").description("견적 발행일"),
                                fieldWithPath("data[0].estimate[0].isFinal").type("String").description("최종 견적 여부")
                        )
                ));
        ;

    }

    @WithMockUser
    @Test
    public void reservationEstimate_shouldReturnSuccess() throws Exception {
        Authentication atc = new TestingAuthenticationToken(myUserDetails, null, "USER");
        String idempotencyKey = "idempotency_key";

        ReservationDto.RequestEstimate requestEstimate = ReservationDto.RequestEstimate.builder()
                .amount(1000.0)
                .idempotencyKey(idempotencyKey)
                .description("이래저래")
                .build();

        mockMvc.perform(post("/v1/api/reservation/estimate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(requestEstimate))
                        .with(authentication(atc))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code").value(Codes.NORMAL.name()))
                .andExpect(jsonPath("message").value(Codes.NORMAL.getDescription()))
                .andDo(MockMvcRestDocumentation.document("estimate",
                        preprocessRequest(prettyPrint()), // 요청 본문을 예쁘게 출력
                        preprocessResponse(prettyPrint()), // 응답 본문을 예쁘게 출력

                        requestHeaders( // 요청 헤더 문서화
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer token 형식의 인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("idempotencyKey").type("String").description("예약 키"),
                                fieldWithPath("description").type("String").description("견적 사유"),
                                fieldWithPath("amount").type("Number").description("견적가")),
                        responseFields(
                                fieldWithPath("code").type("String").description("응답 결과 코드"),
                                fieldWithPath("message").type("String").description("응답 메시지")
                        )
                ));
    }

}