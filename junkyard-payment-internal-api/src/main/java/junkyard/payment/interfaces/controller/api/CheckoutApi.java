package junkyard.payment.interfaces.controller.api;

import junkyard.payment.domain.checkout.CheckoutResult;
import junkyard.payment.application.CheckoutFacade;
import junkyard.response.CommonResponse;
import junkyard.security.userdetails.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/payment")
public class CheckoutApi {
    private final CheckoutFacade checkoutFacade;

    @PostMapping("checkout")
    public CommonResponse<CheckoutDto.CheckoutResponse> checkout(@AuthenticationPrincipal MyUserDetails userDetail,
                                                                 CheckoutDto.CheckoutRequest request) {
        CheckoutResult checkout = checkoutFacade.checkout(Long.parseLong(userDetail.getUsername()), request);
        return CommonResponse.success(CheckoutDto.CheckoutResponse.by(checkout));
    }
}

