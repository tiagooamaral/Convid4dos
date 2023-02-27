package com.webplanet.convid4dos.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.webplanet.convid4dos.AddGuestActivity;
import com.webplanet.convid4dos.R;
import com.webplanet.convid4dos.model.Guest;

import java.util.List;

public class GuestListAdapter extends ArrayAdapter<Guest> {

    private Activity context;
    private List<Guest> trackList;  // lista para armazenar as tracks

    public GuestListAdapter(Activity context, List<Guest> trackList) {

        super(context, R.layout.layout_guest_list, trackList);
        this.context = context;
        this.trackList = trackList;
    }

    // método que é chamado para fornecer cada item da lista
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // criando um objeto "inflador"
        LayoutInflater inflater = context.getLayoutInflater();

        // usando o inflador para criar uma View a partir do arquivo de layout
        // que fizemos definindo os itens da lista
        View listViewItem = inflater.inflate(R.layout.layout_guest_list, null, true);

        // pegando referências para as views que definimos dentro do item da lista,
        // isto é, os 2 textviews: nome e "rating" da trilha (música)
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        Switch switchStatus = (Switch) listViewItem.findViewById(R.id.switch2);

        // a posição da trilha na lista (armazenamento) é a mesma na lista (listview)
        // então usamos esse valor (position) para acessar o objeto "Guest" correto
        // dentro da lista trackList
        Guest guest = trackList.get(position);

        // finalmente, colocamos os valores do objeto track recuperado
        // nas views que formam nosso item da lista
        textViewName.setText(guest.getGuestName().toUpperCase());
        switchStatus.setText(guest.getGuestStatus());

        if(switchStatus.getText().equals("Sim")) {
            switchStatus.setText("Sim");
            switchStatus.setChecked(true);
            switchStatus.setTextColor(R.color.green);
        } else {
            switchStatus.setText("Não");
            switchStatus.setChecked(false);
            switchStatus.setTextColor(R.color.red);
        }
        switchStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchStatus.getText().equals("Não")) {
                    AddGuestActivity.updateStatus(String.valueOf(textViewName.getText()), "Sim");
                    switchStatus.setText("Sim");
                    switchStatus.setChecked(true);
                    switchStatus.setTextColor(R.color.green);

                } else {
                    AddGuestActivity.updateStatus(String.valueOf(textViewName.getText()), "Não");
                    switchStatus.setText("Não");
                    switchStatus.setChecked(false);
                    switchStatus.setTextColor(R.color.red);
                }
            }
        });
        //     o rating é numérico (int), convertemos para string para
        //     colocar no textView

        // a view está pronta! É só devolver para quem pediu
        return listViewItem;
    }
}
