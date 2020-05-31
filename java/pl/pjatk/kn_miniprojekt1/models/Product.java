package pl.pjatk.kn_miniprojekt1.models;  // pl.pjatk.kn_miniprojekt1

public class Product {

    private String productId;
    private String productName;
    private String productAmount;
    private String productPrice;
    private boolean isBought;

    public Product()
    {
    }

    public Product(String id){
        productId = id;}

    public String getId()
    {
        return productId;
    }

    public void setId(String id)
    {
        this.productId = id;
    }

    public String getName()
    {
        return productName;
    }

    public void setName(String name)
    {
        this.productName = name;
    }

    public String getCount()
    {
        return productAmount;
    }

    public void setCount(String amount)
    {
        this.productAmount = amount;
    }

    public String getPrice()
    {
        return productPrice;
    }

    public void setPrice(String price)
    {
        this.productPrice = price;
    }

    public boolean isPurchased()
    {
        return isBought;
    }

    public void isBoughtSet(boolean isBought)
    {
        this.isBought = isBought;
    }
}
