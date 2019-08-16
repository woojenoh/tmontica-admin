package com.internship.tmontica_admin.user;

import com.internship.tmontica_admin.user.exception.UserValidException;
import com.internship.tmontica_admin.user.model.request.AdminSignInRequestDTO;
import com.internship.tmontica_admin.user.model.response.AdminSignInResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/signin")
    public ResponseEntity<AdminSignInResponseDTO> signIn(@RequestBody @Valid AdminSignInRequestDTO adminSignInRequestDTO, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            throw new UserValidException("Admin Sign-in Form", "로그인 폼 데이터가 올바르지 않습니다.", bindingResult);
        }
        User user = modelMapper.map(adminSignInRequestDTO, User.class);
        userService.signInCheck(user);
        return new ResponseEntity<>(modelMapper.map(userService.makeJwtToken(user), AdminSignInResponseDTO.class), HttpStatus.OK);
    }
}
