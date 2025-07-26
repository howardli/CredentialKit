/*
 * Copyright (c) 2025. Xiahaimoyu. All Rights Reserved.
 */
package com.xiahaimoyu.credentialkit.enums;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrgCategoryTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void validateRegistrationDepartment() {
        for (OrgCategory i : OrgCategory.values()) {
            assertThat(i.name().startsWith(i.getRegistrationDepartment().name())).isTrue();
            assertThat(i.getCode().startsWith(i.getRegistrationDepartment().getCode())).isTrue();
        }
    }

    @Test
    void validateOther() {
        for (RegistrationDepartment i : RegistrationDepartment.values()) {
            if (i == RegistrationDepartment.OTHER) {
                continue;
            }
            OrgCategory orgCategory = OrgCategory.getByCode(i.getCode() + "9");
            assertThat(orgCategory).isNotNull();
            assertThat(orgCategory.name()).isEqualTo(i.name() + "_OTHER");
            assertThat(orgCategory.getDesc()).isEqualTo("其他");
        }
    }
}