package dev.contentseeker10.warehouse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseService {

    private static final Map<Integer, ProductGroup> groups = new ConcurrentHashMap<>();
    private static final Map<Integer, Product> products = new ConcurrentHashMap<>();

    public static boolean addProductGroup(String name, String description) {
        ProductGroup newGroup = new ProductGroup(name, description);
        groups.put(newGroup.getId(), newGroup);
        return true;
    }

    public static boolean addProductToGroup(int groupId, String name, String description, double price) {
        ProductGroup group = groups.get(groupId);
        if (group == null)
            return false;
        Product newProduct = new Product(name, description, 0, price);
        synchronized (group) {
            group.getProductsList().add(newProduct);
        }
        products.put(newProduct.getId(), newProduct);
        return true;
    }

    public static int getProductAmount(int productId) {
        if (products.get(productId) == null)
            return -1;
        return products.get(productId).getAmount();
    }

    public static boolean writeOffProduct(int productId, int amount) {
        Product product = products.get(productId);
        if (product == null || amount < 1)
            return false;
        synchronized (product) {
            int productAmount = product.getAmount();
            if (amount > productAmount)
                return false;
            else
                product.setAmount(productAmount - amount);
        }
        return true;
    }

    public static boolean writeOnProduct(int productId, int amount) {
        Product product = products.get(productId);
        if (product == null || amount < 1)
            return false;
        synchronized (product) {
            int productAmount = product.getAmount();
            product.setAmount(productAmount + amount);
        }
        return true;
    }

    public static boolean setProductPrice(int productId, double price) {
        Product product = products.get(productId);
        if (product == null || price < 0.0)
            return false;
        synchronized (product) {
            product.setPrice(price);
        }
        return true;
    }

}
