package com.Travelrithm.controller;

import com.Travelrithm.dto.UserRequestDto;
import com.Travelrithm.dto.UserResponseDto;
import com.Travelrithm.security.jwt.CustomUserDetails;
import com.Travelrithm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping
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
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(userService.findUser(userId));
    }


    @PutMapping("/update") //전체 수정
    public ResponseEntity<UserResponseDto> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserRequestDto userRequestDto) {
        Integer userId = userDetails.getUserId();
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
        Integer userId = userDetails.getUserId();
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}