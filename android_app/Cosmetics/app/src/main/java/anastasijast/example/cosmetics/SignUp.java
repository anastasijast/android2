package anastasijast.example.cosmetics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    int i;
    Button btn;
    User user;
    Spinner sp;
    String kategorija,email,password,lok,ime,broj,vreme;
    EditText im,br,rvreme;
    EditText em,pass;
    TextView lokacija;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=0;
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        im=findViewById(R.id.ime_signup);
        br=findViewById(R.id.broj_signup);
        em=findViewById(R.id.email_signup);
        pass=findViewById(R.id.lozinka_signup);
        btn=findViewById(R.id.registrira);
        sp=findViewById(R.id.spinner_signup);
        rvreme=findViewById(R.id.rvreme);
        lokacija=findViewById(R.id.lokacija);
        lokacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Map.class));
            }
        });
        sp.setPrompt("Студиото нуди услуги за:");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ime=im.getText().toString();
                broj=br.getText().toString();
                vreme=rvreme.getText().toString();
                kategorija=(String)sp.getSelectedItem();
                email=em.getText().toString();
                password=pass.getText().toString();
                lok=lokacija.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = new User(ime, email, password, broj, kategorija);
                            FirebaseUser user_new = mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference("users").child(user_new.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        i=1;
                                    }
                                    else {
                                        Toast.makeText(SignUp.this,"Неуспешно!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Salon_user dopolni=new Salon_user(vreme,lok);
                            FirebaseDatabase.getInstance().getReference("salon").child(user_new.getUid()).setValue(dopolni).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(i==1){
                                            Toast.makeText(SignUp.this,"Успешна регистрација!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),Email.class));
                                        }
                                    }
                                    else {
                                        Toast.makeText(SignUp.this,"Неуспешно!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(SignUp.this, "Неуспешно!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
    }
    public void onResume(){
        super.onResume();
        lokacija.setText(getIntent().getStringExtra("mytext"));

    }
}