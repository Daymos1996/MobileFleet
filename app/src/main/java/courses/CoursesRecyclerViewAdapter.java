package courses;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import chat.ChatActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import users.StaticVariable;
import users.courses;

public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.ItemViewHolder> {
    private List<courses> coursesList = new ArrayList<>();
    private ArrayList<String> courseInformation = new ArrayList<>();
    private Context mContext;
    private DatabaseReference mRef;
    private String nip;


    public CoursesRecyclerViewAdapter(Context mContext, DatabaseReference ref, final ArrayList<String> coursesIdArrayList, String nip) {
        this.mContext = mContext;
        this.mRef = ref;
        this.nip=nip;

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coursesList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (int i = 0; i < coursesIdArrayList.size(); i++) {
                        if (postSnapshot.getKey().equals(coursesIdArrayList.get(i))) {
                            courses coursess = new courses();
                            String FirstName = postSnapshot.child("firstName").getValue().toString();
                            String LastName=  postSnapshot.child("lastName").getValue().toString();
                            String fromWhere = postSnapshot.child("fromWhere").getValue().toString();
                            String toWhere = postSnapshot.child("toWhere").getValue().toString();
                            String courseTime = postSnapshot.child("courseTime").getValue().toString();
                            String distance = postSnapshot.child("distance").getValue().toString();
                            String numberInvoice = postSnapshot.child("numberInvoice").getValue().toString();
                            String plateNumber = postSnapshot.child("plateNumber").getValue().toString();
                            String numberOfPallets = postSnapshot.child("numberOfPallets").getValue().toString();
                            String cost = postSnapshot.child("cost").getValue().toString();

                            coursess.setCost(cost);
                            coursess.setCourseTime(courseTime);
                            coursess.setDistance(distance);
                            coursess.setFirstName(FirstName);
                            coursess.setLastName(LastName);
                            coursess.setFromWhere(fromWhere);
                            coursess.setToWhere(toWhere);
                            coursess.setNumberInvoice(numberInvoice);
                            coursess.setPlateNumber(plateNumber);
                            coursess.setNumberOfPallets(numberOfPallets);
                            coursesList.add(coursess);
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
    public CoursesRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.layout_courses_list_item, viewGroup, false);
        return new CoursesRecyclerViewAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CoursesRecyclerViewAdapter.ItemViewHolder itemViewHolder, final int i) {
        itemViewHolder.myName.setText(coursesList.get(i).getFirstName()+" "+coursesList.get(i).getLastName());
        itemViewHolder.courseTime.setText(coursesList.get(i).getCourseTime());
        itemViewHolder.fromWhere.setText(coursesList.get(i).getFromWhere());
        itemViewHolder.toWhere.setText(coursesList.get(i).getToWhere());
        itemViewHolder.cost.setText(coursesList.get(i).getCost() + " zl");
        itemViewHolder.invoiceNumber.setText(coursesList.get(i).getNumberInvoice());
        itemViewHolder.plate.setText(coursesList.get(i).getPlateNumber());



        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseInformation.add(coursesList.get(i).getFirstName());
                courseInformation.add(coursesList.get(i).getLastName());
                courseInformation.add(coursesList.get(i).getCourseTime());
                courseInformation.add(coursesList.get(i).getFromWhere());
                courseInformation.add(coursesList.get(i).getToWhere());
                courseInformation.add(coursesList.get(i).getDistance());
                courseInformation.add(coursesList.get(i).getPlateNumber());
                courseInformation.add(coursesList.get(i).getNumberOfPallets());
                courseInformation.add(coursesList.get(i).getCost());
                courseInformation.add(coursesList.get(i).getNumberInvoice());
                courseInformation.add(nip);


                Intent intent = new Intent(mContext, CourseInformationActivity.class);
                intent.putExtra(StaticVariable.COURSE_INFORMATION, courseInformation);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView myName,courseTime,fromWhere,toWhere,cost,invoiceNumber,plate;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            myName = itemView.findViewById(R.id.txtName);
            courseTime = itemView.findViewById(R.id.courseTime);
            fromWhere = itemView.findViewById(R.id.fromWhere);
            toWhere = itemView.findViewById(R.id.toWhere);
            cost = itemView.findViewById(R.id.cost);
            invoiceNumber = itemView.findViewById(R.id.invoiceNumber);
            plate = itemView.findViewById(R.id.plateNumber);

        }

    }
}
