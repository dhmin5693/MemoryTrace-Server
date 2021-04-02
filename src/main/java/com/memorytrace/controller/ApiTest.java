package com.memorytrace.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "API TEST", tags = "TEST")
@RestController
@RequestMapping("/api")
public class ApiTest {
    @ApiOperation(value = "Swagger Test API")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get response")
    })
    @PostMapping("/test")
    public ResponseEntity apiTest() {
        return ResponseEntity.ok("OK");
    }
}
