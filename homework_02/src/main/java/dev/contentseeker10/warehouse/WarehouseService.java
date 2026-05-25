package dev.contentseeker10.warehouse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseService {

    private static final WarehouseService INSTANCE = new WarehouseService();

    private WarehouseService() {}

    public static WarehouseService getInstance() {
        return INSTANCE;
    }

    private final Map<Integer, ProductGroup> groups = new ConcurrentHashMap<>();
    private final Map<Integer, Product> products = new ConcurrentHashMap<>();

    public Map<Integer, ProductGroup> getGroups() {
        return groups;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public boolean addProductGroup(String name, String description) {
        ProductGroup newGroup = new ProductGroup(name, description);
        groups.put(newGroup.getId(), newGroup);
        return true;
    }

    public boolean addProductToGroup(int groupId, String name, String description, double price) {
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

    public int getProductAmount(int productId) {
        if (products.get(productId) == null)
            return -1;
        return products.get(productId).getAmount();
    }

    public boolean writeOffProduct(int productId, int amount) {
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

    public boolean writeOnProduct(int productId, int amount) {
        Product product = products.get(productId);
        if (product == null || amount < 1)
            return false;
        synchronized (product) {
            int productAmount = product.getAmount();
            product.setAmount(productAmount + amount);
        }
        return true;
    }

    public boolean setProductPrice(int productId, double price) {
        Product product = products.get(productId);
        if (product == null || price < 0.0)
            return false;
        synchronized (product) {
            product.setPrice(price);
        }
        return true;
    }

}
