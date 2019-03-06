package cashflow.main;

public class Entry {

    private String date,price,categorie,io;


    public Entry(String date, String price, String categorie, String io) {
        this.date = date;
        this.price = price;
        this.categorie = categorie;
        this.io=io;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getIo() {
        return io;
    }

    public void setIo(String io) {
        this.io = io;
    }

    @Override
    public String toString() {
        return date+";"+price+";"+categorie+";"+io;
    }
}
