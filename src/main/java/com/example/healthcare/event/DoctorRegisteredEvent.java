    package com.example.healthcare.event;

    import com.example.healthcare.enums.Gender;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DoctorRegisteredEvent {
        private String userId;
        private String email;
        private String username;
        private String licenseUrl;
        private Gender gender;
        private String country;
        private String dateOfBirth;
    }

