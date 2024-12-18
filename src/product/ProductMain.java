package product;

import java.util.List;

public class ProductMain {
    public static void main(String[] args) {
        List<Product> products = ProductService.selectAll();
        for (Product product : products) {
            System.out.println(product);
        }

        Product product = ProductService.selectByNo("p05");
        if(product == null){
            System.out.println("Product is null");
        }else {
            System.out.println(product);
        }

        ProductService.insert("p09", "맛있는떡볶이", 200, 3000, "한밭식품");
        Product product09 = ProductService.selectByNo("p09");
        if(product09 == null){
            System.out.println("INSERt Product is null");
        }else {
            System.out.println(product09);
        }

        ProductService.update("p09",  199, 3000);
        Product productUpdate09 = ProductService.selectByNo("p09");
        if(productUpdate09 == null){
            System.out.println("update Product is null");
        }else {
            System.out.println(productUpdate09);
        }

        ProductService.delete("p09");
        Product deletedProduct = ProductService.selectByNo("p09");
        if(deletedProduct == null){
            System.out.println("delete Product is null");
        }else {
            System.out.println("삭제 실패");
        }

    }
}
