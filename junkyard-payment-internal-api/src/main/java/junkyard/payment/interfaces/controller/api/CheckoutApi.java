package junkyard.payment.interfaces.controller.api;

import junkyard.payment.domain.checkout.CheckoutResult;
import junkyard.payment.application.CheckoutFacade;
import junkyard.response.CommonResponse;
import junkyard.security.userdetails.user.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/payment")
public class CheckoutApi {
    private final CheckoutFacade checkoutFacade;

    @PostMapping("checkout")
    public CommonResponse<CheckoutDto.CheckoutResponse> checkout(@AuthenticationPrincipal MyUserDetails userDetail,
                                                                 @RequestBody CheckoutDto.CheckoutRequest request) {
        CheckoutResult checkout = checkoutFacade.checkout(Long.parseLong(userDetail.getUsername()), request);
        return CommonResponse.success(CheckoutDto.CheckoutResponse.by(checkout));
    }
}
