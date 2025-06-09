package com.tdd.billing.services;
import com.tdd.billing.entities.UserRole;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    public List<String> getRoles() {
        return Arrays.stream(UserRole.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
