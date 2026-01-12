    package com.example.healthcare.event;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class DoctorRegisteredEvent {
        private String userId;
        private String email;
        private String username;
        private String licenseUrl;
    }
