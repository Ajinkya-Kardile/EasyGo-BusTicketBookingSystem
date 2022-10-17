package com.ajinkya.easygo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.easygo.adapters.PassengerListAdapter;
import com.ajinkya.easygo.model.PassengerInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;

public class TicketBooking extends AppCompatActivity implements PaymentStatusListener {

    private TextView BT_BusNumber, BK_Date, BT_FromLocation, BT_ToLocation, BT_StartTime, BT_EndTime, BT_SeatAvailable, BT_BusType, BT_TotalPrice;
    private EditText BT_PassengerName, BT_PhoneNo;
    private Button btnPay, btnAddPassenger;
    private RecyclerView recyclerView;
    private ArrayList<PassengerInfo> arrayList;


    private String BusNumber;
    private String Date;
    private String FromLocation;
    private String ToLocation;
    private String startTime;
    private String endTime;
    private String seatAvailable;
    private String BusType;
    private String TicketPrice;

    private String TransactionDetails;

    private final String UPI = "8308679079202@paytm";
    private EasyUpiPayment easyUpiPayment;


    private DatabaseReference seatNo;
    private int no_of_seat;

    PassengerInfo passengerInfo;
    String str_total_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_booking);


        Instantiate();
        GetStringFromIntent();
        ProgressDialog configure = new ProgressDialog(this);
        configure.setCancelable(false);
        configure.setTitle("Configuring Environment");
        configure.show();
        SetStringToTextView(configure);
        //GetNoOFPlace();


        //Here I have created arraylist to call the data
        arrayList = new ArrayList<>();

        PassengerListAdapter adapter = new PassengerListAdapter(arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        btnAddPassenger.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                String passengerName = BT_PassengerName.getText().toString();
                String phoneNo = BT_PhoneNo.getText().toString();
                if (!passengerName.equals("") && !phoneNo.equals("")) {
                    passengerInfo = new PassengerInfo();
                    passengerInfo.setPassengerName(passengerName);
                    passengerInfo.setPhoneNumber(phoneNo);
                    arrayList.add(passengerInfo);
                    adapter.notifyDataSetChanged();
                    BT_PassengerName.setText("");
                    BT_PhoneNo.setText("");
                    calculate_price();
                } else
                    Toast.makeText(TicketBooking.this, "Please fill Passenger Details", Toast.LENGTH_SHORT).show();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!arrayList.isEmpty()) {
                    no_of_seat = Integer.parseInt(seatAvailable);
                    if (no_of_seat <= 0) {
                        Toast.makeText(TicketBooking.this, "Sorry seats are full", Toast.LENGTH_LONG).show();
                    } else {
                        MakePayment();
                    }

                } else {
                    Toast.makeText(TicketBooking.this, "Please select some places", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    private void MakePayment() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        assert user != null;
//        String UserId = user.getUid();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
        String transactionId = df.format(c);;


        PaymentApp paymentApp = PaymentApp.ALL;


        // START PAYMENT INITIALIZATION
        EasyUpiPayment.Builder builder = new EasyUpiPayment.Builder(this)
                .with(paymentApp)
                .setPayeeVpa(UPI)
//                .setPayeeMerchantCode("ZzJyZQ32070723294346")
                .setPayeeMerchantCode("5734")
                .setPayeeName("EasyGO")
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionId)
                .setDescription("Ticket Payment")
                .setAmount("50.00");
        // END INITIALIZATION
        try {
            // Build instance
            easyUpiPayment = builder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(this, "Error: "+ exception.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void Instantiate() {
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("EasyGo");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        BT_BusNumber = findViewById(R.id.BT_BusNumber);
        BK_Date = findViewById(R.id.BT_date);
        BT_FromLocation = findViewById(R.id.BT_FromLocation);
        BT_ToLocation =  findViewById(R.id.BT_ToLocation);
        BT_StartTime = findViewById(R.id.BT_StartTime);
        BT_EndTime = findViewById(R.id.BT_EndTime);
        BT_SeatAvailable = findViewById(R.id.BT_SeatAvailable);
        BT_BusType = findViewById(R.id.BT_BusType);
        BT_PassengerName = findViewById(R.id.BT_PassengerName);
        BT_PhoneNo = findViewById(R.id.BT_passengerPhone);
        btnAddPassenger = findViewById(R.id.BT_AddPassenger);
        recyclerView = findViewById(R.id.BT_RecyclerView);
        BT_TotalPrice = findViewById(R.id.BT_TotalPrice);
        btnPay = findViewById(R.id.BT_PayBtn);

    }




    private void GetStringFromIntent() {
        BusNumber = getIntent().getStringExtra("BusNo");
        Date = getIntent().getStringExtra("Date");
        FromLocation = getIntent().getStringExtra("FromLocation");
        ToLocation = getIntent().getStringExtra("ToLocation");
        startTime = getIntent().getStringExtra("StartTime");
        endTime = getIntent().getStringExtra("EndTime");
        seatAvailable = getIntent().getStringExtra("NumberOfSeat");
        BusType = getIntent().getStringExtra("BusType");
        TicketPrice = getIntent().getStringExtra("Price");

        String seatNoIDReference = FromLocation + ToLocation;
        seatNo = FirebaseDatabase.getInstance().getReference().child("Buses").child(Date).child(seatNoIDReference).child(BusNumber);

    }


    @SuppressLint("SetTextI18n")
    private void SetStringToTextView(ProgressDialog configure) {
        BT_BusNumber.setText(BusNumber);
        BK_Date.setText(Date);
        BT_FromLocation.setText(FromLocation);
        BT_ToLocation.setText(ToLocation);
        BT_StartTime.setText(startTime);
        BT_EndTime.setText(endTime);
        BT_SeatAvailable.setText(seatAvailable);
        BT_BusType.setText(BusType);
        BT_TotalPrice.setText(TicketPrice+"/-");
        configure.dismiss();
    }




    @SuppressLint("SetTextI18n")
    private void calculate_price() {
        int number_of_traveller = arrayList.size();
        int Price = Integer.parseInt(TicketPrice);
        int total_price = Price * number_of_traveller;

        str_total_price = Integer.toString(total_price);
        BT_TotalPrice.setText(str_total_price+"/-");
    }

    private void Book_Ticket() {
        if (no_of_seat <= 0) {
            Toast.makeText(this, "Sorry, Seats Not Available...", Toast.LENGTH_LONG).show();
        } else {




            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String User = user.getUid();
            String BusInfo = BusNumber+","+FromLocation+","+ToLocation+","+startTime+","+endTime;


            DatabaseReference AdminTicketList = FirebaseDatabase.getInstance().getReference().
                    child("Tickets").child("AdminSideCheck").child(Date).child(BusInfo);

            DatabaseReference usersTicketList = FirebaseDatabase.getInstance().getReference().
                    child("Tickets").child("UserSideCheck").child(User);


            //Here is the code to store AdminTicketList
            int Length = arrayList.size();
            for (int a = 0; a < Length; a++) {
                HashMap<String, Object> passengerInformation = new HashMap<>();
                passengerInformation.put("PassengerName", arrayList.get(a).getPassengerName());
                passengerInformation.put("PassengerPhone", arrayList.get(a).getPhoneNumber());
                String seatNo =String.valueOf(Integer.parseInt(seatAvailable)-a);
                passengerInformation.put("PassengerSeatNo", seatNo);
                AdminTicketList.child(User+(seatNo)).updateChildren(passengerInformation);
            }


            //Here is the code to store UserTicketList
            for (int a = 0; a < Length; a++) {
                HashMap<String, Object> ticketInformation = new HashMap<>();
                ticketInformation.put("PassengerName", arrayList.get(a).getPassengerName());
                ticketInformation.put("PassengerPhone", arrayList.get(a).getPhoneNumber());
                ticketInformation.put("BusNumber", BusNumber);
                ticketInformation.put("Date", Date);
                ticketInformation.put("From", FromLocation);
                ticketInformation.put("TO", ToLocation);
                ticketInformation.put("StartTime", startTime);
                ticketInformation.put("EndTime", endTime);
                ticketInformation.put("BusType", BusType);
                String seatNo =String.valueOf(Integer.parseInt(seatAvailable)-a);
                ticketInformation.put("SeatNo", seatNo);
                usersTicketList.child(BusNumber+FromLocation+ToLocation+seatNo).updateChildren(ticketInformation);
            }



            int new_val = Integer.parseInt(seatAvailable) - arrayList.size();
            String st = Integer.toString(new_val);
            Map<String, Object> seat = new HashMap<>();
            seat.put("NumberOfSeat", st);
            seatNo.updateChildren(seat).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent in = new Intent(TicketBooking.this, MyTickets.class);
                        startActivity(in);
                        finish();
                    }
                }
            });


            

        }


    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(this, "Cancelled By User", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCompleted(@NonNull TransactionDetails transactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString());
        TransactionDetails = transactionDetails.toString();
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    private void onTransactionSuccess() {
        // Payment Success
        Toast.makeText(this, "Transaction Completed Successfully...", Toast.LENGTH_LONG).show();
        Book_Ticket();
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_LONG).show();
    }

    private void onTransactionFailed() {
        // Payment Failed
        Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
    }
}