package com.github.starwacki.account.controller;


import com.github.starwacki.components.account.controller.AccountController;
import com.github.starwacki.components.account.service.AccountService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private  AccountService accountService;



}