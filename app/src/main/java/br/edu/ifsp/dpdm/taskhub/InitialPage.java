package br.edu.ifsp.dpdm.taskhub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.dpdm.taskhub.adapters.TaskListAdapter;
import br.edu.ifsp.dpdm.taskhub.dao.entities.TaskDAO;
import br.edu.ifsp.dpdm.taskhub.model.Task;

public class InitialPage extends AppCompatActivity {

    private Task t;
    private static List<Task> tasks;
    private static TaskDAO taskDAO;
    private ListView lvTasks;
    private String operation;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {refreshList();});
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_page);

        Button openTaskForm = findViewById(R.id.btAdd);
        openTaskForm.setOnClickListener(arg0 -> {
            Intent it = new Intent(getApplicationContext(), TaskManager.class).putExtra("operation", "update");
            mStartForResult.launch(it);
        });

        tasks = new ArrayList<>();
        operation = "New";

        lvTasks = (ListView) findViewById(R.id.lvTasks);
        lvTasks.setOnItemClickListener(selectTasks);
        lvTasks.setOnItemLongClickListener(removeTask);
        taskDAO = new TaskDAO(getApplicationContext());
        refreshList();
    }

    private AdapterView.OnItemClickListener selectTasks = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
            t = tasks.get(pos);
            Intent it = new Intent(getApplicationContext(), TaskManager.class);
            it.putExtra("selectedItem", t);
            it.putExtra("operation", "Update");
            mStartForResult.launch(it);
        }
    };

    private void removeTask(final int idFuncionario) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir funcionario?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Deseja excluir essa funcionario?")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sim),
                        (dialog, id) -> {
                            if (taskDAO.deletar(idFuncionario)) {
                                refreshList();
                                showMessage(getString(R.string.msgExclusao));
                            } else {
                                showMessage(getString(R.string.msgFalhaExclusao));
                            }

                        })
                .setNegativeButton(getString(R.string.nao),
                        (dialog, id) -> dialog.cancel());
        builder.create();
        builder.show();
    }

    public void refreshList() {
        tasks = taskDAO.listAll();
        if (tasks != null) {
            tasks.size();
            TaskListAdapter pla = new TaskListAdapter(
                    getApplicationContext(), tasks);
            lvTasks.setAdapter(pla);
        }
    }

    private final AdapterView.OnItemLongClickListener removeTask = (arg0, arg1, pos, arg3) -> {
        removeTask(tasks.get(pos).getId());
        return true;
    };

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}
