package org.jj.providers;

import org.jetbrains.annotations.VisibleForTesting;
import org.jj.matchingEngine.MatchingEngineImpl;
import org.jj.product.Product;
import org.jj.product.ProductStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MatchingEngineProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineProvider.class);

    private final Map<Product, MatchingEngineImpl> productToMatchingEngine = new HashMap<>();
    private final ProductStore productStore;


    public MatchingEngineProvider(ProductStore productStore) {
        this.productStore = productStore;

        for (Product product : productStore.getAllProducts()) {
            productToMatchingEngine.put(product, new MatchingEngineImpl(new SystemTimestampProvider(), new IntIdProvider()));
        }
    }

    public MatchingEngineImpl getMatchingEngine(int productId) {
        Product product = productStore.getProduct(productId);

        if (product == null) {
            LOGGER.error("Product Does Not Exist");
            return null;
        }

        return productToMatchingEngine.computeIfAbsent(product,
                key -> new MatchingEngineImpl(new SystemTimestampProvider(), new IntIdProvider()));
    }
}
