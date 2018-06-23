package adriancardenas.com.ehealth.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import adriancardenas.com.ehealth.Adapters.ViewHolders.TableRowViewHolder;
import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.model.TableRow;

/**
 * Created by Adrian on 23/06/2018.
 */

public class GenericRecyclerAdapter extends RecyclerView.Adapter<TableRowViewHolder> {
    private List<TableRow> tableRowList;

    public GenericRecyclerAdapter(List<TableRow> list) {
        tableRowList = list;
    }

    @NonNull
    @Override
    public TableRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_row, parent, false);
        TableRowViewHolder vh = new TableRowViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TableRowViewHolder holder, int position) {
        holder.bind(tableRowList.get(position));
    }

    @Override
    public int getItemCount() {
        return tableRowList.size();
    }
}
