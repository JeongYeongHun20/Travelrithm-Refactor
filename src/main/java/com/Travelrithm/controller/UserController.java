package com.Travelrithm.controller;

import com.Travelrithm.dto.AuthUser;
import com.Travelrithm.dto.register.UserRequestDto;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.security.jwt.CustomUserDetails;
import com.Travelrithm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping("/local")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        System.out.println(userRequestDto.name());
        return ResponseEntity.ok(userService.createUser(userRequestDto));
    }




    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/myPage")
    public ResponseEntity<UserResponseDto> getUserById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(userService.findUser(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        AuthUser authUser=new AuthUser(userDetails.getUserId(),userDetails.getUsername(),userDetails.geNickname());

        return ResponseEntity.ok(authUser);

    }

    @PutMapping("/update") //전체 수정
    public ResponseEntity<UserResponseDto> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserRequestDto userRequestDto) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(userService.updateUser(userId, userRequestDto));
    }
    /*
    @PatchMapping("/{id}") //부분 수정(추후 추가)
    public ResponseEntity<UserResponseDTO> patchUser(@PathVariable Integer id, @RequestBody UserRequestDTO user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
     */

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}