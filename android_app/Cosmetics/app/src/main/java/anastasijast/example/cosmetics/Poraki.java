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

public class Poraki extends AppCompatActivity {
    DatabaseReference databaseReference,db,pom;
String[] dat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poraki);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        LinearLayout layout=findViewById(R.id.porakil);
        Date date = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd",   date); // 20
        int d=Integer.parseInt(day);
        String month  = (String) DateFormat.format("MM",   date); // 06
        int m=Integer.parseInt(month);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1 = snapshot.child("termin");
                Iterable<DataSnapshot> children = snapshot1.getChildren();
                for (DataSnapshot ds : children) {
                    if (ds.child("korisnik").getValue(String.class).equals(user) && (ds.child("status").getValue(Integer.class) != (Integer) 3)) {
                        db = FirebaseDatabase.getInstance().getReference("users/" + ds.child("salon").getValue(String.class));
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                User value = snapshot2.getValue(User.class);
                                if(!ds.child("datum").getValue(String.class).isEmpty()){
                                    dat=ds.child("datum").getValue(String.class).split("/");
                                }
                                int datd=Integer.parseInt(dat[0]);
                                int datm=Integer.parseInt(dat[1]);
                                if(ds.child("status").getValue(Integer.class) == (Integer) 1){
                                    LinearLayout nov = new LinearLayout(getApplicationContext());
                                    nov.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    nov.setOrientation(LinearLayout.VERTICAL);
                                    nov.setPadding(10, 10, 10, 10);
                                    TextView text = new TextView(getApplicationContext());
                                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    text.setTextColor(Color.parseColor("#424242"));
                                    text.setTextSize(15);
                                    text.setPadding(25, 10, 10, 20);
                                    nov.addView(text);
                                    layout.addView(nov);
                                    String sourceString = "Закажано во "+value.getName()+ " на <b>" + ds.child("datum").getValue(String.class) + " во " +
                                            ds.child("vreme").getValue(String.class) + " часот." +
                                            "</b>" + "<br>" + "Број за контакт: "+ value.getBroj();
                                    text.setText(Html.fromHtml(sourceString));
                                    if((datd<d && datm==m)||(datm<m)) {
                                        nov.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), Rating.class);
                                                String k = value.getName() + "/" + ds.child("salon").getValue(String.class) + "/" + ds.getKey();
                                                intent.putExtra("zaRating", k);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    if((datd>d && datm==m)||(datm>m)){
                                        Button button = new Button(getApplicationContext());
                                        button.setText("Откажи термин");
                                        button.setTextColor(Color.parseColor("#424242"));
                                        button.setTextSize(10);
                                        button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                        LinearLayout.LayoutParams rel_button = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                                        rel_button.setMargins(10, 0, 10, 20);
                                        button.setLayoutParams(rel_button);
                                        nov.addView(button);
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                pom= FirebaseDatabase.getInstance().getReference("termin/"+ds.getKey());
                                                pom.removeValue();
                                                startActivity(new Intent(getApplicationContext(),Welcome.class));
                                            }
                                        });
                                    }
                                }
                                if(ds.child("status").getValue(Integer.class) == (Integer) 2){
                                    LinearLayout nov = new LinearLayout(getApplicationContext());
                                    nov.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    nov.setOrientation(LinearLayout.VERTICAL);
                                    nov.setPadding(10, 10, 10, 10);
                                    TextView text = new TextView(getApplicationContext());
                                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    text.setTextColor(Color.parseColor("#424242"));
                                    text.setTextSize(15);
                                    text.setPadding(25, 10, 10, 20);
                                    nov.addView(text);
                                    layout.addView(nov);
                                    String sourceString = "Одбиен термин во "+value.getName()+ " на <b>" + ds.child("datum").getValue(String.class) + " во " +
                                            ds.child("vreme").getValue(String.class) + " часот." +
                                            "</b>" + "<br>";
                                    text.setText(Html.fromHtml(sourceString));
                                    nov.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), Odbieno.class);
                                            String k =ds.getKey();
                                            intent.putExtra("zaOdbieno", k);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                if((ds.child("status").getValue(Integer.class) == (Integer) 0) && ((datd<d && datm==m)||(datm<m))){
                                    LinearLayout nov = new LinearLayout(getApplicationContext());
                                    nov.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    nov.setOrientation(LinearLayout.VERTICAL);
                                    nov.setPadding(10, 10, 10, 10);
                                    TextView text = new TextView(getApplicationContext());
                                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    text.setTextColor(Color.parseColor("#424242"));
                                    text.setTextSize(15);
                                    text.setPadding(25, 10, 10, 20);
                                    nov.addView(text);
                                    layout.addView(nov);
                                    String sourceString = "Терминот во "+value.getName()+ " на <b>" + ds.child("datum").getValue(String.class) + " во " +
                                            ds.child("vreme").getValue(String.class) + " часот" +
                                            "</b>" + " не е активен.";
                                    text.setText(Html.fromHtml(sourceString));
                                    nov.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), Odbieno.class);
                                            String k =ds.getKey();
                                            intent.putExtra("zaOdbieno", k);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }

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