package org.atos.commutermap.users.network.service;

import com.google.common.collect.ImmutableMap;
import org.atos.commutermap.network.service.UsersApi;
import org.atos.commutermap.network.service.model.PlainUser;
import org.atos.commutermap.users.dao.ApplicationUserRepository;
import org.atos.commutermap.users.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserApiController implements org.atos.commutermap.network.service.UserApi, UsersApi {

    @Autowired
    ApplicationUserRepository userRepository;

    @Transactional
    @Override
    public ResponseEntity<Void> loginUser(@Valid org.atos.commutermap.network.service.model.User user) {
        Optional<ApplicationUser> userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            if (!userByEmail.get().accessTokens.containsKey(user.getTokenIssuer())) {
                userByEmail.get().accessTokens.put(user.getTokenIssuer(), user.getAccessToken());
                userRepository.save(userByEmail.get());
            }
            return ResponseEntity.ok().build();
        } else {
            ApplicationUser createdUser = userRepository.save(new ApplicationUser(user.getEmail(), ImmutableMap.of(user.getTokenIssuer(), user.getAccessToken())));
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
