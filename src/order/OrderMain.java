package order;



import java.util.Date;
import java.util.List;

public class OrderMain {

    public static void main(String[] args) {

        // 전체 주문 정보 검색
        System.out.println("\n전체 상품 정보 검색\n----------------------------------------");
        List<Order> orderList = OrderService.selectAll();
        for (Order order : orderList) {
            System.out.printf("%-5s : %-12s : %-12s : %-5s : %-12s : %10d : %-20s : %-12s\n", order.getOrderNo(), order.getCustomerId(),
                    order.getCustomerName(), order.getProductNo(), order.getProductName(), order.getQuantity(), order.getDestination(), order.getOrderDate());
        }

        // 번호로 검색하기
        System.out.println("\n번호로 검색하기 - o07\n----------------------------------------");
        {
            Order order = OrderService.selectById("o07");
            if (order != null) {
                System.out.println(order);
            } else {
                System.out.println("o07 not exist !!");
            }
        }

        // 고객 번호로 검색하기
        System.out.println("\n고객 번호로 검색하기 - apple\n----------------------------------------");
        List<Order> orderList2 = OrderService.selectByCustomerId("apple");
        for (Order order : orderList2) {
            System.out.printf("%-5s : %-12s : %-12s : %-5s : %-12s : %10d : %-20s : %-12s\n", order.getOrderNo(), order.getCustomerId(),
                    order.getCustomerName(), order.getProductNo(), order.getProductName(), order.getQuantity(), order.getDestination(), order.getOrderDate());
        }

        // 상품 번호로 검색하기
        System.out.println("\n상품 번호로 검색하기 - p03\n----------------------------------------");
        List<Order> orderList3 = OrderService.selectByProductNo("p03");
        for (Order order : orderList3) {
            System.out.printf("%-5s : %-12s : %-12s : %-5s : %-12s : %10d : %-20s : %-12s\n", order.getOrderNo(), order.getCustomerId(),
                    order.getCustomerName(), order.getProductNo(), order.getProductName(), order.getQuantity(), order.getDestination(), order.getOrderDate());
        }

        // 새로운 주문 추가하기 -> 추가 확인 검새
        System.out.println("\n새로운 주문 추가하기\n----------------------------------------");
        String newOrderNo = OrderService.insert("apple", "p03", 100, "대전시 유성구", new Date());
        if (newOrderNo != null) {
            Order order = OrderService.selectById(newOrderNo);
            if (order != null) {
                System.out.println(order);
            } else {
                System.out.println(newOrderNo + " not exist !!");
            }
        } else {
            System.out.println(newOrderNo + " 주문 추가에 실패했습니다.");
        }

//        // 주문 정보 수정하기 -> 추가 확인 검새
//        System.out.println("\n주문 정보 수정하기 - " + newOrderNo + "\n----------------------------------------");
//        if (OrderService.updateById(newOrderNo, 200, "대전시 유성구") > 0) {
//            Order order = OrderService.selectById(newOrderNo);
//            if (order != null) {
//                System.out.println(order);
//            } else {
//                System.out.println(newOrderNo + " not exist !!");
//            }
//        } else {
//            System.out.println(newOrderNo + " 주문 정보 수정에 실패했습니다.");
//        }

        // 주문 정보 삭제하기 -> 추가 확인 검새
        System.out.println("\n주문 정보 삭제하기 - " + newOrderNo + "\n----------------------------------------");
        if (OrderService.deleteById(newOrderNo) > 0) {
            Order order = OrderService.selectById(newOrderNo);
            if (order != null) {
                System.out.println(newOrderNo + " exist !!");
            } else {
                System.out.println(newOrderNo + " 정보 삭제에 성공했습니다 !!");
            }
        } else {
            System.out.println(newOrderNo + " 주문 정보 삭제에 실패했습니다.");
        }

    }

}