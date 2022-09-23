package com.example.mallapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {

    private static final String CART_ITEMS_KEY = "CART_ITEMS_KEY";
    private static int ORDER_ID = 0;
    public static int ID = 0;

    private static final String DB_NAME = "fake_db";
    private static final String ALL_ITEMS_KEY = "ALL_ITEMS_KEY";
    private static final Gson gson = new Gson();
    private static final Type groceryListType = new TypeToken<ArrayList<GroceryItem>>() {
    }.getType(); //Use for convert json to arraylist

    public static void initSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> allItems = gson.fromJson(sharedPreferences.getString(ALL_ITEMS_KEY, null), groceryListType);
        if (null == allItems) {
            initAllItems(context);
        }
    }

    private static void initAllItems(Context context) {
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

        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALL_ITEMS_KEY, gson.toJson(allItems));
        editor.commit();
    }

    public static ArrayList<GroceryItem> getAllItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        return gson.fromJson(sharedPreferences.getString(ALL_ITEMS_KEY, null), groceryListType);
    }

    public static int getID() {
        return ++ID;
    }

    public static int getOrderId() {
        return ++ORDER_ID;
    }

    public static void changeRate(Context context, int itemId, int newRate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<GroceryItem> allItems = gson.fromJson(sharedPreferences.getString(ALL_ITEMS_KEY, null), groceryListType);
        if (null != allItems) {
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i : allItems) {
                if (i.getId() == itemId) {
                    i.setRate(newRate);
                }
                newItems.add(i);
            }

            editor.remove(ALL_ITEMS_KEY);
            editor.putString(ALL_ITEMS_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static void increasePopularityPoint(Context context, GroceryItem item, int points) {
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i : allItems) {
                if (i.getId() == item.getId()) {
                    i.setPopularityPoint(i.getPopularityPoint() + points);
                    newItems.add(i);
                } else {
                    newItems.add(i);
                }
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(ALL_ITEMS_KEY);
            Gson gson = new Gson();
            editor.putString(ALL_ITEMS_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }


    public static void changeUserPoint(Context context, GroceryItem item, int points) {
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i : allItems) {
                if (i.getId() == item.getId()) {
                    i.setUserPoint(i.getUserPoint() + points);
                }

                newItems.add(i);
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(ALL_ITEMS_KEY);

            editor.putString(ALL_ITEMS_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static void clearCartItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_ITEMS_KEY);
        editor.commit();
    }

    public static void deleteItemFromCart(Context context, GroceryItem item) {
        ArrayList<GroceryItem> cartItems = getCartItems(context);
        if (null != cartItems) {
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i : cartItems) {
                if (i.getId() != item.getId()) {
                    newItems.add(i);
                }
            }

            SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CART_ITEMS_KEY);
            editor.putString(CART_ITEMS_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static void addReview(Context context, Review review) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            ArrayList<GroceryItem> newItems = new ArrayList<>();
            for (GroceryItem i : allItems) {
                if (i.getId() == review.getGroceryItemId()) {
                    ArrayList<Review> reviews = i.getReviews();
                    reviews.add(review);
                    i.setReviews(reviews);
                }
                newItems.add(i);
            }

            editor.remove(ALL_ITEMS_KEY);
            editor.putString(ALL_ITEMS_KEY, gson.toJson(newItems));
            editor.commit();
        }
    }

    public static ArrayList<Review> getReviewById(Context context, int itemId) {
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            for (GroceryItem i : allItems) {
                if (i.getId() == itemId) {
                    ArrayList<Review> reviews = i.getReviews();
                    return reviews;
                }
            }
        }

        return null;
    }

    public static ArrayList<GroceryItem> searchForItems(Context context, String text) {
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            ArrayList<GroceryItem> items = new ArrayList<>();
            for (GroceryItem item : allItems) {
                if (item.getName().equalsIgnoreCase(text)) {
                    items.add(item);
                }

                String[] names = item.getName().split(" ");
                for (int i = 0; i < names.length; i++) {
                    if (text.equalsIgnoreCase(names[i])) {
                        boolean doesExist = false;

                        for (GroceryItem j : items) {
                            if (j.getId() == item.getId()) {
                                doesExist = true;
                            }
                        }

                        if (!doesExist) {
                            items.add(item);
                        }
                    }
                }
            }

            return items;
        }

        return null;
    }

    public static ArrayList<GroceryItem> getItemsByCategory(Context context, String category) {
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            ArrayList<GroceryItem> items = new ArrayList<>();
            for (GroceryItem item : allItems) {
                if (item.getCategory().equals(category)) {
                    items.add(item);
                }
            }

            return items;
        }

        return null;
    }

    public static ArrayList<String> getCategories(Context context) {
        ArrayList<GroceryItem> allItems = getAllItems(context);
        if (null != allItems) {
            ArrayList<String> categories = new ArrayList<>();
            for (GroceryItem item : allItems) {
                boolean doesExist = false;
                for (String s : categories) {
                    if (item.getCategory().equals(s)) {
                        doesExist = true;
                    }
                }

                if (!doesExist) {
                    categories.add(item.getCategory());
                }
            }

            return categories;
        }

        return null;
    }

    public static void addItemToCart(Context context, GroceryItem item) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> cartItems = gson.fromJson(sharedPreferences.getString(CART_ITEMS_KEY, null), groceryListType);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        cartItems.add(item);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_ITEMS_KEY);
        editor.putString(CART_ITEMS_KEY, gson.toJson(cartItems));
        editor.commit();
    }

    public static ArrayList<GroceryItem> getCartItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        ArrayList<GroceryItem> cartItems = gson.fromJson(sharedPreferences.getString(CART_ITEMS_KEY, null), groceryListType);
        return cartItems;
    }

    public static String getLicenses() {
        String licenses = "";

        //Gson
        licenses += "Gson\n";
        licenses += "Copyright 2008 Google Inc.\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n\n" +
                "limitations under the License.\n\n";

        //Glide
        licenses += "Glide\n";
        licenses += "License for everything not in third_party and not otherwise marked:\n" +
                "\n" +
                "Copyright 2014 Google, Inc. All rights reserved.\n" +
                "\n" +
                "Redistribution and use in source and binary forms, with or without modification, are\n" +
                "permitted provided that the following conditions are met:\n" +
                "\n" +
                "   1. Redistributions of source code must retain the above copyright notice, this list of\n" +
                "         conditions and the following disclaimer.\n" +
                "\n" +
                "   2. Redistributions in binary form must reproduce the above copyright notice, this list\n" +
                "         of conditions and the following disclaimer in the documentation and/or other materials\n" +
                "         provided with the distribution.\n" +
                "\n" +
                "THIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED\n" +
                "WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n" +
                "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR\n" +
                "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
                "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
                "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n" +
                "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF\n" +
                "ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "\n" +
                "The views and conclusions contained in the software and documentation are those of the\n" +
                "authors and should not be interpreted as representing official policies, either expressed\n" +
                "or implied, of Google, Inc.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "License for third_party/disklrucache:\n" +
                "\n" +
                "Copyright 2012 Jake Wharton\n" +
                "Copyright 2011 The Android Open Source Project\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "License for third_party/gif_decoder:\n" +
                "\n" +
                "Copyright (c) 2013 Xcellent Creations, Inc.\n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining\n" +
                "a copy of this software and associated documentation files (the\n" +
                "\"Software\"), to deal in the Software without restriction, including\n" +
                "without limitation the rights to use, copy, modify, merge, publish,\n" +
                "distribute, sublicense, and/or sell copies of the Software, and to\n" +
                "permit persons to whom the Software is furnished to do so, subject to\n" +
                "the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be\n" +
                "included in all copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
                "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n" +
                "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n" +
                "NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE\n" +
                "LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION\n" +
                "OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION\n" +
                "WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n" +
                "---------------------------------------------------------------------------------------------\n" +
                "License for third_party/gif_encoder/AnimatedGifEncoder.java and\n" +
                "third_party/gif_encoder/LZWEncoder.java:\n" +
                "\n" +
                "No copyright asserted on the source code of this class. May be used for any\n" +
                "purpose, however, refer to the Unisys LZW patent for restrictions on use of\n" +
                "the associated LZWEncoder class. Please forward any corrections to\n" +
                "kweiner@fmsware.com.\n" +
                "\n" +
                "-----------------------------------------------------------------------------\n" +
                "License for third_party/gif_encoder/NeuQuant.java\n" +
                "\n" +
                "Copyright (c) 1994 Anthony Dekker\n" +
                "\n" +
                "NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994. See\n" +
                "\"Kohonen neural networks for optimal colour quantization\" in \"Network:\n" +
                "Computation in Neural Systems\" Vol. 5 (1994) pp 351-367. for a discussion of\n" +
                "the algorithm.\n" +
                "\n" +
                "Any party obtaining a copy of these files from the author, directly or\n" +
                "indirectly, is granted, free of charge, a full and unrestricted irrevocable,\n" +
                "world-wide, paid up, royalty-free, nonexclusive right and license to deal in\n" +
                "this software and documentation files (the \"Software\"), including without\n" +
                "limitation the rights to use, copy, modify, merge, publish, distribute,\n" +
                "sublicense, and/or sell copies of the Software, and to permit persons who\n" +
                "receive copies from any such party to do so, with the only requirement being\n" +
                "that this copyright notice remain intact.\n\n";

        //Retrofit
        licenses += "Retrofit\n";
        licenses += "Copyright 2013 Square, Inc.\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.\n\n";

        return licenses;
    }
}
