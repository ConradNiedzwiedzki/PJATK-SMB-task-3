package pl.pjatk.kn_miniprojekt1.data;

public class DatabaseSchema
{
    public static final class ProductsContract
    {
        public static final String NAME = "Product";
        public static final class ProductColumns
        {
            public static final String UUID = "id";
            public static final String NAME = "name";
            public static final String AMOUNT = "amount";
            public static final String PRICE = "price";
            public static final String BOUGHT = "bought";
        }
    }
}
