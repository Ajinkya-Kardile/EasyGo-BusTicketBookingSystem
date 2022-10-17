package com.ajinkya.easygo;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.easygo.model.BusModel;
import com.ajinkya.easygo.model.IDs;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;


    Calendar myCalendar = Calendar.getInstance();
    TextView FromLocation, ToLocation;
    EditText TvDate;
    Dialog dialog;
    List<IDs> iDs;


    private DatabaseReference database, databaseReference;
    private Button SearchBtn;
    private ProgressDialog progressDialog;
    private String toLocPin, fromLocPin, toLocation,fromLocation, FinalDate, path;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Initialize();
        FirebaseDataRetrieve();
        Buttons();

    }

    private void Buttons(){
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!ToLocation.getText().toString().isEmpty()&&!FromLocation.getText().toString().isEmpty()){

                    if(!toLocPin.equals(fromLocPin)){
                        Searching();
                    }else{
                        Toast.makeText(HomeActivity.this,"Location is repeated",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(HomeActivity.this,"Please fill Locations",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void Initialize(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("EasyGo");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);


        mDrawer = findViewById(R.id.drawer);
        nvDrawer = findViewById(R.id.navMenu);
        FromLocation = findViewById(R.id.FromLocation);
        ToLocation = findViewById(R.id.ToLocation);
        TvDate = findViewById(R.id.Date);
        SearchBtn = findViewById(R.id.SearchBus);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Configuring Environment");
        progressDialog.setCancelable(false);
        progressDialog.show();


        //firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.e(TAG, "Initialize: currentUser"+ firebaseUser);
        String userId = firebaseUser.getUid();
        database = FirebaseDatabase.getInstance("https://book-my-ticket-ebe24-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                .child("Users").child(userId);
        Log.e(TAG, "Initialize: database "+database );
        setDateToEditText(TvDate);

        try {
            setupDrawerContent(nvDrawer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);


        recyclerView = findViewById(R.id.MNblload);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(gridLayoutManager);

    }
    private void FirebaseDataRetrieve(){
        DatabaseReference locationListRef = FirebaseDatabase.getInstance().getReference("Locations");
        Log.e(TAG, "FirebaseDataRetrieve: locationListRef "+ locationListRef);

        locationListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                iDs = new ArrayList<>();

                for (DataSnapshot idSnapShot:dataSnapshot.getChildren()){
                    iDs.add(idSnapShot.getValue(IDs.class));
                }

                ArrayList<String> locationList = new ArrayList<>();
                for (IDs id: iDs){
                    locationList.add(id.getPlace());
                }
                searchableSpinner(FromLocation, locationList, "fromLoc");
                searchableSpinner(ToLocation, locationList, "toLoc");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: "+ databaseError.getMessage() );
                Toast.makeText(HomeActivity.this, "something wrong...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

        });

    }





    public void Searching(){
        if(TvDate.getText().toString().isEmpty()){
            Toast.makeText(HomeActivity.this,"Please select a specific date",Toast.LENGTH_LONG).show();
            //User should select a specific date.

        }else{

            path = fromLocation + toLocation;
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Buses").child(FinalDate).child(path);
            Log.e(TAG, "Searching: databaseReference "+ databaseReference);

            FirebaseRecyclerOptions<BusModel> options =
                    new FirebaseRecyclerOptions.Builder<BusModel>()
                            .setQuery(databaseReference, BusModel.class)
                            .build();

            FirebaseRecyclerAdapter<BusModel, BusInfoModel> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BusModel, BusInfoModel>(options) {
                @NonNull
                @Override
                public BusInfoModel onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.bus_loading, parent, false);

                    return new BusInfoModel(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull BusInfoModel holder, int position, @NonNull BusModel model) {


                    final String key  = getRef(position).getKey();
                    Log.e(TAG, "onBindViewHolder: key "+key );


                    assert key != null;
                    databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild("ToLocation") && dataSnapshot.hasChild("StartTime")
                                    &&dataSnapshot.hasChild("FromLocation") && dataSnapshot.hasChild("BusNo")
                                    && dataSnapshot.hasChild("Date")&&dataSnapshot.hasChild("EndTime") && dataSnapshot.hasChild("BusType")){

                                Log.e(TAG, "onDataChange: data is available" );
                                final String toLocation = Objects.requireNonNull(dataSnapshot.child("ToLocation").getValue()).toString();
                                final String endTime = Objects.requireNonNull(dataSnapshot.child("EndTime").getValue()).toString();
                                final String fromLocation = Objects.requireNonNull(dataSnapshot.child("FromLocation").getValue()).toString();
                                final String BusNo = Objects.requireNonNull(dataSnapshot.child("BusNo").getValue()).toString();
                                final String Date = Objects.requireNonNull(dataSnapshot.child("Date").getValue()).toString();
                                final String startTime = Objects.requireNonNull(dataSnapshot.child("StartTime").getValue()).toString();
                                final String TypeSit = Objects.requireNonNull(dataSnapshot.child("BusType").getValue()).toString();
                                final String NoOfSit = Objects.requireNonNull(dataSnapshot.child("NumberOfSeat").getValue()).toString();
                                final String Ticket_price = Objects.requireNonNull(dataSnapshot.child("TicketPrice").getValue()).toString();

                                holder.settextBusno(BusNo);
                                holder.settextDate(Date);
                                holder.settextLocationEnd(toLocation);
                                holder.settextLocationStart(fromLocation);
                                holder.settextSeatType(TypeSit);
                                holder.settextTimeStart(startTime);
                                holder.setTextTimeEnd(endTime);
                                holder.settextPrice(Ticket_price);



                                holder.blCard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent in = new Intent(HomeActivity.this,TicketBooking.class);
                                        in.putExtra("EndTime",endTime);
                                        in.putExtra("StartTime",startTime);
                                        in.putExtra("BusNo",BusNo);
                                        in.putExtra("NumberOfSeat",NoOfSit);
                                        in.putExtra("BusType",TypeSit);
                                        in.putExtra("FromLocation", fromLocation);
                                        in.putExtra("ToLocation", toLocation);
                                        in.putExtra("Date", Date);
                                        in.putExtra("Price",Ticket_price);
                                        startActivity(in);
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled: "+databaseError.getMessage() );

                        }
                    });
                }
            };
            firebaseRecyclerAdapter.startListening();
            recyclerView.setAdapter(firebaseRecyclerAdapter);


        }



    }



    private void setDateToEditText(EditText editText) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                FinalDate = dateFormat.format(myCalendar.getTime());
                editText.setText(FinalDate);


            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(HomeActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }



    private void searchableSpinner(TextView textView, ArrayList<String> stringArrayList, String spinnerId){
        textView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dialog=new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(800,1200);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter= new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_list_item_1, stringArrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        textView.setText(adapter.getItem(position));

                        if(spinnerId.equals("toLoc")){
                            IDs iD = iDs.get(position);
                            toLocPin = iD.getLocation_pin();
                            toLocation = iD.getPlace();
                        }else if(spinnerId.equals("fromLoc")){
                            IDs iD = iDs.get(position);
                            fromLocPin = iD.getLocation_pin();
                            fromLocation = iD.getPlace();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }


    private void setupDrawerContent(NavigationView navigationView) throws FileNotFoundException {

        View headerView = nvDrawer.getHeaderView(0);
        CircleImageView imageView_Profile = headerView.findViewById(R.id.profileImg);
        TextView UserName = headerView.findViewById(R.id.TvUserName);
        TextView UserMail = headerView.findViewById(R.id.TvUserEmail);

        //get user info from Google Firebase
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                String email = Objects.requireNonNull(dataSnapshot.child("Email").getValue()).toString();
                UserName.setText(name);
                UserMail.setText(email);
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);

                        return true;
                    }

                });

    }

    @SuppressLint("NonConstantResourceId")
    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.searchBus:
                break;
            case R.id.MyTickets:
                Intent in = new Intent(HomeActivity.this, MyTickets.class);
                startActivity(in);
                break;
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                Intent in1 = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(in1);
                break;
        }

        menuItem.setChecked(false);
        mDrawer.closeDrawers();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



