package dev.contentseeker10.warehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class Product {

    private static final AtomicInteger idSequence = new AtomicInteger(0);
    private final int id;

    private String name;
    private String description;

    private int quantity;
    private double price;

    public Product(String name, String description, int quantity, double price) {
        this.id = idSequence.incrementAndGet();
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\tid: " + id +
                "\n\tname: '" + name + '\'' +
                "\n\tdescription: '" + description + '\'' +
                "\n\tquantity: " + quantity +
                "\n\tprice: " + price +
                "\n}";
    }
}
