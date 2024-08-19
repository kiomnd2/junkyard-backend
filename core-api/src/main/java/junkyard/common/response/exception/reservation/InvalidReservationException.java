package junkyard.common.response.exception.reservation;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class InvalidReservationException extends BaseException {
    public InvalidReservationException(Codes codes, String reservationId, String name) {
        super(codes, reservationId, name);
    }
}
