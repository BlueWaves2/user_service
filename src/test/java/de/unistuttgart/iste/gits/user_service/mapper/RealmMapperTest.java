package de.unistuttgart.iste.gits.user_service.mapper;

import de.unistuttgart.iste.gits.common.user_handling.LoggedInUser;
import de.unistuttgart.iste.gits.generated.dto.RealmRoles;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RealmMapperTest {

    RealmMapper realmMapper = new RealmMapper(new ModelMapper());

    @Test
    void internalRolesToGraphQlRoles() {
        final HashSet<LoggedInUser.RealmRole> keycloakRealmRoles = new HashSet<>();
        keycloakRealmRoles.add(LoggedInUser.RealmRole.COURSE_CREATOR);
        keycloakRealmRoles.add(LoggedInUser.RealmRole.SUPER_USER);

        final List<RealmRoles> graphQLRoles = realmMapper.internalRolesToGraphQlRoles(keycloakRealmRoles);

        assertTrue(graphQLRoles.contains(RealmRoles.COURSE_CREATOR));
        assertTrue(graphQLRoles.contains(RealmRoles.SUPER_USER));
    }
}