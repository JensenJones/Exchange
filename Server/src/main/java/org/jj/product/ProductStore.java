package org.jj.product;

import org.jj.providers.IdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStore.class);

    private final Map<Integer, Product> idToProduct = new HashMap<>();
    private final Map<String, Integer> symbolToId = new HashMap<>();
    private final IdProvider idProvider;

    public ProductStore(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public int addProduct(String name, String symbol) {
        symbol = symbol.strip();
        name = name.strip();

        if (idToProduct.containsKey(symbolToId.get(symbol))) {
            LOGGER.error("Product with that symbol already exists");
            return -1;
        }

        int id = idProvider.generateId();

        Product product = new Product(id, symbol, name);
        symbolToId.put(symbol, id);
        idToProduct.put(id, product);

        return id;
    }

    public boolean containsProduct(int id) {
        return idToProduct.containsKey(id);
    }

    public Product getProduct(int id) {
        return idToProduct.get(id);
    }

    public Product getProduct(String symbol) {
        return idToProduct.get(symbolToId.get(symbol));
    }

    public boolean removeProduct(int id) {
        Product product = idToProduct.remove(id);
        if (product == null) {
            return false;
        }

        String symbol;
        symbolToId.entrySet().removeIf(e -> e.getValue().equals(id));

        return true;
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        for (Map.Entry<Integer, Product> product : idToProduct.entrySet()) {
            productList.add(product.getValue());
        }
        return productList;
    }

    public int getProductId(String productSymbol) {
        return symbolToId.get(productSymbol);
    }
}
