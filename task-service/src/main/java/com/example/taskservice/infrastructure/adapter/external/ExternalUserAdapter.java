package com.example.taskservice.infrastructure.adapter.external;

import com.example.taskservice.domain.ports.out.ExternalUserPort;
import com.example.taskservice.infrastructure.adapter.external.feign.UserFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalUserAdapter implements ExternalUserPort {

    private final UserFeignClient userFeignClient;

    @Override
    public boolean existsUserById(Long userId) {
        try {
            userFeignClient.getUserById(userId);
            return true;
        } catch (FeignException.NotFound e) {
            return false;
        }
    }
}
