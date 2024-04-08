package br.edu.ifsp.dpdm.taskhub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

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
    private CheckBox edCompleted;

    private String operation;
    private Spinner spPriority;
    private String[] priorities = {"High", "Medium", "Easy"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edID = (EditText) findViewById(R.id.edID);
        edTitle = (EditText) findViewById(R.id.edTitle);
        edDescription = (EditText) findViewById(R.id.edDescription);
        edDeadline = (EditText) findViewById(R.id.edDeadline);
        spPriority = (Spinner) findViewById(R.id.spPriority);
        edCompleted = (CheckBox) findViewById(R.id.ckCompleted);
        taskDAO = new TaskDAO(getApplicationContext());

        operation = "New";

        loadPriorities();
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

        if (operation.equalsIgnoreCase("New")) {
            t = new Task();
        }

        t.setTitle(edTitle.getText().toString());

        t.setPriority(priorities[spPriority.getSelectedItemPosition()]);
        t.setDescription(edDescription.getText().toString());

        if (operation.equalsIgnoreCase("new")) {
            taskDAO.salvar(t);
            showMessage("Task added with success!");
        } else {
            taskDAO.atualizar(t);
            showMessage("Task updated with success!");
        }

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


    public void loadData(Task task) {
        edID.setText(String.valueOf(task.getId()));
        edTitle.setText(task.getTitle());
        edDescription.setText(task.getDescription());
        edDeadline.setText(String.valueOf(task.getDeadline()));
        spPriority.setSelection(task.getPriority().equalsIgnoreCase("M") ? 0 : 1);
    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        setResult(4, it);
        finish();
    }
}
