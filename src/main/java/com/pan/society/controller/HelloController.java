package com.pan.society.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by panstark
 * create date 2019/5/5
 */
@RestController
@Slf4j
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){

      log.info("hello world.");

      return "welcom to the new society";
    }

    @GetMapping
    public String welcome(){
        log.info("welcome world.");
        return "welcom to the new society";
    }




}
