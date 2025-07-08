package com.project.backend.repositories.specification;

import com.project.backend.models.Class;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ClassSpecificationTest {

    @Test
    void testBySchool() {
        long schoolId = 5L;

        Root<Class> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Root<?> school = mock(Root.class);
        when(root.get("school")).thenReturn((Path<Object>) school);
        when(school.get("id")).thenReturn(mock(Path.class));

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(any(), eq(schoolId))).thenReturn(predicate);

        Predicate result = ClassSpecification.bySchool(schoolId).toPredicate(root, query, cb);

        new ClassSpecification();

        assertNotNull(result);
        verify(root).get("school");
        verify(school).get("id");
        verify(cb).equal(any(), eq(schoolId));
    }
}
