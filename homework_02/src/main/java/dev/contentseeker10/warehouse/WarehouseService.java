package dev.contentseeker10.warehouse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseService {

    private final Map<Integer, ProductGroup> groups = new ConcurrentHashMap<>();
    private final Map<Integer, Product> products = new ConcurrentHashMap<>();

    public boolean addProductGroup(String name, String description) {

    }

    public boolean addProductToGroup(int groupId, String name, String description, double price) {

    }

    public int getProductAmount(int productId) {

    }

    public boolean writeOffProduct(int productId, int amount) {

    }

    public boolean writeOnProduct(int productId, int amount) {

    }

    public boolean setProductPrice(int productId, double price) {

    }

}
