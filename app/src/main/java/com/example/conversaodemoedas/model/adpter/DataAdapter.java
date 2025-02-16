package com.example.conversaodemoedas.model.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conversaodemoedas.R;
import com.example.conversaodemoedas.model.DataModel;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<DataModel> dataList;
    private String[] columnNames; // Nomes das colunas

    public DataAdapter(List<DataModel> dataList, String[] columnNames) {
        this.dataList = dataList;
        this.columnNames = columnNames;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TYPE_HEADER : TYPE_ITEM; // O primeiro item será o cabeçalho
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_row, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.valorReal.setText(columnNames[0]);
            headerHolder.valorDolar.setText(columnNames[1]);
        } else {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            DataModel data = dataList.get(position - 1); // Ajuste para ignorar o cabeçalho
            itemHolder.valorReal.setText(data.getValorRealFormatted());
            itemHolder.valorDolar.setText(data.getValorDolarFormatted());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1; // Adiciona 1 ao tamanho para incluir o cabeçalho
    }

    // ViewHolder para os dados normais
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView valorReal, valorDolar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            valorReal = itemView.findViewById(R.id.trValoReal);
            valorDolar = itemView.findViewById(R.id.trValoDolar);
        }
    }

    // ViewHolder para o cabeçalho
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView valorReal, valorDolar;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            valorReal = itemView.findViewById(R.id.thValorReal);
            valorDolar = itemView.findViewById(R.id.thValorDolar);
        }
    }
}
