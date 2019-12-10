package edu.umich.kanboard.kanboardapi.user;

import edu.umich.kanboard.user.UserEntity;
import edu.umich.kanboard.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class UserRepositoryTest {

    private static UserEntity userEntity;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager em;

    @Before
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("titus");
        userEntity.setPassword("titusr0ckz");
    }

    @Test
    public void findByUsernameReturnsUserEntity() {
        UserEntity user = em.persistAndFlush(userEntity);
        Assertions.assertThat(userRepository.findUserEntityByUsername(userEntity.getUsername())).contains(user);
    }
}