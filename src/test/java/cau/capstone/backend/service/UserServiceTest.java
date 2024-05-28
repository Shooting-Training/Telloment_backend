//package cau.capstone.backend.service;
//
//
//import cau.capstone.backend.User.dto.request.UpdateUserDto;
//import cau.capstone.backend.User.dto.response.ResponseSearchUserDto;
//import cau.capstone.backend.User.dto.response.ResponseSimpleUserDto;
//import cau.capstone.backend.User.model.Follow;
//import cau.capstone.backend.User.model.User;
//import cau.capstone.backend.User.model.repository.FollowRepository;
//import cau.capstone.backend.User.model.repository.UserRepository;
//import cau.capstone.backend.User.service.UserService;
//import cau.capstone.backend.global.security.dto.CreateUserDto;
//import cau.capstone.backend.global.security.dto.ResponseUserDto;
//import cau.capstone.backend.global.util.exception.UserException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private FollowRepository followRepository;
//
//    @DisplayName("회원정보 저장")
//    @Test
//    void saveUser() { // setId() 메서드로 테스트 진행함
//        // Given
//        CreateUserDto createUserDto = CreateUserDto.of("test", "testNickname", "testEmail", "testPassword");
//        User user = User.createUser(createUserDto.getEmail(), createUserDto.getPassword(), createUserDto.getName(), createUserDto.getNickname());
//        user.setId(1L); // 테스트 커밋 중 User에 setId() 메서드 임시적으로 삽입하여 테스트 진행함
//
//        given(userRepository.existsByName("test")).willReturn(false);
//        given(userRepository.save(any(User.class))).willReturn(user);
//
//        // When
//        Long id = userService.saveUser(createUserDto); // id null로 반환됨 (Mock은 실제 DB에 객체를 생성하지 않기 때문)
//
//        // Then
//        assertEquals(1L, id);
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @DisplayName("회원정보 저장 닉네임 중복")
//    @Test
//    void saveUserDupliactedName() {
//        // Given
//        CreateUserDto createUserDto = CreateUserDto.of("test", "testNickname", "testEmail", "testPassword");
//        User user = User.createUser(createUserDto.getEmail(), createUserDto.getPassword(), createUserDto.getName(), createUserDto.getNickname());
//        user.setId(1L); // 테스트 커밋 중 User에 setId() 메서드 임시적으로 삽입하여 테스트 진행함
//
//        given(userRepository.existsByName("test")).willReturn(true);
//
//        // When -> 예외처리
//        assertThrows(UserException.class, () -> userService.saveUser(createUserDto));
//    }
//
//
//    @DisplayName("회원정보 조회")
//    @Test
//    void getUserInfo() {
//        // Given
//        Long userId = 1L;
//        User user = User.createUser("testEmail", "testPassword","test", "testNickname");
//        given(userRepository.findById(userId)).willReturn(Optional.of(user));
//
//        // When
//        ResponseUserDto result = userService.getUserInfo(userId);
//
//        // Then
//        assertEquals("test", result.getName());
//        assertEquals("testNickname", result.getNickname());
//    }
//
//    @DisplayName("회원정보 수정")
//    @Test
//    void updateUserInfo() {
//        // given
//        UpdateUserDto updateUserDto = UpdateUserDto.of("update", "updateNickname", "update.jpg");
//        User user = User.createUser("testEmail", "testPassword","test", "testNickname");
//
//        given(userRepository.findByNickname(updateUserDto.getNickname())).willReturn(Optional.of(user));
//
//        // when
//        userService.updateUserInfo(updateUserDto);
//
//
//        // Then
//        assertEquals("update", user.getName());
//        assertEquals("update", user.getName());
//        assertEquals("updateNickname", user.getNickname());
//        assertEquals("update.jpg", user.getImage());
//    }
//
//    @DisplayName("회원 탈퇴")
//    @Test
//    void deleteUser() {
//        // Given
//        Long userId = 1L;
//        given(userRepository.existsById(userId)).willReturn(true);
//
//        // When
//        userService.deleteUser(userId);
//
//        // Then
//        verify(userRepository, times(1)).deleteById(userId);
//    }
//
//
//    @DisplayName("회원 검색 결과 조회")
//    @Test
//    void searchUser() { // setId() 메서드로 테스트 진행함
//        // Given
//        Long userId1 = 1L;
//        Long userId2 = 2L;
//        Long userId3 = 3L;
//        String name = "John";
//
//        // 사용자 목록 생성
//        User user1 = User.createUser("JohnDoe@gmail.com", "john123", "John Doe", "John");
//        User user2 = User.createUser("JohnDoe1@gmail.com", "john1234", "John Doe1", "John1");
//        User user3 = User.createUser("JohnDoe2@gmail.com", "john12345", "John Doe2", "John2");
//        user1.setId(userId1);
//        user2.setId(userId2);
//        user3.setId(userId3);
//
//        given(userRepository.existsById(userId1)).willReturn(true);
//        given(userRepository.findAllByNameContaining(name)).willReturn(List.of(user1, user2, user3));
//        given(followRepository.existsByFollowerIdAndFolloweeId(userId1, userId2)).willReturn(true);
//        given(followRepository.existsByFolloweeIdAndFollowerId(userId1, userId3)).willReturn(false);
//
//        // When
//        List<ResponseSearchUserDto> result = userService.searchUser(userId1, name);
//
//        // Then
//        assertEquals(3, result.size());
//        assertEquals("John Doe", result.get(0).getName());
//        assertTrue(result.get(0).isFollow());
//        assertEquals("John Doe1", result.get(1).getName());
//        assertFalse(result.get(1).isFollow());
//        verify(userRepository, times(1)).findAllByNameContaining(name);
//    }
//
//    @DisplayName("회원이 특정 회원 팔로우")
//    @Test
//    void followUser() {
//        // Given
//        Long userId = 1L; // 팔로우 요청을 보낸 사용자의 ID
//        Long followId = 2L; // 팔로우할 사용자의 ID
//
//        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
//        given(userRepository.existsById(followId)).willReturn(true); // followId에 해당하는 사용자가 존재함
//        given(followRepository.existsByFollowerIdAndFolloweeId(userId, followId)).willReturn(false); // 아직 팔로우 중이 아님
//
//        // When
//        userService.followUser(userId, followId);
//
//        // Then
//        verify(userRepository, times(1)).existsById(userId); // 사용자 존재 여부 확인
//        verify(userRepository, times(1)).existsById(followId); // 팔로우할 사용자 존재 여부 확인
//        verify(followRepository, times(1)).existsByFollowerIdAndFolloweeId(userId, followId); // 이미 팔로우 중인지 확인
//        verify(followRepository, times(1)).save(any(Follow.class));
//    }
//
//    @DisplayName("회원이 특정 회원 팔로우 중복 요청")
//    @Test
//    void followerUserDuplicate() {
//        // Given
//        Long userId = 1L; // 팔로우 요청을 보낸 사용자의 ID
//        Long followId = 2L; // 팔로우할 사용자의 ID
//
//        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
//        given(userRepository.existsById(followId)).willReturn(true); // followId에 해당하는 사용자가 존재함
//        given(followRepository.existsByFollowerIdAndFolloweeId(userId, followId)).willReturn(true); // 아직 팔로우 중이 아님
//
//        // When -> 예외처리
//        assertThrows(UserException.class, () -> userService.followUser(userId, followId));
//    }
//
//    @DisplayName("회원이 특정 회원 팔로우 취소")
//    @Test
//    void unfollowUser() {
//        // Given
//        Long userId = 1L; // 팔로우 취소를 요청한 사용자의 ID
//        Long unfollowId = 2L; // 팔로우를 취소할 사용자의 ID
//
//        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
//        given(userRepository.existsById(unfollowId)).willReturn(true); // unfollowId에 해당하는 사용자가 존재함
//        given(followRepository.existsByFollowerIdAndFolloweeId(userId, unfollowId)).willReturn(true);
//
//        // When
//        userService.unfollowUser(userId, unfollowId);
//
//        // Then
//        verify(followRepository, times(1)).deleteByFollowerIdAndFolloweeId(userId, unfollowId);
//    }
//
//    @DisplayName("회원이 특정 회원 팔로우 취소 중복 요청")
//    @Test
//    void unfollowUserDuplicate() {
//        // Given
//        Long userId = 1L; // 팔로우 취소를 요청한 사용자의 ID
//        Long unfollowId = 2L; // 팔로우를 취소할 사용자의 ID
//
//        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
//        given(userRepository.existsById(unfollowId)).willReturn(true); // unfollowId에 해당하는 사용자가 존재함
//        given(followRepository.existsByFollowerIdAndFolloweeId(userId, unfollowId)).willReturn(false);
//
//        // When -> 예외처리
//        assertThrows(UserException.class, () -> userService.unfollowUser(userId, unfollowId));
//    }
//}
