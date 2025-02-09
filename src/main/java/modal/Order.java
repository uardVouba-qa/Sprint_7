package modal;

public class Order {
    private String id;
    private String courierId;

    public Order(String id, String courierId) {
        this.id = id;
        this.courierId = courierId;
    }

    public String getId() {
        return id;
    }

    public String getCourierId() {
        return courierId;
    }
}
