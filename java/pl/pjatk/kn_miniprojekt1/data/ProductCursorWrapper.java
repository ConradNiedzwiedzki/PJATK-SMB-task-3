package pl.pjatk.kn_miniprojekt1.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import pl.pjatk.kn_miniprojekt1.models.Product;

import static pl.pjatk.kn_miniprojekt1.data.DatabaseSchema.*;

public class ProductCursorWrapper extends CursorWrapper
{
    public ProductCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Product getProduct()
    {
        String uuid = getString(getColumnIndex(ProductsContract.ProductColumns.UUID));
        String name = getString(getColumnIndex(ProductsContract.ProductColumns.NAME));
        String amount = getString(getColumnIndex(ProductsContract.ProductColumns.AMOUNT));
        String price = getString(getColumnIndex(ProductsContract.ProductColumns.PRICE));
        boolean bought = getInt(getColumnIndex(ProductsContract.ProductColumns.BOUGHT)) > 0;

        Product product = new Product(uuid);
        product.setName(name);
        product.setCount(amount);
        product.setPrice(price);
        product.isBoughtSet(bought);

        return product;
    }
}
