package com.webplanet.convid4dos.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webplanet.convid4dos.R;
import com.webplanet.convid4dos.model.HostList;

import java.util.List;

public class HostListAdapter extends ArrayAdapter<HostList> {

    private Activity context;
    private List<HostList> hostList;  // lista para armazenar os artitas

    public HostListAdapter(Activity context, List<HostList> hostList) {

        super(context, R.layout.layout_host_list_item, hostList);
        this.context = context;
        this.hostList = hostList;
    }

    // método que é chamado para fornecer cada item da lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // criando um objeto "inflador"
        LayoutInflater inflater = context.getLayoutInflater();

        // usando o inflador para criar uma View a partir do arquivo de layout
        // que fizemos definindo os itens da lista
        View listViewItem = inflater.inflate(R.layout.layout_host_list_item, null, true);

        // pegando referências para as views que definimos dentro do item da lista,
        // isto é, os 2 textviews
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewNameHostList);

        // a posição do hosta na lista (armazenamento) é a mesma na lista (listview)
        // então usamos esse valor (position) para acessar o objeto "Host" correto
        // dentro da lista hostList
        HostList host = hostList.get(position);

        // finalmente, colocamos os valores do objeto hosta recuperado
        // nas views que formam nosso item da lista
        textViewName.setText(host.getHostListName());

        // a view está pronta! É só devolver para quem pediu
        return listViewItem;
    }
}
