package chat;

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

import daymos.lodz.uni.math.pl.mobilefleet.R;
import profile.ProfileActivity;
import users.FindDrivers;
import users.StaticVariable;

public class ChatFriendsRecyclerViewAdapter extends RecyclerView.Adapter<ChatFriendsRecyclerViewAdapter.ItemViewHolder> {
    private List<FindDrivers> chatFriendlist = new ArrayList<>();
    private Context mContext;
    private DatabaseReference mRef;


    public ChatFriendsRecyclerViewAdapter(Context mContext, DatabaseReference ref, final ArrayList<String> chatFriendsIdList) {
        this.mContext = mContext;
        this.mRef = ref;
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatFriendlist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (int i = 0; i < chatFriendsIdList.size(); i++) {
                        if (postSnapshot.getKey().equals(chatFriendsIdList.get(i))) {
                            FindDrivers employee = new FindDrivers();
                            String FirstName = postSnapshot.child("first_name").getValue().toString();
                            String LastName=  postSnapshot.child("last_name").getValue().toString();
                            String photo = postSnapshot.child("profilURl").getValue().toString();
                            String id = postSnapshot.getKey();
                            String nip = postSnapshot.child("nip").getValue().toString();
                            employee.setFirst_name(FirstName);
                            employee.setLast_name(LastName);
                            employee.setProfilURl(photo);
                            employee.setId(id);
                            employee.setNip(nip);
                            chatFriendlist.add(employee);
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
        itemViewHolder.myName.setText(chatFriendlist.get(i).getFirst_name()+" "+chatFriendlist.get(i).getLast_name());
        Picasso.with(mContext).load(chatFriendlist.get(i).getProfilURl()).placeholder(R.drawable.com_facebook_profile_picture_blank_portrait).into(itemViewHolder.myImage);
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = chatFriendlist.get(i).getId();
                String nip=chatFriendlist.get(i).getNip();
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(StaticVariable.KEY_CHAT, id);
                intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return chatFriendlist.size();
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
