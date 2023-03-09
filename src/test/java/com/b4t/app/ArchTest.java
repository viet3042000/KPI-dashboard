package com.b4t.app;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {
    public static void main(String[] args) {
        String str = "20121";
        String year = str.substring(0, 4);
        String quar = str.substring(4);
        System.out.println(year + ""+quar);
    }

   /* @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.b4t.app");

        noClasses()
            .that()
                .resideInAnyPackage("com.b4t.app.service..")
            .or()
                .resideInAnyPackage("com.b4t.app.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.b4t.app.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }*/
}
