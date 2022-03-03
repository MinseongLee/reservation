package com.youwent;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = ReservationApplication.class)
public class PackageDependencyTests {

    private static final String ACCOUNT = "..modules.account..";
    private static final String FACILITY = "..modules.facility..";
    private static final String MAIN = "..modules.main..";
    private static final String RESERVATION = "..modules.reservation..";

    @ArchTest
    ArchRule modulesPackageRule = classes().that().resideInAPackage("com.youwent.modules..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("com.youwent.modules..");

}
