package edu.umich.kanboard.kanboardapi.user;

import edu.umich.kanboard.user.UserDetailsServiceImpl;
import edu.umich.kanboard.user.UserEntity;
import edu.umich.kanboard.user.UserExceptions;
import edu.umich.kanboard.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserDetailsServiceImplTest {


    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test(expected = UsernameNotFoundException.class)
    public void loadByUsername_throwsUsernameNotFound() {
        String username = "garbage";
        when(userRepository.findUserEntityByUsername(username)).thenReturn(Optional.empty());
        userDetailsService.loadUserByUsername(username);
    }

    @Test
    public void loadByUsername_success() {

        UserEntity expectedUser = createSimpleUserEntity();
        when(userRepository.findUserEntityByUsername(expectedUser.getUsername())).thenReturn(Optional.of(expectedUser));
        UserDetails actual = userDetailsService.loadUserByUsername(expectedUser.getUsername());

        assertEquals(expectedUser.getUsername(), actual.getUsername());
        assertEquals(expectedUser.getPassword(), actual.getPassword());
    }

    @Test
    public void createUser_success() {
        String expectedPassword = "encoded-password";
        UserEntity user = createSimpleUserEntity();

        when(userRepository.findUserEntityByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(encoder.encode(user.getPassword())).thenReturn(expectedPassword);
        UserEntity actualUser = userDetailsService.createUser(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        assertEquals(actualUser.getPassword(), expectedPassword);
    }

    @Test(expected = UserExceptions.UserAlreadyExistsException.class)
    public void createUser_usernameAlreadyExists() {
        UserEntity user = createSimpleUserEntity();

        when(userRepository.findUserEntityByUsername(user.getUsername())).thenReturn(Optional.of(user));
        userDetailsService.createUser(user);
        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    private UserEntity createSimpleUserEntity() {
        UserEntity user = new UserEntity();
        String username = "this-is-a-great-test";
        String password = "this-is-a-great-password";
        user.setUsername(username);
        user.setPassword(password);
        user.setUserId((long) 1);
        return user;
    }
}