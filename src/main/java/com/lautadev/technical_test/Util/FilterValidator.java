package com.lautadev.technical_test.Util;


import com.lautadev.technical_test.Entities.User;
import com.lautadev.technical_test.Exception.InvalidFilterException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterValidator {
    public static Predicate buildPredicate(CriteriaBuilder cb, Root<User> root, Map<String, String> filters) {
        List<Predicate> predicates = new ArrayList<>();

        filters.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                switch (key) {
                    case "name":
                        predicates.add(cb.like(root.get("name"), "%" + value + "%"));
                        break;
                    case "email":
                        predicates.add(cb.like(root.get("email"), "%" + value + "%"));
                        break;
                    case "isDeleted":
                        boolean boolValue;
                        try {
                            boolValue = Boolean.parseBoolean(value);
                        } catch (Exception e) {
                            throw new InvalidFilterException("Invalid value for " + key);
                        }
                        predicates.add(cb.equal(root.get("isDeleted"), boolValue));
                        break;
                }
            }
        });

        if (!filters.containsKey("isDeleted")) {
            predicates.add(cb.equal(root.get("isDeleted"), false));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

