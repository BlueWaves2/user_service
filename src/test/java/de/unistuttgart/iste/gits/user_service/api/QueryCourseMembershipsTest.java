package de.unistuttgart.iste.gits.user_service.api;

import de.unistuttgart.iste.gits.common.testutil.GitsPostgresSqlContainer;
import de.unistuttgart.iste.gits.common.testutil.GraphQlApiTest;
import de.unistuttgart.iste.gits.generated.dto.CourseMembership;
import de.unistuttgart.iste.gits.user_service.persistence.dao.CourseMembershipEntity;
import de.unistuttgart.iste.gits.user_service.persistence.dao.CourseRole;
import de.unistuttgart.iste.gits.user_service.persistence.repository.CourseMembershipRepository;
import de.unistuttgart.iste.gits.user_service.test_config.MockKeycloakConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ContextConfiguration(classes = MockKeycloakConfiguration.class)
@GraphQlApiTest
public class QueryCourseMembershipsTest {

    @Container
    public static PostgreSQLContainer<GitsPostgresSqlContainer> postgreSQLContainer = GitsPostgresSqlContainer.getInstance();

    @Autowired
    private CourseMembershipRepository membershipRepository;

    @Test
    void testNoMembershipExisting(GraphQlTester tester){
        //GraphQL query
        String query = """
                query {
                    courseMemberships(id: "%s") {
                        userId
                        courseId
                        role
                    }
                }
                """.formatted(UUID.randomUUID());
        tester.document(query)
                .execute()
                .path("courseMemberships")
                .entityList(CourseMembership.class)
                .hasSize(0);
    }

    @Test
    void testMembership(GraphQlTester tester){

        UUID userId = UUID.randomUUID();
        List<CourseMembership> DTOList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            UUID courseId = UUID.randomUUID();
            CourseMembershipEntity entity = CourseMembershipEntity.builder().userId(userId).courseId(courseId).courseRole(CourseRole.STUDENT).build();
            CourseMembership dto = CourseMembership.builder().setUserId(userId).setCourseId(courseId).setRole(CourseRole.STUDENT.toString()).build();
            membershipRepository.save(entity);
            DTOList.add(dto);
        }
        //GraphQL query
        String query = """
                query {
                    courseMemberships(id: "%s") {
                        userId
                        courseId
                        role
                    }
                }
                """.formatted(userId);
        tester.document(query)
                .execute()
                .path("courseMemberships")
                .entityList(CourseMembership.class)
                .hasSize(2)
                .contains(DTOList.get(0), DTOList.get(1));
    }
}
