package com.lautadev.technical_test.Repository.specifications;

import com.lautadev.technical_test.Entities.User;
import com.lautadev.technical_test.Util.FilterValidator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class UserSpecification implements Specification<User> {

    private final Map<String, String> filters;

    public UserSpecification(Map<String, String> filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return FilterValidator.buildPredicate(cb, root, filters);
    }
}


