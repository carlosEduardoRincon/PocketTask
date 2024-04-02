package br.edu.ifsp.dpdm.taskhub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.dpdm.taskhub.adapters.TaskListAdapter;
import br.edu.ifsp.dpdm.taskhub.dao.TaskRepository;
import br.edu.ifsp.dpdm.taskhub.dao.entities.TaskDAO;
import br.edu.ifsp.dpdm.taskhub.model.Task;

public class TaskManager extends Activity {

    private Task t;
    private List<Task> tasks;
    private TaskDAO taskDAO;
    private EditText edID;
    private EditText edTitle;
    private EditText edDescription;
    private EditText edDeadline;
    private EditText edCompleted;
    private ListView lvTasks;
    private String operation;
    private Spinner spPriority;
    private String[] priorities = {"High", "Medium", "Easy"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        edID = (EditText) findViewById(R.id.edID);
        edTitle = (EditText) findViewById(R.id.edNome);
        edDescription = (EditText) findViewById(R.id.edIdade);
        spPriority = (Spinner) findViewById(R.id.spPriority);
        lvTasks = (ListView) findViewById(R.id.lvTasks);
        lvTasks.setOnItemClickListener(selectTasks);
        lvTasks.setOnItemLongClickListener(removeTask);
        tasks = new ArrayList<>();
        operation = "New";
        taskDAO = new TaskDAO(getApplicationContext());
        loadPriorities();
        refreshList();
    }

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

    private void loadPriorities() {
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, priorities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPriority.setAdapter(adapter);
        } catch (Exception ex) {
            showMessage("Erro: " + ex.getMessage());
        }
    }

    public void saveTask(View v) {

        if (operation.equalsIgnoreCase("Novo")) {
            t = new Task();
        }

        t.setTitle(edTitle.getText().toString());

        t.setPriority(priorities[spPriority.getSelectedItemPosition()]
                .equalsIgnoreCase("Masculino") ? "M" : "F");
        t.setDescription(edDescription.getText().toString());

        if (operation.equalsIgnoreCase("Novo")) {
            taskDAO.salvar(t);
            showMessage("Funcionário cadastrado com sucesso!");
        } else {
            taskDAO.atualizar(t);
            showMessage("Funcionário atualizado com sucesso!");
        }

        refreshList();
        cleanData();
    }

    public void novo(View v) {
        operation = new String("Novo");
        cleanData();
    }

    private void cleanData() {
        edID.setText("");
        edTitle.setText("");
        edDescription.setText("");
        edCompleted.setText("");
        edDeadline.setText("");
        spPriority.setSelection(0);
    }

    private void refreshList() {
        tasks = taskDAO.listAll();
        if (tasks != null) {
            tasks.size();
            TaskListAdapter pla = new TaskListAdapter(
                    getApplicationContext(), tasks);
            lvTasks.setAdapter(pla);
        }
    }

    private AdapterView.OnItemClickListener selectTasks = (arg0, arg1, pos, id) -> {
        operation = "Atualizar";
        t = tasks.get(pos);
        loadData(t);
    };

    private AdapterView.OnItemLongClickListener removeTask = (arg0, arg1, pos, arg3) -> {
        removeTask(tasks.get(pos).getId());
        return true;
    };

    private void loadData(Task task) {
        edID.setText(String.valueOf(task.getId()));
        edTitle.setText(task.getTitle());
        edDescription.setText(task.getDescription());
        edDeadline.setText(String.valueOf(task.getDeadline()));
        spPriority.setSelection(task.getPriority().equalsIgnoreCase("M") ? 0 : 1);
    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
