package com.vuthy.mobilebankingapi.init;

import com.vuthy.mobilebankingapi.domain.Role;
import com.vuthy.mobilebankingapi.domain.User;
import com.vuthy.mobilebankingapi.repository.RoleRepository;
import com.vuthy.mobilebankingapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityInit {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {

        if(roleRepository.count()==0){
            Role defaultRole = new Role();
            defaultRole.setRole("USER");
            Role admin = new Role();
            admin.setRole("ADMIN");
            Role staff = new Role();
            staff.setRole("STAFF");
            Role customer = new Role();
            customer.setRole("CUSTOMER");
            roleRepository.saveAll(List.of(defaultRole, admin, staff, customer));

            if (userRepository.count()==0){
                User userAdmin = new User();
                userAdmin.setUsername("admin");
                userAdmin.setIsEnabled(true);
                userAdmin.setPassword(passwordEncoder.encode("admin"));
                userAdmin.setRoles(List.of(defaultRole, admin));

                User userStaff = new User();
                userStaff.setUsername("staff");
                userStaff.setPassword(passwordEncoder.encode("staff"));
                userStaff.setIsEnabled(true);
                userStaff.setRoles(List.of(defaultRole, staff));

                User userCustomer = new User();
                userCustomer.setUsername("customer");
                userCustomer.setPassword(passwordEncoder.encode("customer"));
                userCustomer.setIsEnabled(true);
                userCustomer.setRoles(List.of(defaultRole, customer));

                userRepository.saveAll(List.of(userAdmin,userCustomer,userStaff));
            }
        }


    }

}
