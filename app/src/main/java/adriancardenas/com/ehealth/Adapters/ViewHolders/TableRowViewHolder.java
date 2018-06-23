package adriancardenas.com.ehealth.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import adriancardenas.com.ehealth.R;
import adriancardenas.com.ehealth.model.TableRow;

/**
 * Created by Adrian on 23/06/2018.
 */

public class TableRowViewHolder extends RecyclerView.ViewHolder {
    TextView date;
    TextView value;
    public TableRowViewHolder(View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date);
        value = itemView.findViewById(R.id.value);
    }

    public void bind(TableRow tableRow){
        this.date.setText(tableRow.getColumn1());
        this.value.setText(tableRow.getColumn2());
    }
}
