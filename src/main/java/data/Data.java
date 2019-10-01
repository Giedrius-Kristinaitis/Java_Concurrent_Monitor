package data;

public class Data implements Comparable<Data> {

    private String title;
    private double price;
    private int quantity;
    private String result;

    public Data(String title, double price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(Data o) {
        if (result == null || o == null || o.result == null) {
            return 0;
        }

        return result.compareTo(o.result);
    }
}
