package org.jj.Product;

import org.jj.Main;
import org.jj.Providers.IdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private final Map<Integer, Product> idToProduct = new HashMap<>();
    private final Map<String, Product> symbolToProduct = new HashMap<>();
    private final IdProvider idProvider;

    public ProductStore(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public int addProduct(String name, String symbol) {
        if (symbolToProduct.containsKey(symbol)) {
            throw new IllegalArgumentException("Product with that symbol already exists");
        }

        int id = idProvider.generateId();

        Product product = new Product(id, symbol, name);
        symbolToProduct.put(symbol, product);
        idToProduct.put(id, product);

        return id;
    }

    public boolean containsProduct(int id) {
        return idToProduct.containsKey(id);
    }

    public Product getProduct(int id) {
        return idToProduct.get(id);
    }

    public boolean removeProduct(int id) {
        Product product = idToProduct.remove(id);
        if (product == null) {
            return false;
        }
        symbolToProduct.remove(product.getSymbol());
        return true;
    }

    // TODO - remove unless you need it. I don't think you need it?
    public List<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        for (Map.Entry<Integer, Product> product : idToProduct.entrySet()) {
            productList.add(product.getValue());
        }
        return productList;
    }
}
