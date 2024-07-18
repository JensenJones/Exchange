package org.jj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStore {
    private final Map<Integer, Product> idToProduct = new HashMap<>();
    private final Map<String, Product> symbolToProduct = new HashMap<>();
    private final IdProvider idProvider;

    public ProductStore(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public int addProduct(String name, String symbol) {
        if (symbolToProduct.containsKey(symbol)) {
            return -1;
        }

        int id = idProvider.getNewId();
        Product product = new Product(id, symbol, name);
        symbolToProduct.put(symbol, product);
        idToProduct.put(id, product);

        return id;
    }

    public boolean hasProduct(int id) {
        return idToProduct.containsKey(id);
    }

    public boolean hasProduct(String symbol) {
        return symbolToProduct.containsKey(symbol);
    }

    public boolean removeProduct(int id) {
        Product product = idToProduct.remove(id);
        if (product == null) {
            return false;
        }
        symbolToProduct.remove(product.getSymbol());
        return true;
    }

    public boolean removeProduct(String symbol) {
        Product product = symbolToProduct.remove(symbol);
        if (product == null) {
            return false;
        }
        idToProduct.remove(product.getId());
        return true;
    }
}
