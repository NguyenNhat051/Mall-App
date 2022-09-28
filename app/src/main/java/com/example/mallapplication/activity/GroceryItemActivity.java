package com.example.mallapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView itemImage, firstEmptyStar, firstFilledStar, secondEmptyStar, secondFilledStar,
            thirdEmptyStar, thirdFilledStar, fourthEmptyStar, fourthFilledStar, fifthEmptyStar, fifthFilledStar;
    private Button btnAddToCart;
    private RelativeLayout firstStarRelLayout, secondStarRelLayout, thirdStarRelLayout, fourthStarRelLayout, fifthStarRelLayout;
    private MaterialToolbar toolbar;

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

            handleRating();
        }
    }

    private void handleRating() {
        switch (incomingItem.getRate()) {
            case 0:
                firstEmptyStar.setVisibility(View.VISIBLE);
                firstFilledStar.setVisibility(View.GONE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 1:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.VISIBLE);
                secondFilledStar.setVisibility(View.GONE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 2:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.VISIBLE);
                thirdFilledStar.setVisibility(View.GONE);
                break;
            case 3:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                break;
            case 4:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                fourthEmptyStar.setVisibility(View.GONE);
                fourthFilledStar.setVisibility(View.VISIBLE);
                break;
            case 5:
                firstEmptyStar.setVisibility(View.GONE);
                firstFilledStar.setVisibility(View.VISIBLE);
                secondEmptyStar.setVisibility(View.GONE);
                secondFilledStar.setVisibility(View.VISIBLE);
                thirdEmptyStar.setVisibility(View.GONE);
                thirdFilledStar.setVisibility(View.VISIBLE);
                fourthEmptyStar.setVisibility(View.GONE);
                fourthFilledStar.setVisibility(View.VISIBLE);
                fifthEmptyStar.setVisibility(View.GONE);
                fifthFilledStar.setVisibility(View.VISIBLE);
            default:
                break;
        }

        firstStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 1) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 1);
                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (1 - incomingItem.getRate()) * 2);
                    incomingItem.setRate(1);
                    handleRating();
                }
            }
        });

        secondStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 2) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 2);
                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (2 - incomingItem.getRate()) * 2);
                    incomingItem.setRate(2);
                    handleRating();
                }
            }
        });

        thirdStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 3) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 3);
                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (3 - incomingItem.getRate()) * 2);
                    incomingItem.setRate(3);
                    handleRating();
                }
            }
        });

        fourthStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 4) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 4);
                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (4 - incomingItem.getRate()) * 2);
                    incomingItem.setRate(4);
                    handleRating();
                }
            }
        });

        fifthStarRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomingItem.getRate() != 5) {
                    Utils.changeRate(GroceryItemActivity.this, incomingItem.getId(), 5);
                    Utils.changeUserPoint(GroceryItemActivity.this, incomingItem, (5 - incomingItem.getRate()) * 2);
                    incomingItem.setRate(5);
                    handleRating();
                }
            }
        });
    }

    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddReview = findViewById(R.id.txtAddReview);
        itemImage = findViewById(R.id.itemImage);
        firstEmptyStar = findViewById(R.id.firstEmptyStar);
        firstFilledStar = findViewById(R.id.firstFilledStar);
        secondEmptyStar = findViewById(R.id.secondEmptyStar);
        secondFilledStar = findViewById(R.id.secondFilledStar);
        thirdEmptyStar = findViewById(R.id.thirdEmptyStar);
        thirdFilledStar = findViewById(R.id.thirdFilledStar);
        fourthEmptyStar = findViewById(R.id.fourthEmptyStar);
        fourthFilledStar = findViewById(R.id.fourthFilledStar);
        fifthEmptyStar = findViewById(R.id.fifthEmptyStar);
        fifthFilledStar = findViewById(R.id.fifthFilledStar);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        reviewsRecView = findViewById(R.id.reviewsRecView);
        firstStarRelLayout = findViewById(R.id.firstStarRelLayout);
        secondStarRelLayout = findViewById(R.id.secondStarRelLayout);
        thirdStarRelLayout = findViewById(R.id.thirdStarRelLayout);
        fourthStarRelLayout = findViewById(R.id.fourthStarRelLayout);
        fifthStarRelLayout = findViewById(R.id.fifthStarRelLayout);
        toolbar = findViewById(R.id.toolbar);
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