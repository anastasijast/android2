package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Saloni extends AppCompatActivity {
    DatabaseReference databaseReference;
    String kat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloni);
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        kat=getIntent().getStringExtra("zaKat");
        LinearLayout layout=findViewById(R.id.saloni);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshot1=snapshot.child("users");
                Iterable<DataSnapshot> children=snapshot1.getChildren();
                for(DataSnapshot ds : children) {
                    if(!ds.child("kategorija").getValue(String.class).equals("korisnik") &&ds.child("kategorija").getValue(String.class).equals(kat)){
                        Drawable drawable=getDrawable(R.drawable.edit_border);
                        LinearLayout nov=new LinearLayout(getApplicationContext());
                        nov.setBackground(drawable);
                        nov.setOrientation(LinearLayout.VERTICAL);
                        nov.setPadding(10,10,10,10);
                        TextView text=new TextView(getApplicationContext());
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        Button button=new Button(getApplicationContext());
                        button.setText("Повеќе");
                        button.setTextSize(8);
                        button.setTextColor(Color.parseColor("#FFFFFF"));
                        button.setBackgroundColor(Color.parseColor("#808080"));
                        LinearLayout.LayoutParams rel_button = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                        rel_button.setMargins(10, 0, 10, 20);
                        button.setLayoutParams(rel_button);
                        text.setTextColor(Color.parseColor("#FFFFFF"));
                        text.setTextSize(15);
                        text.setPadding(20,10,10,15);
                        String sourceString = "<b>"+ds.child("name").getValue(String.class)+"</b>"+
                                "<br>Салон за <b>"+ds.child("kategorija").getValue(String.class)+"</b>";
                        text.setText(Html.fromHtml(sourceString));
                        nov.addView(text);
                        nov.addView(button);
                        layout.addView(nov);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(),Salon_pregled.class);
                                intent.putExtra("salon",ds.getKey());
                                startActivity(intent);
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