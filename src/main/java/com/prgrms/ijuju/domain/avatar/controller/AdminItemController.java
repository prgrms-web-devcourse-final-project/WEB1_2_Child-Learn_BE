package com.prgrms.ijuju.domain.avatar.controller;

import com.prgrms.ijuju.domain.avatar.service.AdminItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member/avatar")
@Slf4j
public class AdminItemController {

    @Autowired
    private AdminItemService adminItemService;


}
