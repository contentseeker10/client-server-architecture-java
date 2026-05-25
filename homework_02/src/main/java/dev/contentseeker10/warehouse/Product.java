package dev.contentseeker10.warehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class Product {

    private static final AtomicInteger idSequence = new AtomicInteger(0);
    private final int id;

    private String name;
    private String description;

    private volatile int amount;
    private volatile double price;

    public Product(String name, String description, int amount, double price) {
        this.id = idSequence.incrementAndGet();
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.price = price;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\tid: " + id +
                "\n\tname: '" + name + '\'' +
                "\n\tdescription: '" + description + '\'' +
                "\n\tamount: " + amount +
                "\n\tprice: " + price +
                "\n}";
    }
}
