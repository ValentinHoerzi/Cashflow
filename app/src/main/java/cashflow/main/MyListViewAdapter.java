package cashflow.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter {

    private List<Entry> data;
    private LayoutInflater inflater;
    private int layoutId;

    public MyListViewAdapter(Context context, int layoutId, List<Entry> data) {
        this.data = data;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Entry e = data.get(position);
        View listItem = (view==null)
                ? inflater.inflate(layoutId,null)
                : view;
        ((TextView)listItem.findViewById(R.id.Datum)).setText(e.getDate());
        ((TextView)listItem.findViewById(R.id.Euro)).setText(
                (e.getIo().equals("Einnahmen"))
                        ?"+ "+e.getPrice()
                        :"- "+e.getPrice());
        ((TextView)listItem.findViewById(R.id.Kategorie)).setText(e.getCategorie());
        return listItem;
    }
}
