package br.edu.ifsp.dpdm.taskhub.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.dpdm.taskhub.model.Task;

public class TaskListAdapter extends BaseAdapter {

    private Context context;
    private List<Task> lista;

    public TaskListAdapter(Context context, List<Task> lista) {
        this.context = context;
        this.lista = lista;
    }

    public int getCount() {
        return lista.size();
    }

    public Object getItem(int position) {
        return lista.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        Task t = lista.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.tasks, null);

        TextView id = (TextView) view.findViewById(R.id.txtIdTask);
        id.setText("ID: " + t.getId());

        TextView title = (TextView) view.findViewById(R.id.txtTitleTask);
        title.setText("Title: " + t.getTitle());

        TextView description = (TextView) view.findViewById(R.id.txtDescriptionTask);
        description.setText("Description: " + t.getDescription());

        TextView deadline = (TextView) view.findViewById(R.id.txtDeadlineTask);
        deadline.setText("Deadline: " + t.getPriority());

        TextView priority = (TextView) view.findViewById(R.id.txtPriority);
        priority.setText("Priority: " + t.getPriority());

        TextView completed = (TextView) view.findViewById(R.id.txtCompleted);
        completed.setText("Completed: " + t.isCompleted());

        return view;
    }
}
