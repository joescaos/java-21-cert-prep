package org.joescaos.app;

import org.joescaos.data.ProductManager;
import org.joescaos.data.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Shop {

  public static void main(String[] args) {
    ProductManager productManager = ProductManager.getInstance();

    ExecutorService es = Executors.newFixedThreadPool(3);

    Runnable r = () -> {
        productManager.createProduct(101, "Cake", BigDecimal.valueOf(19.0), Rating.NOT_RATED, LocalDate.now().plusDays(15));
        productManager.createProduct(102, "CupCake", BigDecimal.valueOf(12.0), Rating.NOT_RATED, LocalDate.now().plusDays(10));
        productManager.createProduct(103, "Tea", BigDecimal.valueOf(10.0), Rating.NOT_RATED);

        productManager.reviewProduct(101, Rating.FOUR_STAR, "good");
        productManager.reviewProduct(101, Rating.FIVE_STAR, "Nice cake");

        productManager.reviewProduct(103, Rating.THREE_STAR, "It was kinda cold");

        productManager.printProductReport(101, "es_CO");
        productManager.printProductReport(102, "en_US");
        productManager.printProductReport(103, "en_US");

    };


    es.submit(r);
    es.shutdown();




  }
}
