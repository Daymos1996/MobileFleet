package drivers;

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
import profile.ProfileActivity;
import users.FindDrivers;
import users.StaticVariable;

public class DriversRecyclerViewAdapter extends RecyclerView.Adapter<DriversRecyclerViewAdapter.ItemViewHolder> {
    private List<FindDrivers> driversList = new ArrayList<>();
    private ArrayList<String> driverInformation = new ArrayList<>();

    private Context mContext;
    private DatabaseReference mRef;


    public DriversRecyclerViewAdapter(Context mContext, DatabaseReference ref, final ArrayList<String> driversIdList) {
        this.mContext = mContext;
        this.mRef = ref;
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driversList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (int i = 0; i < driversIdList.size(); i++) {
                        if (postSnapshot.getKey().equals(driversIdList.get(i))) {
                            FindDrivers driver = new FindDrivers();
                            String firstname = postSnapshot.child("first_name").getValue().toString();
                            String lastname = postSnapshot.child("last_name").getValue().toString();
                            String photo = postSnapshot.child("profilURl").getValue().toString();
                            String phone = postSnapshot.child("phone").getValue().toString();
                            String id = postSnapshot.getKey();
                            String nip = postSnapshot.child("nip").getValue().toString();
                            driver.setFirst_name(firstname);
                            driver.setLast_name(lastname);
                            driver.setProfilURl(photo);
                            driver.setId(id);
                            driver.setPhone(phone);
                            driver.setNip(nip);
                            driversList.add(driver);
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
        view = mInflater.inflate(R.layout.layout_list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.myName.setText(driversList.get(i).getFirst_name()+" "+driversList.get(i).getLast_name());
        Picasso.with(mContext).load(driversList.get(i).getProfilURl()).placeholder(R.drawable.ic_account_box_black_24dp).into(itemViewHolder.myImage);



        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverInformation.add(driversList.get(i).getId());
                driverInformation.add( driversList.get(i).getFirst_name() + " " + driversList.get(i).getLast_name());
                driverInformation.add(driversList.get(i).getPhone());
                driverInformation.add(driversList.get(i).getProfilURl());
                String id = driversList.get(i).getId();
                String nip=driversList.get(i).getNip();
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(StaticVariable.DRIVER_INFORMATION, driverInformation);
                intent.putExtra(StaticVariable.KEY_FRIEND_ID, id);
                intent.putExtra(StaticVariable.KEY_CHAT, id);
                intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
                mContext.startActivity(intent);

            }
        });

        itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String id = driversList.get(i).getId();
                String nip=driversList.get(i).getNip();
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(StaticVariable.KEY_CHAT, id);
                intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
                mContext.startActivity(intent);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return driversList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView myName;
        ImageView myImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            myName = itemView.findViewById(R.id.txtName);
            myImage = itemView.findViewById(R.id.image);
        }

    }
}
