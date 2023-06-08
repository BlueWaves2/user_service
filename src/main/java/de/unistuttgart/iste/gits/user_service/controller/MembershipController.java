package de.unistuttgart.iste.gits.user_service.controller;

import de.unistuttgart.iste.gits.generated.dto.CourseMembershipDto;
import de.unistuttgart.iste.gits.generated.dto.CourseMembershipInputDto;
import de.unistuttgart.iste.gits.user_service.service.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @QueryMapping
    public List<CourseMembershipDto> courseMemberships(@Argument(name="id") UUID userId) {

        return membershipService.getAllMembershipsByUser(userId);
    }
    @MutationMapping
    public CourseMembershipDto createMembership(@Argument(name = "input")CourseMembershipInputDto inputDto){
        return membershipService.createMembership(inputDto);
    }

    @MutationMapping
    public CourseMembershipDto updateMembership(@Argument(name = "input")CourseMembershipInputDto inputDto){
        return membershipService.updateMembershipRole(inputDto);
    }

    @MutationMapping
    public CourseMembershipDto deleteMembership(@Argument(name = "input")CourseMembershipInputDto inputDto){
        return membershipService.deleteMembership(inputDto);
    }
}
