package com.example.medicare_pharmacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.medicare_pharmacyapp.Model.Stock;
import com.example.medicare_pharmacyapp.ViewHolder.CartViewHolder;
import com.example.medicare_pharmacyapp.ViewHolder.StockViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Check_Stock extends AppCompatActivity {

    private RecyclerView stockList;
    private DatabaseReference ordersRef;
    FloatingActionButton check2;
    private Button editBtn,delBtn;
    private String getText;

    private String productID = "";
    private DatabaseReference productsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__stock);
        productID=getIntent().getStringExtra("pid");


        ordersRef = FirebaseDatabase.getInstance().getReference().child("Stock");
        stockList = findViewById(R.id.stock_list);
        stockList.setLayoutManager(new LinearLayoutManager(this));
        check2 = findViewById(R.id.fab);
        editBtn = (Button) findViewById(R.id.edit_stock_btn);






        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Check_Stock.this, Check_Stock.class);
                startActivity(intent1);

            }
        });




    }



    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<Stock> options =
                new FirebaseRecyclerOptions.Builder<Stock>()
                        .setQuery(ordersRef, Stock.class)
                        .build();

        FirebaseRecyclerAdapter<Stock, StockViewHolder> adapter =
                new FirebaseRecyclerAdapter<Stock, StockViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StockViewHolder stockViewHolder, final int option, @NonNull Stock stock) {

                        stockViewHolder.txtProductName.setText("Name: " + stock.getPname());
                        stockViewHolder.txtProductPrice.setText("price of one product: " + stock.getPrice() + "LKR");
                        stockViewHolder.txtProductQuantity.setText("Quantity: " + stock.getQuantity());
                        stockViewHolder.date.setText("Stored date , time : " +stock.getDate()+","+stock.getTime());
                        stockViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String pID = getRef(option).getKey();

                                Intent intent = new Intent(Check_Stock.this, editStock.class);
                                intent.putExtra("pid", pID);
                                startActivity(intent);

                            }
                        });


                        stockViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{

                                        "Yes",
                                        "No"


                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(Check_Stock.this);
                                builder.setTitle("Do you want to delete this  ?");


                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(i == 0){

                                            String pID = getRef(option).getKey();
                                            RemoveOrder(pID);

                                        }else {
                                            finish();
                                        }

                                    }
                                });

                                builder.show();

                            }
                        });

                    }





                    @NonNull
                    @Override
                    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_layout, parent, false);
                        StockViewHolder holder = new StockViewHolder(view);

                        return holder;

                    }
                };


        stockList.setAdapter(adapter);
        adapter.startListening();

    }

    private void RemoveOrder(String pID) {


        ordersRef.child(pID).removeValue();

    }
}