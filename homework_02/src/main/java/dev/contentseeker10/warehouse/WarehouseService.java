package dev.contentseeker10.warehouse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseService {

    private final Map<Integer, ProductGroup> groups = new ConcurrentHashMap<>();
    private final Map<Integer, Product> products = new ConcurrentHashMap<>();

    public boolean addProductGroup(String name, String description) {
        ProductGroup newGroup = new ProductGroup(name, description);
        groups.put(newGroup.getId(), newGroup);
        return true;
    }

    public boolean addProductToGroup(int groupId, String name, String description, double price) {
        if (groups.get(groupId) == null)
            return false;
        Product newProduct = new Product(name, description, 0, price);
        ProductGroup group = groups.get(groupId);
        synchronized (group) {
            group.getProductsList().add(newProduct);
        }
        products.put(newProduct.getId(), newProduct);
        return true;
    }

    public int getProductAmount(int productId) {
        if (products.get(productId) == null)
            return -1;
        return products.get(productId).getAmount();
    }

    public boolean writeOffProduct(int productId, int amount) {
        if (products.get(productId) == null)
            return false;
        Product product = products.get(productId);
        synchronized (product) {
            int productAmount = product.getAmount();
            if (amount >= productAmount)
                product.setAmount(0);
            else
                product.setAmount(productAmount - amount);
        }
        return true;
    }

    public boolean writeOnProduct(int productId, int amount) {
        if (products.get(productId) == null)
            return false;
        Product product = products.get(productId);
        synchronized (product) {
            int productAmount = product.getAmount();
            product.setAmount(productAmount + amount);
        }
        return true;
    }

    public boolean setProductPrice(int productId, double price) {
        if (products.get(productId) == null || price < 0.0)
            return false;
        Product product = products.get(productId);
        synchronized (product) {
            product.setPrice(price);
        }
        return true;
    }

}
