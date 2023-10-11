package de.unistuttgart.iste.gits.user_service.mapper;

import de.unistuttgart.iste.gits.common.user_handling.LoggedInUser;
import de.unistuttgart.iste.gits.generated.dto.RealmRoles;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class RealmMapper {

    private final ModelMapper modelMapper;

    public RealmMapper(final ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public List<RealmRoles> internalRolesToGraphQlRoles (final Set<LoggedInUser.RealmRole> realmRoleSet){
        return realmRoleSet.stream().map(role -> modelMapper.map(role, RealmRoles.class)).toList();
    }

    public List<RealmRoles> keycloakRolesToGraphQlRoles(final List<String> keycloakRoles){
        final Set<LoggedInUser.RealmRole> internalRoles = LoggedInUser.RealmRole.getRolesFromKeycloakRoleList(keycloakRoles);
        return internalRolesToGraphQlRoles(internalRoles);
    }
}
