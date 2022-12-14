package com.example.mallapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallapplication.Database.GroceryItem;
import com.example.mallapplication.R;
import com.example.mallapplication.Utils;
import com.example.mallapplication.adapter.CartAdapter;

import java.util.ArrayList;

public class FirstCartFragment extends Fragment implements CartAdapter.DeleteItem, CartAdapter.TotalPrice {

    private RecyclerView recyclerView;
    private TextView txtSum, txtNoItems;
    private Button btnNext;
    private RelativeLayout itemsRelLayout;

    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment_cart, container, false);

        initViews(view);

        adapter = new CartAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initCart();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new SecondCartFragment());
                transaction.commit();
            }
        });

        return view;
    }

    private void initCart() {
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if (null != cartItems) {
            if (cartItems.size() > 0) {
                txtNoItems.setVisibility(View.GONE);
                itemsRelLayout.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            } else {
                txtNoItems.setVisibility(View.VISIBLE);
                itemsRelLayout.setVisibility(View.GONE);
            }
        } else {
            txtNoItems.setVisibility(View.VISIBLE);
            itemsRelLayout.setVisibility(View.GONE);
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        txtSum = view.findViewById(R.id.txtTotalPrice);
        txtNoItems = view.findViewById(R.id.txtNoItem);
        btnNext = view.findViewById(R.id.btnNext);
        itemsRelLayout = view.findViewById(R.id.itemsRelLayout);
    }

    @Override
    public void onDeleteResult(GroceryItem item) {
        Utils.deleteItemFromCart(getActivity(), item);
        ArrayList<GroceryItem> cartItems = Utils.getCartItems(getActivity());
        if (null != cartItems) {
            if (cartItems.size() > 0) {
                txtNoItems.setVisibility(View.GONE);
                itemsRelLayout.setVisibility(View.VISIBLE);
                adapter.setItems(cartItems);
            } else {
                txtNoItems.setVisibility(View.VISIBLE);
                itemsRelLayout.setVisibility(View.GONE);
            }
        } else {
            txtNoItems.setVisibility(View.VISIBLE);
            itemsRelLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getTotalPrice(double price) {
        txtSum.setText(String.valueOf(price) + "$");
    }
}
