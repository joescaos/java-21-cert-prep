package org.joescaos.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import org.joescaos.data.Product;
import org.joescaos.data.ProductManager;
import org.joescaos.data.Rating;

public class Shop {

  public static void main(String[] args) {
    ProductManager productManager = new ProductManager(Locale.US);
    Product tea = productManager.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
    //productManager.printProductReport(101);
    productManager.reviewProduct(101, Rating.FIVE_STAR, "Nice coup of tea");
    productManager.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
    productManager.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
    productManager.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
    productManager.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
    //productManager.printProductReport(101);
    Product cake = productManager.createProduct(102, "Cake", BigDecimal.valueOf(1.98), Rating.NOT_RATED,
            LocalDate.now().plusDays(2));
    //productManager.printProductReport(102);
    productManager.reviewProduct(102, Rating.FIVE_STAR, "Nice piece of cake");
    productManager.reviewProduct(102, Rating.TWO_STAR, "too expensive");
    //productManager.printProductReport(102);

    ProductManager productManagerSP = new ProductManager("es-CO");
    Product p3 = productManagerSP.createProduct(103, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
    //productManagerSP.printProductReport(103);
    productManagerSP.reviewProduct(103, Rating.FIVE_STAR, "Buena taza de te");
    productManagerSP.reviewProduct(103, Rating.TWO_STAR, "No estaba tan buena");
    productManagerSP.reviewProduct(103, Rating.FOUR_STAR, "Delicioso te");
    productManagerSP.reviewProduct(103, Rating.FOUR_STAR, "Buen te");
    productManagerSP.reviewProduct(103, Rating.THREE_STAR, "Deberían poner algo de limon");
    //productManagerSP.printProductReport(103);
    Product p4 = productManagerSP.createProduct(104, "Cake", BigDecimal.valueOf(1.98), Rating.NOT_RATED,
            LocalDate.now().plusDays(2));
    //productManagerSP.printProductReport(104);
    productManagerSP.reviewProduct(104, Rating.FIVE_STAR, "Que buena porcion de pastel");
    productManagerSP.reviewProduct(104, Rating.TWO_STAR, "muy caro");
    //productManagerSP.printProductReport(104);
    productManager.printProducts((p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal());
    productManagerSP.printProducts((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
    productManager.printProducts((p1, p2) -> p2.getBestBefore().compareTo(p1.getBestBefore()));


  }
}
