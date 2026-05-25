package dev.contentseeker10.warehouse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class WarehouseServiceTest {

    @BeforeEach
    void setUp() {
        WarehouseService.getInstance().getGroups().clear();
        WarehouseService.getInstance().getProducts().clear();
        Product.resetIdSequence();
        ProductGroup.resetIdSequence();
    }

    @Test
    void shouldAddProductGroup() {
        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        assertThat(WarehouseService.getInstance().getGroups().isEmpty()).isEqualTo(false);
        assertThat(WarehouseService.getInstance().getGroups().get(1).getName()).isEqualTo("Grocery");
    }

    @Test
    void shouldAddProductToGroup() {
        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        WarehouseService.getInstance().addProductToGroup(1, "Buckwheat", "I eat buckwheat like every 2 weeks.", 50.0);
        assertThat(WarehouseService.getInstance().getProducts().isEmpty()).isEqualTo(false);
        assertThat(WarehouseService.getInstance().getProducts().get(1).getName()).isEqualTo("Buckwheat");
    }

    @Test
    void shouldGetProductAmount() {
        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        WarehouseService.getInstance().addProductToGroup(1, "Buckwheat", "I eat buckwheat like every 2 weeks.", 50.0);
        assertThat(WarehouseService.getInstance().getProductAmount(1)).isEqualTo(0);
    }

    @Test
    void shouldWriteOnAndOffProduct() {
        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        WarehouseService.getInstance().addProductToGroup(1, "Buckwheat", "I eat buckwheat like every 2 weeks.", 50.0);
        WarehouseService.getInstance().writeOnProduct(1, 100);
        assertThat(WarehouseService.getInstance().getProducts().get(1).getAmount()).isEqualTo(100);
        WarehouseService.getInstance().writeOffProduct(1, 50);
        assertThat(WarehouseService.getInstance().getProducts().get(1).getAmount()).isEqualTo(50);
    }

    @Test
    void shouldNotWriteOffProduct() {
        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        WarehouseService.getInstance().addProductToGroup(1, "Buckwheat", "I eat buckwheat like every 2 weeks.", 50.0);
        WarehouseService.getInstance().writeOnProduct(1, 100);
        assertThat(WarehouseService.getInstance().getProducts().get(1).getAmount()).isEqualTo(100);
        WarehouseService.getInstance().writeOffProduct(1, 200);
        assertThat(WarehouseService.getInstance().getProducts().get(1).getAmount()).isEqualTo(100);
    }

    @Test
    void setProductPrice() {
        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        WarehouseService.getInstance().addProductToGroup(1, "Buckwheat", "I eat buckwheat like every 2 weeks.", 50.0);
        WarehouseService.getInstance().setProductPrice(1, 100.0);
        assertThat(WarehouseService.getInstance().getProducts().get(1).getPrice()).isEqualTo(100.0);
    }
}