package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Salon extends AppCompatActivity {

    Button btn;
    Button termini,zak;
    TextView podatoci,naslov,zv;
    DatabaseReference databaseReference,db,dbRating;
    static Float pr,brojac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pr= (float)0.0;
        brojac=(float)0.0;
        setContentView(R.layout.activity_salon);
        zv=findViewById(R.id.zvezdi);
        termini=findViewById(R.id.termini);
        zak=findViewById(R.id.termini_zak);
        podatoci=findViewById(R.id.salon_podatok);
        naslov=findViewById(R.id.salon_naslov);
        btn=findViewById(R.id.odjavi_s);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        dbRating=FirebaseDatabase.getInstance().getReference();
        FirebaseUser user_new = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+user_new.getUid());
        db=FirebaseDatabase.getInstance().getReference("salon/"+user_new.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User value=snapshot.getValue(User.class);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        Salon_user su=snapshot1.getValue(Salon_user.class);
                        dbRating.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                DataSnapshot snapshot3 = snapshot2.child("rating");
                                Iterable<DataSnapshot> children = snapshot3.getChildren();
                                for (DataSnapshot ds : children) {
                                    if (ds.child("salonID").getValue(String.class).equals(user_new.getUid().toString())) {
                                        pr = pr + ds.child("rejting").getValue(Float.class);
                                        brojac++;
                                    }
                                }
                                if (pr == 0 && brojac == 0) {
                                    pr = (float) 0.0;
                                } else {
                                    pr = pr / brojac;
                                }
                                naslov.setText(value.getName());
                                zv.setText("★"+pr.toString());
                                String source="<b> Салон за </b>"+value.getKategorija()+"<br><b>Емаил:</b> "+value.getEmail()+
                                        "<br><b>Телефонски број: </b>"+value.getBroj()+"<br><b>Локација: </b><br>"
                                        +su.getLokacija()+"<br><b>Работно време: </b><br>"+su.getRabotno();
                                podatoci.setText(Html.fromHtml(source));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        termini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Termini_salon.class));
            }
        });
        zak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Zakazani_termini.class));
            }
        });
    }
}