package Cars;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import chat.ChatActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversRecyclerViewAdapter;
import profile.ProfileActivity;
import users.FindCars;
import users.FindDrivers;
import users.StaticVariable;

public class CarsRecyclerViewAdapter extends RecyclerView.Adapter<CarsRecyclerViewAdapter.ItemViewHolder> {
    private List<FindCars> carsList = new ArrayList<>();
    private ArrayList<String> carInformation = new ArrayList<>();

    private Context mContext;
    private DatabaseReference mRef;


    public CarsRecyclerViewAdapter(Context mContext, DatabaseReference ref, final ArrayList<String> carsIdList, final String nip) {
        this.mContext = mContext;
        this.mRef = ref;
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (int i = 0; i < carsIdList.size(); i++) {
                        if (postSnapshot.getKey().equals(carsIdList.get(i))) {
                            FindCars cars = new FindCars();

                            String carUrl = postSnapshot.child("carUrl").getValue().toString();
                            String carBrand = postSnapshot.child("carBrand").getValue().toString();
                            String plateNumber = postSnapshot.child("plateNumber").getValue().toString();
                            String vinNumber = postSnapshot.child("vinNumber").getValue().toString();
                            String carMileage = postSnapshot.child("carMileage").getValue().toString();
                            String engineCapacity = postSnapshot.child("engineCapacity").getValue().toString();
                            String motorPower = postSnapshot.child("motorPower").getValue().toString();
                            String yearProduction = postSnapshot.child("yearProduction").getValue().toString();
                            String technicalExamination = postSnapshot.child("technicalExamination").getValue().toString();
                            String oc = postSnapshot.child("oc").getValue().toString();

                            cars.setCarUrl(carUrl);
                            cars.setCarBrand(carBrand);
                            cars.setPlateNumber(plateNumber);
                            cars.setVinNumber(vinNumber);
                            cars.setCarMileage(carMileage);
                            cars.setEngineCapacity(engineCapacity);
                            cars.setMotorPower(motorPower);
                            cars.setYearProduction(yearProduction);
                            cars.setTechnicalExamination(technicalExamination);
                            cars.setOc(oc);
                            cars.setNip(nip);

                            carsList.add(cars);
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_car_list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.myName.setText(carsList.get(i).getCarBrand());
        itemViewHolder.myPlateNumber.setText(carsList.get(i).getPlateNumber());
        Picasso.with(mContext).load(carsList.get(i).getCarUrl()).placeholder(R.drawable.ic_account_box_black_24dp).into(itemViewHolder.myImage);



        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carInformation.add(carsList.get(i).getCarUrl());
                carInformation.add(carsList.get(i).getCarBrand());
                carInformation.add(carsList.get(i).getPlateNumber());
                carInformation.add(carsList.get(i).getVinNumber());
                carInformation.add(carsList.get(i).getCarMileage());
                carInformation.add(carsList.get(i).getEngineCapacity());
                carInformation.add(carsList.get(i).getMotorPower());
                carInformation.add(carsList.get(i).getYearProduction());
                carInformation.add(carsList.get(i).getTechnicalExamination()) ;
                carInformation.add(carsList.get(i).getOc());
                carInformation.add(carsList.get(i).getNip());

                // String id = driversList.get(i).getId();
                // String nip=driversList.get(i).getNip();
                Intent intent = new Intent(mContext, CarInformationActivity.class);
                intent.putExtra(StaticVariable.CAR_INFORMATION, carInformation);
               // intent.putExtra(StaticVariable.KEY_FRIEND_ID, id);
               // intent.putExtra(StaticVariable.KEY_CHAT, id);
               // intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return carsList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView myName;
        TextView myPlateNumber;
        ImageView myImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            myPlateNumber=itemView.findViewById(R.id.txtPlateNumber);
            myName = itemView.findViewById(R.id.txtName);
            myImage = itemView.findViewById(R.id.image);
        }
    }
}