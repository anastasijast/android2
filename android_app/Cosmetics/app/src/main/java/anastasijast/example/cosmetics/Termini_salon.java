package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class Termini_salon extends AppCompatActivity {
    DatabaseReference databaseReference,db,pom;
    Integer i;
    String kluc;
    String[] dat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termini_salon);
        Date date = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd",   date); // 20
        int d=Integer.parseInt(day);
        String month  = (String) DateFormat.format("MM",   date); // 06
        int m=Integer.parseInt(month);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayout layout=findViewById(R.id.termini_s);
        i=0;
        kluc="";
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("termin");
                Iterable<DataSnapshot> children=snapshot1.getChildren();
                for(DataSnapshot ds : children) {
                    if(!ds.child("datum").getValue(String.class).isEmpty()){
                        dat=ds.child("datum").getValue(String.class).split("/");
                    }
                    int datd=Integer.parseInt(dat[0]);
                    int datm=Integer.parseInt(dat[1]);
                    if ((ds.child("salon").getValue(String.class).equals(user)) && (ds.child("status").getValue(Integer.class) == (Integer) 0)
                        && ((datd>=d && datm==m) || (datm>m))){
                        kluc= ds.getKey();
                        Drawable drawable = getDrawable(R.drawable.edit_border);
                        LinearLayout nov = new LinearLayout(getApplicationContext());
                        nov.setBackground(drawable);
                        nov.setOrientation(LinearLayout.VERTICAL);
                        nov.setPadding(10, 15, 10, 10);
                        TextView text = new TextView(getApplicationContext());
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        Button button = new Button(getApplicationContext());
                        button.setText("??????????????");
                        button.setTextColor(Color.parseColor("#424242"));
                        button.setTextSize(10);
                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        Button button1 = new Button(getApplicationContext());
                        LinearLayout.LayoutParams rel_button = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                        rel_button.setMargins(10, 0, 10, 20);
                        button.setLayoutParams(rel_button);
                        button1.setLayoutParams(rel_button);
                        button1.setText("??????????");
                        button1.setTextSize(10);
                        button1.setTextColor(Color.parseColor("#424242"));
                        button1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        text.setTextColor(Color.parseColor("#FFFFFF"));
                        text.setTextSize(20);
                        text.setPadding(25, 10, 10, 20);
                        nov.addView(text);
                        nov.addView(button);
                        nov.addView(button1);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Termini_salon.this, "???????????????? ???? ????????????????!", Toast.LENGTH_SHORT).show();
                                i=1;
                                databaseReference.child("termin").child(kluc).child("status").setValue(i);
                                startActivity(new Intent(getApplicationContext(), Salon.class));
                            }
                        });
                        button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(Termini_salon.this, "???????????????? ???? ???? ????????????????!", Toast.LENGTH_SHORT).show();
                                i=2;
                                databaseReference.child("termin").child(kluc).child("status").setValue(i);
                                startActivity(new Intent(getApplicationContext(), Salon.class));
                            }
                        });
                        layout.addView(nov);
                        db = FirebaseDatabase.getInstance().getReference("users/" + ds.child("korisnik").getValue(String.class));
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                User value = snapshot2.getValue(User.class);
                                String sourceString = "<b>" + ds.child("datum").getValue(String.class) + " ???? " +
                                        ds.child("vreme").getValue(String.class) + " ??????????" +
                                        "</b>" + "<br> ??????: " + value.getName() +
                                        "<br>" + value.getEmail();
                                text.setText(Html.fromHtml(sourceString));
                            }

                            // @Override
                            //public void onCancelled(@NonNull DatabaseError error) {
                            //}
                            //});
                            //}
                            //}

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}