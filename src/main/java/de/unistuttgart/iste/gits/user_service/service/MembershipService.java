package de.unistuttgart.iste.gits.user_service.service;

import de.unistuttgart.iste.gits.generated.dto.CourseMembershipDto;
import de.unistuttgart.iste.gits.generated.dto.CourseMembershipInputDto;
import de.unistuttgart.iste.gits.user_service.mapper.MembershipMapper;
import de.unistuttgart.iste.gits.user_service.persistence.dao.CourseMembershipEntity;
import de.unistuttgart.iste.gits.user_service.persistence.dao.CourseMembershipPk;
import de.unistuttgart.iste.gits.user_service.persistence.repository.CourseMembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final CourseMembershipRepository courseMembershipRepository;

    private final MembershipMapper membershipMapper;


    /**
     * Allows to retrieve a List of Membership Objects that link a course with a User and their role in the course
     * @param userId User ID for which course memberships are queried
     * @return List of course memberships
     */
    public List<CourseMembershipDto> getAllMembershipsByUser(UUID userId) {
        //init
        List<CourseMembershipEntity> membershipEntities;

        // get entities from database
        membershipEntities = courseMembershipRepository.findCourseMembershipEntitiesByUserIdOrderByCourseId(userId);

        return membershipEntities.stream()
                .map(membershipMapper::entityToDto)
                .toList();
    }

    /**
     * creates a new course membership
     * @param inputDto contains user ID, course ID, and course role
     * @return created entity
     */
    public CourseMembershipDto createMembership(CourseMembershipInputDto inputDto) {
        CourseMembershipEntity entity = courseMembershipRepository.save(membershipMapper.dtoToEntity(inputDto));

        return membershipMapper.entityToDto(entity);
    }

    /**
     * Updates the role of a user in a course
     * @param inputDto contains user ID, course ID, and course role
     * @return updated entity
     */
    public CourseMembershipDto updateMembershipRole(CourseMembershipInputDto inputDto) {

        //make sure entity exists in database
        requireMembershipExisting(new CourseMembershipPk(inputDto.getUserId(), inputDto.getCourseId()));

        CourseMembershipEntity entity = courseMembershipRepository.save(membershipMapper.dtoToEntity(inputDto));
        return membershipMapper.entityToDto(entity);
    }

    /**
     * deletes a course membership of a user
     * @param inputDto contains user ID, course ID, and course role
     * @return deleted entity
     */
    public CourseMembershipDto deleteMembership(CourseMembershipInputDto inputDto) {

        CourseMembershipEntity entity = membershipMapper.dtoToEntity(inputDto);

        //make sure entity exists in database
        CourseMembershipPk membershipPk = new CourseMembershipPk(entity.getUserId(), entity.getCourseId());

        requireMembershipExisting(membershipPk);
        courseMembershipRepository.deleteById(membershipPk);

        return membershipMapper.entityToDto(entity);
    }

    /**
     * Helper function to validate existence of an entity in the database
     * @param membershipPk Primary key of entity to be checked
     */
    private void requireMembershipExisting(CourseMembershipPk membershipPk) {
        if (!courseMembershipRepository.existsById(membershipPk)) {
            throw new EntityNotFoundException("User with id " + membershipPk.getUserId() + " not member in course" + membershipPk.getCourseId());
        }

    }

}
