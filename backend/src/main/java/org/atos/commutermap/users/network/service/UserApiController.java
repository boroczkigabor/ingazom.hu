package org.atos.commutermap.users.network.service;

import org.atos.commutermap.network.service.UsersApi;
import org.atos.commutermap.network.service.model.PlainUser;
import org.atos.commutermap.users.dao.ApplicationUserRepository;
import org.atos.commutermap.users.model.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserApiController implements org.atos.commutermap.network.service.UserApi, UsersApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private ApplicationUserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    @Transactional
    @Override
    public ResponseEntity<Void> loginUser(String authorizationToken, String authorizationProvider) {
        ApplicationUser applicationUser = tokenService.retrieveUserDetailsWithToken(authorizationToken);

        Optional<ApplicationUser> userByEmail = userRepository.findByEmail(applicationUser.email);
        if (userByEmail.isPresent()) {
            LOGGER.debug("User is already in the system, updating tokens...");
            userByEmail.get().accessTokens.putAll(applicationUser.accessTokens);
            return ResponseEntity.ok().build();
        } else {
            ApplicationUser createdUser = userRepository.save(applicationUser);
            LOGGER.info("Registered a new user {}", createdUser.toPlainUser());
            return ResponseEntity.created(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("user/{id}")
                            .build(createdUser.userId))
                    .build();
        }
    }

    @Override
    public ResponseEntity<PlainUser> getUser(Long id) {
        Optional<ApplicationUser> userById = userRepository.findById(id);

        if (userById.isPresent()) {
            return ResponseEntity.ok(userById.get().toPlainUser());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<PlainUser>> getAllUsers() {
        return ResponseEntity.ok(
                StreamSupport.stream(userRepository.findAll().spliterator(), false)
                        .map(ApplicationUser::toPlainUser)
                        .collect(Collectors.toList()));
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }
}
