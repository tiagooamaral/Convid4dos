package com.webplanet.convid4dos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webplanet.convid4dos.adapters.GuestListAdapter;
import com.webplanet.convid4dos.model.Guest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AddGuestActivity extends AppCompatActivity {

    static TextView textViewArtistName;
    EditText editTextGuestName;

    Switch switchAddStatus;
    Button buttonAddGuest;
    ListView listViewGuests;

    DatabaseReference databaseGuests;

    // variável para armazenar a lista de trilhas que leremos
    // do banco de dados
    List<Guest> guestList;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);

        textViewArtistName = (TextView) findViewById(R.id.textViewArtistName);
        editTextGuestName = (EditText) findViewById(R.id.editTextGuestName);
        switchAddStatus = (Switch) findViewById(R.id.switchAddStatus);
        buttonAddGuest = (Button) findViewById(R.id.buttonAddGuest);
        listViewGuests = (ListView) findViewById(R.id.listViewGuests);

        Intent intent = getIntent();
        String id = intent.getStringExtra("HOST_LIST_ID");
        String name = intent.getStringExtra("HOST_LIST_NAME");

        // vamos efetivamente criar a lista para armazenar as guests
        guestList = new ArrayList<>();

        textViewArtistName.setText(name);

        // relembrando... databaseGuests aponta para o nó "id", debaixo de "Guests",
        // isto é, aponta APENAS para os guests (músicas) do artista "id"
        databaseGuests = FirebaseDatabase.getInstance().getReference("Guests").child(name);

        switchAddStatus.setText("Não");
        switchAddStatus.setChecked(false);
        switchAddStatus.setTextColor(R.color.red);

        switchAddStatus.setOnClickListener(view -> {
            if(switchAddStatus.isChecked()) {
                switchAddStatus.setText("Sim");
                switchAddStatus.setChecked(true);
                switchAddStatus.setTextColor(R.color.green);
            } else {
                switchAddStatus.setText("Não");
                switchAddStatus.setChecked(false);
                switchAddStatus.setTextColor(R.color.red);
            }
        });

        buttonAddGuest.setOnClickListener(view -> saveGuest());
    }

    // lembrando... usaremos aqui o método onStart() para poder atualizar a lista
    // de trilhas, no caso do app ficar em segundo plano, e depois ser chamado
    // novamente. Rever o ciclo de vida de um app android para entender melhor
    // este ponto, que também já foi discutido em um post anterior.
    @Override
    protected void onStart() {

        super.onStart();

        // criando um tratador de eventos relacionado com nosso
        // banco de dados Firebase
        databaseGuests.addValueEventListener( new ValueEventListener() {

            // método chamado automaticamente quando houver mudança nos dados
            // armazenados no firebase
            // lembre-se!! databaseGuests "aponta" para a chave "id" do artista
            //  selecionado, debaixo da chave "Guests" no JSON dos dados no firebase.
            // Então, se algo mudar "ali dentro",
            // (isto é, dados de trilhas deste artista específico), este método será chamado.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // se entrou aqui, é porque mudou alguma coisa nas trilhas
                // que estão no banco de dados. Então, vamos limpar a lista
                // que armazena essas trilhas, para recuperar esses dados
                // novamente (já que não sabemos exatamente o que mudou)
                guestList.clear();

                // Recebemos um objeto DataSnapshot, que tem os dados
                // apontados por nossa referencia no firebase, isto é,
                // os dados que estão "debaixo" da chave "id" do artista selecionado,
                // debaixo da chave "Guests".
                // Vamos então "varrer" esse objeto, pegando os dados lá dentro
                // e criando objetos da nossa classe Guest, para colocar na lista
                for(DataSnapshot guestSnapshot : dataSnapshot.getChildren()) {

                    // guestSnapshot tem um dos "filhos" de "Guests", isto é,
                    // tem os dados de uma trilha.
                    // Vamos então criar um objeto trilha, a partir desses dados
                    //
                    // ... getValue(Guest.class)
                    //     pegue os dados, e a partir deles crie um objeto da
                    //     classe Guest.
                    Guest guest = guestSnapshot.getValue(Guest.class);

                    // enfim, colocamos o objeto trilha criado a partir dos dados lidos
                    // na nossa lista de trilhas
                    guestList.add(guest);
                }

                guestList.sort((Comparator<? super Guest>) Comparator.comparing((Guest o) -> ((Guest) o).getGuestStatus()));
                // agora que temos nossa lista de trilhas atualizada,
                // podemos criar o adapter que vai ser responsável por
                // colocar esses dados no ListView,
                // passando nossa lista para este adapter
                GuestListAdapter adapter = new GuestListAdapter(AddGuestActivity.this, guestList);

                // finalmente, informamos ao ListView quem é o adapter que vai
                // exibir os dados
                listViewGuests.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    private void saveGuest() {
        String guestName = editTextGuestName.getText().toString().toUpperCase().trim();
        String guestStatus = switchAddStatus.getText().toString().trim();

        if(!TextUtils.isEmpty(guestName)) {

            String id = databaseGuests.push().getKey();
            Guest guest = new Guest(id, guestName, guestStatus);
            databaseGuests.child(guestName).setValue(guest);
            Toast.makeText(this, "Convidado gravado com sucesso!", Toast.LENGTH_LONG).show();
        }
        else {

            Toast.makeText(this, "Precisa digitar o nome do Convidado!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateStatus(String name, String status){
        String host = String.valueOf(textViewArtistName.getText());

        DatabaseReference databaseGuests = FirebaseDatabase.getInstance().getReference("Guests").child(host).child(name);

        HashMap map = new HashMap();
        map.put("guestStatus", status);
        databaseGuests.updateChildren(map);

        Toast.makeText(textViewArtistName.getContext(), "Presença alterada com sucesso!", Toast.LENGTH_LONG).show();
    }
}