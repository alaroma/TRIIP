package com.example.var8;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Dialogs {
    public static final int IDD_MAIN = 1;
    public static final int IDD_DEL = 2;
    public static final int IDD_ABOUT = 3; // Идентификаторы диалоговых окон
  //  public static ListView labelsList;
    //public static ArrayList<ListAdapter> labels = new ArrayList<ListAdapter>();

    public static AlertDialog getDialog(final Activity activity, int ID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        switch(ID) {
            case IDD_ABOUT: // Диалоговое окно About


                // Get the layout inflater
                LayoutInflater inflater = activity.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout

                builder.setView(inflater.inflate(R.layout.dialog, null));

                builder.setTitle("О приложениии");
               // builder.setMessage(R.string.dialog_about_message);
                builder.setCancelable(true);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                return builder.create();
            case IDD_MAIN: // Диалоговое окно Rate the app
//                builder.setTitle(R.string.dialog_rate_title);
//                builder.setMessage(R.string.dialog_rate_message);
//                builder.setCancelable(true);
//                builder.setPositiveButton(R.string.dialog_rate_ok, new DialogInterface.OnClickListener() { // Переход на оценку приложения
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Переход
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNeutralButton(R.string.dialog_rate_cancel, new DialogInterface.OnClickListener() { // Оценить приложение потом

                    final String[] ed_lab ={"Редактировать", "Удалить"};
                    builder.setItems(ed_lab, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch(item) {
                                case 0:
                                    Toast.makeText(activity, "Научимся))))", Toast.LENGTH_LONG).show();
                                    break;
                                case 1:
                                    Dialog delete = Dialogs.getDialog(activity, Dialogs.IDD_DEL);
                                    dialog.dismiss();
                                    delete.show();

                                    break;
                            }
                        }
                    });
                    builder.setCancelable(true);
                    return builder.create();

            case IDD_DEL: // Диалог настроек
               builder.setMessage("Удалить метку безвозвратно?");
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() { // Кнопка ОК
                    public void onClick(DialogInterface dialog, int whichButton) {

                      //  labelsList = (ListView) activity.findViewById(R.id.list);
                        //final Adapter labelAdapter = new Adapter(activity, R.layout.list_item, labels);

                        //labelsList.setAdapter(labelAdapter);
                       // activity.labels.remove(labelsList.getTag());
                     //   activity.labelsList.invalidateViews();

                        //labelAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // Кнопка Отмена
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                return builder.create();
            default:
                return null;
        }
    }
}
