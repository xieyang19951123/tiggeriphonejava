package com.xy.tigger.shopvip.controller;

import com.xy.tigger.shopvip.service.ActionService;
import com.xy.tigger.uitls.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("home")
public class HomeController {

    @Autowired
    private ActionService actionService;

    @RequestMapping("action")
    public R action(@RequestParam Integer uid){
        return actionService.action(uid);
    }
}
