package dev.contentseeker10.warehouse;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductGroup {

    private static final AtomicInteger idSequence = new AtomicInteger(0);
    private final int id;

    private String name;
    private String description;

    private Set<Product> productsList;

    public ProductGroup(String name, String description) {
        this.id = idSequence.incrementAndGet();
        this.name = name;
        this.description = description;
        productsList = new LinkedHashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(Set<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public String toString() {
        return '{' +
                "\n\tid: " + id +
                "\n\tname: '" + name + '\'' +
                "\n\tdescription: '" + description + '\'' +
                "\n\tproductsList: \n" + productsList +
                '}';
    }
}
