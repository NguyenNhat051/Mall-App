package com.example.mallapplication.Database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CartItemDao {

    @Query("INSERT INTO cart_items (groceryItemId) VALUES (:id)")
    void insert(int id);

    @Query("SELECT grocery_items.*  FROM grocery_items INNER JOIN cart_items ON cart_items.groceryItemId = grocery_items.id")
    List<GroceryItem> getAllCartItems();

    @Query("DELETE FROM cart_items WHERE groceryItemId = :id")
    void deleteItemById(int id);

    @Query("DELETE FROM cart_items")
    void clearCart();
}
