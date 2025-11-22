package tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import utils.Specifications;

@Slf4j
public class APIBaseTest {
    protected final static String URL = "https://reqres.in/";

    @BeforeEach
    public void setUp() {
        setupSpecifications(200);
    }

    protected static void setupSpecifications(int expectedStatusCode) {
        Specifications.resetAllSpecs();
        switch (expectedStatusCode) {
            case 400:
                Specifications.setSpec(
                        Specifications.requestSpecification(URL),
                        Specifications.responseSpecification400()
                );
                break;
            case 403:
                Specifications.setSpec(
                        Specifications.requestSpecification(URL),
                        Specifications.responseSpecification403()
                );
                break;
            default:
                Specifications.setSpec(
                        Specifications.requestSpecification(URL),
                        Specifications.responseSpecification200()
                );
        }
    }
}
