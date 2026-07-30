package api.factory;

import api.entity.User;
import api.util.Role;

public class UserFactory {

    public static User createValidUser(){
        return User.builder()
                .id(1L)
                .name("Gabriel Test")
                .email("gabriel@test.com")
                .password("123456")
                .role(Role.CLIENT)
                .department("TI")
                .active(true)
                .build();
    }


    public static User createClient(){

        return User.builder()
                .name("Client Test")
                .email("client@test.com")
                .password("123456")
                .role(Role.CLIENT)
                .department("Support")
                .active(true)
                .build();

    }


    public static User createTechnician(){

        return User.builder()
                .id(1L)
                .name("Technician Test")
                .email("technician@test.com")
                .password("123456")
                .role(Role.TECHNICIAN)
                .department("Technical")
                .active(true)
                .build();

    }
    public static User createTechnicianDifferentId(){

        return User.builder()
                .id(2L)
                .name("Technician Test")
                .email("technician@test.com")
                .password("123456")
                .role(Role.TECHNICIAN)
                .department("Technical")
                .active(true)
                .build();

    }


    public static User createAdmin(){

        return User.builder()
                .name("Admin Test")
                .email("admin@test.com")
                .password("123456")
                .role(Role.ADMIN)
                .department("Management")
                .active(true)
                .build();

    }
}
