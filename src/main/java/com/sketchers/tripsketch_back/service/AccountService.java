package com.sketchers.tripsketch_back.service;

import com.sketchers.tripsketch_back.repository.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;

    public int deleteUser(int userId) {
        return accountMapper.deleteUser(userId);
    }
}
