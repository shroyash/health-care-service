// appointmentrequest/service/state/AppointmentStatusTransition.java
package com.example.healthcare.appointmentRequest.service.state;


import com.example.healthcare.appointmentRequest.enums.AppointmentRequestStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentStatusTransition {

    public void validate(AppointmentRequestStatus current,
                         AppointmentRequestStatus next) {

        if (current == AppointmentRequestStatus.APPROVED ||
                current == AppointmentRequestStatus.REJECTED) {
            throw new IllegalStateException(
                    "Request is already " + current.name().toLowerCase()
                            + " and cannot be changed");
        }

        if (current == next) {
            throw new IllegalStateException(
                    "Request is already in " + current.name().toLowerCase() + " state");
        }
    }
}