import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ValidationsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void nameValidatingEmptyString() {
        assertThrows(IllegalArgumentException.class , ()->Product.nameValidating(""));
    }
    @Test
    void nameValidatingSortingWithNumber() {
        assertThrows(IllegalArgumentException.class , ()->Product.nameValidating("name2 name"));
        assertThrows(IllegalArgumentException.class , ()->Product.nameValidating("name% name"));
    }

    @Test
    void descriptionValidatingIllegalSymbols() {
        assertThrows(IllegalArgumentException.class , ()->Product.descriptionValidating("name2 name"));
        assertThrows(IllegalArgumentException.class , ()->Product.descriptionValidating("name% name"));
    }

    @Test
    void typeValidationTypesFromList() {
        boolean isTrue;
        for (String t:Product.getProductCategories()) {
            isTrue = Product.typeValidation(t);
            assertTrue(isTrue);
        }
    }
    @Test
    void typeValidationTypesNotFromList() {
        assertThrows(IllegalArgumentException.class , ()->Product.typeValidation(""));
        assertThrows(IllegalArgumentException.class , ()->Product.typeValidation("type"));
    }

    @Test
    void priceValidationEmpty() {
        assertThrows(IllegalArgumentException.class , ()->Product.priceValidation(""));
    }
    @Test
    void priceValidationWrongFormat() {
        assertThrows(IllegalArgumentException.class , ()->Product.priceValidation("price"));
        assertThrows(IllegalArgumentException.class , ()->Product.priceValidation("213,323,32"));
        assertThrows(IllegalArgumentException.class , ()->Product.priceValidation("324,43price"));
        assertThrows(IllegalArgumentException.class , ()->Product.priceValidation("324,43$"));
    }

    @Test
    void formatPrice() {
        assertEquals("23.2",Product.formatPrice("23,2"));
        assertEquals("23.2.2.2",Product.formatPrice("23,2,2,2"));
    }

}