package com.example.mallapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mallapplication.Database.GroceryItem;
import com.example.mallapplication.R;
import com.example.mallapplication.Review;
import com.example.mallapplication.Utils;
import com.example.mallapplication.adapter.ReviewAdapter;
import com.example.mallapplication.dialog.AddReviewDialog;
import com.example.mallapplication.services.TrackUserTime;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class GroceryItemActivity extends AppCompatActivity implements AddReviewDialog.AddReview {

    private boolean isBound = false;
    private TrackUserTime mService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackUserTime.LocalBinder binder = (TrackUserTime.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
            mService.setItem(incomingItem);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    public static final String GROCERY_ITEM_KEY = "GROCERY_ITEM_KEY";

    private RecyclerView reviewsRecView;
    private TextView txtName, txtPrice, txtDescription, txtAddReview;
    private ImageView itemImage;
    private Button btnAddToCart;
    private MaterialToolbar toolbar;
    private RatingBar ratingBar;

    private GroceryItem incomingItem;

    ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_item);

        initView();

        setSupportActionBar(toolbar);

        adapter = new ReviewAdapter();

        Intent intent = getIntent();
        if (null != intent) {
            incomingItem = intent.getParcelableExtra(GROCERY_ITEM_KEY);
            if (null != incomingItem) {
                Utils.changeUserPoint(this, incomingItem, 1);
                txtName.setText(incomingItem.getName());
                txtDescription.setText(incomingItem.getDescription());
                txtPrice.setText(String.valueOf(incomingItem.getPrice()) + "$");
                Glide.with(this)
                        .asBitmap()
                        .load(incomingItem.getImageUrl())
                        .into(itemImage);
            }

            ArrayList<Review> reviews = Utils.getReviewById(this, incomingItem.getId());

            reviewsRecView.setAdapter(adapter);
            reviewsRecView.setLayoutManager(new LinearLayoutManager(this));
            if (null != reviews) {
                if (reviews.size() > 0) {
                    adapter.setReviews(reviews);
                }
            }

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.addItemToCart(GroceryItemActivity.this, incomingItem);
                    Intent cartIntent = new Intent(GroceryItemActivity.this, CartActivity.class);
                    cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cartIntent);
                }
            });

            txtAddReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddReviewDialog dialog = new AddReviewDialog();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(GROCERY_ITEM_KEY, incomingItem);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "add review");
                }
            });

            ratingBar.setRating(incomingItem.getRate());

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), (int) rating);
                        Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, ((int) rating - incomingItem.getRate()) * 2);
                        incomingItem.setRate((int) rating);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                }
            });
        }
    }


    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddReview = findViewById(R.id.txtAddReview);
        itemImage = findViewById(R.id.itemImage);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        reviewsRecView = findViewById(R.id.reviewsRecView);
        toolbar = findViewById(R.id.toolbar);
        ratingBar = findViewById(R.id.ratingBar);
    }

    @Override
    public void onAddReviewResult(Review review) {
        Utils.addReview(this, review);
        Utils.changeUserPoint(this, incomingItem, 3);
        adapter.setReviews(Utils.getReviewById(this, incomingItem.getId()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, TrackUserTime.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(serviceConnection);
        }
    }
}