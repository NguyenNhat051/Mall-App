package com.example.mallapplication.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

@Database(entities = {GroceryItem.class, CartItem.class}, version = 1)
public abstract class MallDataBase extends RoomDatabase {
    public abstract GroceryItemDao groceryItemDao();

    public abstract CartItemDao cartItemDao();

    private static MallDataBase instance;

    public static synchronized MallDataBase getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context, MallDataBase.class, "mall_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(initDbCallback)
                    .build();
            return instance;
        }
        return instance;
    }

    private static RoomDatabase.Callback initDbCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new InitDbAsyncTask(instance).execute();
        }
    };

    private static class InitDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private GroceryItemDao groceryItemDao;

        public InitDbAsyncTask(MallDataBase db) {
            this.groceryItemDao = db.groceryItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<GroceryItem> allItems = new ArrayList<>();

            GroceryItem milk = new GroceryItem("Milk", "Milk is a white liquid food produced by the mammary glands " +
                    "of mammals. It is the primary source of nutrition for young mammals (including breastfed human infants) before they " +
                    "are able to digest solid food.[1] Immune factors and immune-modulating components in milk contribute to milk immunity. " +
                    "Early-lactation milk, which is called colostrum, contains antibodies that strengthen the immune system, and thus " +
                    "reduces the risk of many diseases. Milk contains many nutrients, including protein and lactose.",
                    "https://cdn.tgdd.vn/Products/Images/2386/223729/bhx/sua-tuoi-tiet-trung-co-duong-dalat-milk-bich-220ml-202006110917544701.jpg",
                    "Drink", 2.3, 69);

            GroceryItem iceCream = new GroceryItem("Ice Cream", "Ice cream is a sweetened frozen food typically eaten as a " +
                    "snack or dessert. It may be made from milk or cream and is flavoured with a sweetener, either sugar or an alternative, " +
                    "and a spice, such as cocoa or vanilla, or with fruit such as strawberries or peaches. It can also be made by whisking " +
                    "a flavored cream base and liquid nitrogen together.",
                    "https://upload.wikimedia.org/wikipedia/commons/d/da/Strawberry_ice_cream_cone_%285076899310%29.jpg"
                    , "Food", 1.4, 100);

            GroceryItem coca = new GroceryItem("Coca Cola", "Coca-Cola, or Coke, is a carbonated soft drink manufactured " +
                    "by the Coca-Cola Company. Originally marketed as a temperance drink and intended as a patent medicine, it was invented " +
                    "in the late 19th century by John Stith Pemberton in Atlanta, Georgia. In 1888.",
                    "https://thtmart.com.vn/wp-content/uploads/2021/05/Nuoc-giai-khat-Coca-Cola-lon-330ml-1.jpg", "Drink"
                    , 3.0, 80);

            GroceryItem strawberry = new GroceryItem("Strawberry", "The garden strawberry is a widely grown hybrid species " +
                    "of the genus Fragaria, collectively known as the strawberries, which are cultivated worldwide for " +
                    "their fruit. The fruit is widely appreciated for its characteristic aroma, bright red color, juicy texture, " +
                    "and sweetness. It is consumed in large quantities, either fresh or in such prepared foods as jam, juice, " +
                    "pies, ice cream, milkshakes, and chocolates. Artificial strawberry flavorings and aromas are also widely " +
                    "used in products such as candy, soap, lip gloss, perfume, and many others.",
                    "https://cf.shopee.vn/file/3f791a9d8bc278d82869c063de97f9ec"
                    , "Fruit",
                    12.5, 90);

            GroceryItem cherry = new GroceryItem("Cherry", "A cherry is the fruit of many plants of the genus Prunus, and " +
                    "is a fleshy drupe (stone fruit).",
                    "https://product.hstatic.net/200000370453/product/cherry-do-my-500x500_298895034b8247789f77e673179153ca.jpg",
                    "Fruit", 11, 95);

            GroceryItem shampoo = new GroceryItem("Johnson's Baby Shampoo", "Shampoo is a hair care product, typically in the " +
                    "form of a viscous liquid, that is used for cleaning hair. Less commonly, shampoo is available in solid bar format. " +
                    "Shampoo is used by applying it to wet hair, massaging the product into the scalp, and then rinsing it out. Some users " +
                    "may follow a shampooing with the use of hair conditioner.",
                    "https://nhathuoclongchau.com/images/product/2021/08/00010043-johnson-baby-shampoo-200ml-4683-6123_large.jpg",
                    "Clean", 20, 150);

            allItems.add(milk);
            allItems.add(iceCream);
            allItems.add(coca);
            allItems.add(strawberry);
            allItems.add(cherry);
            allItems.add(shampoo);

            for (GroceryItem g : allItems) {
                groceryItemDao.insert(g);
            }
            return null;
        }
    }
}
