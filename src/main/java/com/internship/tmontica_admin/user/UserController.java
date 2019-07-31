package com.internship.tmontica_admin.user;

import com.internship.tmontica_admin.user.model.request.AdminSignInReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody @Valid AdminSignInReqDTO adminSignInReqDTO) {

        userService.signIn(adminSignInReqDTO);
        return new ResponseEntity<>(UserResponseMessage.LOG_IN_SUCCESS.getMessage(), HttpStatus.OK);
    }



}
